package com.chinarewards.metro.model.merchandise;

import java.util.Date;

import com.chinarewards.metro.domain.category.Category;
import com.chinarewards.metro.domain.merchandise.MerchandiseStatus;

/**
 * 
 * @author weishengshui
 * 
 */
public class CategoryVo {

	private Category category;
	private String categoryId;
	private String merchandiseId;
	private String merCode;
	private MerchandiseStatus status;
	private Date on_offTime;
	private Long displaySort;
	private String fullName;// 类别全名

	public CategoryVo() {
	}

	public CategoryVo(String categoryId, MerchandiseStatus status,
			Date on_offTime, Long displaySort, String merchandiseId) {
		this.categoryId = categoryId;
		this.status = status;
		this.on_offTime = on_offTime;
		this.displaySort = displaySort;
		this.merchandiseId = merchandiseId;
	}

	public CategoryVo(String merCode, MerchandiseStatus status, Long displaySort) {
		this.merCode = merCode;
		this.status = status;
		this.displaySort = displaySort;
	}

	public CategoryVo(String categoryId, String merCode,
			MerchandiseStatus status, Long displaySort) {
		this.categoryId = categoryId;
		this.merCode = merCode;
		this.status = status;
		this.displaySort = displaySort;
	}

	public CategoryVo(Category category, MerchandiseStatus status,
			Long displaySort) {
		this.categoryId = category.getId();
		this.category = category;
		this.status = status;
		this.displaySort = displaySort;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public MerchandiseStatus getStatus() {
		return status;
	}

	public void setStatus(MerchandiseStatus status) {
		this.status = status;
	}

	public Date getOn_offTime() {
		return on_offTime;
	}

	public void setOn_offTime(Date on_offTime) {
		this.on_offTime = on_offTime;
	}

	public Long getDisplaySort() {
		return displaySort;
	}

	public void setDisplaySort(Long displaySort) {
		this.displaySort = displaySort;
	}

	public String getMerCode() {
		return merCode;
	}

	public void setMerCode(String merCode) {
		this.merCode = merCode;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getMerchandiseId() {
		return merchandiseId;
	}

	public void setMerchandiseId(String merchandiseId) {
		this.merchandiseId = merchandiseId;
	}

}
