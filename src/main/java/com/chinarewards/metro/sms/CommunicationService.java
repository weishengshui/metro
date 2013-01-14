package com.chinarewards.metro.sms;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinarewards.metro.core.common.HBDaoSupport;
import com.chinarewards.metro.domain.sms.SMSOutbox;
import com.chinarewards.metro.domain.sms.SMSSendStatus;

/**
 * communication gateway SMS process service .
 * 
 * @author qingminzou
 * 
 */

@Service
public class CommunicationService implements ICommunicationService {

	private static Logger log = LoggerFactory
			.getLogger(CommunicationService.class);

	static int PROCESS_SMS_OUTBOX_ITEMS_PER_HEARTBEAT = 5;

	public static final int MAX_FAILCOUNT = 3;

	@Autowired
	private IQueueProcessor queueProc;

	@Autowired
	private HBDaoSupport hbDaoSupport;

	// ------ private
	/**
	 * Fix the provider to use by lookup database
	 */
	private void determineProvider(List<SMSOutbox> outbox) {
		for (SMSOutbox o : outbox) {
			if (o.getProvider() == null) {
				// XXX default
				o.setProvider("mxtong");
			}
		}
	}

	public void heartbeat() {
		log.trace("Heartbeat");
		// pickup new jobs and delegate to provider
		List<SMSOutbox> outbox = queueProc.dequeueSMSOutbox();

		// determine the provider to use if provider field is EMPTY
		determineProvider(outbox);

		// delegate to the actual provider for sending
		queueProc.delegateToSMSProvider(outbox);
		// check previous sending status if it is sent by async method
		queueProc.updateSMSSendingProgress();

		// inbox check
		log.debug("Production environment process smsInbox!");
		queueProc.processSMSInbox();

		// process error handling for outbox
		queueProc.processPreviousErrors();
		// archive outbox for performance consideration
		queueProc.archiveOutbox();
	}

	/**
	 * Queue an SMS message, return the ID in outbox queue
	 * 
	 * @author kmtong
	 */
	public Long queueSMS(String provider, String destId, String destination,
			String content, int priority, Date expectSendDate) {
		return queueSMS(provider, destId, null, destination, content, priority,
				expectSendDate);
	}

	/**
	 * Queue an SMS with Source number, implemented only for MAS and
	 * ChinaTelecom
	 * 
	 * @param provider
	 * @param source
	 * @param destination
	 * @param content
	 * @param priority
	 * @param expectSendDate
	 * @return
	 */
	public Long queueSMS(String provider,String destId, String source, String destination,
			String content, int priority, Date expectSendDate) {
		SMSOutbox sms = new SMSOutbox();
		sms.setPriority(priority);
		sms.setContent(content);
		sms.setSource(source);
		sms.setDestination(destination);
		sms.setProvider(provider);
		sms.setSubmitDate(new Date());
		sms.setExpectSendDate(expectSendDate);
		sms.setStatus(SMSSendStatus.QUEUED);
		sms.setDestId(destId);
		hbDaoSupport.save(sms);
		return sms.getId();
	}

	public IQueueProcessor getQueueProc() {
		return queueProc;
	}

	public void setQueueProc(IQueueProcessor queueProc) {
		this.queueProc = queueProc;
	}

	public HBDaoSupport getHbDaoSupport() {
		return hbDaoSupport;
	}

	public void setHbDaoSupport(HBDaoSupport hbDaoSupport) {
		this.hbDaoSupport = hbDaoSupport;
	}
}
