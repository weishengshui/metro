package com.chinarewards.metro.service.user.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.chinarewards.metro.core.common.Dictionary;
import com.chinarewards.metro.core.common.HBDaoSupport;
import com.chinarewards.metro.core.common.JDBCDaoSupport;
import com.chinarewards.metro.core.common.MD5;
import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.core.common.UserContext;
import com.chinarewards.metro.domain.user.ResourceOperation;
import com.chinarewards.metro.domain.user.Resources;
import com.chinarewards.metro.domain.user.Role;
import com.chinarewards.metro.domain.user.RoleResources;
import com.chinarewards.metro.domain.user.UserInfo;
import com.chinarewards.metro.domain.user.UserRole;
import com.chinarewards.metro.service.user.IUserService;

public class UserService implements IUserService,UserDetailsService{

	@Autowired
	private HBDaoSupport hbDaoSupport;
	@Autowired
	private JDBCDaoSupport jdbcDaoSupport;
	
	/*------------UserInfo----------*/
	
	@Override
	public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException {
		UserInfo userInfo = findUserByName(username);
        if(userInfo == null){
             throw new UsernameNotFoundException("用户没找到!");
        } else{
            List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
            for (Role role : userInfo.getRoles()) {
                 SimpleGrantedAuthority authImpl = new SimpleGrantedAuthority(role.getName());
                 auths.add(authImpl);
            }
            boolean enables = true;  
            boolean accountNonExpired = true;  
            boolean credentialsNonExpired = true;  
            boolean accountNonLocked = userInfo.getDisable() == Dictionary.USER_STATE_NORMAL;  
            User user = new User(username, userInfo.getPassword(), enables , 
            		accountNonExpired, credentialsNonExpired, accountNonLocked, auths);
            return user;
        }
	}
	
	@Override
	public UserInfo findUserByName(String userName) {
		return hbDaoSupport.findTByHQL("from UserInfo where userName = ?",userName);
	}

	@Override
	public List<UserInfo> findUserInfo(UserInfo userInfo, Page page) {
		List<Object> args = new ArrayList<Object>();
		List<Object> argsCount = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlCount = new StringBuffer();
		sql.append("SELECT ui.id,ui.disable,ui.`password`,ui.username,group_concat(r.role_name) userRole FROM ");
		sql.append("UserInfo ui LEFT JOIN UserRole ur ON ur.user_id = ui.id LEFT JOIN Role r ON ur.role_id = r.id WHERE 1=1 ");
		
		sqlCount.append("SELECT COUNT(1) FROM UserInfo ui WHERE 1=1");
		
		if(StringUtils.isNotEmpty(userInfo.getUserName())){
			sql.append(" AND ui.username like ?");
			sqlCount.append(" AND ui.username like ?");
			args.add(userInfo.getUserName());
			argsCount.add(userInfo.getUserName());
		}
		sql.append("GROUP BY ui.username,ui.id ORDER BY ui.id desc LIMIT ?,?");
		args.add(page.getStart());
		args.add(page.getRows());
		if(argsCount.size() > 0){
			page.setTotalRows(jdbcDaoSupport.findCount(sqlCount.toString(), argsCount.toArray()));
		}else{
			page.setTotalRows(jdbcDaoSupport.findCount(sqlCount.toString()));
		}
		return jdbcDaoSupport.findTsBySQL(UserInfo.class, sql.toString(),args.toArray());
	}
	
	@Override
	public void removeUser(String id) {
		String hql = "delete from UserInfo where id = :id";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", id);
		hbDaoSupport.executeHQL(hql, map);
	}
	
	@Override
	public UserInfo saveUser(UserInfo user) {
		user.setDisable(Dictionary.USER_STATE_NORMAL);
		user.setPassword(MD5.MD5Encoder(user.getPassword()));
		return hbDaoSupport.save(user);
	}
	
	@Override
	public void updatePassword(Integer id,String password) {
		String hql = "update UserInfo set password = :password where id = :id";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("password", MD5.MD5Encoder(password));
		map.put("id", id);
		hbDaoSupport.executeHQL(hql, map);
	};
	
	
	@Override
	public void lockUser(Integer id, Integer disable) {
		String hql = "update UserInfo set disable = :disable where id = :id";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("disable", disable);
		map.put("id", id);
		hbDaoSupport.executeHQL(hql, map);
	}
	/*------------Role-------------*/
	
