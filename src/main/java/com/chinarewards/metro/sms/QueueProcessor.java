package com.chinarewards.metro.sms;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chinarewards.metro.core.common.HBDaoSupport;
import com.chinarewards.metro.domain.sms.PriorityPeriodLimit;
import com.chinarewards.metro.domain.sms.SMSInbox;
import com.chinarewards.metro.domain.sms.SMSOutbox;
import com.chinarewards.metro.domain.sms.SMSOutboxHistory;
import com.chinarewards.metro.domain.sms.SMSReceiveStatus;
import com.chinarewards.metro.domain.sms.SMSSendStatus;

@Component
public class QueueProcessor implements IQueueProcessor {

	private static Logger log = LoggerFactory.getLogger(QueueProcessor.class);

	static int PROCESS_SMS_OUTBOX_ITEMS_PER_HEARTBEAT = 5;
	public static final int MAX_FAILCOUNT = 3;

	@Autowired
	private HBDaoSupport hbDaoSupport;

	@Autowired
	IProviderService providers;

	@PostConstruct
	public void initAllSP() {
		log.debug("QueueProcessor Initializing");
		providers.init();
		log.debug("QueueProcessor Initialized");
	}

	@PreDestroy
	public void destroyAllSP() {
		log.debug("QueueProcessor Destroying");
		providers.destroy();
		log.debug("QueueProcessor Destroyed");
	}

	// @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void updateSMSSendingProgress() {

		List<SMSOutbox> sending = hbDaoSupport.findTsByHQL(
				"FROM SMSOutbox sms WHERE sms.status = ?",
				SMSSendStatus.SENDING);

