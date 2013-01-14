package com.chinarewards.metro.domain.account;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
public class Transaction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1282692012693272974L;

	@Id
	private String txId;

	@Enumerated(EnumType.STRING)
	private TxStatus status;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date transactionDate;

	@Enumerated(EnumType.STRING)
	private Business busines;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date createdAt;

	private String createdBy;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date lastModifiedAt;

	private String lastModifiedBy;

	@Version
	private Long version;

	public String getTxId() {
		return txId;
	}

	public void setTxId(String txId) {
		this.txId = txId;
	}

	public TxStatus getStatus() {
		return status;
	}

	public void setStatus(TxStatus status) {
		this.status = status;
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

	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Business getBusines() {
		return busines;
	}

	public void setBusines(Business busines) {
		this.busines = busines;
	}
}
