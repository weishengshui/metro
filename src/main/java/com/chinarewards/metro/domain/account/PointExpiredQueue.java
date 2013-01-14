package com.chinarewards.metro.domain.account;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class PointExpiredQueue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3740999235117485325L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	@ManyToOne
	private Transaction tx;

	@ManyToOne
	private AccountBalanceUnits accBalanceUnits;

	@Enumerated(EnumType.STRING)
	private QueueStatus status;

	private Date createdAt;

	private Date CompletedAt;

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

	public AccountBalanceUnits getAccBalanceUnits() {
		return accBalanceUnits;
	}

	public void setAccBalanceUnits(AccountBalanceUnits accBalanceUnits) {
		this.accBalanceUnits = accBalanceUnits;
	}

	public QueueStatus getStatus() {
		return status;
	}

	public void setStatus(QueueStatus status) {
		this.status = status;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getCompletedAt() {
		return CompletedAt;
	}

	public void setCompletedAt(Date completedAt) {
		CompletedAt = completedAt;
	}

}
