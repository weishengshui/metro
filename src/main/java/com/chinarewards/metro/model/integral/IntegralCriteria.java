package com.chinarewards.metro.model.integral;

import java.util.Date;

import com.chinarewards.metro.core.common.Page;

public class IntegralCriteria {

	// 操作员
	private String operationPeople;
	// 操作时间段
	private Date start;
	private Date end;

	private Page paginationDetail;

	public Page getPaginationDetail() {
		return paginationDetail;
	}

	public void setPaginationDetail(Page paginationDetail) {
		this.paginationDetail = paginationDetail;
	}

	public String getOperationPeople() {
		return operationPeople;
	}

	public void setOperationPeople(String operationPeople) {
		this.operationPeople = operationPeople;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

}
