package com.chinarewards.metro.domain.pos;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.chinarewards.metro.core.common.DateTimeSerializer;

/**
 * POS机绑定
 * @author huangshan
 *
 */
@Entity
public class PosBind implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3025187170728080699L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String code;

	private Date bindDate;

	private Integer fId;  //门店/活动 Id

	@Column(nullable = false)
	private Date createdAt;

	private String createdBy;

	@Column(nullable = false)
	private Date lastModifiedAt;

	private String lastModifiedBy;

	private Integer mark; //0: 绑定活动, 1: 绑定门店
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getBindDate() {
		return bindDate;
	}

	public void setBindDate(Date bindDate) {
		this.bindDate = bindDate;
	}

	public Integer getfId() {
		return fId;
	}

	public void setfId(Integer fId) {
		this.fId = fId;
	}

	public Integer getMark() {
		return mark;
	}

	public void setMark(Integer mark) {
		this.mark = mark;
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

	@JsonSerialize(using = DateTimeSerializer.class)
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
