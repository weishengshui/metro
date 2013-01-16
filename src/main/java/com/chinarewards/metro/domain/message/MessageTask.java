package com.chinarewards.metro.domain.message;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;


@Entity
public class MessageTask implements Serializable{
	private static final long serialVersionUID = 1L;
	
	

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String taskId;
	
	private String taskName;
	private String content;
	
	@Temporal(TemporalType.TIMESTAMP) 
	private Date planSendTime;
	
	@Temporal(TemporalType.TIMESTAMP) 
	private Date actualSendTime;
	
	@Temporal(TemporalType.TIMESTAMP) 
	private Date endTime;
	private Integer taskStates;
	private int amount;
	@Transient
	private int successAmount;
	@Transient	
	private int failureAmount;
	@Transient	
	private int notSentAmount;
	@Transient	
	private File telephoneFile;
	
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public Date getPlanSendTime() {
		return planSendTime;
	}
	public void setPlanSendTime(Date planSendTime) {
		this.planSendTime = planSendTime;
	}
	public Date getActualSendTime() {
		return actualSendTime;
	}
	public void setActualSendTime(Date actualSendTime) {
		this.actualSendTime = actualSendTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getTaskStates() {
		return taskStates;
	}
	public void setTaskStates(Integer taskStates) {
		this.taskStates = taskStates;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getSuccessAmount() {
		return successAmount;
	}
	public void setSuccessAmount(int successAmount) {
		this.successAmount = successAmount;
	}
	public int getFailureAmount() {
		return failureAmount;
	}
	public void setFailureAmount(int failureAmount) {
		this.failureAmount = failureAmount;
	}
	public int getNotSentAmount() {
		return notSentAmount;
	}
	public void setNotSentAmount(int notSentAmount) {
		this.notSentAmount = notSentAmount;
	}
	public File getTelephoneFile() {
		return telephoneFile;
	}
	public void setTelephoneFile(File telephoneFile) {
		this.telephoneFile = telephoneFile;
	}
	
	
}
