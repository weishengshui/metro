package com.chinarewards.metro.core.common;


import java.io.Serializable;

/**
 * 
 * 
 * @author weishengshui
 *
 */
public class TreeNode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7873821617294251961L;

	public TreeNode() {
		super();
	}

	public TreeNode(Object id, String text, String state, String attributes) {
		super();
		this.id = id;
		this.text = text;
		this.state = state;
		this.attributes = attributes;
	}

	private Object id;


	private String text;

	private String state;
	
	private String attributes;
	

	public Object getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}


	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}
	
	
	

}

