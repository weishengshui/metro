package com.chinarewards.metro.domain.shop;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.chinarewards.metro.core.common.DateSerializer;
import com.chinarewards.metro.core.common.DateTimeSerializer;

@Entity
public class Shop implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7706001567577683282L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id ;
	
	private String num;//总店编号
	
	private String name ;//总店名称
	
	private String url;//网址
	
	private String province ;//省
	
	private String city ;//市	
	
	private String region ;//区
	
	private String address ;//门店地址
	
	private String features ;//特色描述
	
	private String workPhone ;//固定电话
	
	private String businessHours;//营业时间
	
	@Column(updatable = false)
	private Integer siteId; //所属站台Id
	
	@Column(updatable = false)
	private Integer orderNo; //站台排序编号
	
	private String discountModel; //优惠码生成方式
	
	private String expresion ; //　当discountModel值是0,有效表示生成规则 ex: 10000 ~100000
	
	private String privilegeTile; //优惠信息标题
	
	private String privilegeDesc; //优惠信息描述
	
	private Date activeDate; //有效期

	private Date expireDate; 
	
	private String linkman; //联系人
	
	private String email;	//电子邮件
	
	private String note; //文件导入说明
	
	/**
	 * 临时
	 */
	@Transient
	private Integer allCount; //所有优惠码
	
	@Transient
	private Integer validateCount; //验证优惠码
	
	@Transient
	private String siteName;
	
	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public Integer getAllCount() {
		return allCount;
	}

	public void setAllCount(Integer allCount) {
		this.allCount = allCount;
	}

	public Integer getValidateCount() {
		return validateCount;
	}

	public void setValidateCount(Integer validateCount) {
		this.validateCount = validateCount;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getPrivilegeTile() {
		return privilegeTile;
	}

	public void setPrivilegeTile(String privilegeTile) {
		this.privilegeTile = privilegeTile;
	}

	public String getPrivilegeDesc() {
		return privilegeDesc;
	}

	public void setPrivilegeDesc(String privilegeDesc) {
		this.privilegeDesc = privilegeDesc;
	}

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(Date activeDate) {
		this.activeDate = activeDate;
	}

	@JsonSerialize(using = DateSerializer.class)
	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFeatures() {
		return features;
	}

	public void setFeatures(String features) {
		this.features = features;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

//	public ShopChain getShopChain() {
//		return shopChain;
//	}
//
//	public void setShopChain(ShopChain shopChain) {
//		this.shopChain = shopChain;
//	}

	public String getBusinessHours() {
		return businessHours;
	}

	public void setBusinessHours(String businessHours) {
		this.businessHours = businessHours;
	}

//	public MetroLineSite getMetroLineSite() {
//		return metroLineSite;
//	}
//
//	public void setMetroLineSite(MetroLineSite metroLineSite) {
//		this.metroLineSite = metroLineSite;
//	}

	public String getDiscountModel() {
		return discountModel;
	}

	public void setDiscountModel(String discountModel) {
		this.discountModel = discountModel;
	}

	public String getExpresion() {
		return expresion;
	}

	public void setExpresion(String expresion) {
		this.expresion = expresion;
	}
	
	

}
