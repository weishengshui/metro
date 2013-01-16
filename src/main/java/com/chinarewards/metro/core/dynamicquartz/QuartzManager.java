/**
 * 
 */
package com.chinarewards.metro.core.dynamicquartz;

import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdScheduler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class QuartzManager {
	private static Scheduler scheduler;

	static {
		  ApplicationContext context = new ClassPathXmlApplicationContext("quartzDynamic.xml");  
         scheduler = (StdScheduler) context.getBean("schedulerFactory");  
	}

	public static boolean enableCronSchedule(JobDetail jobDetail,String name,String timer) {
		
		try {
			
	            scheduler.addJob(jobDetail, true);  

	            CronTrigger cronTrigger =new CronTrigger(name, Scheduler.DEFAULT_GROUP, jobDetail.getName(), Scheduler.DEFAULT_GROUP);  
	            cronTrigger.setCronExpression(timer);  

	            scheduler.scheduleJob(cronTrigger); 
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static boolean disableSchedule(String jobId, String jobGroupId) {
		if (jobId.equals("") || jobGroupId.equals("")) {
			return false;
		}
		try {
			Trigger trigger = getJobTrigger(jobId, jobGroupId);
			if (null != trigger) {
				scheduler.deleteJob(jobId, jobGroupId);
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static JobDetail getJobDetail(String jobId, String jobGroupId) {
		if (jobId.equals("") || jobGroupId.equals("") || null == jobId
				|| jobGroupId == null) {
			return null;
		}
		try {
			return scheduler.getJobDetail(jobId, jobGroupId);
		} catch (SchedulerException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Trigger getJobTrigger(String jobId, String jobGroupId) {
		if (jobId.equals("") || jobGroupId.equals("") || null == jobId
				|| jobGroupId == null) {
			return null;
		}
		try {
			return scheduler.getTrigger(jobId + "Trigger", jobGroupId);
		} catch (SchedulerException e) {
			e.printStackTrace();
			return null;
		}
	}

}
