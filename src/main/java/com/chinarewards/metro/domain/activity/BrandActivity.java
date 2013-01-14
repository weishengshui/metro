package com.chinarewards.metro.domain.activity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.chinarewards.metro.domain.brand.Brand;

@Entity
public class BrandActivity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int gid;

	@ManyToOne
	private ActivityInfo activityInfo;

	public ActivityInfo getActivityInfo() {
		return activityInfo;
	}
	public void setActivityInfo(ActivityInfo activityInfo) {
		this.activityInfo = activityInfo;
	}
	private Date joinTime;
	@ManyToOne
	private Brand brand;

	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public Date getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

}
