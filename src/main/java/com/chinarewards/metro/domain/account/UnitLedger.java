package com.chinarewards.metro.domain.account;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 积分基本信息维护历史
 */
@Entity
public class UnitLedger implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8693040717043328241L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	// 见 Dictionary 常量定义
	private String unitId;

	private String unitCode;

	private String displayName;

	// 从获得开始计算，此货币的有效期,最小单位按天来计算,为空表示长期有效
	private Integer available;

	// 见 Dictionary 常量定义，积分有效期的单位
	private Integer availableUnit;

	// 对应人民币价值,例如 0.8 表示一个积分对应人民币8毛钱
	private double price;

	private int numberOfDecimals; // number of decimals for every unit

	// 操作时间
	@Column(nullable = false)
	private Date operationTime;

	// 操作人
	private String operationPeople;

	public UnitLedger() {
	}

	public UnitLedger(Unit unit) {
		this.unitId = unit.getUnitId();
		this.unitCode = unit.getUnitCode();
		this.displayName = unit.getDisplayName();
		this.available = unit.getAvailable();
		this.availableUnit = unit.getAvailableUnit();
		this.price = unit.getPrice();
		this.numberOfDecimals = unit.getNumberOfDecimals();
		this.operationTime = unit.getLastModifiedAt();
		this.operationPeople = unit.getLastModifiedBy();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public Date getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}

	public String getOperationPeople() {
		return operationPeople;
	}

	public void setOperationPeople(String operationPeople) {
		this.operationPeople = operationPeople;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
