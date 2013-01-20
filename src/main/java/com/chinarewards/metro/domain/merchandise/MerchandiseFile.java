package com.chinarewards.metro.domain.merchandise;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.annotations.GenericGenerator;

/**
 * 商品附件
 * 
 * @author qingminzou
 * 
 */
@Entity
public class MerchandiseFile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8147708279487398072L;

	@Column(updatable = false)
	private Date createdAt;
	@Column(updatable = false)
	private Integer createdBy;

	@Column(length = 4000)
	private String description;

	private long filesize;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	/**
	 * 商品有一张概要图，还有一些详细图 当值为<code>overview</code>时候说明是概要图，一个商品只有一张,
	 * <code>others</code>其他详细描述图片可以有多张
	 */
	@Enumerated(EnumType.STRING)
	private MerchandiseImageType imageType;

	@Column(nullable = false)
	private Date lastModifiedAt;
	@Column(nullable = false)
	private Integer lastModifiedBy;

	@ManyToOne(fetch = FetchType.LAZY)//默认是FetchType.EAGER
	private Merchandise merchandise;

	private String mimeType;

	private String originalFilename;

	private String url;

	private int width;
	private int height;

	public Date getCreatedAt() {
		return createdAt;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public String getDescription() {
		return description;
	}

	public long getFilesize() {
		return filesize;
	}

	public String getId() {
		return id;
	}

	public MerchandiseImageType getImageType() {
		return imageType;
	}

	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	public Integer getLastModifiedBy() {
		return lastModifiedBy;
	}
	
	@JsonBackReference
	public Merchandise getMerchandise() {
		return merchandise;
	}

	public String getMimeType() {
		return mimeType;
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public String getUrl() {
		return url;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setImageType(MerchandiseImageType imageType) {
		this.imageType = imageType;
	}

	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	public void setLastModifiedBy(Integer lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public void setMerchandise(Merchandise merchandise) {
		this.merchandise = merchandise;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	
}
