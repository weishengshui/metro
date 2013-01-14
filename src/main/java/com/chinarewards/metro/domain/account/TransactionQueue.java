package com.chinarewards.metro.domain.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * 交易冻结队列明细,当交易提交过来需要冻结<br>
 * 记录此表 解冻交易会根据这张表的数据，处理交易
 * 
 * @author qingminzou
 * 
 */
@Entity
public class TransactionQueue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8830279770946125790L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@ManyToOne
	private Transaction tx;

	// 0 deposit , 1 withdrawal
	private int type;

	// 积分单位
	private String unitCode;

	private BigDecimal units;

	@ManyToOne
	private Account account;

	private Date createdAt;

	private String createdBy;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Transaction getTx() {
		return tx;
	}

	public void setTx(Transaction tx) {
		this.tx = tx;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public BigDecimal getUnits() {
		return units;
	}

	public void setUnits(BigDecimal units) {
		this.units = units;
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

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}
