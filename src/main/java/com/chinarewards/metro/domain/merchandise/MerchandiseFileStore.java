package com.chinarewards.metro.domain.merchandise;

import java.io.Serializable;

public class MerchandiseFileStore implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3362790198522860260L;

	private String mimeType;

	private String originalFilename;

	private long filesize;

	private String sourceUrl;

	private String operator;

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public long getFilesize() {
		return filesize;
	}

	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
}
