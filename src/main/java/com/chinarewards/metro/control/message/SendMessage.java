package com.chinarewards.metro.control.message;

import java.util.Date;
import java.util.List;

import org.hsqldb.lib.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.chinarewards.metro.core.common.DateTools;
import com.chinarewards.metro.core.common.Dictionary;
import com.chinarewards.metro.core.servlet.CommonInitServlet;
import com.chinarewards.metro.domain.message.MessageTelephone;
import com.chinarewards.metro.service.message.IMessageService;
import com.chinarewards.metro.service.message.InitTelephoneTask;
import com.chinarewards.metro.service.message.impl.MessageService;
import com.chinarewards.metro.sms.ICommunicationService;
import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;



public class SendMessage implements Runnable{
	

	private IMessageService messageservice;
	private ICommunicationService communicationservice;

	private String telephones;//电话号码
	private String content;//短信内容
	private int priority;//优先级
	private String taskid;//任务id
	private List<MessageTelephone> telephonelist;
	
	/**
	 * 创建任务，及时发送
	 * @param telephones
	 * @param content
	 * @param priority
	 * @param taskid
	 */
	public SendMessage(String telephones,String content,int priority,String taskid,IMessageService messageservice,ICommunicationService communicationservice){
		this.telephones=telephones;
		this.content=content;
		this.priority=priority;
		this.taskid=taskid;
		this.messageservice=messageservice;
		this. communicationservice= communicationservice;
	}
	
	/**
	 * 从数据库说去号码发送
	 * @param telephonelist
	 * @param content
	 * @param priority
	 * @param taskid
	 */
	public SendMessage(List telephonelist,String content,int priority,String taskid,IMessageService messageservice,ICommunicationService communicationservice){
		this.telephonelist=telephonelist;
		this.content=content;
		this.priority=priority;
		this.taskid=taskid;
		this.messageservice=messageservice;
		this. communicationservice= communicationservice;
	}
	
	/* 对当前任务里所有号码发送短信
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
	
			if(!StringUtil.isEmpty(telephones)){
				String[] t=telephones.split(",");
			System.out.println("---------------");
					//当前任务状态变为执行中
				messageservice.updateMessageTaskSatats(taskid, Dictionary.TASK_EXECUTING);
						for (int i = 0; i < t.length; i++) {
							if(!t[i].trim().equals("")){
								try {
									this.send(taskid, t[i]);
									Thread.sleep(2000);
								} catch (Exception e) {
									
								   messageservice.updateMessageStates(taskid, t[i], 2);
								}
							}
						}
						
						messageservice.updateMessageTaskSatats(taskid, Dictionary.TASK_END);
						messageservice.updateMessageTaskEndtime(taskid, DateTools.dateToHour());
			}	
			if(telephonelist!=null&&telephonelist.size()!=0){
				//当前任务状态变为执行中
				messageservice.updateMessageTaskSatats(taskid, Dictionary.TASK_EXECUTING);	
				for(MessageTelephone t:telephonelist){
					if(t!=null){
						try {
							if(!t.getTelephone().trim().equals("")){
								this.send(taskid, t.getTelephone());
								Thread.sleep(2000);
							}
						} catch (Exception e) {
							messageservice.updateMessageStates(taskid,t.getTelephone(), 2);
						}
					}
				}
				messageservice.updateMessageTaskSatats(taskid, Dictionary.TASK_END);
				messageservice.updateMessageTaskEndtime(taskid, DateTools.dateToHour());
			}
	}
	
	public void send(String taskid,String tp){
		//检查当前任务状态
		if((Integer)InitTelephoneTask.messageTask_map.get(taskid)==Dictionary.TASK_EXECUTING){
			 System.out.println(taskid);
				//调用短信接口，发送短信
			Long isSucess= communicationservice.queueSMS("","", tp, content,priority,DateTools.dateToHour());
			 
			 //修改号码状态
			int states=0;
			//states=1;
			states=(isSucess!=null)?1:2;
			messageservice.updateMessageStates(taskid, tp, states);
			 
			 
			 //测试
			//messageservice.updateMessageStates(taskid, tp, 1);
		}
	}

}
