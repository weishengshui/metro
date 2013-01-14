package com.chinarewards.metro.domain.account;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class AccountBalanceUnits implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5027144968787918174L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	@ManyToOne
	private Transaction tx;

	@ManyToOne
	private AccountBalance accountBalance;

	private double units;

	private double unitPrice;
	
	/**
	 * 是否失效
	 */
	private boolean expired;

	@Temporal(TemporalType.DATE)
	private Date expDate;

	@ManyToOne
	private Unit unit;

	private Date createdAt;

	private String createdBy;

	private Date lastUpadtedAt;

	private String lastUpdatedBy;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Transaction getTx() {
		return tx;
	}

	public void setTx(Transaction tx) {
		this.tx = tx;
	}

	public AccountBalance getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(AccountBalance accountBalance) {
		this.accountBalance = accountBalance;
	}

	public double getUnits() {
		return units;
	}

	public void setUnits(double units) {
		this.units = units;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Date getExpDate() {
		return expDate;
	}

	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastUpadtedAt() {
		return lastUpadtedAt;
	}

	public void setLastUpadtedAt(Date lastUpadtedAt) {
		this.lastUpadtedAt = lastUpadtedAt;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}
}
