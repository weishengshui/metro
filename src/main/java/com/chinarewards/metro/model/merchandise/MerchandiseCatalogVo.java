package com.chinarewards.metro.model.merchandise;

import com.chinarewards.metro.domain.merchandise.Merchandise;
import com.chinarewards.metro.domain.merchandise.MerchandiseCatalog;
import com.chinarewards.metro.domain.merchandise.MerchandiseStatus;

public class MerchandiseCatalogVo {

	private String id;
	private String name;
	private String code;
	private String model;
	private MerchandiseStatus status;
	private Long displaySort;

	public MerchandiseCatalogVo(MerchandiseCatalog merchandiseCatalog) {
		Merchandise merchandise = merchandiseCatalog.getMerchandise();

		this.id = merchandiseCatalog.getId();
		this.name = merchandise.getName();
		this.code = merchandise.getCode();
		this.model = merchandise.getModel();
		this.status = merchandiseCatalog.getStatus();
		this.displaySort = merchandiseCatalog.getDisplaySort();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public MerchandiseStatus getStatus() {
		return status;
	}

	public void setStatus(MerchandiseStatus status) {
		this.status = status;
	}

	public Long getDisplaySort() {
		return displaySort;
	}

	public void setDisplaySort(Long displaySort) {
		this.displaySort = displaySort;
	}

}
