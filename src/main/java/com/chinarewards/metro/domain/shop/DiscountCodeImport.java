package com.chinarewards.metro.domain.shop;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DiscountCodeImport implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -2260106073967586193L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id ;
	
	private Date importDate ;
	
	private String discountNum ;
	
	private Integer isRecived; 
	
	private Integer shopId;

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getImportDate() {
		return importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	public String getDiscountNum() {
		return discountNum;
	}

	public void setDiscountNum(String discountNum) {
		this.discountNum = discountNum;
	}

	public Integer getIsRecived() {
		return isRecived;
	}

	public void setIsRecived(Integer isRecived) {
		this.isRecived = isRecived;
	}

}
