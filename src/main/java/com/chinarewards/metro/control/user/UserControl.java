package com.chinarewards.metro.control.user;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinarewards.metro.core.common.Constants;
import com.chinarewards.metro.core.common.MD5;
import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.core.common.UserContext;
import com.chinarewards.metro.domain.user.Role;
import com.chinarewards.metro.domain.user.RoleResources;
import com.chinarewards.metro.domain.user.UserInfo;
import com.chinarewards.metro.domain.user.UserRole;
import com.chinarewards.metro.service.user.IUserService;

@Controller
public class UserControl {

	@Autowired
	private IUserService userService;
	
	@RequestMapping("/login")
	public String login(){
		return "login";
	}
	
	@RequestMapping("/index")
	public String index(HttpServletResponse response){
		response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
		return "index";
	}
	
	@ResponseBody
	@RequestMapping("user/findUserResources")
	public List<Map<String,Object>> findUserResources(){
		return userService.findUserResourcesJson();
	}
	
	/**
	 * 登录验证成功
	 **/
	@RequestMapping(value = "/authorize")
	public String authorize(HttpServletRequest request){
		SecurityContext context = SecurityContextHolder.getContext();
		UserDetails user = (UserDetails) context.getAuthentication().getPrincipal();
		UserInfo userInfo = userService.findUserByName(user.getUsername());
		UserContext.setSessionAttribute(request, UserContext.USER_ID, userInfo.getId());
		UserContext.setSessionAttribute(request, UserContext.USER_NAME, userInfo.getUserName());
		UserContext.setSessionAttribute(request, UserContext.LOGIN_IP, request.getRemoteHost());
		UserContext.setSessionAttribute(request, UserContext.LOGIN_TIME, new Date());
		UserContext.setSessionAttribute(request, UserContext.USER_RESOURCE, userService.findUserResources());
		return "redirect:/index";
	}
	
	@RequestMapping("user/userIndex")
	public String userIndex(Model model){
		return "user/userList";
	}
	
	/**
	 * 查询用户
	 * @param userInfo
	 * @param page
	 * @return
	 */
	@RequestMapping("user/findUserInfos")
	public Map<String,Object> findUserInfos(UserInfo userInfo,Page page){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("rows", userService.findUserInfo(userInfo, page));
		map.put("total", page.getTotalRows());
		return map;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "user/save")
	public UserInfo create(UserInfo user){
		return userService.saveUser(user);
	}
	
	@ResponseBody
	@RequestMapping(value = "user/findUserByName")
	public String findUserByName(String userName){
		UserInfo user = userService.findUserByName(userName);
		if(user != null){
			return "suc";
		}
		return "";
	}
	
	@ResponseBody
	@RequestMapping("user/resetPassword")
	public String resetPassword(Integer id)throws Exception{
		userService.updatePassword(id,Constants.RESET_PASSWORD);
		return Constants.RESET_PASSWORD;
	}
	
	
	@ResponseBody
	@RequestMapping("user/lockUser")
	public void lockUser(Integer id,Integer disable)throws Exception{
		userService.lockUser(id, disable);
	}
	
	@ResponseBody
	@RequestMapping(value = "user/validateOldPwd")
	public String validateOldPwd(String oldpwd,String newpwd){
		UserInfo userInfo = userService.findUserByName(UserContext.getUserName());
		if(MD5.MD5Encoder(oldpwd).equals(userInfo.getPassword())){
			userService.updatePassword(userInfo.getId(),newpwd);
			return "suc";
		}else{
			return "";
		}
	}
	
	/*----------  ROLE  ------------*/
	
	@RequestMapping(value = "user/roleList")
	public String roleList(){
		return "user/roleList";
	}
	
	@ResponseBody
	@RequestMapping(value = "user/saveRole")
	public void saveRole(Role role){
		userService.saveRole(role);
	}
	
	@RequestMapping(value = "user/findRoles")
	public Map<String,Object> findRoles(Role role,Page page){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("rows", userService.findRoles(role, page));
		map.put("total", page.getTotalRows());
		return map;
	}
	
	/**
	 * 查询角色名是否存在
	 * @param roleName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("user/findRoleByName")
	public String findRoleByName(String roleName,Integer id){
		Role role = userService.findRoleByName(roleName);
		if(role != null){
			if(!role.getId().equals(id)){
				return "suc";	
			}else{
				return "";
			}
		}
		return "";
	}
	
	/**
	 * 为角色添加权限
	 * @param model
	 * @param role
	 * @return
	 */
	@RequestMapping(value = "user/roleAuthority")
	public String roleAuthority(Model model,Role role){
		// 查询角色资源,用于保存时判断
		List<RoleResources> list = userService.findResourceByRole(role.getId());
		String s = "";
		for(RoleResources roleRes : list){
			if(!"".equals(s)) s += ",";
			s += roleRes.getResourcesId();
		}
		model.addAttribute("old", s);
		return "user/roleAuth";
	}
	
	@ResponseBody
	@RequestMapping(value = "user/findResources")
	public List<Map<String,Object>> findResources(Integer roleId) throws IOException{
		return userService.findResourcesByType(roleId);
	}
	
	@ResponseBody
	@RequestMapping(value = "user/saveRoleAuthority")
	public String saveRoleAuthority(String roleId,String resourceIds,String old){
		return userService.saveRoleAuth(roleId, resourceIds, old);
	}
	
	/*---------- User Role --------------*/
	@RequestMapping(value = "/userRoleList")
	public String userRoleList(){
		return "/user/userRole";
	}
	
	@ResponseBody
	@RequestMapping(value = "user/saveUserRole")
	public void saveUserRole(String roleIds,Integer userId){
		userService.saveUserRole(roleIds, userId);
	}
	
	@ResponseBody
	@RequestMapping(value = "user/findUserRole")
	public List<UserRole> findUserRole(Integer userId){
		return userService.findUserRole(userId);
	}
}
