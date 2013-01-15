package com.chinarewards.metro.domain.merchandise;

import com.chinarewards.metro.domain.category.Category;

/**
 * 
 * @author weishengshui
 * 
 */
public class CategoryVo {
	
	private Category category;
	private String categoryId;
	private String merCode;
	private MerchandiseStatus status;
	private Long displaySort;
	private String fullName;// 类别全名

	public CategoryVo() {
		// TODO Auto-generated constructor stub
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

	public Long getDisplaySort() {
		return displaySort;
	}

	public void setDisplaySort(Long displaySort) {
		this.displaySort = displaySort;
	}

//	@Override
//	public String toString() {
//		return "[categoryId=" + categoryId + ", status=" + status
//				+ ", displaySort=" + displaySort + ", fullName="+fullName+ "]";
//	}

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
	
	

}
