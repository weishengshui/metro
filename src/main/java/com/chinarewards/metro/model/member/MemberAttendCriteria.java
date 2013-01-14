package com.chinarewards.metro.model.member;

import com.chinarewards.metro.core.common.Page;

/**
 * 会员 短信发送历史记录 查询条件
 * 
 * @author weishengshui
 * 
 */
public class MemberAttendCriteria {

	private Integer id;// 会员id

	// 分页条件
	private Page paginationDetail;

	public MemberAttendCriteria() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Page getPaginationDetail() {
		return paginationDetail;
	}

	public void setPaginationDetail(Page paginationDetail) {
		this.paginationDetail = paginationDetail;
	}

}
