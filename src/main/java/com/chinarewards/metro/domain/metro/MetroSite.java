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
public class MetroSite implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6222134011143605636L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO )
	private Integer id;

	private String name;
	
	private String descs;
	
	@OneToMany(mappedBy = "site",cascade=CascadeType.ALL)
	private List<MetroLineSite> lineSites;
	
	@Transient
	private Integer orderNo;
	
	@Transient
	private String lineName;
	
	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getDescs() {
		return descs;
	}

	public void setDescs(String descs) {
		this.descs = descs;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getId() {
		return id;
	}
	
	@JsonIgnore
	public List<MetroLineSite> getLineSites() {
		return lineSites;
	}

	@JsonIgnore
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

}
