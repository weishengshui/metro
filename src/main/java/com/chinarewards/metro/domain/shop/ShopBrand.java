package com.chinarewards.metro.domain.shop;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.chinarewards.metro.domain.brand.Brand;

@Entity
public class ShopBrand implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3596075934980955022L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@ManyToOne
	private Brand brand;

	@Column(name = "shop_id")
	private Integer shopId;
	
	private Date joinDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}
	
}
