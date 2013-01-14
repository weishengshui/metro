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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/constant.js"></script>
<script>
function getStatus(v){
  //  var status = ${statusJson};
    for(var i=0;i<v.length;i++){
        if(v == v[i].key){
        	alert(status[i].value);
        	return status[i].value;
        };
    };
}

function newSend(){
	
}
	
</script>

</head>
<body>
	<dir>
	   	<!-- 显示列表Table -->
	 <table id="table" class="easyui-datagrid" data-options="rownumbers:true,url:'listTelephone?tackid=${task.taskId}',fitColumns:true,striped:true,loadMsg:'正在载入...',pagination:true,
		rownumbers:true,pageList:pageList,singleSelect:true">
	    <thead>  
	        <tr>  
	            <th data-options="field:'telephone',width:30">电话号码</th>
	            <th data-options="field:'sendTime',width:30">发送时间</th>
	            <th data-options="field:'states',width:30,formatter:function(v){return getStatus(v)}">发送结果</th>
	            <th data-options="field:'taskId',hidden:true"></th>
	        </tr>   
	    </thead>  
	 </table> 
	 <div style="text-align:left;">
  			<br>
  			<button type="button" onclick="newSend();" id="view">重新发送</button>&nbsp;&nbsp;&nbsp;&nbsp;
  			
	</div>
	</dir>
</body>
</html>