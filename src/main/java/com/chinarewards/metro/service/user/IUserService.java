package com.chinarewards.metro.service.user;

import java.util.List;
import java.util.Map;

import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.domain.user.Resources;
import com.chinarewards.metro.domain.user.Role;
import com.chinarewards.metro.domain.user.RoleResources;
import com.chinarewards.metro.domain.user.UserInfo;
import com.chinarewards.metro.domain.user.UserRole;

public interface IUserService {

	/**
	 * 根据UserName查询UserInfo
	 * @param userName
	 * @return
	 */
	public UserInfo findUserByName(String userName);
	/**
	 * 查询UserInfo
	 * @param userInfo
	 * @param page
	 * @return
	 */
	public List<UserInfo> findUserInfo(UserInfo userInfo,Page page);
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	public UserInfo saveUser(UserInfo user); 
	/**
	 * 删除User
	 */
	public void removeUser(String id);
	
	/**
	 * 修改密码
	 */
	public void updatePassword(Integer id,String password);
	/**
	 * 锁定用户
	 * @param id
	 * @param status
	 */
	public void lockUser(Integer id,Integer disable);
	
	
	/*----------  ROLE  ------------*/
	/**
	 * 添加角色
	 */
	public void saveRole(Role role);
	/**
	 * 查询角色
	 * @param role
	 * @param page
	 * @return
	 */
	public List<Role> findRoles(Role role,Page page);
	/**
	 * 查询资源(菜单)
	 * @return
	 */
	public List<Map<String,Object>> findResourcesByType(Integer roleId);
	
	public List<Resources> findResources();
	
	/**
	 * 根据角色ID查询权限
	 * @return
	 */
	public List<RoleResources> findResourceByRole(Integer roleId);
	/**
	 * 保存角色权限(保存后返回用户最新的resourceID)
	 */
	public String saveRoleAuth(String roleId,String resourceIds,String old);
	/**
	 * 更新权限值
	 * @param roleId
	 * @param resourcesId
	 * @param rights
	 */
	public void updateRights(Integer roleId,Integer resourcesId, Integer rights);
	/**
	 * 删除角色权限
	 */
	public void delRoleAuth(Integer roleId,Integer resourceId);
	/**
	 * 查询用户角色
	 */
	public List<UserRole> findUserRole(Integer userId);
	/**
	 * 为用户添加角色
	 * @param roleIds
	 * @param userId
	 */
	public void saveUserRole(String roleIds,Integer userId);
	/**
	 * 查看用户权限(所属资源菜单)
	 */
	public List<Map<String,Object>> findUserResourcesJson();
	
	public List<Resources> findUserResources();
	/**
	 * 查询角色名是否存在
	 * @param roleName
	 * @return
	 */
	public Role findRoleByName(String roleName);
	
	public List<Resources> findResourceByZero();
}
