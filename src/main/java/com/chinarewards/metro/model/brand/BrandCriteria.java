package com.chinarewards.metro.model.brand;

import java.util.Date;

import com.chinarewards.metro.core.common.Page;

public class BrandCriteria {
	
	//品牌名称
	private String name;
	//公司名称
	private String companyName;
	//品牌创建时间范围
	private Date createStart;
	private Date createEnd;
	//是否申请联合会员
	private String unionInvited;
	
	private Page paginationDetail;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Date getCreateStart() {
		return createStart;
	}

	public void setCreateStart(Date createStart) {
		this.createStart = createStart;
	}

	public Date getCreateEnd() {
		return createEnd;
	}

	public void setCreateEnd(Date createEnd) {
		this.createEnd = createEnd;
	}

	public String getUnionInvited() {
		return unionInvited;
	}

	public void setUnionInvited(String unionInvited) {
		this.unionInvited = unionInvited;
	}

	public Page getPaginationDetail() {
		return paginationDetail;
	}

	public void setPaginationDetail(Page paginationDetail) {
		this.paginationDetail = paginationDetail;
	}
	
	
	
}
