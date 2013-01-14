package com.chinarewards.metro.domain.member;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.chinarewards.metro.domain.account.Business;

/**
 * 会员--积分账户历史记录，用于记录会员积分账户获取积分、使用积分的记录
 * 
 * @author weishengshui
 * 
 */
@Entity
public class MemberIntegralAccountHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	// 交易编号
	private String businiessNo;
	// 交易时间
	private Date transactionDate;

	// 商家名称
	private String shopName;
	// 交易类型
	@Enumerated(EnumType.STRING)
	private Business business;
	// 交易金额
	private double txAmount;
	// 礼品名称---购买的商品名称
	private String giftName;
	// 礼品数量
	private Integer giftCount;
	// 交易来源----使用积分时，如在商家兑换即商家名称，如在网上兑换即网站
	private String txSource;

	// 交易前积分
	private double preTxIntegral;

	// 大于0为获得积分，小于0为使用积分
	private double integral;

	// 积分规则名称
	private String ruleName;
	// 积分状态
	private IntegralStatus status;

	@ManyToOne
	private Member member;

	public MemberIntegralAccountHistory() {
	}

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

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	public double getTxAmount() {
		return txAmount;
	}

	public void setTxAmount(double txAmount) {
		this.txAmount = txAmount;
	}

	public String getGiftName() {
		return giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public String getTxSource() {
		return txSource;
	}

	public void setTxSource(String txSource) {
		this.txSource = txSource;
	}

	public double getPreTxIntegral() {
		return preTxIntegral;
	}

	public void setPreTxIntegral(double preTxIntegral) {
		this.preTxIntegral = preTxIntegral;
	}

	public double getIntegral() {
		return integral;
	}

	public void setIntegral(double integral) {
		this.integral = integral;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public IntegralStatus getStatus() {
		return status;
	}

	public void setStatus(IntegralStatus status) {
		this.status = status;
	}

	public Integer getGiftCount() {
		return giftCount;
	}

	public void setGiftCount(Integer giftCount) {
		this.giftCount = giftCount;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

}
