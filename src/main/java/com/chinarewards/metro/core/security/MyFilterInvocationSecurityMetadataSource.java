package com.chinarewards.metro.core.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.chinarewards.metro.core.common.UserContext;
import com.chinarewards.metro.domain.user.ResourceOperation;
import com.chinarewards.metro.domain.user.Resources;
import com.chinarewards.metro.service.user.IUserService;


public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource{

	@Autowired
	private IUserService userService;
	
	private static List<Resources> resourcesList = new ArrayList<Resources>();
	private static Map<Integer,Set<ResourceOperation>> map = new HashMap<Integer,Set<ResourceOperation>>();
	private static String path = "/";
	
	public void init(){
		resourcesList = userService.findResources();
		for(Resources res : resourcesList){
			if(res.getOpertions() != null){
				map.put(res.getId(), res.getOpertions());
			}
		}
	}
	
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}
	
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object)throws IllegalArgumentException {
		ConfigAttribute config = new SecurityConfig(AuthenticatedVoter.IS_AUTHENTICATED_FULLY);
		Collection<ConfigAttribute> returnCollection = new ArrayList<ConfigAttribute>();
		returnCollection.add(config);
		final HttpServletRequest request = ((FilterInvocation) object).getRequest();
		String url = request.getServletPath();
		if(request.getSession().getAttribute(UserContext.USER_ID)==null) return returnCollection;
		if(!isDbUrl(url)) return returnCollection;
		boolean isAuth = true;//checkUserAuth(request,url);
		if(isAuth)
			return returnCollection;
		return null;
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	/**
	 * 如果URL没保存在DB则不验证,如修改密码等
	 * @param url
	 * @return
	 */
	public boolean isDbUrl(String url){
		boolean flag = false;
		for(Resources res : resourcesList){
			if(url.equals(path+res.getUrl())){
				flag = true;break;
			}
			if(res.getOpertions().size() > 0){
				for(ResourceOperation operation : res.getOpertions()){
					if(url.equals(path+operation.getUrl())){
						flag = true;break;
					}
				}
			}
		}
		return flag;
	}
	/**
	 * 验证用户权限
	 * @param url
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean checkUserAuth(HttpServletRequest request,String url){
		boolean flag = false;
		List<Resources> list = (List<Resources>) request.getSession().getAttribute(UserContext.USER_RESOURCE);
		for(Resources res : list){
			if(StringUtils.isNotEmpty(res.getUrl())){
				if(url.equals(path+res.getUrl()) || path.equals(url)){
					flag = true;break;
				}
				if(map.get(res.getId()) != null){
					Integer rights = res.getAccesss();
					for(ResourceOperation operation : map.get(res.getId())){
						if(url.equals(path+operation.getUrl())){
							int value = (int) Math.pow(2, Integer.parseInt(operation.getLimitValue()));
							if((rights & value) == value){
								flag = true;break;
							}
						}
					}
				}
			}
		}
		return flag;
	}
	
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
}
