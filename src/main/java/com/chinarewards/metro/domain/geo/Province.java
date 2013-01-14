package com.chinarewards.metro.domain.geo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Province implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7253476739343561861L;

	@Id
	private int id;
	
	private String name ;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
