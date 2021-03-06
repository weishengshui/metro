package com.chinarewards.metro.control.message;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.chinarewards.metro.core.common.CommonUtil;
import com.chinarewards.metro.core.common.Constants;
import com.chinarewards.metro.core.common.DateTools;
import com.chinarewards.metro.core.common.Dictionary;
import com.chinarewards.metro.core.common.FileUtil;
import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.core.common.UUIDUtil;
import com.chinarewards.metro.core.dynamicquartz.CustomJob;
import com.chinarewards.metro.core.dynamicquartz.SendMessageJob;
import com.chinarewards.metro.core.dynamicquartz.QuartzManager;
import com.chinarewards.metro.core.dynamicquartz.QuartzSendMessage;
import com.chinarewards.metro.domain.message.MessageTask;
import com.chinarewards.metro.domain.message.MessageTelephone;
import com.chinarewards.metro.model.common.AjaxResponseCommonVo;
import com.chinarewards.metro.service.message.IMessageService;
import com.chinarewards.metro.sms.ICommunicationService;
import com.chinarewards.utils.StringUtil;
import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;





@Controller
@RequestMapping("/message")
public class MessageControl {
	
	@Autowired
	private IMessageService messageservice;
	
	@Autowired  
    private TaskExecutor taskExecutor;  
	@Autowired  
	private ICommunicationService communicationservice;
	
	@RequestMapping("/add")
	public String add(){
		return "message/add";
	}
	
	@RequestMapping(value = "/addSave")
	@ResponseBody
	public void addSave(@RequestParam("telephoneFile") MultipartFile mFile,String taskName,HttpServletResponse response,String content,String planSendDate,String sendType) throws IOException{
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		MessageTask mTask = new MessageTask();
		if(!StringUtil.isEmptyString(taskName)){
			if(!StringUtil.isEmptyString(content)){
				mTask.setTaskName(taskName);
				mTask.setContent(content);
			}
			if (!StringUtil.isEmptyString(planSendDate)){
				mTask.setPlanSendTime(DateTools.stringToDate(planSendDate));
			}
			if("1".equals(sendType)){
				mTask.setPlanSendTime(null);
				mTask.setActualSendTime(DateTools.dateToHour());
			}
		}
		
		String telephones="";
		String fileName = mFile.getOriginalFilename();
		File file=null;
		String encoding = "utf-8"; 
		String suffix = getSuffix(fileName);
		fileName = UUIDUtil.generate() + suffix;
		int isQualified=0;
		
		FileUtil.saveFile(mFile.getInputStream(), Constants.MESSAGETASK_CSV_DIR, fileName);
		file=new File(Constants.MESSAGETASK_CSV_DIR, fileName); 
			//读取csv文件
			if (file.isFile() && file.exists()) {  
	            InputStreamReader read = new InputStreamReader(  
	                    new FileInputStream(file), encoding);  
	            BufferedReader bufferedReader = new BufferedReader(read);  
	            String lineTXT = null;  
	            while ((lineTXT = bufferedReader.readLine()) != null) {  
	            	telephones+=lineTXT.toString().trim()+",";
	           }  
	           isQualified=messageservice.checkTelephone(telephones, null);
	             read.close();  
	        }else{  
	            System.out.println("找不到指定的文件！");  
	        }
       
		if(isQualified==0){
			MessageTask mt=null;
			if (mTask!=null) { // insert
				mt=messageservice.add(mTask, telephones);
				out.println(CommonUtil.toJson(new AjaxResponseCommonVo(
						"添加成功")));
			}
			//立即发送短信，用线程池的方式
			if("1".equals(sendType)&&mt!=null){
				 messageservice.updateMessageTaskSatats(mt.getTaskId(), Dictionary.TASK_NOTEXECUTE);
				 taskExecutor.execute(new SendMessage(telephones,mTask.getContent(),Constants.TASK_PRIORITY,mt.getTaskId(),messageservice,communicationservice));  
			}
			
			//定时发送
			if("2".equals(sendType)&&mTask.getPlanSendTime()!=null){
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time = formatter.format(mTask.getPlanSendTime());
				    CustomJob job = new CustomJob();
				    List<MessageTelephone> mtplist=messageservice.selectMTelByTaskidStates(mt.getTaskId(),0);
				    
				    String[] str=time.split(" ");
				    String[] str1=str[0].split("-");
				    String[] str2=str[1].split(":");
				    String timer=Integer.valueOf(str2[2])+" "+Integer.valueOf(str2[1])+" "+Integer.valueOf(str2[0])+" "+Integer.valueOf(str1[2])+" "+Integer.valueOf(str1[1])+" ? "+Integer.valueOf(str1[0]);
			
					JobDetail jobDetail = new JobDetail();  
//					jobDetail = new JobDetail(job.getJobId(),
//							job.getJobGroup(),
//							job.getStateFulljobExecuteClass());
		            jobDetail.setName(mt.getTaskId());  
		        	jobDetail.getJobDataMap().put("taskid", mt.getTaskId());
					jobDetail.getJobDataMap().put("content", mTask.getContent());
					jobDetail.getJobDataMap().put("telephonelist", mtplist);
					jobDetail.getJobDataMap().put("priority", Constants.TASK_PRIORITY);
		        	QuartzSendMessage sendmessage = new QuartzSendMessage(); 
		        	sendmessage.setCommunicationService(communicationservice);
		        	sendmessage.setMessageservice(messageservice);
		        	sendmessage.setTaskExecutor(taskExecutor);
		        
		            jobDetail.getJobDataMap().put("sendmessage", sendmessage);  
		            jobDetail.setJobClass(SendMessageJob.class);  
		            QuartzManager.enableCronSchedule(jobDetail, mt.getTaskId(),timer);
		            messageservice.updateMessageTaskSatats(mt.getTaskId(), Dictionary.TASK_NOTEXECUTE);
			}
			
		}else if(isQualified==1){
			out.println(CommonUtil.toJson(new AjaxResponseCommonVo("号码有重复，请检查号码！")));
		}else if(isQualified==2){
			out.println(CommonUtil.toJson(new AjaxResponseCommonVo("号码不足11位，请检查号码！")));
		}
			out.flush();
	}
	
