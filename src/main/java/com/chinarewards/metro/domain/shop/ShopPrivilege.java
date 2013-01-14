package com.chinarewards.metro.domain.shop;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.chinarewards.metro.domain.privilege.Privilege;

@Entity
public class ShopPrivilege implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5465926240850944851L;

	@Id
	private int id;

	@ManyToOne
	private Privilege privilege;

	@ManyToOne
	private Shop shop;

	// actived , disabled
	private String status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Privilege getPrivilege() {
		return privilege;
	}

	public void setPrivilege(Privilege privilege) {
		this.privilege = privilege;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
