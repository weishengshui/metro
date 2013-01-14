package com.chinarewards.metro.domain.sms;

public enum SMSSendStatus {

	/**
	 * ready to send
	 */
	QUEUED,

	/**
	 * sending
	 */
	SENDING,

	/**
	 * Sent
	 */
	SENT,

	/**
	 * Send ERROR
	 */
	ERROR,

	/**
	 * hold, to be released to QUEUED
	 */
	PAUSED,

	/**
	 * ready to be erased by timer jobs
	 */
	DESTROYABLE,

	/**
	 * dequeued and about to send
	 */
	READY
	
}