	public List<Map<String,Object>> findResourcesByType(Integer roleId) {
		List<Resources> resourcesList = findResources();//查询所有资源
		/*
		List<Integer> typesList = new ArrayList<Integer>();
		for(Resources res : resourcesList){
			if(typesList.contains(res.getType()))
				continue;
			typesList.add(res.getType());
		}
		
		Map<Integer,List<Resources>> typeMap = new HashMap<Integer,List<Resources>>();
		for(Integer typeId : typesList){
			List<Resources> typeList = new ArrayList<Resources>();
			for(Resources ress : resourcesList){
				if(typeId == ress.getId() || (ress.getType() == typeId && typeId == 0)){
					typeList.add(ress);
				}
			}
			typeMap.put(typeId, typeList);
		}*/
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<RoleResources> roleResourceList = findResourceByRole(roleId);
		Map<String,Object> parentMaps = new HashMap<String, Object>();
		parentMaps.put("id", "");
		parentMaps.put("text", "功能菜单");
		List<Map<String,Object>> parentList = new ArrayList<Map<String,Object>>();
		for(Resources res : resourcesList){
			if(res.getType() == 0){
				Map<String,Object> parentMap = new HashMap<String, Object>(); //  父节点
				parentMap.put("id", "");
				parentMap.put("text", res.getName());
				parentMap.put("state", "closed");
				List<Object> subList = new ArrayList<Object>();
				for(Resources ress : resourcesList){
					if(ress.getType() == res.getId()){
						Map<String,Object> subMap = new HashMap<String, Object>();	//子节点
						subMap.put("id", ress.getId());
						subMap.put("text", ress.getName());
						//如果没有按钮也被选中
						for(RoleResources roleRes : roleResourceList){
							if(roleRes.getResourcesId() == ress.getId()){
								subMap.put("checked", "true"); //TODO 。。。。
							}
						}
						
						List<Object> subsList = new ArrayList<Object>();
						for(Resources resss : resourcesList){
							if(resss.getType() == ress.getId()){
								Map<String,Object> subsMap = new HashMap<String, Object>();	//第三子节点
								subsMap.put("id", resss.getId());
								subsMap.put("text", resss.getName());
								if(resss.getOpertions().size() > 0){
									List<Object> buttonList = new ArrayList<Object>();
									for(ResourceOperation option : resss.getOpertions()){  //循环第三节点按钮
										Map<String,Object> buttonMap = new HashMap<String, Object>();
										buttonMap.put("id", resss.getId()+"_"+option.getLimitValue());
										buttonMap.put("text", option.getOperation());
										//如果角色应该权限则选中
										for(RoleResources roleRes : roleResourceList){
											if(roleRes.getResourcesId() == resss.getId()){
												Integer rights = roleRes.getRights();
												int value = (int) Math.pow(2, Integer.parseInt(option.getLimitValue()));
												if((rights & value) == value){
													buttonMap.put("checked","true");
													break;
												}
											}
										}
										buttonList.add(buttonMap);
									}
									subsMap.put("children", buttonList);
								}
								subsList.add(subsMap);
							}
							
						}
						if(subsList.size() > 0){
							subMap.put("children", subsList);
						}
						
						if(ress.getOpertions().size() > 0){
							List<Object> buttonList = new ArrayList<Object>();
							for(ResourceOperation option : ress.getOpertions()){
								Map<String,Object> buttonMap = new HashMap<String, Object>();
								buttonMap.put("id", ress.getId()+"_"+option.getLimitValue());
								buttonMap.put("text", option.getOperation());
								for(RoleResources roleRes : roleResourceList){//循环第二节点按钮
									if(roleRes.getResourcesId() == ress.getId()){
										Integer rights = roleRes.getRights();
										int value = (int) Math.pow(2, Integer.parseInt(option.getLimitValue()));
										if((rights & value) == value){
											buttonMap.put("checked","true");
											break;
										}
									}
								}
								buttonList.add(buttonMap);
							}
							subMap.put("children", buttonList);
						}
						subList.add(subMap);
					}
				}
				parentMap.put("children", subList);
				list.add(parentMap);
			}
		}
		parentMaps.put("children", list);
		parentList.add(parentMaps);
		return parentList;
	};
	
