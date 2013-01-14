package com.chinarewards.metro.domain.geo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class City implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1830835893788137566L;

	@Id
	private int id;
	
	private String name ;
	
	private String codeName;
	
	@ManyToOne
	private Province province ;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
