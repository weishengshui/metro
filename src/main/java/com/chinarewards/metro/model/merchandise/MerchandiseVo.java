package com.chinarewards.metro.model.merchandise;

import java.util.Date;

import com.chinarewards.metro.domain.category.Category;
import com.chinarewards.metro.domain.merchandise.MerchandiseStatus;

public class MerchandiseVo {

	private String mId; // 商品ID
	private String mcId; // 兑换商品ID

	// 商品编号
	private String code;

	// 商品型号
	private String model;

	// 商品名称
	private String name;

	private String description;

	// **********************************
	// 采购单价
	private String purchasePrice;

	// 供应商名称
	private String supplierName;

	// 商品类别
	private Category category;

	private Date createdAt;

	private String createdBy;

	private Date lastModifiedAt;

	private String lastModifiedBy;

	private double price;

	// 兑换单位(RMB，缤刻)
	private String unitId;

	private MerchandiseStatus status;

	private long displaySort;

	public String getMId() {
		return mId;
	}

	public void setMId(String mId) {
		this.mId = mId;
	}

	public String getMcId() {
		return mcId;
	}

	public void setMcId(String mcId) {
		this.mcId = mcId;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(String purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public MerchandiseStatus getStatus() {
		return status;
	}

	public void setStatus(MerchandiseStatus status) {
		this.status = status;
	}

	public long getDisplaySort() {
		return displaySort;
	}

	public void setDisplaySort(long displaySort) {
		this.displaySort = displaySort;
	}

}
