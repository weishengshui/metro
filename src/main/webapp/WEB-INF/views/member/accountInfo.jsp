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
	fieldset{margin-bottom:10px;margin:0px;width:600px;font-size:14px;}
	.showTable td{width:100px;}
</style>
<script type="text/javascript">

	$(document).ready(function() {
		/* $('#dg').datagrid('load',{
			id: $('#id_').val()
		}); */
		$('#dg').datagrid({
			url:'soonExpire',
			queryParams:{
				id: $('#id_').val(),
			}	
		});
	});
	
	function formatterdate(val, row) {
        var date = new Date(val);
        return date.getFullYear() + '-' + (date.getMonth() + 1);// + '-' + date.getDate();
	}
</script>
</head>
<body style="padding:20px;">
	<input type="hidden" name="id" id="id_" value="${id }">
	<fieldset>
		<legend>积分账户</legend>
		<table class="showTable">
			<tr>
				<td>有效积分：</td>
				<td>${avalableJiFen }</td>
				<td>冻结积分：</td>
				<td>${froznJiFen }</td>
			</tr>
		</table>
	</fieldset>
	<br/>
	<fieldset>
		<legend>储值账户</legend>
		<table class="showTable">
			<tr>
				<td>余额：</td>
				<td>${rmbBalance }</td>
			</tr>
		</table>
	</fieldset>
	<br/>
	<fieldset>
		<legend>即将过期的积分</legend>
		<table id="dg" class="easyui-datagrid" data-options="url:'',fitColumns:true,striped:true,loadMsg:'正在载入...',
			rownumbers:true,singleSelect:true">
		    <thead>  
		        <tr>  
		            <th data-options="field:'months',width:30">月份</th>  
		            <th data-options="field:'units',width:30">积分数</th>
		        </tr>  
		    </thead>  
		</table> 
			
	</fieldset>
</body>
</html>