package com.chinarewards.metro.domain.member;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.chinarewards.metro.core.common.DateTimeSerializer;

@Entity
public class Member implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private String name;	//名
	private String surname; //姓
	private Integer sex;
	private String phone;
	private Date birthDay;
	private String identityCard; //身份证
	private String email;
	
	private String address ;
	private String province;
	private String city;
	private String area;
	private String postcode;
	private String source;	//注册来源 0表示系统注册 , 非0表示来源pos机且是posId
	
	@Column(nullable = true)
	private Integer industry;  //行业
	private String company;
	
	@Column(nullable = true)
	private Integer profession;//职业
	
	@Column(nullable = true)
	private Integer position;  //职位
	
	@Column(nullable = true)
	private Integer salary;    //年薪
	
	@Column(nullable = true)
	private Integer education; //学历
	
	@Column(updatable = false)
	private Integer status;
	
	@Column(updatable = false)
	private Date createDate;
	
	@Column(updatable = false)
	private String createUser;
	private Date updateDate;
	private String updateUser;
	
	@Column(updatable = false)
	private String account;
	
	@Column(updatable = false)
	private String password;
	
	@OneToOne(cascade=CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private MemberCard card;
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	private String valiCode;
	
	public String getValiCode() {
		return valiCode;
	}

	public void setValiCode(String valiCode) {
		this.valiCode = valiCode;
	}

	public MemberCard getCard() {
		return card;
	}

	public void setCard(MemberCard card) {
		this.card = card;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
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

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public Integer getIndustry() {
		return industry;
	}

	public void setIndustry(Integer industry) {
		this.industry = industry;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Integer getProfession() {
		return profession;
	}

	public void setProfession(Integer profession) {
		this.profession = profession;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Integer getSalary() {
		return salary;
	}

	public void setSalary(Integer salary) {
		this.salary = salary;
	}

	public Integer getEducation() {
		return education;
	}

	public void setEducation(Integer education) {
		this.education = education;
	}
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Member other = (Member) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