	@Override
	public List<Role> findRoles(Role role, Page page) {
		DetachedCriteria criteria = DetachedCriteria.forClass(role.getClass());
		criteria.addOrder(Order.desc("id"));
		if(StringUtils.isNotEmpty(role.getName())){
			criteria.add(Restrictions.like("name", role.getName(),MatchMode.ANYWHERE));
		}
		return hbDaoSupport.findPageByCriteria(page, criteria);
	}
	
	@Override
	public void saveRole(Role role) {
		hbDaoSupport.saveOrUpdate(role);
	}
	
	@Override
	public Role findRoleByName(String roleName) {
		String hql = "from Role where name = ?";
		return hbDaoSupport.findTByHQL(hql, roleName);
	}
	
	@Override
	public List<Resources> findResources() {
		return hbDaoSupport.findAll(Resources.class);
	}
	
	@Override
	public List<RoleResources> findResourceByRole(Integer roleId) {
		String hql = "from RoleResources where roleId = ?";
		return hbDaoSupport.findTsByHQL(hql, roleId);
	}
	
	@Override
	public void delRoleAuth(Integer roleId, Integer resourceId) {
		String hql = "delete from RoleResources where roleId = :roleId and resourcesId = :resourceId";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("roleId", roleId);
		map.put("resourceId", resourceId);
		hbDaoSupport.executeHQL(hql, map);
	}
	
	@Override
	public String saveRoleAuth(String roleId, String resourceIds, String old) {
		String newIds = ""; 							//保存用户最新设置的权限ID 需要返回到页面
		String[] resourceIdss = resourceIds.split(","); //最新设置的权限
		String[] olds = old.split(",");				    //设置之前的权限
		List<Integer> delIds = new ArrayList<Integer>();  //保存要删除的ID
		List<Integer> addIds = new ArrayList<Integer>();  //保存要保存的ID
		Map<String,Integer> rightsMap = new HashMap<String, Integer>(); // 保存按钮权限值
		String flag = "";
		//传过来的resourceIds如：2_1,2_4,3_1,5   2_1 表示2是资源菜单的ID, 1表选中的操作(如添加)。需要解析
		Integer rightsInt = 0,n = 0;
		for(String resourcesId : resourceIdss){			//判断添加
			boolean isAdd = true;
			String rightss[] = resourcesId.split("_");
			if(n != Integer.parseInt(rightss[0])) rightsInt = 0; //如：2_1,2_4,3_1 循环到'3_1'时 需要把 rightsInt清零
			n = Integer.parseInt(rightss[0]);
			if(rightss.length > 1){
				rightsInt += (int)Math.pow(2, Integer.parseInt(rightss[1]));
				rightsMap.put(rightss[0], rightsInt);
			}
			if(flag.equals(rightss[0])) continue;
			flag = rightss[0];
			if(!"".equals(newIds)) newIds += ",";
			newIds += rightss[0];
			for(String oldId : olds){
				if(rightss[0].equals(oldId)){
					isAdd = false;break;
				}
			}
			if(isAdd){
				addIds.add(Integer.parseInt(rightss[0]));
			}
		}
		
		for(String oldId : olds){						//判断删除
			boolean isDel = true;
			flag = "";
			for(String resourcesId : resourceIdss){
				String rightss[] = resourcesId.split("_");
				if(flag.equals(rightss[0])) continue;
				flag = rightss[0];
				if(oldId.equals(rightss[0])){
					isDel = false;break;
				}
			} 
			if(isDel && !"".equals(oldId)){
				delIds.add(Integer.parseInt(oldId));
			}
		}
		
		if(addIds.size() > 0){
			for(Integer resourceId : addIds){               //添加
				RoleResources rr = new RoleResources();
				rr.setRoleId(Integer.parseInt(roleId));
				rr.setResourcesId(resourceId);
				hbDaoSupport.save(rr);
			}
		}
		if(delIds.size() > 0){
			for(Integer resourceId : delIds){               //删除
				delRoleAuth(Integer.parseInt(roleId), resourceId);
			}
		}
		//保存权限值
		Set<Entry<String, Integer>> set = rightsMap.entrySet(); 
		for (Iterator<Entry<String, Integer>> iterator = set.iterator(); iterator.hasNext();) {
			Entry<String, Integer> entry = (Entry<String, Integer>) iterator.next();
			updateRights(Integer.parseInt(roleId), Integer.parseInt(entry.getKey()), entry.getValue());
		}
		return newIds;
	}
	
