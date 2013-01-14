package com.chinarewards.metro.core.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典表
 * 
 * @author huangshan
 * 
 */
public class Dictionary {
	/**
	 * 积分单位代码
	 */
	public static final String UNIT_CODE_BINKE = "BINKE"; // 缤刻
	public static final String UNIT_CODE_RMB = "RMB"; // 人民币

	/**
	 * 会员 优惠码状态
	 */
	public static final int MEMBER_DISCOUNT_NUMBER_NOT_USED = 0; // 未使用
	public static final int MEMBER_DISCOUNT_NUMBER_USED = 1; // 已使用
	public static final int MEMBER_DISCOUNT_NUMBER_EXPIRED = 2; // 过期

	/**
	 * 用户状态
	 */
	public static final int USER_STATE_NORMAL = 0; // 正常
	public static final int USER_STATE_LOCKED = 1; // 锁定

	/**
	 * 会员状态
	 */
	public static final int MEMBER_STATE_ACTIVATE = 1; // 已激活
	public static final int MEMBER_STATE_NOACTIVATE = 2; // 未激活
	public static final int MEMBER_STATE_LOGOUT = 3; // 注销

	/**
	 * 会员性别
	 */
	public static final int MEMBER_SEX_NOLIMIT = 0; // 不限制
	public static final int MEMBER_SEX_MALE = 1; // 男
	public static final int MEMBER_SEX_FEMALE = 2; // 女

	/**
	 * 优惠码生成方式
	 */
	public static final int PROME_Code_AUTO = 0; // 系统生成
	public static final int PROME_Code_IMPORT = 1; // 文件导入

	/**
	 * 积分基本信息
	 */
	public static final String INTEGRAL_RMB_ID = "0"; // 积分--人民币ID
	public static final String INTEGRAL_BINKE_ID = "1"; // 积分--缤刻ID
	public static final int INTEGRAL_AVAILABLE_UNIT_DAY = 0; // 积分--有效期单位：天
	public static final int INTEGRAL_AVAILABLE_UNIT_MONTH = 1; // 积分--有效期单位：月
	public static final String INTEGRAL_BINKE_DEFAULT_NAME = "缤刻"; // 积分--缤刻积分默认名称：缤刻

	/**
	 * 营销任务状态
	 */
	public static final int TASK_EXECUTING = 1;// 执行中
	public static final int TASK_END = 2;// 已结束
	public static final int TASK_PAUSE = 3;// 暂停
	public static final int TASK_CANCEL = 4;// 取消
	public static final int TASK_CRATING = 5;// 创建中
	public static final int TASK_FAILURE = 6;// 失败
	public static final int TASK_NOTEXECUTE = 7;// 未执行
	
	/**
	 * 获取会员状态集合
	 * 
	 * @return
	 */
	public static List<BoxValue<Integer, String>> findMemberStatus() {
		List<BoxValue<Integer, String>> list = new ArrayList<BoxValue<Integer, String>>();
		list.add(new BoxValue<Integer, String>(MEMBER_STATE_ACTIVATE, "已激活"));
		list.add(new BoxValue<Integer, String>(MEMBER_STATE_NOACTIVATE, "未激活"));
		list.add(new BoxValue<Integer, String>(MEMBER_STATE_LOGOUT, "已注销"));
		return list;
	}

	public static List<BoxValue<Integer, String>> findMemberSex() {
		List<BoxValue<Integer, String>> list = new ArrayList<BoxValue<Integer, String>>();
		list.add(new BoxValue<Integer, String>(MEMBER_SEX_NOLIMIT, "不限制"));
		list.add(new BoxValue<Integer, String>(MEMBER_SEX_MALE, "男"));
		list.add(new BoxValue<Integer, String>(MEMBER_SEX_FEMALE, "女"));
		return list;
	}

	/**
	 * 获取营销任务状态集合
	 * 
	 * @return
	 */
	public static List<BoxValue<Integer, String>> findMessageTaskStatus() {
		List<BoxValue<Integer, String>> list = new ArrayList<BoxValue<Integer, String>>();
		list.add(new BoxValue<Integer, String>(TASK_NOTEXECUTE, "未执行"));
		list.add(new BoxValue<Integer, String>(TASK_EXECUTING, "执行中"));
		list.add(new BoxValue<Integer, String>(TASK_END, "已结束"));
		list.add(new BoxValue<Integer, String>(TASK_PAUSE, "暂停"));
		list.add(new BoxValue<Integer, String>(TASK_CANCEL, "取消"));
		list.add(new BoxValue<Integer, String>(TASK_CRATING, "创建中"));
		list.add(new BoxValue<Integer, String>(TASK_FAILURE, "失败"));
		return list;
	}
	
	public static String getPicPath(String pathName){
		if("SHOP_IMG".equals(pathName)){
			return Constants.SHOP_IMG;
		}else if("UPLOAD_TEMP_BASE_DIR".equals(pathName)){
			return Constants.UPLOAD_TEMP_BASE_DIR;
		}else if("MERCHANDISE_IMAGE_DIR".equals(pathName)){
			return Constants.MERCHANDISE_IMAGE_DIR;
		}else if("MERCHANDISE_IMAGE_BUFFER".equals(pathName)){
			return Constants.MERCHANDISE_IMAGE_BUFFER;
		}else if("BRAND_IMAGE_DIR".equals(pathName)){
			return Constants.BRAND_IMAGE_DIR;
		}else if("BRAND_IMAGE_BUFFER".equals(pathName)){
			return Constants.BRAND_IMAGE_BUFFER;
		}else if("ACTIVITY_IMAGE_DIR".equals(pathName)){
			return Constants.ACTIVITY_IMAGE_DIR;
		}else if("ACTIVITY_IMAGE_BUFFER".equals(pathName)){
			return Constants.ACTIVITY_IMAGE_BUFFER;
		}else if("MERCHANT_IMAGE_DIR".equals(pathName)){
			return Constants.MERCHANT_IMAGE_DIR;
		}else {
			return "";
		}
	}
}
