package com.chinarewards.metro.core.timer;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class EmailReportJob extends QuartzJobBean {
	
//	private int timeout;
	
	
	public void setTimeout(int timeout) {
//		this.timeout = timeout;
	}


	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("EmailReportJob executeInternal() time is "+dateFormat.format(new Date()));
	}
	
}	
