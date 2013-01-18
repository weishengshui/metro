package com.chinarewards.metro.model.merchandise;

import com.chinarewards.metro.domain.merchandise.MerchandiseSaleform;

//售卖形式VO
public class SaleFormVo {

	private String unitId;
	private Double price;
	// 优惠价
	private Double preferentialPrice;

	public SaleFormVo(String unitId, Double price, Double preferentialPrice) {
		this.unitId = unitId;
		this.price = price;
		this.preferentialPrice = preferentialPrice;
	}
	
	public SaleFormVo(MerchandiseSaleform saleform) {
		this.unitId = saleform.getUnitId();
		this.price = saleform.getPrice();
		this.preferentialPrice = saleform.getPreferentialPrice();
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

	public Double getPreferentialPrice() {
		return preferentialPrice;
	}

	public void setPreferentialPrice(Double preferentialPrice) {
		this.preferentialPrice = preferentialPrice;
	}

}
