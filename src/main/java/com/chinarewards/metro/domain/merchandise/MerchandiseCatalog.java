package com.chinarewards.metro.domain.merchandise;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.annotations.GenericGenerator;

import com.chinarewards.metro.domain.category.Category;

/**
 * 商品目录
 * 
 * @author weishengshui
 * 
 */
@Entity
public class MerchandiseCatalog implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3335728039915055972L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	@ManyToOne
	private Merchandise merchandise;

	@ManyToOne
	private Category category;

	@Enumerated(EnumType.STRING)
	private MerchandiseStatus status;
	
	//上下架时间
	private Date on_offTIme;

	// 　商品排序，数字越大越靠前
	private Long displaySort;

	private Date createdAt;

	private Integer createdBy;

	@Column(nullable = false)
	private Date lastModifiedAt;

	@Column(nullable = false)
	private Integer lastModifiedBy;
	
	public MerchandiseCatalog() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@JsonBackReference
	public Merchandise getMerchandise() {
		return merchandise;
	}

	public void setMerchandise(Merchandise merchandise) {
		this.merchandise = merchandise;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public MerchandiseStatus getStatus() {
		return status;
	}

	public void setStatus(MerchandiseStatus status) {
		this.status = status;
	}

	public Date getOn_offTIme() {
		return on_offTIme;
	}

	public void setOn_offTIme(Date on_offTIme) {
		this.on_offTIme = on_offTIme;
	}

	public Long getDisplaySort() {
		return displaySort;
	}

	public void setDisplaySort(Long displaySort) {
		this.displaySort = displaySort;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	public Integer getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(Integer lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	
	
}
