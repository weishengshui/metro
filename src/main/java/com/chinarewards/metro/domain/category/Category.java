package com.chinarewards.metro.domain.category;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.chinarewards.metro.domain.merchandise.MerchandiseCatalog;

/**
 * 类别
 * @author qingminzou
 *
 */
@Entity
//@JsonIgnoreProperties(value={"hibernateLazyInitializer","parent"})
public class Category implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8147708279487398072L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	@ManyToOne(fetch=FetchType.EAGER, optional=true)
	private Category parent ;
	
	private long lft;
	
	private long rgt;
	
	private Long displaySort;
	
	private String name;
	
	private String description ;
	
	@Column(nullable = false)
	private Date createdAt;

	private Integer createdBy;

	@Column(nullable = false)
	private Date lastModifiedAt;

	private Integer lastModifiedBy;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}


	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public long getLft() {
		return lft;
	}

	public void setLft(long lft) {
		this.lft = lft;
	}

	public long getRgt() {
		return rgt;
	}

	public void setRgt(long rgt) {
		this.rgt = rgt;
	}

	public Long getDisplaySort() {
		return displaySort;
	}

	public void setDisplaySort(Long displaySort) {
		this.displaySort = displaySort;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(Integer lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	
}