	@Override
	public void updateRights(Integer roleId,Integer resourcesId, Integer rights) {
		String hql = "update RoleResources set rights = :rights where resourcesId = :resourcesId and roleId = :roleId";
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("rights", rights);
		map.put("resourcesId", resourcesId);
		map.put("roleId", roleId);
		hbDaoSupport.executeHQL(hql, map);
	}
	
	@Override
	public List<UserRole> findUserRole(Integer userId) {
		String hql = "from UserRole where userId = ?";
		return hbDaoSupport.findTsByHQL(hql, userId);
	}
	
	@Override
	public void saveUserRole(String roleIds, Integer userId) {
		String hql = "delete from UserRole where userId = :userId";
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		hbDaoSupport.executeHQL(hql, map);
		
		String roleIdss[] = roleIds.split(",");
		if(StringUtils.isNotEmpty(roleIds)){
			for(String roleId : roleIdss){
				 UserRole ur = new UserRole();
				 ur.setRoleId(Integer.parseInt(roleId));
				 ur.setUserId(userId);
				 hbDaoSupport.save(ur);
			}
		}
	}
	
	@Override
	public List<Map<String,Object>> findUserResourcesJson() {
		List<Resources> resourcesList = UserContext.getUserResource();
		List<Resources> parentList = findResourceByZero();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(Resources res : parentList){
			if(res.getType() == 0){
				Map<String,Object> parentMap = new HashMap<String, Object>(); //  父节点
				parentMap.put("id", res.getId());
				parentMap.put("text", res.getName());
				parentMap.put("state", "closed");
				List<Object> subList = new ArrayList<Object>();
				for(Resources ress : resourcesList){
					if(ress.getType() == res.getId()){
						Map<String,Object> subMap = new HashMap<String, Object>();	//子节点
						List<Object> subsList = new ArrayList<Object>();
						for(Resources resss : resourcesList){
							if(resss.getType() == ress.getId()){
								Map<String,Object> subsMap = new HashMap<String, Object>();	//第三子节点
								subsMap.put("id", resss.getId());
								subsMap.put("text", "<a href='javascript:addTab(\""+resss.getName()+"\",\""+resss.getUrl()+"\")'>"+resss.getName()+"</a>");
								subsList.add(subsMap);
							}
						}
						subMap.put("id", ress.getId());
						if(subsList.size() > 0){
							subMap.put("text", ress.getName());
							subMap.put("children", subsList);
							subMap.put("state", "closed");
						}else{
							subMap.put("text", "<a href='javascript:addTab(\""+ress.getName()+"\",\""+ress.getUrl()+"\")'>"+ress.getName()+"</a>");
						}
						subList.add(subMap);
					}
				}
				if(subList.size() == 0) continue;
				parentMap.put("children", subList);
				list.add(parentMap);
			}
		}
		return list;
	}
	
	@Override
	public List<Resources> findUserResources() {
		String sql = "select r.id,r.url,r.type,r.name,rr.rights accesss from RoleResources rr left join Resources r on rr.resources_id = r.id " +
		 "left join UserRole ur on ur.role_id = rr.role_id where ur.user_id = ?";
		return  jdbcDaoSupport.findTsBySQL(Resources.class, sql, UserContext.getUserId());
	}
	
	@Override
	public List<Resources> findResourceByZero() {
		String hql = "from Resources where type = 0";
		return hbDaoSupport.findTsByHQL(hql);
	}
	
	public void setHbDaoSupport(HBDaoSupport hbDaoSupport) {
		this.hbDaoSupport = hbDaoSupport;
	}

	public void setJdbcDaoSupport(JDBCDaoSupport jdbcDaoSupport) {
		this.jdbcDaoSupport = jdbcDaoSupport;
	}
}
