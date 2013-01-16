package com.chinarewards.metro.service.message.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hsqldb.lib.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinarewards.metro.core.common.DateTools;
import com.chinarewards.metro.core.common.Dictionary;
import com.chinarewards.metro.core.common.HBDaoSupport;
import com.chinarewards.metro.core.common.JDBCDaoSupport;
import com.chinarewards.metro.core.common.Page;
import com.chinarewards.metro.domain.message.MessageTask;
import com.chinarewards.metro.domain.message.MessageTelephone;
import com.chinarewards.metro.domain.shop.Shop;
import com.chinarewards.metro.model.message.MessageCount;
import com.chinarewards.metro.service.message.IMessageService;
import com.chinarewards.metro.service.message.InitTelephoneTask;


@Service
public class MessageService implements IMessageService {
	@Autowired
	private HBDaoSupport hbDaoSupport;
	
	@Autowired
	private JDBCDaoSupport jdbcDaoSupport;
	
	
	@Override
	public MessageTask add(MessageTask mTask, String telephones) {
			
		if(!("").equals(telephones)){
			MessageTask mt=null;
			String[] t=telephones.split(",");
			int amount=0;
			if(mTask!=null){
				
			    mt=	hbDaoSupport.save(mTask);
			}
			for (int i = 0; i < t.length; i++) {
				if(!"".equals(t[i].trim())){
						MessageTelephone mtelephone=new MessageTelephone();
						mtelephone.setMessageTask(mTask);
						mtelephone.setTelephone(t[i]);
						hbDaoSupport.save(mtelephone);
						amount++;
				}
			}
			this.updateMessageAmount(mTask.getTaskId(), amount);
			return mt;
		}		
		return null;
	}

	@Override
	public void updateMessageTaskSatats(String taskid, int taskStates) {
		//改变内存中短信状态
		if(taskStates== Dictionary.TASK_END||taskStates== Dictionary.TASK_CANCEL){
			InitTelephoneTask.messageTask_map.remove(taskid);
		}else{
			InitTelephoneTask.messageTask_map.put(taskid, taskStates);	
		}
		 //改变DB中短信状态
		String hql = "update MessageTask set taskStates=:taskStates where taskId=:taskId";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("taskStates", taskStates);
		map.put("taskId", taskid);
		hbDaoSupport.executeHQL(hql, map);
	}
	
	
	public void updateMessageAmount(String taskid, int amount){
		String hql = "update MessageTask set amount = :amount where taskId=:taskId";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("amount", amount);
		map.put("taskId", taskid);
		hbDaoSupport.executeHQL(hql, map);
	}

	@Override
	public List<MessageTask> listMessagesTask(MessageTask mt, Page page) {
	
		List<Object> args = new ArrayList<Object>();
		List<Object> argsCount = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select taskId,taskName,content,planSendTime,actualSendTime,endTime,taskStates ,amount from MessageTask where 1=1");
		
	
		StringBuffer sqlCount = new StringBuffer();
		sqlCount.append("SELECT count(1) FROM MessageTask  WHERE 1=1");
		if(mt.getTaskStates()!=null&&!"".equals(mt.getTaskStates())){
			sql.append(" AND taskStates = ?");
			sqlCount.append(" and taskStates = ?");
			args.add(mt.getTaskStates());
			argsCount.add(mt.getTaskStates());
		}
		if(StringUtils.isNotEmpty(mt.getTaskName())){
			sql.append(" AND taskName = ?");
			sqlCount.append(" and taskName = ?");
			args.add(mt.getTaskName());
			argsCount.add(mt.getTaskName());
		}
		
		sql.append(" LIMIT ?,?");
		args.add(page.getStart());
		args.add(page.getRows());
		if(argsCount.size()>0){
			page.setTotalRows(jdbcDaoSupport.findCount(sqlCount.toString(),argsCount.toArray()));
		}else{
			page.setTotalRows(jdbcDaoSupport.findCount(sqlCount.toString()));
		}
	
		
		
		List<MessageTask> mtlist = jdbcDaoSupport.findTsBySQL(MessageTask.class, sql.toString(),args.toArray());
		//根据taskid获取发送成功号码数，失败号码数
		
		for(int i=0;i<mtlist.size();i++){
			MessageTask m=mtlist.get(i);
			MessageCount mc=this.selectMessageCount(m.getTaskId());
			if(mc!=null){
				mtlist.get(i).setSuccessAmount(mc.getSuccessAmount());
				mtlist.get(i).setFailureAmount(mc.getFailureAmount());
				mtlist.get(i).setNotSentAmount(mc.getNotSentAmount());
			}
			
		}
		
		return mtlist;
		
		
//		
//		String sql="select mt.*, s.successAmount,f.failureAmount from MessageTask mt,"+
//		"("+
//		  "select tp.messageTask_taskId as taskId,  count(tp.messageTask_taskId) as successAmount"+ 
//		  " from MessageTelephone tp  "+
//		  "where tp.states=1"+ 
//		") s,"+
//		"("+
//		"	select tp.messageTask_taskId as taskId,  count(tp.messageTask_taskId) as failureAmount"+
//		"	from MessageTelephone tp  "+
//		"where tp.states=2"+	
//		") f"+
//        "	where s.taskId=mt.taskId  and f.taskId=mt.taskId";
	
	}

