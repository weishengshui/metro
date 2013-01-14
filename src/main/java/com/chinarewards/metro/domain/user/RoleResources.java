package com.chinarewards.metro.domain.user;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
/**
 * 角色资源
 * @author huangshan
 *
 */
@Entity
public class RoleResources implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO )
	private Integer id;
	
	@Column(name = "role_id")
	private Integer roleId;
	
	@Column(name = "resources_id")
	private Integer resourcesId;
	
	private Integer rights;
	
	public Integer getRights() {
		return rights;
	}

	public void setRights(Integer rights) {
		this.rights = rights;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getResourcesId() {
		return resourcesId;
	}

	public void setResourcesId(Integer resourcesId) {
		this.resourcesId = resourcesId;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoleResources other = (RoleResources) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
