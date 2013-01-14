package com.chinarewards.metro.core.common;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class CommonUtil {
	
	protected final static Logger logger = Logger.getLogger(CommonUtil.class);
	
	/**
	 * 获取request对象
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return servletRequestAttributes.getRequest();
	}
	
	/**
	 * ajax response输出
	 * @param str
	 * @param response
	 */
	public static void output(String str, HttpServletResponse response) {
		try {
			response.getOutputStream().write(str.getBytes("UTF-8"));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 返回输出流
	 * @param response
	 * @return 
	 */
	public static ServletOutputStream output(HttpServletResponse response) {
		try {
			return response.getOutputStream();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 对象转换成json格式
	 * @param o
	 * @return
	 */
	public static String toJson(Object o) {
		String s = null;
		try {
			s = new ObjectMapper().writeValueAsString(o);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return s;
	}
	
	/**
	 * 将 "1,2,3,3,5" 字符转为Integer数组
	 * @param value
	 * @return
	 */
	public static Integer[] getIntegers(String value) {
		try {
			if (value != null) {
				String[] param = value.split(",");
				Set<Integer> set = new LinkedHashSet<Integer>();
				for (int i = 0; i < param.length; i++)
					set.add(Integer.valueOf(param[i]));
				Integer[] rs = new Integer[set.size()];
				return set.toArray(rs);
			}
		} catch (Exception e) {}
		return null;
	}
}
