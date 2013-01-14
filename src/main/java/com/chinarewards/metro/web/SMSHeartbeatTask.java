package com.chinarewards.metro.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chinarewards.metro.sms.ICommunicationService;

@Component
public class SMSHeartbeatTask {

	Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private ICommunicationService communicationService;

	public void doIt() {
		try {
			communicationService.heartbeat();
		} catch (Throwable e) {
			log.error("Heartbeat Error from Session Bean", e);
		}
	}

	public ICommunicationService getCommunicationService() {
		return communicationService;
	}

	public void setCommunicationService(
			ICommunicationService communicationService) {
		this.communicationService = communicationService;
	}
}
