package com.chinarewards.metro.domain.activity;

import java.util.Date;

public class BrandMode {
	private int gid ;
	private String brandId ;
	private String activityId ;
	private String name ;
	private String companyName ;
	private Date joinTime ;
	
	public BrandMode(int gid, String brandId, String activityId, String name,
			String companyName, Date joinTime) {
		super();
		this.gid = gid;
		this.brandId = brandId;
		this.activityId = activityId;
		this.name = name;
		this.companyName = companyName;
		this.joinTime = joinTime;
	}
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
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
	public Date getJoinTime() {
		return joinTime;
	}
	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}
}
