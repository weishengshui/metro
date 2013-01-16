package com.chinarewards.metro.core.dynamicquartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.chinarewards.metro.control.message.SendMessage;

public class SendMessageJob extends QuartzJobBean {  
      
    private QuartzSendMessage  sendmessage;  
  
    protected void executeInternal(JobExecutionContext context)  
            throws JobExecutionException {  
          
    	sendmessage.executeInternal(context);  
    }

	public QuartzSendMessage getSendmessage() {
		return sendmessage;
	}

	public void setSendmessage(QuartzSendMessage sendmessage) {
		this.sendmessage = sendmessage;
	}


  
  
}  
