package com.chinarewards.metro.sms;

import java.util.List;

/**
 * Implementor should perform proper setup with the actual provider, like
 * setting up logging in with the account/password.
 * 
 * @author kmtong
 * 
 */
public interface ISMSReceiveQueue {

	/**
	 * called before any operations
	 */
	void init();

	/**
	 * called during system destroy, free resources here
	 */
	void destroy();

	/**
	 * return new messages (not yet marked as read)
	 * 
	 * @return
	 * @throws SMSException
	 */
	List<SMSMessage> poll() throws SMSException;

	/**
	 * Mark messages as READ
	 * 
	 * @param messages
	 * @throws SMSException
	 */
	void markAsRead(List<SMSMessage> messages) throws SMSException;
}
