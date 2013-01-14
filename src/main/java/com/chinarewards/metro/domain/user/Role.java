package com.chinarewards.metro.domain.user;


import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
/**
 * 角色
 * @author huangshan
 *
 */
@Entity
public class Role implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO )
	private Integer id;
	
	@Column(name = "role_name")
	private String name;
	
	@Column(name = "role_desc")
	private String desc;
	
//	@ManyToMany(cascade={CascadeType.ALL})
//	@JoinTable(name="user_role",joinColumns=@JoinColumn(name="user_id"),inverseJoinColumns=@JoinColumn(name="role_id"))
	@Transient
	private Set<UserInfo> users = new LinkedHashSet<UserInfo>();
	
//	@ManyToMany(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
//	@JoinTable(name="roles_resources",joinColumns=@JoinColumn(name="role_id"),inverseJoinColumns=@JoinColumn(name="resource_id"))
	@Transient
	private Set<Resources> resources = new LinkedHashSet<Resources>();
	
	public Set<Resources> getResources() {
		return resources;
	}

	public void setResources(Set<Resources> resources) {
		this.resources = resources;
	}

	public Set<UserInfo> getUsers() {
		return users;
	}

	public void setUsers(Set<UserInfo> users) {
		this.users = users;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