	@Override
	public MessageTask viewMessageTask(String taskid) {
		MessageTask mt= hbDaoSupport.findTById(MessageTask.class, taskid);
		if(mt!=null){
			MessageCount mc=this.selectMessageCount(taskid);
			if(mc!=null){
				mt.setSuccessAmount(mc.getSuccessAmount());
				mt.setFailureAmount(mc.getFailureAmount());
				mt.setNotSentAmount(mc.getNotSentAmount());
			}	
			
		}
		return mt;
	}

	@Override
	public void updateMessageStates(String taskid, String telephone, int states) {
		String hql = "update MessageTelephone set states =:states , sendTime =:sendTime where messageTask_taskId=:messageTask_taskId and telephone=:telephone";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("states", states);
		map.put("telephone", telephone);
		map.put("messageTask_taskId", taskid);
		map.put("sendTime", DateTools.dateToHour());
		hbDaoSupport.executeHQL(hql, map);
	}

	@Override
	public List<MessageTelephone> selectMTelByTaskidStates(String taskId,int states) {
		StringBuffer hql = new StringBuffer();
		hql.append("from MessageTelephone where 1=1");
		
			hql.append(" and messageTask_taskId =?");
			hql.append(" and states =?");
		hql.append(" order by telephoneId desc");
		return hbDaoSupport.findTsByHQL(hql.toString(),taskId,states);
		
	}

	@Override
	public MessageCount selectMessageCount(String taskId) {
		String sql="select s.successAmount,f.failureAmount,n.notSentAmount from"+
		"("+
		  "select tp.messageTask_taskId as taskId,  count(tp.messageTask_taskId) as successAmount"+ 
		  " from MessageTelephone tp  "+
		  " where tp.states=1 and      tp.messageTask_taskId=?"+ 
		") s,"+
		"("+
		"	select tp.messageTask_taskId as taskId,  count(tp.messageTask_taskId) as failureAmount"+
		"	from MessageTelephone tp  "+
		" where tp.states=2 and      tp.messageTask_taskId=?"+	
		") f,"+
		"("+
		"select tp.messageTask_taskId as taskId,  count(tp.messageTask_taskId) as notSentAmount "+
		" from MessageTelephone tp "+ 
		" where tp.states=0 and   tp.messageTask_taskId=?"+
		")n";
		return jdbcDaoSupport.findTBySQL(MessageCount.class, sql, taskId,taskId,taskId);
	}

	@Override
	public List<MessageTask> listTaskByStates(int states) {
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append("from MessageTask where 1=1");
		hql.append(" and taskStates=?");
		map.put("taskStates",states);
	
		return hbDaoSupport.findTsByHQL(hql.toString(),states);
	}

	@Override
	public void deleteMessageTask(String taskid) {
		String thql = "delete from MessageTelephone where messageTask_taskId in(:taskid)";
		Map<String,Object> tmap = new HashMap<String, Object>();
		tmap.put("taskid", taskid);
		hbDaoSupport.executeHQL(thql, tmap);
		
		String hql = "delete from MessageTask where taskId in(:taskid)";
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("taskid", taskid);
		hbDaoSupport.executeHQL(hql, map);
	}

	@Override
	public List<MessageTelephone> listTelephoneByTaskid(String taskId,Page page,int states) {
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append("from MessageTelephone where 1=1");
		
		hql.append(" and messageTask_taskId = :tackId");
		map.put("tackId",taskId);
		if(states!=0){
			hql.append(" and states = :states");
			map.put("states",states);
		}
	
		hql.append(" order by telephoneId desc");
		return hbDaoSupport.findTsByHQLPage(hql.toString(), map, page);
	}

	@Override
	public void updateMessageTaskEndtime(String taskid, Date endTime) {
		String hql = "update MessageTask set endTime=:endTime where taskId=:taskId";
		Map<String,Object> map = new HashMap<String,Object>();
		 map.put("endTime", endTime);
		map.put("taskId", taskid);
		hbDaoSupport.executeHQL(hql, map);
		
	}

	@Override
	public int checkTelephone(String sTelephone, List<MessageTelephone> listTelephone) {
		int tel=0;
		if(!StringUtil.isEmpty(sTelephone)){
			String[] t=sTelephone.split(",");
			String[] tl=sTelephone.split(",");
			for (int i = 0; i < t.length; i++) {
				if(!t[i].trim().equals("")){
					String tvalue=t[i];
					int count=0;
					for(int j = 0; j < tl.length; j++){
						if(!tl[j].trim().equals("")){
							if(tvalue.equals(tl[j])){
								count++;
							}
						}
					}
					if(count>=2){
						tel=1;
						break;
					}
					if(t[i].length()!=11){
						tel=2;
						break;
					}
				}
			}
		}
		if(listTelephone!=null&&listTelephone.size()!=0){
			for(MessageTelephone t:listTelephone){
				int count=0;
				for(MessageTelephone tl:listTelephone){
					if(!t.getTelephone().trim().equals("")&&!tl.getTelephone().trim().equals("")){
						if(t.getTelephone().equals(tl.getTelephone())){
							count++;
						}
					}
				}
				if(count>=2){
					tel=1;
					break;
				}
				if(t.getTelephone().length()!=11){
					tel=2;
					break;
				}
			}
		}
		return tel;
	}
}
