package com.chinarewards.metro.core.common;


public class Page {

	//当前页
	private Integer page;
	//每页显示行数
	private Integer rows;
	//总行数
	private Integer totalRows;
	
	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(Integer totalRows) {
		this.totalRows = totalRows;
	}

	public int getStart(){
		if(page == null || rows == null){
			page = 1;
			rows = 20;
		}
		return (page - 1) * rows;
	}
}
