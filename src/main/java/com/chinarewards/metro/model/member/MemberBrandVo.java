package com.chinarewards.metro.model.member;

import java.util.Date;

/**
 * 会员参加哪些品牌的联合会员VO
 * 
 * @author weishengshui
 * 
 */
public class MemberBrandVo {

	private String brandName;
	private Date joinedDate;
	
	public MemberBrandVo() {
	}
	
	public MemberBrandVo(String brandName,Date joinedDate) {
		this.brandName = brandName;
		this.joinedDate = joinedDate;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Date getJoinedDate() {
		return joinedDate;
	}

	public void setJoinedDate(Date joinedDate) {
		this.joinedDate = joinedDate;
	}

}
