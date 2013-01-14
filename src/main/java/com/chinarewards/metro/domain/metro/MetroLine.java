package com.chinarewards.metro.domain.metro;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Data ref:http://map.baidu.com/subways/data/sw_shanghai.xml?v=20111010
 * 
 * @author qingminzou
 * 
 */
@Entity
public class MetroLine implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7462361956768433094L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO )
	private Integer id;

	private String name;

	private String numno;

	private String descs;

	@OneToMany(mappedBy = "line",cascade=CascadeType.ALL)
	private List<MetroLineSite> lineSites;
	
	@Transient
	private Integer c;
	
	@Transient
	private Integer orderNo;
	
	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getC() {
		return c;
	}

	public void setC(Integer c) {
		this.c = c;
	}

	public Integer getId() {
		return id;
	}

	@JsonIgnore
	public List<MetroLineSite> getLineSites() {
		return lineSites;
	}

	public void setLineSites(List<MetroLineSite> lineSites) {
		this.lineSites = lineSites;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumno() {
		return numno;
	}

	public void setNumno(String numno) {
		this.numno = numno;
	}

	public String getDescs() {
		return descs;
	}

	public void setDescs(String descs) {
		this.descs = descs;
	}

}
