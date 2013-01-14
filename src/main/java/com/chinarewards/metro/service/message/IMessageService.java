package com.chinarewards.metro.service.message;

import java.util.Date;
import java.util.List;

import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.domain.message.MessageTask;
import com.chinarewards.metro.domain.message.MessageTelephone;
import com.chinarewards.metro.model.message.MessageCount;

public interface IMessageService {
	
	
	/**新增任务
	 * @param mTask
	 * @param telephones
	 */
	MessageTask add(MessageTask mTask,String telephones);
	
	
	
	/**
	 * 删除任务
	 * @param taskid
	 * @param taskStates
	 */
	void deleteMessageTask(String taskid);
	
	
	/**
	 * 修改任务状态
	 * @param taskid
	 * @param taskStates
	 */
	void updateMessageTaskSatats(String taskid,int taskStates);
	
	


	/**
	 * 修改短信数目
	 * @param taskid
	 * @param amount
	 */
	void updateMessageAmount(String taskid, int amount);
	
	/**
	 * 查询所有短信任务
	 * @param messageTask
	 * @param page
	 * @return
	 */
	List<MessageTask> listMessagesTask(MessageTask messageTask,Page page);
	
	/**
	 * 查看短信任务
	 * @param taskid
	 * @return
	 */
	MessageTask viewMessageTask(String taskid);
	
	/**
	 * 查询所有执行中的短信任务
	 * @param messageTask
	 * @param page
	 * @return
	 */
	List<MessageTask> listTaskByStates(int states);
	
	
	
	/**
	 *根据taskid和telephone修改当前发送电话号码的状态
	 * @param taskid
	 * @param telephone
	 * @param states
	 */
	void updateMessageStates(String taskid,String telephone,int states);
	
	
	/**
	 * 根据任务id、states获取电话号码
	 * @param taskId
	 * @return
	 */
	List<MessageTelephone> selectMTelByTaskidStates(String taskId,int states);
	
	
	/**
	 * 根据taskid获取现有电话号码
	 * @param taskId
	 * @return
	 */
	List<MessageTelephone> listTelephoneByTaskid(String taskId,Page page,int states);
	
	/**
	 * 根据taskid，计算出发送失败、成功号码
	 * @param taskId
	 * @return
	 */
	MessageCount selectMessageCount(String taskId);
	
	
	/**
	 * 修改任务结束时间
	 * @param taskid
	 * @param taskStates
	 */
	void updateMessageTaskEndtime(String taskid,Date endTime);
	
}
