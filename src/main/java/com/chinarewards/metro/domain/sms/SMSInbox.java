package com.chinarewards.metro.domain.sms;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

/**
 * SMS Inbox
 * 
 * @author kmtong
 * 
 */
@Entity
public class SMSInbox {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	String source;
	String destination;
	String content;
	Date receivedDate;
	String provider;
	String providerMessageId;
	@Enumerated(EnumType.STRING)
	SMSReceiveStatus status;

	@Version
	long optlock;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public SMSReceiveStatus getStatus() {
		return status;
	}

	public void setStatus(SMSReceiveStatus status) {
		this.status = status;
	}

	public long getOptlock() {
		return optlock;
	}

	public void setOptlock(long optlock) {
		this.optlock = optlock;
	}

	public String getProviderMessageId() {
		return providerMessageId;
	}

	public void setProviderMessageId(String providerMessageId) {
		this.providerMessageId = providerMessageId;
	}

}
