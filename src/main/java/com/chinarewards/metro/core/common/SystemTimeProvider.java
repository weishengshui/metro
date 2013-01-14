package com.chinarewards.metro.core.common;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class SystemTimeProvider {

	/**
	 * 获得系统当前时间，请在代码中不要使用 new Date()等 
	 * @return
	 */
	public static Date getCurrentTime() {
		return Calendar.getInstance().getTime();
	}
}
