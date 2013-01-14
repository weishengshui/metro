package com.chinarewards.metro.domain.geo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Region implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3466588679187847410L;

	@Id
	private int id;
	
	private String name ;
	
	@ManyToOne
	private City city ;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
