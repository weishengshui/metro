package com.chinarewards.metro.core.common;

import java.io.File;


public abstract class Constants {

	public static String sp = File.separator;
	/**
	 * 分页参数的大小
	 */
	public static final int PERPAGE_SIZE = 10;

	/**
	 * 商品类别根节点
	 */
	public static final String MERCHANDISE_CATAGORY_ROOT = "0";

	public static final String UPLOAD_TEMP_UID_PREFIX = "tmp@";

	/**
	 * 上传的临时目录根路径
	 */
	public static final String UPLOAD_TEMP_BASE_DIR = System
			.getProperty("user.home") + "/metro/archive";

	/**
	 * 商品图片存放正式目录
	 */
	public static final String MERCHANDISE_IMAGE_DIR = System
			.getProperty("user.dir") + "/merchandise/images/";

	/**
	 * 商品图片缓存目录
	 */
	public static final String MERCHANDISE_IMAGE_BUFFER = System
			.getProperty("user.home") + "/merchandise/images/";

	/**
	 * 品牌logo存放正式目录
	 */
	public static final String BRAND_IMAGE_DIR = System
			.getProperty("user.dir") + "/brand/images/";

	/**
	 * 品牌logo缓存目录
	 */
	public static final String BRAND_IMAGE_BUFFER = System
			.getProperty("user.home") + "/brand/images/";
	
	/**
	 * 活动图片存放正式目录
	 */
	public static final String ACTIVITY_IMAGE_DIR = System
			.getProperty("user.dir") + "/activity/images/";

	/**
	 * 活动图片缓存目录
	 */
	public static final String ACTIVITY_IMAGE_BUFFER = System
			.getProperty("user.home") + "/activity/images/";
	
	/**
	 * 营销号码文件存放目录
	 */
	public static final String MESSAGETASK_CSV_DIR = System
			.getProperty("user.dir") + "/message/csv/";

	/**
	 * 导出联合会员EXCEL的目录
	 * 
	 */
	public static final String EXPORT_UNIONMEMBER_DIR = System
			.getProperty("user.dir") + "/metro/export/unionmember";

	/**
	 * 商户图片存放正式目录
	 */
	public static final String MERCHANT_IMAGE_DIR = System
			.getProperty("user.dir") + "/metro/archive/merchant/images";

	/**
	 * 上传文件存储在SESSION中的属性名
	 */
	public static final String UPLOADED_ARCHIVES = "uploaded_archives";
	/**
	 * 重置密码 (初始化密码)
	 */
	public static final String RESET_PASSWORD = "123456";
	
	/**
	 * 短信营销发送优先级
	 */
	public static final int TASK_PRIORITY = 2;
	/**
	 * 门店图片
	 */
	public static final String SHOP_IMG = System.getProperty("user.dir")+sp+"shop"+sp+"images"+sp;

}
