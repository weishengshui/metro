package com.chinarewards.metro.core.common;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.chinarewards.metro.domain.user.Resources;


public class UserContext {

	public static final String USER_ID = "USER_ID";
	public static final String USER_NAME = "USER_NAME";
	public static final String USER_RESOURCE = "USER_RESOURCE";
	public static final String LOGIN_IP = "LOGIN_IP";
	public static final String LOGIN_TIME = "LOGIN_TIME";
	
	/**
	 * 获取useId
	 * @return
	 */
	public static Integer getUserId() {
		HttpServletRequest request = CommonUtil.getRequest();
		return (Integer) getSessionAttribute(request, USER_ID);
    }
	
	/**
	 * 获取userName
	 * @return
	 */
	public static String getUserName() {
		HttpServletRequest request = CommonUtil.getRequest();
		return (String) getSessionAttribute(request, USER_NAME);
    }
	
	/**
	 * 获取登录用户的IP
	 * @return
	 */
	public static String getLoginIp() {
		HttpServletRequest request = CommonUtil.getRequest();
		return (String) getSessionAttribute(request, LOGIN_IP);
    }
	
	/**
	 * 获取登录用户的时间
	 * @return
	 */
	public static Date getLoginTime() {
		HttpServletRequest request = CommonUtil.getRequest();
		return (Date) getSessionAttribute(request, LOGIN_TIME);
    }
	
	/**
	 * 获取用户资源权限
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Resources> getUserResource() {
		HttpServletRequest request = CommonUtil.getRequest();
		return (List<Resources>) getSessionAttribute(request, USER_RESOURCE);
    }
	
    public static void setSessionAttribute(HttpServletRequest request, String name, Object value) {
        request.getSession().setAttribute(name, value);
    }
    
    public static void removeSessionAttribute(HttpServletRequest request, String name) {
        request.getSession().removeAttribute(name);
    }
	
    private static Object getSessionAttribute(HttpServletRequest request, String key) {
        return request.getSession().getAttribute(key);
    }
}
