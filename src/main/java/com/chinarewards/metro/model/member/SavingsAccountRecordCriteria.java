package com.chinarewards.metro.model.member;

import java.util.Date;

import com.chinarewards.metro.core.common.Page;

/**
 * 储蓄账户记录查询条件
 * 
 * @author weishengshui
 *
 */
public class SavingsAccountRecordCriteria {

	// member id
	private Integer id;
	// 交易编号
	private String businiessNo;
	// 交易时间段
	private Date transactionDateStart;
	private Date transactionDateEnd;
	// 分页条件
	private Page paginationDetail;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Page getPaginationDetail() {
		return paginationDetail;
	}

	public void setPaginationDetail(Page paginationDetail) {
		this.paginationDetail = paginationDetail;
	}

	public String getBusiniessNo() {
		return businiessNo;
	}

	public void setBusiniessNo(String businiessNo) {
		this.businiessNo = businiessNo;
	}

	public Date getTransactionDateStart() {
		return transactionDateStart;
	}

	public void setTransactionDateStart(Date transactionDateStart) {
		this.transactionDateStart = transactionDateStart;
	}

	public Date getTransactionDateEnd() {
		return transactionDateEnd;
	}

	public void setTransactionDateEnd(Date transactionDateEnd) {
		this.transactionDateEnd = transactionDateEnd;
	}

}
