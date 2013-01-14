package com.chinarewards.metro.domain.sms;

public enum SMSReceiveStatus {

	/**
	 * new SMS received
	 */
	NEW,

	/**
	 * mark as read
	 */
	READ,

	/**
	 * ready to be erased by timer jobs
	 */
	DESTROYABLE

}
