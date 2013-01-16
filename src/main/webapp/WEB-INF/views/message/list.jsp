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
<script>
	
function getStatus(v){
    var status = ${statusJson};
    for(var i=0;i<status.length;i++){
        if(v == status[i].key){
        	return status[i].value;
        };
    };
}
</script>

</head>
<body>
	<!-- 查询条件Table -->
	<form id="searchForm">
		<table style="font-size:13px;">
			<tr>
				<td>任务名称:</td>
				<td><input type="text" name=taskName size="18"/></td>
				<td>状态:</td>
				<td>
					<select name="taskStates" style="width:100px;height:22px;">
						<option value=""></option>
						<c:forEach items="${status }" var="s">
							<option value="${s.key }">${s.value }</option>
						</c:forEach>
					</select>	
				</td>
				<td>&nbsp;<a id="btn" href="javascript:void(0)" onclick="searchs()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></td>
			</tr>
		<table>
	</form>



	    
	<!-- 显示列表Table -->
	<table id="table" class="easyui-datagrid" data-options="rownumbers:true,url:'listMessagesTask',fitColumns:true,loadMsg:'正在载入...',pagination:true,
		pageList:pageList,singleSelect:true,onClickRow:function(rowIndex,rowData){checkSelect(rowData.taskId,rowData.taskStates);}">
	    <thead>  
	        <tr>  
	            <th data-options="field:'taskId',checkbox:true"></th>
	            <th data-options="field:'taskStates',width:30,formatter:function(v){return getStatus(v)}">任务状态</th>
	            <th data-options="field:'taskName',width:30">任务名称</th>  
	            <th data-options="field:'planSendTime',width:30,formatter:function(v){return dateFormat(v)}">计划发送时间</th>
	            <th data-options="field:'actualSendTime',width:30,formatter:function(v){return dateFormat(v)}">实际发送时间</th>
	            <th data-options="field:'endTime',width:30,formatter:function(v){return dateFormat(v)}">结束时间</th>
	            <th data-options="field:'amount',width:30">总数目</th>
	            <th data-options="field:'successAmount',width:30">成功数目</th>
	            <th data-options="field:'failureAmount',width:30">失败数目</th>
	            <th data-options="field:'notSentAmount',width:30">未发送数目</th>
	        </tr>   
	    </thead>  
	</table> 
	
	<div style="text-align:left;">
  			<br>
  			<button type="button" onclick="viewTask();" id="view">查看任务</button>&nbsp;&nbsp;&nbsp;&nbsp;
  			<button type="button" onclick="pauseTask();" id="pauser">暂停</button>&nbsp;&nbsp;&nbsp;&nbsp;
  			<button type="button" onclick="restartTask();" id="restart">重启</button>&nbsp;&nbsp;&nbsp;&nbsp;
  			<button type="button" onclick="cancelTask();" id="cancel">取消</button>&nbsp;&nbsp;&nbsp;&nbsp;
  		    <button type="button" onclick="resetTask();" id="reset">重置任务</button>&nbsp;&nbsp;&nbsp;&nbsp;
  			<button type="button" onclick="deleteTask();" id="delete">删除</button>&nbsp;&nbsp;&nbsp;&nbsp;
  			
	</div>

<script type="text/javascript">

function searchs(){
    //load 加载数据分页从第一页开始, reload 从当前页开始
	$('#table').datagrid('load',getForms("searchForm"));
}


