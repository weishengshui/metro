package com.chinarewards.metro.sms;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represent an SMS message for Sending/Receiving.
 * 
 * @author kmtong
 * 
 */
@XmlRootElement
public class SMSMessage {

	String id;

	// 短信发送优先级 有 ’1-5‘ 个级别，
	// 1.为最高级别，之后依次级别递减。
	// 3 为默认的普通级别。
	Integer priority = 3;

	String destination;
	String source;

	String content;
	String externalId;

	String provider;

	Date expectSendDate;

	Date dateReceived;
	Date dateSent;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
	}

	public Date getDateSent() {
		return dateSent;
	}

	public void setDateSent(Date dateSent) {
		this.dateSent = dateSent;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	/**
	 * 短信发送优先级 有 ’1-5‘ 个级别<BR>
	 * 1.为最高级别，之后依次级别递减。<BR>
	 * 3 为默认的普通级别。
	 * 
	 * @return
	 */
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	/**
	 * 短信期望发送日期
	 * 
	 * @return
	 */
	public Date getExpectSendDate() {
		return expectSendDate;
	}

	public void setExpectSendDate(Date expectSendDate) {
		this.expectSendDate = expectSendDate;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

}
