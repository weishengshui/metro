package com.chinarewards.metro.domain.metro;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class MetroLineSite implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1119359406108424513L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO )
	private Integer id;

	private Integer orderNo;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "lineId", unique = false)
	private MetroLine line;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "siteId", unique = false)
	private MetroSite site;
	
	@Transient
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public MetroLine getLine() {
		return line;
	}

	public void setLine(MetroLine line) {
		this.line = line;
	}

	public MetroSite getSite() {
		return site;
	}

	public void setSite(MetroSite site) {
		this.site = site;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
}
