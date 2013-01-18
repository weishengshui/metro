package com.chinarewards.metro.model.merchandise;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.chinarewards.metro.domain.merchandise.Merchandise;
import com.chinarewards.metro.domain.merchandise.MerchandiseSaleform;

public class MerchandiseVo {
	
	private String id;
	
	// 商品编号
	private String code;

	// 商品型号
	private String model;

	// 商品名称
	private String name;

	// 品牌名称
	private String brandName;

	// 售卖形式
	private List<SaleFormVo> saleFormVos = new ArrayList<SaleFormVo>();

	// 采购价
	private Double purchasePrice;

	public MerchandiseVo() {
	}

	public MerchandiseVo(Merchandise merchandise) {
		this.id = merchandise.getId();
		this.code = merchandise.getCode();
		this.name = merchandise.getName();
		this.model = merchandise.getModel();
		if (null != merchandise.getBrand()) {
			this.brandName = merchandise.getBrand().getName();
		} else {
			this.brandName = "";
		}
		this.purchasePrice = merchandise.getPurchasePrice();
		
		Set<MerchandiseSaleform> saleforms = merchandise.getMerchandiseSaleforms();
		if(null != saleforms && saleforms.size() > 0){
			for(MerchandiseSaleform saleform : saleforms){
				this.saleFormVos.add(new SaleFormVo(saleform));
			}
		}
	}
	
	public MerchandiseVo(MerchandiseSaleform merchandiseSaleform) {
		
		Merchandise merchandise = merchandiseSaleform.getMerchandise();
		
		this.id = merchandise.getId();
		this.code = merchandise.getCode();
		this.name = merchandise.getName();
		this.model = merchandise.getModel();
		if (null != merchandise.getBrand()) {
			this.brandName = merchandise.getBrand().getName();
		} else {
			this.brandName = "";
		}
		this.purchasePrice = merchandise.getPurchasePrice();
		
		this.saleFormVos.add(new SaleFormVo(merchandiseSaleform));
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public List<SaleFormVo> getSaleFormVos() {
		return saleFormVos;
	}

	public void setSaleFormVos(List<SaleFormVo> saleFormVos) {
		this.saleFormVos = saleFormVos;
	}

	public Double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

}
