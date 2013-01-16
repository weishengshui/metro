package com.chinarewards.metro.vo.integral;

import java.util.Date;

public class ExpiryBalanceUnits {

	private String id;

	private String memberName;

	private String memberCard;

	private Date obtainedAt;

	private String opt;

	private double units;

	private String transactionNo;

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberCard() {
		return memberCard;
	}

	public void setMemberCard(String memberCard) {
		this.memberCard = memberCard;
	}

	public Date getObtainedAt() {
		return obtainedAt;
	}

	public void setObtainedAt(Date obtainedAt) {
		this.obtainedAt = obtainedAt;
	}

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}

	public double getUnits() {
		return units;
	}

	public void setUnits(double units) {
		this.units = units;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
