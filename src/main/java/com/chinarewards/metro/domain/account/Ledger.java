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
public class Ledger implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 847217523921871763L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	public Ledger() {
	}

	public Ledger(Date expDate, String unitCode, double unitPrice,
			double units, Transaction transaction, Account account) {
		super();
		this.expDate = expDate;
		this.unitCode = unitCode;
		this.unitPrice = unitPrice;
		this.units = units;
		this.transaction = transaction;
		this.account = account;
	}

	/**
	 * 预计积分过期日期
	 */
	@Temporal(TemporalType.DATE)
	private Date expDate;

	/**
	 * 积分单位
	 */
	private String unitCode;

	/**
	 * 积分价值
	 */
	double unitPrice;

	/**
	 * 积分数量
	 */
	double units;

	@ManyToOne(optional = false)
	Transaction transaction;

	@ManyToOne(optional = false)
	Account account;

	public Date getExpDate() {
		return expDate;
	}

	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
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

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
