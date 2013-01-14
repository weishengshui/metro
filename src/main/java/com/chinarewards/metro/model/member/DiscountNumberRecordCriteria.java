package com.chinarewards.metro.model.member;

import java.util.Date;

import com.chinarewards.metro.core.common.Page;

public class DiscountNumberRecordCriteria {

	private Integer id;
	//优惠码
	private String discountNum;
	// 优惠码状态
	private Integer discountNumStatus;

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

	public String getDiscountNum() {
		return discountNum;
	}

	public void setDiscountNum(String discountNum) {
		this.discountNum = discountNum;
	}

	public Integer getDiscountNumStatus() {
		return discountNumStatus;
	}

	public void setDiscountNumStatus(Integer discountNumStatus) {
		this.discountNumStatus = discountNumStatus;
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

	public Page getPaginationDetail() {
		return paginationDetail;
	}

	public void setPaginationDetail(Page paginationDetail) {
		this.paginationDetail = paginationDetail;
	}
	
	

}
