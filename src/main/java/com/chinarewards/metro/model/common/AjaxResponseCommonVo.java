package com.chinarewards.metro.model.common;

import java.io.Serializable;

public class AjaxResponseCommonVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -970884185505796390L;

	public AjaxResponseCommonVo() {

	}

	public AjaxResponseCommonVo(String message) {
		this.msg = message;
	}

	private String msg;
	
	private String categoryId;
	
	private Object id;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}
	
	
	

}
