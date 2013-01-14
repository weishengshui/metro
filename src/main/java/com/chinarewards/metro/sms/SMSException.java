package com.chinarewards.metro.sms;

public class SMSException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4738370182186206805L;

	public SMSException() {
		super();
	}

	public SMSException(String message) {
		super(message);
	}

	public SMSException(String message, Throwable cause) {
		super(message, cause);
	}

	public SMSException(Throwable cause) {
		super(cause);
	}
}
