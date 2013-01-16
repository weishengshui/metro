package com.chinarewards.metro.service.discount;

import java.util.Date;

import com.chinarewards.metro.domain.activity.ActivityInfo;
import com.chinarewards.metro.domain.member.Member;
import com.chinarewards.metro.domain.member.MemberCard;
import com.chinarewards.metro.domain.shop.Shop;

public interface IDiscountService {
	/**
	 * 随机自动生成优惠码
	 * @param shop   门店对象 
	 * @return  优惠码
	 */
	public String getCompRandCode(Shop shop);
	
	
	/**
	 * 随机获得导入好的优惠吗
	 * @param shop   门店对象
	 * @return  优惠码
	 */
	public String getImpRandCode(Shop shop);
	
	/**
	 * 根据会员信息获得对应的优惠码信息
	 * @param member   会员对象
	 * @param card   会员卡对象
	 * @param shop   门店对象
	 * @param activityInfo   门店对象
	 * @return  优惠码
	 */
	public String getDiscountCode(Member member,MemberCard card,Shop shop,ActivityInfo activityInfo);
	
	
	
	/**
	 * 检测权益优惠码是否过期，如果过期返回false，否则根据权益优惠码把当前状态修改为使用中
	 * @param code  优惠码
	 * @param expiredDate 过期时间
	 * @return     false--过期；true--未使用
	 */
	boolean checkDiscountCode(String code);
	
	
	
	
}
