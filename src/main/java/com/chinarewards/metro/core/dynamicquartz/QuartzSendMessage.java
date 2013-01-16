package com.chinarewards.metro.core.dynamicquartz;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean.StatefulMethodInvokingJob;

import com.chinarewards.metro.control.message.SendMessage;
import com.chinarewards.metro.core.common.Dictionary;
import com.chinarewards.metro.domain.message.MessageTelephone;
import com.chinarewards.metro.service.message.IMessageService;
import com.chinarewards.metro.sms.CommunicationService;
import com.chinarewards.metro.sms.ICommunicationService;

public class QuartzSendMessage extends StatefulMethodInvokingJob {
	
	@Autowired
	private IMessageService messageservice;
	
	@Autowired  
	private ICommunicationService communicationService;
	
	@Autowired  
    private TaskExecutor taskExecutor;  
	
	private static int i = 0;
	private int j = 0; 
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
	
		String taskid=(String) context.getJobDetail().getJobDataMap().get("taskid");
		String content=(String) context.getJobDetail().getJobDataMap().get("content");
		List<MessageTelephone> phlist=(List<MessageTelephone>) context.getJobDetail().getJobDataMap().get("telephonelist");
		int priority=(Integer) context.getJobDetail().getJobDataMap().get("priority");
		//context.getJobDetail().getJobDataMap().get("messageservice");
		if(phlist!=null){
			 messageservice.updateMessageTaskSatats(taskid, Dictionary.TASK_NOTEXECUTE);
			 taskExecutor.execute(new SendMessage(phlist,content,priority,taskid,messageservice,communicationService)); 
		}
	}
	
	



	public void setMessageservice(IMessageService messageservice) {
		this.messageservice = messageservice;
	}


	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public void setCommunicationService(ICommunicationService communicationService) {
		this.communicationService = communicationService;
	}

	
	
}
