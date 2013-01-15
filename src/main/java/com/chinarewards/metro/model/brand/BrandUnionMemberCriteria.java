package com.chinarewards.metro.model.brand;

import java.util.Date;

import com.chinarewards.metro.core.common.Page;

public class BrandUnionMemberCriteria {
	
	private Integer brandId;
	private String memberName;
	private String cardNumber;
	private Date joinedStart;
	private Date joinedEnd;

	private Page paginationDetail;

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Date getJoinedStart() {
		return joinedStart;
	}

	public void setJoinedStart(Date joinedStart) {
		this.joinedStart = joinedStart;
	}

	public Date getJoinedEnd() {
		return joinedEnd;
	}

	public void setJoinedEnd(Date joinedEnd) {
		this.joinedEnd = joinedEnd;
	}

	public Page getPaginationDetail() {
		return paginationDetail;
	}

	public void setPaginationDetail(Page paginationDetail) {
		this.paginationDetail = paginationDetail;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}
	
	

}
