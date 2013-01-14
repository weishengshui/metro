package com.chinarewards.metro.model.message;

public class MessageCount {
    private String taskid;
    private int failureAmount;
    private int successAmount;
    private int notSentAmount;
    
    
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public int getFailureAmount() {
		return failureAmount;
	}
	public void setFailureAmount(int failureAmount) {
		this.failureAmount = failureAmount;
	}
	public int getSuccessAmount() {
		return successAmount;
	}
	public void setSuccessAmount(int successAmount) {
		this.successAmount = successAmount;
	}
	public int getNotSentAmount() {
		return notSentAmount;
	}
	public void setNotSentAmount(int notSentAmount) {
		this.notSentAmount = notSentAmount;
	}
    
    
    
}
