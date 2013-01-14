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

	public static boolean enableCronSchedule(CustomJob schedulingJob, boolean isStateFull,JobDetail jobDetail) {
		if (schedulingJob == null) {
			return false;
		}
		try {
			CronTrigger trigger = (CronTrigger) scheduler
					.getTrigger(schedulingJob.getTriggerName(),
							schedulingJob.getJobGroup());
			if (null == trigger) {
//				JobDetail jobDetail = null;
//				if (isStateFull) {
//					jobDetail = new JobDetail(schedulingJob.getJobId(),
//							schedulingJob.getJobGroup(),
//							schedulingJob.getStateFulljobExecuteClass());
//				} else {
//					jobDetail = new JobDetail(schedulingJob.getJobId(),
//							schedulingJob.getJobGroup(),
//							schedulingJob.getJobExecuteClass());
//				}
//				jobDetail.setJobDataMap(paramsMap);
				trigger = new CronTrigger(schedulingJob.getTriggerName(),
						schedulingJob.getJobGroup(),
						schedulingJob.getCronExpression());
				scheduler.scheduleJob(jobDetail, trigger);
			} else {
				trigger.setCronExpression(schedulingJob.getCronExpression());
				scheduler.rescheduleJob(trigger.getName(), trigger.getGroup(),
						trigger);
			}
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
