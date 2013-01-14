package com.chinarewards.metro.domain.account;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Unit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4972133380657924259L;

	@Id
	// 见 Dictionary 常量定义
	private String unitId;

	private String unitCode;

	private String displayName;// 显示名称

	// 从获得开始计算，此货币的有效期,根据有效期单位计算,为空表示长期有效
	private Integer available;

	// 见 Dictionary 常量定义，积分有效期的单位
	private Integer availableUnit;

	// 对应人民币价值,例如 0.8 表示一个积分对应人民币8毛钱
	private double price;

	private int numberOfDecimals; // number of decimals for every unit

	@Column(updatable = false)
	@Temporal(TemporalType.DATE)
	private Date createdAt;

	@Column(updatable = false)
	private String createdBy;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date lastModifiedAt;

	private String lastModifiedBy;

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Integer getAvailable() {
		return available;
	}

	public void setAvailable(Integer available) {
		this.available = available;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public int getNumberOfDecimals() {
		return numberOfDecimals;
	}

	public void setNumberOfDecimals(int numberOfDecimals) {
		this.numberOfDecimals = numberOfDecimals;
	}

	public Integer getAvailableUnit() {
		return availableUnit;
	}

	public void setAvailableUnit(Integer availableUnit) {
		this.availableUnit = availableUnit;
	}

}
