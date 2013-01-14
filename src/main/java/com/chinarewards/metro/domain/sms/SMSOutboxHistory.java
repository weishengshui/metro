package com.chinarewards.metro.domain.sms;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

/**
 * Outbox queue history
 * 
 * @author kmtong
 * 
 */
@Entity
public class SMSOutboxHistory {

	@Id
	Long id;
	String source; // added in 2.1.0
	String destination;
	String content;
	Date submitDate;
	Date sentDate;
	String provider;
	String providerUsed;
	String providerTaskId;
	@Enumerated(EnumType.STRING)
	SMSSendStatus status;
	Integer failCount = 0;
	Date lastFail;
	Integer priority = 3;
	Date expectSendDate;
	long optlock;
	
	String destId ;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public SMSSendStatus getStatus() {
		return status;
	}

	public void setStatus(SMSSendStatus status) {
		this.status = status;
	}

	public long getOptlock() {
		return optlock;
	}

	public void setOptlock(long optlock) {
		this.optlock = optlock;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getProviderUsed() {
		return providerUsed;
	}

	public void setProviderUsed(String providerUsed) {
		this.providerUsed = providerUsed;
	}

	/**
	 * used for status tracking for the provider
	 * 
	 * @return
	 */
	public String getProviderTaskId() {
		return providerTaskId;
	}

	public void setProviderTaskId(String providerTaskId) {
		this.providerTaskId = providerTaskId;
	}

	public Date getLastFail() {
		return lastFail;
	}

	public void setLastFail(Date lastFail) {
		this.lastFail = lastFail;
	}

	public Integer getFailCount() {
		return failCount;
	}

	public void setFailCount(Integer failCount) {
		this.failCount = failCount;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Date getExpectSendDate() {
		return expectSendDate;
	}

	public void setExpectSendDate(Date expectSendDate) {
		this.expectSendDate = expectSendDate;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestId() {
		return destId;
	}

	public void setDestId(String destId) {
		this.destId = destId;
	}

}
