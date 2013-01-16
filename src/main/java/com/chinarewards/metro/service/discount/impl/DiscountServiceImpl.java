package com.chinarewards.metro.service.discount.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinarewards.metro.core.common.HBDaoSupport;
import com.chinarewards.metro.domain.activity.ActivityInfo;
import com.chinarewards.metro.domain.member.Member;
import com.chinarewards.metro.domain.member.MemberCard;
import com.chinarewards.metro.domain.shop.DiscountCodeImport;
import com.chinarewards.metro.domain.shop.DiscountNumber;
import com.chinarewards.metro.domain.shop.Shop;
import com.chinarewards.metro.service.discount.IDiscountService;

@Service
public class DiscountServiceImpl implements IDiscountService {

	@Autowired
	private HBDaoSupport hbDaoSupport;

	@Override
	public String getCompRandCode(Shop shop) {
		String expre = shop.getExpresion();
		String[] expresion = expre.split("~");
		Random random = new Random();

		Integer compRandCode = 0;

		while (true) {
			Integer code = random.nextInt(Integer.valueOf(expresion[1]));
			if (code > Integer.valueOf(expresion[0])) {

				boolean flag = isUseCode(code.toString());
				if (!flag) {
					compRandCode = code;
					break;
				}
			}
		}
		return compRandCode.toString();
	}

	@Override
	public String getImpRandCode(Shop shop) {
		Random random = new Random();
		String impRandCode = null;
		List<DiscountCodeImport> codes = getImpCode(shop);
		if (codes != null && codes.size() > 0) {
			while (true) {
				int num = random.nextInt(codes.size());
				DiscountCodeImport codeImport = codes.get(num);
				String code = codeImport.getDiscountNum();
				boolean flag = isUseCode(code);
				if (!flag) {
					impRandCode = code;
					String hql = "update DiscountCodeImport set isRecived = ? where id = ?";
					hbDaoSupport.executeHQL(hql, 1, codeImport.getId());
					break;
				}
			}
		}
		return impRandCode;
	}

	@Override
	public String getDiscountCode(Member member, MemberCard card, Shop shop,
			ActivityInfo activityInfo) {

		String code = null;
		String tag = shop.getDiscountModel();

		if (tag.equals("1")) {
			code = getCompRandCode(shop);
			saveDiscountNumber(member, card, shop, code, activityInfo);
		} else {
			code = getImpRandCode(shop);
			saveDiscountNumber(member, card, shop, code, activityInfo);
		}

		return code;
	}

	/**
	 * 把生成的优惠码插到数据库中
	 */
	public void saveDiscountNumber(Member member, MemberCard card, Shop shop,
			String code, ActivityInfo activityInfo) {
		DiscountNumber number = new DiscountNumber();
		Date now_time = new Date();
		number.setActivityInfo(activityInfo);
		number.setDescr(shop.getPrivilegeDesc());
		number.setDiscountNum(code);
		number.setGeneratedDate(now_time);
		Calendar cal = Calendar.getInstance();
		cal.setTime(now_time);
		cal.add(Calendar.HOUR_OF_DAY, 3);
		number.setExpiredDate(cal.getTime());// 默认3小时后过期
		number.setMember(member);
		number.setShop(shop);
		number.setTitle(shop.getPrivilegeTile());
		hbDaoSupport.save(number);
		startThread(number);
	}

	/**
	 * 启动一条线程，每隔5秒定时检查一遍数据 用于处理失效后优惠码
	 */
	public void startThread(DiscountNumber number) {
		ScheduledThreadPoolExecutor scheduledThreadPool = new ScheduledThreadPoolExecutor(
				30);
		scheduledThreadPool.scheduleAtFixedRate(new ThreadPoolTask(number,
				hbDaoSupport), 1000, 5000, TimeUnit.MILLISECONDS);
	}

	/**
	 * 根据门店ID查找所有的导入优惠码信息
	 * 
	 * @param shop
	 *            门店对象
	 */
	public List<DiscountCodeImport> getImpCode(Shop shop) {

		String hql = "from DiscountCodeImport where shopId = ? ";
		List<DiscountCodeImport> codes = hbDaoSupport.findTsByHQL(hql,
				shop.getId());

		return codes;
	}

	/**
	 * 对优惠码的使用状态进行校验
	 * 
	 * @param code
	 *            优惠码
	 * @return flag true:表示此优惠码已经被使用;false:没被使用
	 */
	public boolean isUseCode(String code) {
		Date now_time = new Date();
		boolean flag = false;
		String hql = "from DiscountNumber where discountNum = ?";
		DiscountNumber number = hbDaoSupport.findTByHQL(hql, code);
		if (number != null) {
			Date expiredTime = number.getExpiredDate();
			flag = now_time.before(expiredTime);
		}
		return flag;
	}

	@Override
	public boolean checkDiscountCode(String code) {
        boolean checkResult=true;
    	Date nowTime = new Date();
    	String hql1 = "from DiscountNumber where discountNum = ?";
		DiscountNumber number = hbDaoSupport.findTByHQL(hql1, code);    
		if(number==null){
			checkResult=false;
		}else{
			if(number.getExpiredDate()!=null){
				if(nowTime.before(number.getExpiredDate())){
					//修改状态
					String hql2 = "update DiscountNumber set state=:state where discountNum=:discountNum";
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("discountNum", code);
					map.put("state", 1);
					hbDaoSupport.executeHQL(hql2, map);
				}else{
					checkResult=false;
				}
			}
		}
		return checkResult;
	}
}
