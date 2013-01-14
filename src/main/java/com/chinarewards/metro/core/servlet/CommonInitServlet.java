package com.chinarewards.metro.core.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommonInitServlet extends HttpServlet{

	private static final long serialVersionUID = -2302729181481460112L;
	
	//初始化短信状态
	public static Map<String , Object>  messageTask_map;
	
	  @Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		messageTask_map=new HashMap<String, Object>();
		//从数据库中读取
		
	}
	
	
}
