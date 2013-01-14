package com.chinarewards.metro.sms;

import java.util.List;

import com.chinarewards.metro.domain.sms.SMSOutbox;

/**
 * Services used internally by CommunicationService.
 * 
 * @author kmtong
 * 
 */
public interface IQueueProcessor {

	/**
	 * Dequeue the SMSOutbox, set the status to READY. It will open a new
	 * transaction for this purpose.
	 * 
	 * @return
	 */
	List<SMSOutbox> dequeueSMSOutbox();

	/**
	 * Use the dequeued List of SMSOutbox to send according to the specified SMS
	 * Provider. Status changed to SENDING. It will open a new transaction for
	 * this purpose.
	 * 
	 * @param outbox
	 */
	void delegateToSMSProvider(List<SMSOutbox> outbox);

	/**
	 * Calls for each heartbeat to update any progress change from the provider
	 */
	void updateSMSSendingProgress();

	/**
	 * Try to fix previous errors by trying the next provider, and then update
	 * the status back to QUEUE for next heartbeat processing
	 */
	void processPreviousErrors();

	/**
	 * Inbox handling, MAS only now
	 */
	void processSMSInbox();

	/**
	 * Obtain SMS send provider from code.
	 * 
	 * @param code
	 * @return
	 */
	ISMSSendQueue getSMSSendProvider(String code);

	/**
	 * logic to try different providers.
	 * 
	 * @param providerUsed
	 * @return
	 */
	String tryNextProvider(String providerUsed);

	/**
	 * Backup the outbox for better performance
	 */
	void archiveOutbox();

	/**
	 * Lifecycle methods
	 */
	void initAllSP();

	/**
	 * Lifecycle methods
	 */
	void destroyAllSP();

}
