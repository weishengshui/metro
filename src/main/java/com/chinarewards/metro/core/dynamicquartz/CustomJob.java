
package com.chinarewards.metro.core.dynamicquartz;



public class CustomJob {
	public static final int JS_ENABLED = 0; 
	public static final int JS_DISABLED = 1; 
    public static final int JS_DELETE=2;
	
	private String jobId; 
	private String jobName; 
	private String jobGroup; 
	private int jobStatus; 
	private String cronExpression;
	private String memos;
	private Class<?> stateFulljobExecuteClass;
	private Class<?> jobExecuteClass;

	/**
	 * @return the jobId
	 */
	public String getJobId() {
		return jobId;
	}

	/**
	 * @param jobId
	 *            the jobId to set
	 */
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	/**
	 * @return the jobName
	 */
	public String getJobName() {
		return jobName;
	}

	/**
	 * @param jobName
	 *            the jobName to set
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	/**
	 * @return the jobGroup
	 */
	public String getJobGroup() {
		return jobGroup;
	}

	/**
	 * @param jobGroup
	 *            the jobGroup to set
	 */
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	/**
	 * @return the jobStatus
	 */
	public int getJobStatus() {
		return jobStatus;
	}

	/**
	 * @param jobStatus
	 *            the jobStatus to set
	 */
	public void setJobStatus(int jobStatus) {
		this.jobStatus = jobStatus;
	}

	/**
	 * @return the cronExpression
	 */
	public String getCronExpression() {
		return cronExpression;
	}

	/**
	 * @param cronExpression
	 *            the cronExpression to set
	 */
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	/**
	 * @return the memos
	 */
	public String getMemos() {
		return memos;
	}

	/**
	 * @param memos
	 *            the memos to set
	 */
	public void setMemos(String memos) {
		this.memos = memos;
	}

	
	
	

	/**
	 * @return the stateFulljobExecuteClass
	 */
	public Class<?> getStateFulljobExecuteClass() {
		return stateFulljobExecuteClass;
	}

	/**
	 * @param stateFulljobExecuteClass the stateFulljobExecuteClass to set
	 */
	public void setStateFulljobExecuteClass(Class<?> stateFulljobExecuteClass) {
		this.stateFulljobExecuteClass = stateFulljobExecuteClass;
	}

	/**
	 * @return the jobExecuteClass
	 */
	public Class<?> getJobExecuteClass() {
		return jobExecuteClass;
	}

	/**
	 * @param jobExecuteClass the jobExecuteClass to set
	 */
	public void setJobExecuteClass(Class<?> jobExecuteClass) {
		this.jobExecuteClass = jobExecuteClass;
	}

	
	public String getTriggerName() {
		return this.getJobId() + "Trigger";
	}
}
