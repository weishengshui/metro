package com.chinarewards.metro.domain.sms;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

/**
 * Stores the Priority -> Time Not Available for Sending. TimeRange is in the
 * following format:
 * 
 * hh:mm-hh:mm,hh:mm-hh:mm,...
 * 
 * It is a comma-separated of hh:mm-hh:mm time range. Under the specified
 * time-ranges, the CGW will not send any message of the specified priority.
 * 
 * The time range specified is INCLUSIVE, meaning 10:30-10:30 will check from
 * range 10:30:00.000 to 10:30:59.999.
 * 
 * @author kmtong
 * 
 */
@Entity
public class PriorityPeriodLimit {

	@Id
	int priority;

	String timeRange;

	@Version
	Date lastMod;

	public Date getLastMod() {
		return lastMod;
	}

	public void setLastMod(Date lastMod) {
		this.lastMod = lastMod;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getTimeRange() {
		return timeRange;
	}

	public void setTimeRange(String timeRange) {
		this.timeRange = timeRange;
	}

}
