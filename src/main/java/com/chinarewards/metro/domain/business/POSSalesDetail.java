package com.chinarewards.metro.domain.business;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * pos 机交易 需要记录此表
 * 
 * @author qingminzou
 * 
 */
@Entity
public class POSSalesDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2828655505775238315L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@ManyToOne
	private OrderInfo order;

	private String consumeType;

	private BigDecimal quantity;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public OrderInfo getOrder() {
		return order;
	}

	public void setOrder(OrderInfo order) {
		this.order = order;
	}

	public String getConsumeType() {
		return consumeType;
	}

	public void setConsumeType(String consumeType) {
		this.consumeType = consumeType;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

}
