package com.chinarewards.metro.model.member;

import java.util.Date;

import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.domain.sms.SMSSendStatus;

/**
 * 会员 短信发送历史记录 查询条件
 * 
 * @author weishengshui
 * 
 */
public class MemberSMSOutboxHistoryCriteria {

	private Integer id;// 会员id

	private SMSSendStatus status; // 发送状态
	// 发送时间段
	private Date sentDateStart;
	private Date sentDateEnd;

	// 分页条件
	private Page paginationDetail;

	public MemberSMSOutboxHistoryCriteria() {
	}

	public SMSSendStatus getStatus() {
		return status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setStatus(SMSSendStatus status) {
		this.status = status;
	}

	public Date getSentDateStart() {
		return sentDateStart;
	}

	public void setSentDateStart(Date sentDateStart) {
		this.sentDateStart = sentDateStart;
	}

	public Date getSentDateEnd() {
		return sentDateEnd;
	}

	public void setSentDateEnd(Date sentDateEnd) {
		this.sentDateEnd = sentDateEnd;
	}

	public Page getPaginationDetail() {
		return paginationDetail;
	}

	public void setPaginationDetail(Page paginationDetail) {
		this.paginationDetail = paginationDetail;
	}
	
	
}