	@RequestMapping("/list")
	public String list(Model model)throws Exception{
		model.addAttribute("status", Dictionary.findMessageTaskStatus());
		model.addAttribute("statusJson", CommonUtil.toJson(Dictionary.findMessageTaskStatus()));
		model.addAttribute("test", 1);
		return "message/list";
	}
	
	/**
	 * 获取所有任务
	 * @param messageTask
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listMessagesTask")
	public Map<String,Object> listMessagesTask(MessageTask messageTask,Page page)throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("rows", messageservice.listMessagesTask(messageTask, page));
		map.put("total", page.getTotalRows());
		return map;
	}
	
	/**
	 *  查看任务
	 * @param model
	 * @param taskid
	 * @return
	 */
	@RequestMapping("/viewTask")
	public String viewTask(Model model,String taskid){
//		MessageTask task= messageservice.viewMessageTask(taskid);
		model.addAttribute("task", messageservice.viewMessageTask(taskid));
		//model.addAttribute("states",1);
		model.addAttribute("statusJson", CommonUtil.toJson(Dictionary.findMessageTaskStatus()));
		return "message/viewTask";
	}
	/**
	 * 暂停任务
	 * @param taskId
	 */
	@ResponseBody
	@RequestMapping("/pauseTask")
	public void pauseTask(String taskId){
		 messageservice.updateMessageTaskSatats(taskId, Dictionary.TASK_PAUSE);
	}
	
	/**
	 * 重启任务，通知发短信
	 * @param taskId
	 */
	@ResponseBody
	@RequestMapping("/restartTask")
	public void restartTask(String taskid){
		 messageservice.updateMessageTaskSatats(taskid, Dictionary.TASK_EXECUTING);
		 MessageTask mTask=messageservice.viewMessageTask(taskid);
		 List<MessageTelephone> mtplist=messageservice.selectMTelByTaskidStates(taskid,0);
		 if(!StringUtil.isEmptyString(mTask.getContent())&&mtplist.size()!=0&&mtplist!=null){
			 taskExecutor.execute(new SendMessage(mtplist,mTask.getContent(),Constants.TASK_PRIORITY,taskid,messageservice,communicationservice));
		 }
	}
	
	
	/**
	 * 取消任务
	 * @param taskId
	 */
	@ResponseBody
	@RequestMapping("/cancelTask")
    public void cancelTask(String taskid){
		 messageservice.updateMessageTaskSatats(taskid, Dictionary.TASK_CANCEL);
    }
	
	
	/**
	 * 删除任务
	 * @param taskId
	 */
	@ResponseBody
	@RequestMapping("/deleteTask")
    public void deleteTask(String taskid){
		 messageservice.deleteMessageTask(taskid);
    }
	
	

	
	
	
	/**
	 * 重置任务，通知发短信
	 * @param taskId
	 */
	@ResponseBody
	@RequestMapping("/resetTask")
	public void resetTask(String taskid){
		 messageservice.updateMessageTaskSatats(taskid, Dictionary.TASK_CRATING);
		 MessageTask mTask=messageservice.viewMessageTask(taskid);
		 List<MessageTelephone> mtplist=messageservice.selectMTelByTaskidStates(taskid,2);
		 if(!StringUtil.isEmptyString(mTask.getContent())&&mtplist.size()!=0&&mtplist!=null){
			 taskExecutor.execute(new SendMessage(mtplist,mTask.getContent(),Constants.TASK_PRIORITY,taskid,messageservice,communicationservice));
		 }
	}
	@ResponseBody
	@RequestMapping("/failureTel")
	public  Map<String,Object> failureTelephone(String taskid,Page page){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("rows", messageservice.listTelephoneByTaskid(taskid, page,2));
		map.put("total", page.getTotalRows());
		return map;
	
	}
	
	
	/**
	 * 根据taskid获取telephone
	 * @param messageTask
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/listTel")
	public Map<String,Object>  listTelephone(String taskid,Page page)throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("rows", messageservice.listTelephoneByTaskid(taskid, page,0));
		map.put("total", page.getTotalRows());
		return map;
	}
	
	
	

	@RequestMapping("/sendFailureTel")
	@ResponseBody
	public void sendFailureTel(HttpSession session,
			HttpServletRequest request, HttpServletResponse response,String taskid) throws IOException{
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();

		List<MessageTelephone> alllist=messageservice.selectMTelByTaskidStates(taskid,2);
		MessageTask mt=new MessageTask();
		mt=messageservice.viewMessageTask(taskid);
		if(alllist!=null){
			 messageservice.updateMessageTaskSatats(taskid, Dictionary.TASK_NOTEXECUTE);
			 taskExecutor.execute(new SendMessage(alllist,mt.getContent(),Constants.TASK_PRIORITY,taskid,messageservice,communicationservice));  
		}
		out.println(CommonUtil.toJson(new AjaxResponseCommonVo("正在发送中...")));
		out.flush();
	}
	
	
	private String  getSuffix(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	} 	
}
