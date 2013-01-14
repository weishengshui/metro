package com.chinarewards.metro.sms.mxtong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.net.mxtong.ServicesLocator;
import cn.net.mxtong.ServicesSoap;
import cn.net.mxtong.jobsendeddescription.SendStatus;

import com.chinarewards.metro.sms.ISMSSendQueue;
import com.chinarewards.metro.sms.SMSException;
import com.chinarewards.metro.sms.SMSStatus;

public class MxTongSMSQueue implements ISMSSendQueue {

	private static Log log = LogFactory.getLog(MxTongSMSQueue.class);

	String userId;
	String account;
	String password;

	public MxTongSMSQueue(String userId, String account, String password) {
		log.info("MxTongSMSQueue userId = " + userId);
		log.info("MxTongSMSQueue account = " + account);
		log.info("MxTongSMSQueue password = " + password);
		this.userId = userId;
		this.account = account;
		this.password = password;
	}

	public void init() {
	}

	public void destroy() {
	}

	public SMSStatus getStatus(String taskId) throws SMSException {
		return null;
	}

	public String queue(String provider, String phoneNo, String msg)
			throws SMSException {

		log.info("MxTongSMSQueue.queue provider = " + provider);
		log.info("MxTongSMSQueue.queue userId = " + userId);
		log.info("MxTongSMSQueue.queue account = " + account);

		log.info("MxTongSMSQueue.queue passowrd = " + password);

		log.info("MxTongSMSQueue.queue phoneNo = " + phoneNo);
		log.info("MxTongSMSQueue.queue msg = " + msg);

		boolean result = false;
		try {
			ServicesSoap s = new ServicesLocator().getServicesSoap();
			SendStatus status = s.directSend(userId, account, password,
					phoneNo, msg, null, 1, "1");

			log.info("MxTongSMSQueue.queue directSended status is "
					+ status.getRetCode() + " jobId is " + status.getJobID());
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SMSException(e.getMessage());
		}

		log.info("MxTongSMSQueue.queue result = " + result);
		if (result) {
			return null;
		} else {
			throw new SMSException();
		}
	}

	public boolean isSupportStatusTracking() {
		return false;
	}

	/**
	 * sourceNo not supported, simply delegating
	 */
	public String queue(String provider, String sourceNo, String mobileNo,
			String message) throws SMSException {
		log.error("Source Number not supported, ignoring source number: "
				+ sourceNo);
		return queue(provider, mobileNo, message);
	}

}
