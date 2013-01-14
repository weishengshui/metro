package com.chinarewards.metro.domain.rules;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.chinarewards.metro.core.common.DateSerializer;

@Entity
public class IntegralRule implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3174229799797583622L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id ;

	private String ruleName;
	// 倍数
	private Double times;
	
	// 消费时间区间定义开始
	private Date rangeFrom ;
	
	// 消费时间区间定义结束
	private Date rangeTo ;
	
	// 年龄区间定义开始
	private Integer rangeAgeFrom; 
	
	// 年龄区间定义结束
	private Integer rangeAgeTo;
	
	// 性别 (见Dictionary字典类)
	private Integer gender;
	
	// 消费金额区间定义开始
	private Double AmountConsumedFrom;
	
	// 消费金额区间定义开始
	private Double AmountConsumedTo;

	@Column(nullable = false,updatable = false)
	private Date createdAt;

	@Column(updatable = false)
	private String createdBy;

	@Column(nullable = false)
	private Date lastModifiedAt;

	private String lastModifiedBy;
	
	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getTimes() {
		return times;
	}

	public void setTimes(Double times) {
		this.times = times;
	}

	@JsonSerialize(using = DateSerializer.class)
	public Date getRangeFrom() {
		return rangeFrom;
	}

	public void setRangeFrom(Date rangeFrom) {
		this.rangeFrom = rangeFrom;
	}

	@JsonSerialize(using = DateSerializer.class)
	public Date getRangeTo() {
		return rangeTo;
	}

	public void setRangeTo(Date rangeTo) {
		this.rangeTo = rangeTo;
	}

	public Integer getRangeAgeFrom() {
		return rangeAgeFrom;
	}

	public void setRangeAgeFrom(Integer rangeAgeFrom) {
		this.rangeAgeFrom = rangeAgeFrom;
	}

	public Integer getRangeAgeTo() {
		return rangeAgeTo;
	}

	public void setRangeAgeTo(Integer rangeAgeTo) {
		this.rangeAgeTo = rangeAgeTo;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Double getAmountConsumedFrom() {
		return AmountConsumedFrom;
	}

	public void setAmountConsumedFrom(Double amountConsumedFrom) {
		AmountConsumedFrom = amountConsumedFrom;
	}

	public Double getAmountConsumedTo() {
		return AmountConsumedTo;
	}

	public void setAmountConsumedTo(Double amountConsumedTo) {
		AmountConsumedTo = amountConsumedTo;
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
	
}

