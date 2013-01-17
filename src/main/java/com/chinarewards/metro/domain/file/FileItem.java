package com.chinarewards.metro.domain.file;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

/**
 * Represents a file record in the application.
 * <p>
 * The reason why this entity is not named 'File' is due to the fact that 'file'
 * is a reserved word in some RDBMS systems.
 * 
 */
@Entity
public class FileItem implements Serializable {

	/**
		 * 
		 */
	private static final long serialVersionUID = 7255842876057320170L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	private String mimeType;

	private String originalFilename;

	private Long filesize;

	private Integer width;
	
	private Integer height;

	private String url;

	@Column(length = 4000)
	private String description;

	@Column(nullable = false)
	private Date createdAt;

	private Integer createdBy;

	@Column(nullable = false)
	private Date lastModifiedAt;

	private Integer lastModifiedBy;

	/**
	 * Returns the identity of this object.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the identity of this object.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the MIME type of this file. For example,
	 * <code>application/msword</code>, <code>video/mp4</code>.
	 * <p>
	 * Although it is nullable, programmers should try their best to fill in
	 * values for the file.
	 * 
	 * @return the mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Sets the MIME type of this file. For example,
	 * <code>application/msword</code>, <code>video/mp4</code>.
	 * <p>
	 * Although it is nullable, programmers should try their best to fill in
	 * values for the file.
	 * 
	 * @param mimeType
	 *            the mimeType to set
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * Returns the original filename.
	 * 
	 * @return the the original filename
	 */
	public String getOriginalFilename() {
		return originalFilename;
	}

	/**
	 * Sets the original filename.
	 * 
	 * @param originalFilename
	 *            the original filename to set
	 */
	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	/**
	 * Returns the file size, in bytes, of this file.
	 * 
	 * @return the file size in bytes.
	 */
	public Long getFilesize() {
		return filesize;
	}

	/**
	 * Sets the file size, in bytes, of this file.
	 * 
	 * @param filesize
	 *            the file size to set
	 */
	public void setFilesize(Long filesize) {
		this.filesize = filesize;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Returns the date and time this record is created. This record never
	 * returns <code>null</code>.
	 * 
	 * @return the date and time when this record is created
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Sets the date and time this record is created. This method never returns
	 * <code>null</code>.
	 * 
	 * @param createdAt
	 *            the date and time when this record is created
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Returns the last modified date and time of this record. This method never
	 * returns <code>null</code>.
	 * 
	 * @return the date and time when this record is modified
	 */
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	/**
	 * Sets the last modified date and time of this record. This method never
	 * returns <code>null</code>.
	 * 
	 * @param lastModifiedAt
	 *            the date and time when this record is modified
	 */
	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