		Iterator<SMSOutbox> i = sending.iterator();
		while (i.hasNext()) {
			SMSOutbox out = (SMSOutbox) i.next();
			String provider = out.getProviderUsed();
			log.debug("Status update for message: " + out.getId());
			ISMSSendQueue sp = getSMSSendProvider(provider);
			if (sp != null) {
				try {
					String taskId = out.getProviderTaskId();
					SMSStatus status = sp.getStatus(taskId);
					if (status == SMSStatus.SENT) {
						out.setStatus(SMSSendStatus.SENT);
						out.setSentDate(new Date());
						log.info("Message Sent: " + out.getId());
					} else if (status == SMSStatus.ERROR) {
						out.setStatus(SMSSendStatus.ERROR);
						out.setFailCount(out.getFailCount() + 1);
						out.setLastFail(new Date());
						log.error("Message Error: " + out.getId());
					} else if (status == SMSStatus.QUEUED) {
						// normal, waiting to send
					} else {
						log.error("Status not handled: " + status
								+ ", setting status to ERROR");
						out.setStatus(SMSSendStatus.ERROR);
					}
				} catch (SMSException e) {
					log.error("Get status error", e);
					out.setStatus(SMSSendStatus.ERROR);
					out.setFailCount(out.getFailCount() + 1);
					out.setLastFail(new Date());
				} catch (Exception e) {
					log.error("Get status unknow error", e);
					continue;
				}
			} else {
				log.error("updateSMSSendingProgress Unknown provider: "
						+ provider);
				out.setStatus(SMSSendStatus.ERROR);
				out.setLastFail(new Date());
			}
			hbDaoSupport.update(out);
		}
	}

	// @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void delegateToSMSProvider(List<SMSOutbox> outbox) {
		Iterator<SMSOutbox> i = outbox.iterator();
		while (i.hasNext()) {
			try {
				SMSOutbox out = i.next();

				log.info("Processing message: " + out.getId() + " Prority: "
						+ out.getPriority());
				log.debug(" Source: " + out.getSource());
				log.debug(" Destination: " + out.getDestination());
				log.debug(" Content: " + out.getContent());
				log.debug(" Requested Provider: " + out.getProvider());
				log.debug(" ExpectSendDate: " + out.getExpectSendDate());
				String provider = out.getProvider();
				if (provider == null) {
					// select default
					provider = "mxtong";
				}

				ISMSSendQueue sp = getSMSSendProvider(provider);

				if (sp == null) {
					// unknown provider
					log.error("delegateToSMSProvider Unknown provider: "
							+ provider);
					out.setStatus(SMSSendStatus.ERROR);
					out.setFailCount(out.getFailCount() + 1);
					out.setLastFail(new Date());
					return;
				}

				if (!sp.isSupportStatusTracking()) {
					log.debug("SYNC Provider: " + provider);
					try {
						if (out.getSource() == null) {
							sp.queue(provider, out.getDestination(),
									out.getContent());
						} else {
							sp.queue(provider, out.getSource(),
									out.getDestination(), out.getContent());
						}
						out.setProviderUsed(provider);
						out.setStatus(SMSSendStatus.SENT);
						out.setSentDate(new Date());
						log.info("Message Sent: " + out.getId());
					} catch (SMSException e) {
						log.error("Queue error: Message: " + out.getId(), e);
						out.setStatus(SMSSendStatus.ERROR);
						out.setFailCount(out.getFailCount() + 1);
						out.setLastFail(new Date());
					}
				} else {
					log.debug("ASYNC Provider: " + provider);
					try {
						String taskId = null;
						if (out.getSource() == null) {
							taskId = sp.queue(provider, out.getDestination(),
									out.getContent());
						} else {
							taskId = sp.queue(provider, out.getSource(),
									out.getDestination(), out.getContent());
						}
						out.setProviderUsed(provider);
						out.setStatus(SMSSendStatus.SENDING);
						out.setProviderTaskId(taskId);
					} catch (SMSException e) {
						log.error("Queue error: Message: " + out.getId(), e);
						out.setStatus(SMSSendStatus.ERROR);
						out.setFailCount(out.getFailCount() + 1);
						out.setLastFail(new Date());
					}
				}
				hbDaoSupport.update(out);;
			} catch (Exception e) {
				log.error("Error sending SMS to provider", e);
			}
		}
	}

	// @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<SMSOutbox> dequeueSMSOutbox() {
		List<Integer> denyPriorities = checkPriorityLimit(new Date());
		boolean hasDeniedPriorities = !denyPriorities.isEmpty();

		Map<String, Object> params = new HashMap<String, Object>();

		String hql = "FROM SMSOutbox sms WHERE sms.status = :status AND "
				+ (hasDeniedPriorities ? "sms.priority NOT IN (:priority) AND "
						: "")
				+ "(sms.expectSendDate is null OR sms.expectSendDate<=:esDate) "
				+ "ORDER by sms.priority DESC";

		params.put("status", SMSSendStatus.QUEUED);
		params.put("esDate", new Date());

		List<SMSOutbox> r;
		if (hasDeniedPriorities) {
			params.put("priority", denyPriorities);
		}

		r = hbDaoSupport.findLimitTs(hql,
				PROCESS_SMS_OUTBOX_ITEMS_PER_HEARTBEAT, params);

		Iterator<SMSOutbox> i = r.iterator();
		while (i.hasNext()) {
			SMSOutbox out = i.next();
			out.setStatus(SMSSendStatus.READY);
			hbDaoSupport.update(out);
		}
		return r;
	}

	/**
	 * Return all priorities that the given time cannot send SMS to.
	 * 
	 * @param date
	 * @return
	 */
	public List<Integer> checkPriorityLimit(Date date) {
		List<PriorityPeriodLimit> limits = hbDaoSupport
				.findAll(PriorityPeriodLimit.class);
		List<Integer> ps = new LinkedList<Integer>();
		for (PriorityPeriodLimit p : limits) {
			if (TimeRangeChecker.inRange(p.getTimeRange(), date)) {
				ps.add(p.getPriority());
			}
		}
		return ps;
	}

	/**
	 * Try to fix previous errors
	 */
	// @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void processPreviousErrors() {

		List<SMSOutbox> r = hbDaoSupport
				.findTsByHQL(
						"FROM SMSOutbox sms WHERE sms.status = ? AND sms.failCount <= ?",
						SMSSendStatus.ERROR, MAX_FAILCOUNT);

		Iterator<SMSOutbox> i = r.iterator();
		while (i.hasNext()) {
			SMSOutbox out = i.next();
			// check provider used, use different provider if necessary
			out.setProvider(tryNextProvider(out.getProviderUsed()));
			out.setStatus(SMSSendStatus.QUEUED);
			hbDaoSupport.update(out);
		}
	}

	/**
	 * logic to try different providers.
	 * 
	 * @param providerUsed
	 * @return
	 */
	public String tryNextProvider(String providerUsed) {
		if ("mxtong".equals(providerUsed)) {
			return "handpay";
		}
		// else if ("mas".equals(providerUsed) || "mas-uc".equals(providerUsed))
		// {
		// return "mxtong";
		// }
		// else if ("mxtong".equals(providerUsed)) {
		// return "mas";
		// }
		return "mxtong";
	}

	// @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void processSMSInbox() {
		// inbox handling
		for (String code : providers.getAllProviders()) {
			try {
				ISMSReceiveQueue q = providers.getReceiveQueue(code);
				if (q != null) {
					List<SMSMessage> ms = q.poll();
					for (SMSMessage m : ms) {
						SMSInbox inbox = new SMSInbox();
						inbox.setContent(m.getContent());
						inbox.setDestination(m.getDestination());
						inbox.setSource(m.getSource());
						inbox.setProvider(code);
						inbox.setProviderMessageId(m.getExternalId());
						inbox.setReceivedDate(m.getDateReceived());
						inbox.setStatus(SMSReceiveStatus.NEW);
						hbDaoSupport.update(inbox);
					}
					q.markAsRead(ms);
				}
			} catch (SMSException e) {
				log.error("Poll inbox error [" + code + "]", e);
			}
		}
	}

	/**
	 * Obtain SMS send provider from code.
	 * 
	 * @param code
	 * @return
	 */
	public ISMSSendQueue getSMSSendProvider(String code) {
		return providers.getSendQueue(code);
	}

	// @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void archiveOutbox() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(
				"status",
				Arrays.asList(new SMSSendStatus[] { SMSSendStatus.ERROR,
						SMSSendStatus.SENT }));

		List<SMSOutbox> r = hbDaoSupport.findLimitTs(
				"FROM SMSOutbox sms WHERE sms.status IN (:status)",
				PROCESS_SMS_OUTBOX_ITEMS_PER_HEARTBEAT, params);
		Iterator<SMSOutbox> i = r.iterator();
		while (i.hasNext()) {
			SMSOutbox out = i.next();
			SMSOutboxHistory hist = new SMSOutboxHistory();
			copy(hist, out);
			hbDaoSupport.save(hist);
			hbDaoSupport.delete(out);
		}
	}

	private void copy(SMSOutboxHistory hist, SMSOutbox out) {
		hist.setId(out.getId());
		hist.setContent(out.getContent());
		hist.setSource(out.getSource());
		hist.setDestination(out.getDestination());
		hist.setExpectSendDate(out.getExpectSendDate());
		hist.setFailCount(out.getFailCount());
		hist.setLastFail(out.getLastFail());
		hist.setOptlock(out.getOptlock());
		hist.setPriority(out.getPriority());
		hist.setProvider(out.getProvider());
		hist.setProviderTaskId(out.getProviderTaskId());
		hist.setProviderUsed(out.getProviderUsed());
		hist.setSentDate(out.getSentDate());
		hist.setStatus(out.getStatus());
		hist.setSubmitDate(out.getSubmitDate());
		hist.setDestId(out.getDestId());
	}

	public HBDaoSupport getHbDaoSupport() {
		return hbDaoSupport;
	}

	public void setHbDaoSupport(HBDaoSupport hbDaoSupport) {
		this.hbDaoSupport = hbDaoSupport;
	}

	public IProviderService getProviders() {
		return providers;
	}

	public void setProviders(IProviderService providers) {
		this.providers = providers;
	}

}
