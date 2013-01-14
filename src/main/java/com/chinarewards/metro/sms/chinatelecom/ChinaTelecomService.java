package com.chinarewards.metro.sms.chinatelecom;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.metro.sms.SMSException;
import com.chinarewards.metro.sms.SMSMessage;
import com.chinarewards.metro.sms.SMSStatus;
import com.shouyi.SMGP_API.Sms;
import com.shouyi.SMGP_API.SmsClient;

public class ChinaTelecomService implements IChinaTelecomService {

	Logger log = LoggerFactory.getLogger(this.getClass());

	SmsClient client = new SmsClient();

	// use host table (/etc/hosts) to control
	// the access to the gateway (125.88.123.137)
	private final static String IP_ADDRESS = "smgp-gateway";
	private final static int PORT = 3058;
	private final static String USERNAME = "02002118";
	private final static String PASSWORD = "888888";
	private final static int TIMEOUT = 30;
	private final static String SOURCE = "1065902002118";

	private boolean initialized = false;

	/**
	 * init, nothing to do
	 */
	public void init() {
		int code = client.Login(IP_ADDRESS, PORT, USERNAME, PASSWORD, TIMEOUT);
		if (code == 0) {
			initialized = true;
		} else {
			log.error("Cannot Login to China Telecom SMGP: Code=" + code);
			System.out.println("Cannot Login to China Telecom SMGP: Code="
					+ code);
			initialized = false;
		}
	}

	public void destroy() {
		if (initialized) {
			client.Disconnect();
		}
	}

	public SMSStatus getStatus(String taskId) throws SMSException {
		throw new UnsupportedOperationException("Status Tracking not supported");
	}

	public boolean isSupportStatusTracking() {
		return false;
	}

	public String queue(String provider, String mobileNo, String message)
			throws SMSException {
		return queue(provider, SOURCE, mobileNo, message);
	}

	public String queue(String provider, String sourceNo, String mobileNo,
			String message) throws SMSException {
		if (!initialized) {
			// try to reconnect
			init();
			if (!initialized) {
				throw new SMSException("Provider not initialized!");
			}
		}
		try {
			StringBuilder sb = new StringBuilder();
			int[] result = client.SendLongSms(sourceNo, mobileNo, message,
					false, sb);
			StringBuilder rs = new StringBuilder();
			if (result != null && result.length > 0) {
				if (result[0] == 0) {
					return sb.toString();
				}
				for (int i : result) {
					rs.append(i + ",");
				}
			}
			throw new SMSException("Send error: MessageID=" + sb.toString()
					+ ", Status=" + rs);
		} catch (InternalError e) {
			throw new SMSException(e);
		}
	}

	public void markAsRead(List<SMSMessage> messages) throws SMSException {
		// do nothing
		if (!initialized) {
			throw new SMSException("Provider not initialized!");
		}
	}

	public List<SMSMessage> poll() throws SMSException {
		if (!initialized) {
			// try to reconnect
			init();
			if (!initialized) {
				throw new SMSException("Provider not initialized!");
			}
		}
		ArrayList<Sms> smsRecList = new ArrayList<Sms>();
		List<SMSMessage> res = new ArrayList<SMSMessage>();
		if (client.RecvSms(smsRecList) == 0) {
			for (Sms i : smsRecList) {
				res.add(convertSMS(i));
			}
		}
		return res;
	}

	private SMSMessage convertSMS(Sms i) {
		SMSMessage s = new SMSMessage();
		s.setSource(i.getSrcTermID());
		s.setDestination(i.getDestTermID());
		s.setContent(i.getMsgContent());
		log.info("Received time: " + i.getRecvTime());
		// s.setDateReceived(i.getRecvTime());
		s.setDateReceived(new Date());
		return s;
	}

}
