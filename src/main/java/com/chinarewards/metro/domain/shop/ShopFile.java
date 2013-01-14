package com.chinarewards.metro.domain.shop;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.chinarewards.metro.domain.file.FileItem;

@Entity
public class ShopFile implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -839048092240556342L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@ManyToOne
	private FileItem fileItem;

	@ManyToOne
	private Shop shop;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public FileItem getFileItem() {
		return fileItem;
	}

	public void setFileItem(FileItem fileItem) {
		this.fileItem = fileItem;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}
	
}
