package com.chinarewards.metro.model.member;

import java.util.Date;

import com.chinarewards.metro.domain.sms.SMSSendStatus;

/**
 * 会员 发送短信历史记录VO
 * 
 * @author weishengshui
 * 
 */
public class MemberSMSOutboxHistoryVo {

	private String phoneNumber; // 手机号
	private String content; // 发送内容
	private Date sentDate; // 发送时间
	private SMSSendStatus status; // 发送状态

	public MemberSMSOutboxHistoryVo() {
	}

	public MemberSMSOutboxHistoryVo(String phoneNumber, String content,
			Date sentDate, SMSSendStatus status) {
		this.phoneNumber = phoneNumber;
		this.content = content;
		this.sentDate = sentDate;
		this.status = status;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

}
