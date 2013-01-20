package com.chinarewards.metro.domain.merchandise;

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

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;

import com.chinarewards.metro.domain.brand.Brand;

/**
 * 商品实体模型
 * 
 * @author qingminzou
 * 
 */
@Entity
public class Merchandise implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8147708279487398072L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	// 商品编号
	@Column(unique = true)
	private String code;

	// 商品型号
	@Column(unique = true)
	private String model;

	// 商品名称
	private String name;

	@Column(columnDefinition = "text")
	private String description;

	// 采购单价
	private Double purchasePrice;

	// 运费
	private Double freight;

	// 供应商名称
	private String supplierName;

	@Column(updatable = false)
	private Date createdAt;
	@Column(updatable = false)
	private Integer createdBy;

	@Column(nullable = false)
	private Date lastModifiedAt;

	@Column(nullable = false)
	private Integer lastModifiedBy;

	@OneToMany(mappedBy = "merchandise", fetch = FetchType.LAZY)
	private Set<MerchandiseCatalog> merchandiseCatalogs;

	@OneToMany(mappedBy = "merchandise", fetch = FetchType.LAZY)
	private Set<MerchandiseSaleform> merchandiseSaleforms;

	@OneToMany(mappedBy = "merchandise", fetch = FetchType.LAZY)
	private Set<MerchandiseFile> merchandiseFiles;

	@ManyToOne
	private Brand brand;

	public Double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	@JsonBackReference
	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@JsonManagedReference
	public Set<MerchandiseCatalog> getMerchandiseCatalogs() {
		return merchandiseCatalogs;
	}

	public void setMerchandiseCatalogs(
			Set<MerchandiseCatalog> merchandiseCatalogs) {
		this.merchandiseCatalogs = merchandiseCatalogs;
	}

	@JsonManagedReference
	public Set<MerchandiseSaleform> getMerchandiseSaleforms() {
		return merchandiseSaleforms;
	}

	public void setMerchandiseSaleforms(
			Set<MerchandiseSaleform> merchandiseSaleforms) {
		this.merchandiseSaleforms = merchandiseSaleforms;
	}

	@JsonManagedReference
	public Set<MerchandiseFile> getMerchandiseFiles() {
		return merchandiseFiles;
	}

	public void setMerchandiseFiles(Set<MerchandiseFile> merchandiseFiles) {
		this.merchandiseFiles = merchandiseFiles;
	}

}
