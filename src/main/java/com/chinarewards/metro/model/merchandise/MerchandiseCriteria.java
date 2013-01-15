package com.chinarewards.metro.model.merchandise;

import com.chinarewards.common.models.PageSorting.SortingDetail;
import com.chinarewards.metro.core.common.Page;

/**
 * 商品查询条件
 * 
 * @author weishengshui
 * 
 */
public class MerchandiseCriteria {

	private String id;
	private String code;
	private String name;
	private String model;
	private String unitId; // 根据unitId确定售卖形式
	private String categoryId; //商品目录所属的类别id

	private Page paginationDetail;
	private SortingDetail sortingDetail;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Page getPaginationDetail() {
		return paginationDetail;
	}

	public void setPaginationDetail(Page paginationDetail) {
		this.paginationDetail = paginationDetail;
	}

	public SortingDetail getSortingDetail() {
		return sortingDetail;
	}

	public void setSortingDetail(SortingDetail sortingDetail) {
		this.sortingDetail = sortingDetail;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

}
