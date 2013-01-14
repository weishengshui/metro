package com.chinarewards.metro.model.member;

import java.math.BigDecimal;
import java.util.Date;

import com.chinarewards.metro.domain.account.Business;
import com.chinarewards.metro.domain.account.Transaction;
import com.chinarewards.metro.domain.account.TxStatus;
import com.chinarewards.metro.domain.merchandise.Merchandise;
import com.chinarewards.metro.domain.shop.Shop;

/**
 * 积分记录
 * 
 * @author weishengshui
 * 
 */
public class IntegralGetRecordVo {

	// 交易编号
	private String businiessNo;
	// 交易时间
	private Date transactionDate;

	// 商家名称
	private Shop shop;
	// 交易类型
	private Transaction transaction;
	// 交易金额
	private BigDecimal txAmount;
	// 礼品名称---购买的商品名称
	private Merchandise merchandise;
	// 礼品数量
	private Integer giftCount;
	// 交易来源----使用积分时，如在商家兑换即商家名称，如在网上兑换即网站
	private String txSource;

	// 交易前积分
	private BigDecimal preTxIntegral;

	private BigDecimal integral;

	// 积分规则名称
	private String ruleName;

	public IntegralGetRecordVo(String businiessNo, Date transactionDate,
			Shop shop, Transaction transaction, BigDecimal txAmount,
			Merchandise merchandise, Integer giftCount, String txSource,
			BigDecimal preTxIntegral, BigDecimal integral, String ruleName) {

		this.businiessNo = businiessNo;
		this.transactionDate = transactionDate;
		this.shop = shop;
		this.transaction = transaction;
		this.txAmount = txAmount;
		this.merchandise = merchandise;
		this.giftCount = giftCount;
		this.txSource = txSource;
		this.preTxIntegral = preTxIntegral;
		this.integral = integral;
		this.ruleName = ruleName;
	}

	public IntegralGetRecordVo(String businiessNo) {
		this.businiessNo = businiessNo;

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

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public BigDecimal getTxAmount() {
		return txAmount;
	}

	public void setTxAmount(BigDecimal txAmount) {
		this.txAmount = txAmount;
	}

	public Merchandise getMerchandise() {
		return merchandise;
	}

	public void setMerchandise(Merchandise merchandise) {
		this.merchandise = merchandise;
	}

	public Integer getGiftCount() {
		return giftCount;
	}

	public void setGiftCount(Integer giftCount) {
		this.giftCount = giftCount;
	}

	public String getTxSource() {
		return txSource;
	}

	public void setTxSource(String txSource) {
		this.txSource = txSource;
	}

	public BigDecimal getPreTxIntegral() {
		return preTxIntegral;
	}

	public void setPreTxIntegral(BigDecimal preTxIntegral) {
		this.preTxIntegral = preTxIntegral;
	}

	public BigDecimal getIntegral() {
		return integral;
	}

	public void setIntegral(BigDecimal integral) {
		this.integral = integral;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	
	
}
