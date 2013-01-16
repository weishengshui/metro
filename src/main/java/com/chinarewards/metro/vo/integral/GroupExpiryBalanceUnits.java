package com.chinarewards.metro.vo.integral;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class GroupExpiryBalanceUnits {

	private String transactionNo;

	private String opt;

	private Date transactionDate;

	private String status;

	private long countMembers;

	private double amountPoints;
	
	public Set<String> accounts = new HashSet<String>();

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getCountMembers() {
		return countMembers;
	}

	public void setCountMembers(long countMembers) {
		this.countMembers = countMembers;
	}

	public double getAmountPoints() {
		return amountPoints;
	}

	public void setAmountPoints(double amountPoints) {
		this.amountPoints = amountPoints;
	}

	public Set<String> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<String> accounts) {
		this.accounts = accounts;
	}
}
