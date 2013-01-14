package com.chinarewards.metro.domain.message;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class MessageTelephone implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer telephoneId;
	private String telephone;

	@ManyToOne
	private MessageTask messageTask;
	private int states;//1-发送失败；2-发送成功
	@Temporal(TemporalType.TIMESTAMP) 
    private Date sendTime;
	
	public MessageTask getMessageTask() {
		return messageTask;
	}

	public void setMessageTask(MessageTask messageTask) {
		this.messageTask = messageTask;
	}

	

	public Integer getTelephoneId() {
		return telephoneId;
	}

	public void setTelephoneId(Integer telephoneId) {
		this.telephoneId = telephoneId;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public int getStates() {
		return states;
	}

	public void setStates(int states) {
		this.states = states;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

}
