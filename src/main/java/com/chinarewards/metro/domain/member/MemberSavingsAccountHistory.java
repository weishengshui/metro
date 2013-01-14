package com.chinarewards.metro.domain.member;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * 会员--储蓄账户历史记录，用于记录会员储蓄账户充值、使用的记录
 * 
 * @author weishengshui
 * 
 */

@Entity
public class MemberSavingsAccountHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	// 交易编号
	private String businiessNo;
	// 交易时间
	private Date transactionDate;

	// 交易来源----如在实体店消费就是商家名称，另外就是网站上消费了
	private String txSource;

	// 礼品名称---购买的商品名称
	private String giftName;
	// 礼品数量
	private Integer giftCount;
	// 交易前余额
	private double preTxBalance;

	// 大于0为充值，小于0为消费
	private double balance;

	// 状态--完成、撤销
	private BalanceStatus status;

	@ManyToOne
	private Member member;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBusiniessNo() {
		return businiessNo;
	}

	public void setBusiniessNo(String businiessNo) {
		this.businiessNo = businiessNo;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getTxSource() {
		return txSource;
	}

	public void setTxSource(String txSource) {
		this.txSource = txSource;
	}

	public String getGiftName() {
		return giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public Integer getGiftCount() {
		return giftCount;
	}

	public void setGiftCount(Integer giftCount) {
		this.giftCount = giftCount;
	}

	public double getPreTxBalance() {
		return preTxBalance;
	}

	public void setPreTxBalance(double preTxBalance) {
		this.preTxBalance = preTxBalance;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public BalanceStatus getStatus() {
		return status;
	}

	public void setStatus(BalanceStatus status) {
		this.status = status;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

}
