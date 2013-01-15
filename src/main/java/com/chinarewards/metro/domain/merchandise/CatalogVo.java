package com.chinarewards.metro.domain.merchandise;

/**
 * 
 * @author weishengshui
 * 
 */
public class CatalogVo {

	private String unitId;
	private Double price;
	
	public CatalogVo(String unitId, Double price){
		this.unitId = unitId;
		this.price = price;
	}
	
	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

}
