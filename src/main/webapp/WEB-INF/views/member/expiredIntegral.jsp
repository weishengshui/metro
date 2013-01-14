<%@page import="com.chinarewards.metro.core.common.Dictionary"%>
<%@ page language="java" contentType="text/html; charset=utf8" pageEncoding="utf8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"><%--"http://www.w3.org/TR/html4/loose.dtd" --%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery/themes/icon.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/extend-easyui-validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/PCASClass.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/constant.js"></script>
<style>
	fieldset{margin-bottom:10px;margin:0px;font-size:14px;}
	.showTable td{}
</style>
<script type="text/javascript">
	function searchs(){
		
		$('#table').datagrid({
			url:'expiredIntegral',
			queryParams:{
				id: $('#id_').val(),
				fromDate: $('#fromDate').datebox('getValue'),
				toDate: $('#toDate').datebox('getValue')
			}	
		});
		
	}
</script>
</head>
<body style="padding:20px;">
	<input type="hidden" name="id" id="id_" value="${id }">
	<fieldset>
		<legend>查询条件</legend>
		<table class="showTable">
			<tr>
				<td>时间段：</td>
				<td style="width: 150px;">从&nbsp;&nbsp;<input type="text" name="fromDate" id="fromDate" style="width:100px" class="easyui-datebox" editable="false" /></td>
				<td>至</td>
				<td style="width: 130px;"><input type="text" name="toDate" id="toDate" style="width:100px" class="easyui-datebox" editable="false"  /></td>
				<td><a id="btn" href="javascript:void(0)" onclick="searchs()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></td>
			</tr>
		</table>
	</fieldset>
	<br/>
	<fieldset>
		<legend>过期积分记录</legend>
		<table id="table" class="easyui-datagrid" data-options="url:'',fitColumns:true,striped:true,loadMsg:'正在载入...',pagination:true,
			rownumbers:true,pageList:pageList,singleSelect:true,">
			<thead>  
		        <tr>  
		            <th data-options="field:'expDate',width:80,formatter:function(v,r,i){return dateFormat(v);}">时间</th>
		            <th data-options="field:'units',width:80">积分数</th>
		        </tr>  
		    </thead>  
		</table> 
			
	</fieldset>
</body>
</html>