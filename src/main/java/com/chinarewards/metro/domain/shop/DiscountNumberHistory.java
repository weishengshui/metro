package com.chinarewards.metro.domain.shop;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.chinarewards.metro.domain.member.Member;

@Entity
public class DiscountNumberHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1061030034312465888L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String discountNum;

	private Date generatedDate;

	private Date usedDate;
	
	// 优惠码状态: 未使用、已使用、已过期
	private int status;

	private Date expiredDate;

	// 哪个会员生成通过客户端生成的优惠码
	@ManyToOne
	private Member member;

	// 生成此优惠码的时候，此门店的所发布的优惠(一个门店只能发布一个优惠)
	private String title;

	@Lob
	private String description;
	
	@ManyToOne
	private Shop shop;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getGeneratedDate() {
		return generatedDate;
	}

	public void setGeneratedDate(Date generatedDate) {
		this.generatedDate = generatedDate;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Date getUsedDate() {
		return usedDate;
	}

	public void setUsedDate(Date usedDate) {
		this.usedDate = usedDate;
	}

	public String getDiscountNum() {
		return discountNum;
	}

	public void setDiscountNum(String discountNum) {
		this.discountNum = discountNum;
	}

	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	

}
