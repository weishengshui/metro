package com.chinarewards.metro.core.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.chinarewards.metro.core.common.Dictionary;
import com.chinarewards.metro.domain.message.MessageTask;
import com.chinarewards.metro.service.message.IMessageService;
import com.chinarewards.metro.service.message.impl.MessageService;






public class CommonInitServlet extends AbstractServlet{
	private static final long serialVersionUID = -2302729181481460112L;
	
	//这种初始化的方式取不到当前对象，使用spring的初始化方式
//	@Autowired
//	private MessageService messageservice;
	
	

	//初始化短信状态
	public static Map<String , Object>  messageTask_map;
	
	
	
	//http://stackoverflow.com/questions/467235/access-spring-beans-from-a-servlet-in-jboss
	//http://blog.csdn.net/usrobert/article/details/8313993
	  @Override
	  public void init(ServletConfig config) throws ServletException {
		 super.init(config);
		  messageTask_map=new HashMap<String, Object>();
		
		//从数据库中读取执行中的短信状态
//		   List<MessageTask> mtlist=messageservice.listTaskByStates(Dictionary.TASK_EXECUTING);
//		    if(mtlist!=null){
//			for(MessageTask mt : mtlist){
//				messageTask_map.put(mt.getTaskId(), mt.getTaskStates());	
//			}
//		}	    
		  
	}
	protected void doGet(HttpServletRequest request,
				HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