//pauser,restart,cancel,reset,del(0--disable;1-able)
function checkSelect(id,states){
	
	if(states==3){//暂停----执行、重启
		$("#reset").attr("disabled","disabled");
		$("#pauser").attr("disabled","disabled");
		
		$("#delete").attr("disabled","disabled");
		$("#restart").attr("disabled",false);
	}
	if(states==1){//执行中
		$("#restart").attr("disabled","disabled");
		$("#reset").attr("disabled","disabled");
		$("#delete").attr("disabled","disabled");
		
		$("#pauser").attr("disabled",false);
		$("#cancel").attr("disabled",false);
	}
	if(states==2){//结束
		$("#pauser").attr("disabled","disabled");
		$("#restart").attr("disabled","disabled");
		$("#cancel").attr("disabled","disabled");
		$("#reset").attr("disabled","disabled");
		$("#delete").attr("disabled",false);
	}
	if(states==4){//取消
		$("#pauser").attr("disabled","disabled");
		$("#restart").attr("disabled","disabled");
		$("#cancel").attr("disabled","disabled");
		$("#reset").attr("disabled","disabled");
		$("#delete").attr("disabled",false);
	}
	if(states==5){//创建中
		$("#pauser").attr("disabled","disabled");
		$("#restart").attr("disabled","disabled");
		$("#reset").attr("disabled","disabled");
		$("#delete").attr("disabled","disabled");
		$("#cancel").attr("disabled",false);
		
	}
	if(states==6){//失败
		$("#pauser").attr("disabled","disabled");
		$("#cancel").attr("disabled","disabled");
		$("#restart").attr("disabled","disabled");
		$("#reset").attr("disabled",false);
		$("#delete").attr("disabled",false);
		
	}
	if(states==7){//未执行
		$("#pauser").attr("disabled","disabled");
		$("#restart").attr("disabled","disabled");
		$("#reset").attr("disabled","disabled");
		$("#delete").attr("disabled","disabled");
		$("#cancel").attr("disabled",false);
		
	}
}

function viewTask(){
		var checkedObjs = $('input[type=checkbox][name=taskId]:checked');
		if ($(checkedObjs).size() < 1)
		{
			alert("请选择任务！");
			return;
		}
		var checkvalue=$(checkedObjs).val();
    parent.addTab('查看任务信息','message/viewTask?taskid='+checkvalue);
         
}
function pauseTask(){
	var checkedObjs = $('input[type=checkbox][name=taskId]:checked');
	if ($(checkedObjs).size() < 1)
	{
		alert("请选择任务！");
		return;
	}
	var checkvalue=$(checkedObjs).val();
	$.ajax({
		url:'pauseTask',
		type:'post',
		data:'taskId='+checkvalue,
		success: function(data){
			$('#table').datagrid('reload');
		}
	}); 
}
function restartTask(){
	var checkedObjs = $('input[type=checkbox][name=taskId]:checked');
	if ($(checkedObjs).size() < 1)
	{
		alert("请选择任务！");
		return;
	}
	
	var checkvalue=$(checkedObjs).val();
	$.ajax({
		url:'restartTask',
		type:'post',
		data:'taskid='+checkvalue,
		success: function(data){
			$('#table').datagrid('reload');
		}
	}); 
}
function cancelTask(){
	var checkedObjs = $('input[type=checkbox][name=taskId]:checked');
	if ($(checkedObjs).size() < 1)
	{
		alert("请选择任务！");
		return;
	}
	var checkvalue=$(checkedObjs).val();
	$.ajax({
		url:'cancelTask',
		type:'post',
		data:'taskid='+checkvalue,
		success: function(data){
			$('#table').datagrid('reload');
		}
	}); 
	
}


//状态为失败的，才可以重置，继续发送失败的号码
function resetTask(){
	var checkedObjs = $('input[type=checkbox][name=taskId]:checked');
	if ($(checkedObjs).size() < 1)
	{
		alert("请选择任务！");
		return;
	}
	var checkvalue=$(checkedObjs).val();
	$.ajax({
		url:'resetTask',
		type:'post',
		data:'taskid='+checkvalue,
		success: function(data){
			$('#table').datagrid('reload');
		}
	}); 
}
//失败、已结束、已取消的才可以删除
function deleteTask(){
	var checkedObjs = $('input[type=checkbox][name=taskId]:checked');
	if ($(checkedObjs).size() < 1)
	{
		alert("请选择任务！");
		return;
	}
	var checkvalue=$(checkedObjs).val();
	$.ajax({
		url:'deleteTask',
		type:'post',
		data:'taskid='+checkvalue,
		success: function(data){
			$('#table').datagrid('reload');
		}
	}); 
}
</script>
</body>
</html>