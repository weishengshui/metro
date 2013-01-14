package com.chinarewards.metro.sms;

/**
 * Implementor should perform proper setup with the actual provider, like
 * setting up logging in with the account/password.
 * 
 * @author kmtong
 * 
 */
public interface ISMSSendQueue {

	/**
	 * called before any operations
	 */
	void init();

	/**
	 * called during system destroy, free resources here
	 */
	void destroy();

	/**
	 * 
	 * @param mobileNo
	 * @param message
	 * @return taskId for query
	 * @see #getStatus(String)
	 */
	String queue(String provider, String mobileNo, String message) throws SMSException;

	/**
	 * 
	 * @param mobileNo
	 * @param sourceNo
	 *            source number setup if any
	 * @param message
	 * @return taskId for query
	 * @see #getStatus(String)
	 */
	String queue(String provider, String sourceNo, String mobileNo,
			String message) throws SMSException;
	
	/**
	 * Use the taskId for checking the status of the SMS message.
	 * 
	 * @param taskId
	 * @return
	 */
	SMSStatus getStatus(String taskId) throws SMSException;

	/**
	 * indicate whether this provider support status tracking
	 * 
	 * @return
	 */
	boolean isSupportStatusTracking();
}
