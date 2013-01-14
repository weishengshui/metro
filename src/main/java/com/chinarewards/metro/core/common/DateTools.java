package com.chinarewards.metro.core.common;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 日期工具类
 */
public class DateTools {

	/**
	 * 获取String型系统时间
	 * 
	 * @return String格式时间
	 */
	public static String systemDate() {
		Locale systime = Locale.CHINA;
		SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd",
				systime);
		// 求得本地机的系统时间;
		String strTime = timeformat.format(new Date());
		return strTime;
	}

	/**
	 * 获取String型系统时间(年月日时分秒)yyyy-MM-dd hh:mm:ss
	 * 
	 * @return String格式时间
	 */
	public static String systemDateToHour() {
		Locale systime = Locale.CHINA;
		SimpleDateFormat timeformat = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss", systime);
		// 求得本地机的系统时间;
		String strTime = timeformat.format(new Date());
		return strTime;
	}

	/**
	 * 获取Date型系统时间(年月日)
	 * 
	 * @return String格式时间
	 */
	public static Date dateToYear() {
		Locale systime = Locale.CHINA;
		SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd",
				systime);
		// 求得本地机的系统时间;
		String strTime = timeformat.format(new Date());
		return stringToDateD(strTime);
	}

	/**
	 * 获取Date型系统时间(年月日时分秒)yyyy-MM-dd
	 * 
	 * @return String格式时间
	 */
	public static Date dateToHour() {
		Locale systime = Locale.CHINA;
		SimpleDateFormat timeformat = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss", systime);
		// 求得本地机的系统时间;
		String strTime = timeformat.format(new Date());
		return stringToDate(strTime);
	}

	/**
	 * String转换Date
	 * 
	 * @param strDate
	 * @return Date 格式时间
	 */
	public static Date stringToDate(String strDate) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = null;
		try {
			date = df.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * String转换Date
	 * 
	 * @param strDate
	 * @return Date 格式时间
	 */
	public static Date stringToDateD(String strDate) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = df.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * Date转换String
	 * 
	 * @param date
	 * @return String格式时间
	 */
	public static String dateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String newTime = sdf.format(date);
		return newTime;
	}

	/*************** 时间处理方法 ************************/

	/**
	 * 根据格式获得日期字符串
	 * 
	 * @param sFormat
	 * @return
	 */
	public static String getDateStr(String sFormat) {
		Calendar tCal = Calendar.getInstance();
		Timestamp ts = new Timestamp(tCal.getTime().getTime());
		java.util.Date date = new java.util.Date(ts.getTime());
		SimpleDateFormat formatter = new SimpleDateFormat(sFormat);
		String tmpStr = formatter.format(date);
		return (tmpStr);
	}

	/**
	 * 根据给定格式获取特定时间的格式化显示
	 * 
	 * @param ts
	 * @param sFormat
	 * @return
	 */
	public static String getDateFormat(Timestamp ts, String sFormat) {
		Date date = new Date(ts.getTime());
		SimpleDateFormat formatter = new SimpleDateFormat(sFormat);
		String tmpStr = formatter.format(date);
		return tmpStr;
	}

	/**
	 * 格式化日期
	 * 
	 * @param ts
	 * @return
	 */
	public static String getSDate(Timestamp ts) {
		Date date = new Date(ts.getTime());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String tmpStr = formatter.format(date);

		return tmpStr;
	}

	/**
	 * 将String类型的日期转换为时间
	 * 
	 * @param dt
	 * @return
	 */
	public static Timestamp getTs(String dt) {
		Date date = java.sql.Date.valueOf(dt);
		Calendar tCal = Calendar.getInstance();
		tCal.setTime(date);
		Timestamp ts = new Timestamp(tCal.getTime().getTime());
		return ts;
	}

	/**
	 * 建议获得短日期的处理方式 例如: getShortDate(2004-10-10 10:10:10.123) = 2004-10-10
	 * 
	 * @param dt
	 * @return
	 */
	public static String getShortDate(String dt) {
		try {
			return dt.substring(0, dt.indexOf(" "));
		} catch (Exception e) {
			return dt;
		}
	}

	/**
	 * 取得当前日期时间
	 * 
	 * @return
	 */
	public static Timestamp getCurrDateTime() {
		Calendar tCal = Calendar.getInstance();
		Timestamp createDate = new Timestamp(tCal.getTime().getTime());
		return createDate;
	}

	/**
	 * 获得最常见的日期格式内容 : 年-月-日 小时-分钟-秒
	 * 
	 * @param ts
	 * @return
	 */
	public static String getSDateTime(Timestamp ts) {
		return getDateFormat(ts, "yyyy-mm-dd HH:mm:ss");
	}

	/* 格式化日期 */
	public static String getSTime(Timestamp ts) {
		return getDateFormat(ts, "HH:mm:ss");
	}

	/**
	 * 获取当天的日期
	 * 
	 * @return
	 */
	public static String getToday() {
		java.sql.Timestamp ts = new java.sql.Timestamp(
				System.currentTimeMillis());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(ts);

	}

	/**
	 * 获取当天的日期　yyyyMMdd格式
	 * 
	 * @return
	 */
	public static String getTodays() {
		java.sql.Timestamp ts = new java.sql.Timestamp(
				System.currentTimeMillis());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		return formatter.format(ts);

	}

	// 根据时间获得随机数
	public static String getRnd() {
		Calendar tCal = Calendar.getInstance();
		Timestamp ts = new Timestamp(tCal.getTime().getTime());
		java.util.Date date = new java.util.Date(ts.getTime());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
		String tmpStr = formatter.format(date)
				+ Math.round(Math.random() * 1000 + 1);

		return (tmpStr);
	}

	/**
	 * 计算日期之间的差值 2004-3-25 增加
	 * 
	 * @param dt1
	 * @param dt2
	 * @return
	 */
	public static int dateDiff(Timestamp dt1, Timestamp dt2) {
		long ldate1 = dt1.getTime();
		long ldate2 = dt2.getTime();
		return (int) ((ldate2 - ldate1) / (1000 * 60 * 60 * 24));
	}

	/**
	 * 计算日期之间的差值
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static String dateDiff(Date startTime, Date endTime, String voidFlag) {
		// 按照传入的格式生成一个simpledateformate对象
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		long nh = 1000 * 60 * 60;// 一小时的毫秒数
		long nm = 1000 * 60;// 一分钟的毫秒数
		//long ns = 1000;// 一秒钟的毫秒数
		long diff;
		String dateDiff="";
		try {
			// 获得两个时间的毫秒时间差异
			diff = endTime.getTime() - startTime.getTime();
			//long day = diff / nd;// 计算差多少天
			long hour = diff % nd / nh;// 计算差多少小时
			long min = diff % nd % nh / nm;// 计算差多少分钟
			//long sec = diff % nd % nh % nm / ns;// 计算差多少秒
			if("hourAndMin".equals(voidFlag)){
				dateDiff = dateDiffHMin(hour, min);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateDiff;
	}
	
	public static String dateDiffHMin(long hour, long min){
		return hour+"小时"+min+"分钟";
	}

	/**
	 * 获取明天的日期
	 * 
	 * @return
	 */
	public static String getTomorrow() {
		return getNextDay(getToday());
	}

	/**
	 * 获得当前日期的下一天
	 * 
	 * @param date
	 * @return
	 */
	public static String getNextDay(String date) {
		if (date == null || date.trim().length() == 0) {
			return "";
		}
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(f.parse(date));
		} catch (ParseException ex) {
			return date;
		}
		calendar.add(5, 1);
		return f.format(calendar.getTime());
	}

	/**
	 * LST num为正:当前日期后num天是返回值 num为负:当前日期前num天是返回值 返回的日期的格式:yyyy-MM-dd
	 */
	public static String getTheDay(int num) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar gc = new GregorianCalendar();
		gc.add(GregorianCalendar.DATE, num);
		Date theday = gc.getTime();
		return sdf.format(theday);
	}

	/**************************************/

	/**
	 * 获得当前日期与本周日相差的天数
	 * 
	 * @return 当前日期与本周日相差的天数
	 * */
	private static int getMondayPlus() {
		Calendar cd = Calendar.getInstance();
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		if (dayOfWeek == 0) {
			return -6;
		}
		if (dayOfWeek == 1) {
			return 0;
		} else {
			return 1 - dayOfWeek;
		}
	}

	/**
	 * 获得本周星期日的日期
	 * 
	 * @return 本周星期日的日期
	 * */
	public static Date getCurrentWeekday() {
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
		Date monday = currentDate.getTime();
		return monday;
	}

	/**
	 * 获得本周一的日期
	 * 
	 * @return 本周一的日期
	 * */
	public static Date getMondayOFWeek() {
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(GregorianCalendar.DATE, mondayPlus);
		Date monday = currentDate.getTime();

		return monday;
	}
	
	/**
	 * 获得昨天的日期
	 * 
	 * @return
	 */
	public static String getYesterDay(){
		Date as = new Date(new Date().getTime()-24*60*60*1000);
		SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
		String time = matter1.format(as);
		return time;
	}
	
	/**
	 * 获得本月第一天
	 */
	public static String getCurMonthFrist(){
		return getCurFristOrLast("frist");
	}
	
	/**
	 * 获得本月最后一天
	 */
	public static String getCurMonthLast(){
		return getCurFristOrLast("last");
	}
	
	public static String getCurFristOrLast(String flag) {
        Calendar cal = Calendar.getInstance();
        // 当前月＋1
        cal.add(Calendar.MONTH, 1);
        // 将下个月1号作为日期初始值
        cal.set(Calendar.DATE, 1);
        // 下个月1号减去一天，即得到当前月最后一天
        cal.add(Calendar.DATE, -1);
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String day_end = df.format(cal.getTime());
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, 1);
        if("frist".equals(flag)){
        	return df.format(c.getTime());
        }else if("last".equals(flag)){
        	return day_end;
        }
        return "";
    }
	
	/**
	 * 获取日期的最后一秒，即将日期的时分秒转化为23:59:59
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDateLastSecond(Date date){
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String str = dateFormat.format(date);
		str = str.substring(0, 10) + " 23:59:59";
		try {
			date = dateFormat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
