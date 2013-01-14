package com.chinarewards.metro.model.member;

import java.util.Date;

/**
 * 会员使用优惠码记录VO
 * 
 * @author weishengshui
 * 
 */
public class MemberDiscountNumberVo {

	private Integer txId; // 交易编号
	private Date transactionDate; // 交易时间
	private String discountNum; // 优惠码
	private String content; // 优惠内容
	private String sources; // 来源
	private int status; // 状态
	
	public MemberDiscountNumberVo(int txId, Date generatedDate, Date usedDate, String discountNum, String content, String sources, int status) {
		this.txId = txId;
		if(null != usedDate){  // 交易时间
			this.transactionDate = usedDate;
		}else{
			this.transactionDate = generatedDate;
		}
		this.discountNum = discountNum;
		this.content = content;
		this.sources = "";//sources==null?"":sources; // TODO
		this.status = status;
	}

	public Integer getTxId() {
		return txId;
	}
	public void setTxId(Integer txId) {
		this.txId = txId;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getDiscountNum() {
		return discountNum;
	}
	public void setDiscountNum(String discountNum) {
		this.discountNum = discountNum;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSources() {
		return sources;
	}
	public void setSources(String sources) {
		this.sources = sources;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	

}
