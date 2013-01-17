<%@page import="com.chinarewards.metro.core.common.Dictionary"%>
<%@ page language="java" contentType="text/html; charset=utf8" pageEncoding="utf8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery/themes/icon.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/extend-easyui-validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/constant.js"></script>
<style type="text/css">
	 table tr{height: 35px;}
	{margin-bottom:10px;margin:0px;width:600px;font-size:14px;}
	select{width:155px;height:20px;}
	.red{color:red;font-size:12px;}
</style>
<script>

$(function(){
	
    var senddate= $("#senddate").val();
	if(senddate.length==0){
		$("#immedSend").attr("checked",true);
	}else{
		$("#planSend").attr("checked",true);
	}
	 $('#ft').css("display","none");
	 $('#allt').attr("style","display:none");
	 
	 if(${task.taskStates}!=2){
		 $('#againSend').attr("style","display:none");
	 }
	 
	 //状态值
	  var status = ${statusJson};
	    for(var i=0;i<status.length;i++){
	        if((${task.taskStates}) == status[i].key){
	        	$("#states").html(status[i].value);
	        }
	    }
});
	
	
function viewfailure(){
		var taskid='${task.taskId}';
		 $('#allt').attr("style","display:none");
		
		 $('#ft').css("display","block");
		$.ajax({
			url:'failureTel',
			type:'post',
			data:'taskid='+taskid,
			success: function(data){
				$('#ftable').datagrid('reload');
			}
		}); 
}

function viewAll(){
	var taskid='${task.taskId}';
	 $('#ft').css("display","none");
	 $('#allt').css("display","block");
	 
	$.ajax({
		url:'listTel',
		type:'post',
		data:'taskid='+taskid,
		success: function(data){
			$('#atable').datagrid('reload');
		}
	}); 
}

function newSend(){
	var taskid='${task.taskId}';
	 
	$.ajax({
		url:'sendFailureTel',
		type:'post',
		data:'taskid='+taskid,
		success: function(data){
			alert(eval('('+data+')').msg);
		}
	}); 
}

function sendResult(v){
	var sr="";
	if(v == 1)
		st='成功';
	
	if(v==2)
		st='失败';
	if(v==0)
		st='没发送';
	return st;
}
</script>

</head>
<body>
	
	<div id="tt" class="easyui-tabs" style="height:400px;margin:10px;" > 
     
            <table  border="0" style="padding: 20px;">
			<tr>
				<td><span style="color: red;">*</span>任务名称:</td>
				<td> <input type="hidden" id="taskid" name="taskid" value="${task.taskId}"/>
				${task.taskName}
				</td>
			</tr>
			<tr>
				<td>短信内容:</td>
				<td>${task.content}</td>
			</tr>
			<tr>
				<td>
				    <input type="radio" id="immedSend" name="sendType" value="1"/> 立即发送
				</td>
			</tr>
			<tr>
				<td>
				    <input type="radio" id="planSend" name="sendType" value="2" /> 计划发送时间 
				</td>
			    <td>
			      <input id="planSendDate" name="planSendDate" value="${task.planSendTime}" disabled="true" type="text" style="width:150px" class="easyui-datebox" />
			       <input type="hidden"  id="senddate" name="planSendDate" value="${task.planSendTime}" />
			    </td>
			</tr>
			<tr>
				<td>
				   状态：
				</td>
				<td id="states">
				   ${task.taskStates}
				</td>
			</tr>
			<tr>
				<td>
				   总数目： ${task.amount}
				</td>
				<td>
				   成功数目： ${task.successAmount}
				</td>
			</tr>
			<tr>
				<td>
				   未发数目： ${task.notSentAmount}
				</td>
				<td>
				   失败数目： ${task.failureAmount}
				</td>
			</tr>
			<tr>
				<td style="margin-right:20px;" >
				  <button type="button" onclick="viewfailure();"> 查看失败号码
				  </button>
				</td>
				<td style="margin-right:20px;">
				    <button type="button" onclick="viewAll();">  查看全部号码
				    </button>
				</td>
			</tr>
		</table>
             
      
	</div>
	
	
	
	<div id="allt">  
	<div>${task.taskName}任务--所有电话列表：</div>
	  <table id="atable" class="easyui-datagrid" data-options="rownumbers:true,url:'listTel?taskid=${task.taskId}',fitColumns:true,striped:true,loadMsg:'正在载入...',pagination:true,
		rownumbers:true,pageList:pageList,singleSelect:true">
	    <thead>  
	        <tr>  
	            <th data-options="field:'telephone',width:30">电话号码</th>
	           <th data-options="field:'sendTime',width:30,formatter:function(v){return dateFormat(v)}">发送时间</th>
	            <th data-options="field:'states',width:30,formatter:function(v){return sendResult(v)}">发送结果</th>
	        </tr>   
	    </thead>  
	 </table> 
	</div>
	
	<div id="ft" >  
	<div>${task.taskName}任务--发送失败电话列表：</div>
	 <table id="ftable" class="easyui-datagrid" data-options="rownumbers:true,url:'failureTel?taskid=${task.taskId}',fitColumns:true,striped:true,loadMsg:'正在载入...',pagination:true,
		rownumbers:true,pageList:pageList,singleSelect:true">
	    <thead>  
	        <tr>  
	            <th data-options="field:'telephone',width:30">电话号码</th>
	           <th data-options="field:'sendTime',width:30,formatter:function(v){return dateFormat(v)}">发送时间</th>
	            <th data-options="field:'states',width:30,formatter:function(v){return sendResult(v)}">发送结果</th>
	        </tr>   
	    </thead>  
	 </table> 
	  <div style="text-align:left;">
  			<br>
  			<button type="button" onclick="newSend();" id="againSend">重新发送</button>&nbsp;&nbsp;&nbsp;&nbsp;
	</div>
	</div>
</body>
</html>