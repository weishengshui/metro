package com.chinarewards.metro.sms.chinaunicomm;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.metro.sms.SMSException;
import com.chinarewards.metro.sms.SMSMessage;
import com.chinarewards.metro.sms.SMSStatus;

public class ChinaUnicommService implements IChinaUnicommService {

	Logger log = LoggerFactory.getLogger(this.getClass());

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public SMSStatus getStatus(String arg0) throws SMSException {
		// TODO Auto-generated method stub
		return null;
	}

	public void init() {
		// TODO Auto-generated method stub

	}

	public boolean isSupportStatusTracking() {
		// TODO Auto-generated method stub
		return false;
	}

	public String queue(String arg0, String arg1, String arg2)
			throws SMSException {
		// TODO Auto-generated method stub
		return null;
	}

	public String queue(String arg0, String arg1, String arg2, String arg3)
			throws SMSException {
		// TODO Auto-generated method stub
		return null;
	}

	public void markAsRead(List<SMSMessage> arg0) throws SMSException {
		// TODO Auto-generated method stub

	}

	public List<SMSMessage> poll() throws SMSException {
		// TODO Auto-generated method stub
		return null;
	}

}
