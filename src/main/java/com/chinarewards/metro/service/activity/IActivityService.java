package com.chinarewards.metro.service.activity;

import java.util.Date;
import java.util.List;

import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.domain.activity.ActivityInfo;
import com.chinarewards.metro.domain.activity.BrandMode;
import com.chinarewards.metro.domain.brand.Brand;
import com.chinarewards.metro.domain.file.FileItem;
import com.chinarewards.metro.domain.pos.PosBind;
import com.chinarewards.metro.domain.shop.DiscountNumber;

public interface IActivityService {
	/**
	 * 创建活动
	 * @param activity
	 * @return
	 */
	public ActivityInfo saveActivity(ActivityInfo activity);
	
	/**
	 * 添加优惠信息
	 * @param discountNumber
	 * @return
	 */
	public DiscountNumber saveDiscountNumber(DiscountNumber discountNumber);
	
	/**
	 * 查询优惠信息
	 * @param id
	 * @return
	 */
	public DiscountNumber findDiscountNumberById(String id);
	
	/**
	 * 修改优惠信息
	 * @param title
	 * @param descr
	 * @param id
	 */
	public void updateDiscountNumberByActId(String title,String descr,Integer id); 
	
	/**
	 * 活动绑定POS机
	 * @param posBind
	 * @return
	 */
	public PosBind savePosBind(PosBind posBind);
	
	/**
	 * 修改活动内容
	 * @param activity
	 */
	public void updateActivity(ActivityInfo activity);
	
	/**
	 * 查询活动信息
	 */
	public List<ActivityInfo> findActivity(String activityName,String startDate,String endDate,Page page);
	
	/**
	 * 查询单条活动信息
	 * @param id
	 * @return
	 */
	public ActivityInfo findActivityById(String id);
	
	/**
	 * 查询绑定活动的品牌
	 * @param bm
	 * @param page
	 * @return
	 */
	public List<BrandMode> findBrandAct(Brand brand,Page page);
	
	/**
	 * 根据活动ID查询绑定的品牌
	 * @param brand
	 * @param page
	 * @param id
	 * @return
	 */
	public List<BrandMode> findBrandAct(String name,Page page,Integer id);
	
	/**
	 * 查询没有绑定活动的品牌
	 * @param bm
	 * @param page
	 * @return
	 */
	public List<Brand> findBrandNotBandAct(Brand brand,Page page,String id);
	
	/**
	 * 根据活动的ID删除关联品牌的信息
	 * @param actIds
	 */
	public void deleteActAndBranByIds(String[] actIds);
	
	/**
	 * 参加活动的品牌
	 * @param brandIds
	 * @param actId
	 * @param joinTime
	 */
	public void addBrandAct(String[] brandIds,Integer actId,Date joinTime);
	
	/**
	 * 删除活动信息
	 * @param id
	 */
	public void deleteActivity(String id);
	
	/**
	 * 取消活动信息
	 * @param id
	 */
	public void cancerActivity(String id);
	
	/**
	 * 检测pos机是否绑定门店 
	 * @param code
	 */
	public int checkPosBand(String code);
	
	
	/**
	 * 查询绑定活动的POS机
	 */
	public List<PosBind> queryPosBands(PosBind posBand,Page page);
	
	/**
	 * 根据活动ID取得绑定的POS机
	 * @param posBand
	 * @param page
	 * @param id
	 * @return
	 */
	public List<PosBind> queryPosBands(PosBind posBand,Page page,Integer id);

	/**
	 * 删除绑定活动的POS机
	 * @param posIds
	 */
	public void delPosBand(String[] posIds);
	
	/**
	 * 检查活动的开始时间和名称是否相同
	 * @param name
	 * @param dTime
	 * @return
	 */
	public int checkActNameAndTime(String name,Date dTime);
}
