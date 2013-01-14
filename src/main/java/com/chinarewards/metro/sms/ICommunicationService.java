package com.chinarewards.metro.sms;

import java.util.Date;

public interface ICommunicationService {

	/**
	 * to be called by web layer to trigger all internal processing of the
	 * queue.
	 * 
	 */
	public void heartbeat();

	/**
	 * Queue an SMS for sending, return the message ID for further processing.
	 * 
	 * @param provider
	 * @param destination
	 * @param content
	 * @param expectSendDate
	 * @return
	 */
	public Long queueSMS(String provider, String destId, String destination,
			String content, int priority, Date expectSendDate);
}
