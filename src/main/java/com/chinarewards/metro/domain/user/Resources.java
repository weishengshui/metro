package com.chinarewards.metro.domain.user;


import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
/**
 * 资源
 * @author huangshan
 *
 */
@Entity
public class Resources implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO )
	private Integer id;
		
	private String url;
		
	private Integer type;
	
	private String name;
	
	@Column(name = "button_rights")
	private String rights;

	@Transient
	private Integer accesss;
	
	@OneToMany(fetch = FetchType.EAGER,cascade = {CascadeType.ALL},mappedBy = "resource")
	private Set<ResourceOperation> opertions;
	
	public Set<ResourceOperation> getOpertions() {
		return opertions;
	}

	public void setOpertions(Set<ResourceOperation> opertions) {
		this.opertions = opertions;
	}

	public Integer getAccesss() {
		return accesss;
	}

	public void setAccesss(Integer accesss) {
		this.accesss = accesss;
	}

	public String getRights() {
		return rights;
	}
	
	public void setRights(String rights) {
		this.rights = rights;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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
		Resources other = (Resources) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
