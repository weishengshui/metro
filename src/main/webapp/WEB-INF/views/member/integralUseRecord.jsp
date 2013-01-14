<%@page import="com.chinarewards.metro.core.common.Dictionary"%>
<%@page import="com.chinarewards.metro.domain.account.Business" %>
<%@page import="com.chinarewards.metro.domain.account.TxStatus" %>
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
	/* $(document).ready(function() {
		$('#table').datagrid('load',{
			id: $('#id_').val(),
			businiessNo: $('#businiessNo').val(),
			transactionDateStart: $('#transactionDateStart').datebox('getValue'),
			transactionDateEnd: $('#transactionDateEnd').datebox('getValue')
		});
	}); */
	function searchs(){
		$('#table').datagrid({
			url:'integralUseRecord',
			queryParams:{
				id: $('#id_').val(),
				businiessNo: $('#businiessNo').val(),
				transactionDateStart: $('#transactionDateStart').datebox('getValue'),
				transactionDateEnd: $('#transactionDateEnd').datebox('getValue')
			}	
		});
		/* $('#table').datagrid('load',{
			id: $('#id_').val(),
			businiessNo: $('#businiessNo').val(),
			transactionDateStart: $('#transactionDateStart').datebox('getValue'),
			transactionDateEnd: $('#transactionDateEnd').datebox('getValue')
		}); */
	}
	function businessFormat(v){
		if(v == '<%=Business.POS_REDEMPTION.toString()%>'){
			return "POS机端使用积分";
		}
	}
	function statusFormat(v){
		if(v == '<%=TxStatus.COMPLETED.toString()%>'){
			return "完成";
		}else if(v == '<%=TxStatus.RETURNED.toString()%>'){
			return "撤销";
		}
	}
</script>
</head>
<body style="padding:20px;">
	<input type="hidden" name="id" id="id_" value="${id }">
	<fieldset>
		<legend>查询条件</legend>
		<table class="showTable">
			<tr>
				<td>交易编号</td>
				<td><input type="text" name="businiessNo" id="businiessNo"  /></td>
				<td>时间段</td>
				<td style="width: 150px;"><input type="text" name="transactionDateStart" id="transactionDateStart" style="width:120px" class="easyui-datebox" editable="false" /></td>
				<td align="center">至</td>
				<td style="width: 150px;"><input type="text" name="transactionDateEnd" id="transactionDateEnd" style="width:120px" class="easyui-datebox" editable="false"  /></td>
				<td><a id="btn" href="javascript:void(0)" onclick="searchs()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></td>
			</tr>
		</table>
	</fieldset>
	<br/>
	<fieldset>
		<legend>使用积分记录</legend>
		<table id="table" class="easyui-datagrid" data-options="url:'',fitColumns:true,striped:true,loadMsg:'正在载入...',pagination:true,
			rownumbers:true,pageList:pageList,singleSelect:true,">
			<thead>  
		        <tr>  
		            <th data-options="field:'orderNo',width:40">交易编号</th>  
		            <th data-options="field:'orderTime',width:40,formatter:function(v,r,i){if(r.orderTime){return dateFormat(v);}}">交易时间</th>
		            <th data-options="field:'a',width:40,formatter:function(v,r,i){if(r.merchandise){return r.merchandise.name;}}">礼品名称</th>
		            <th data-options="field:'redemptionQuantity',width:40">数量</th>
		            <th data-options="field:'beforeUnits',width:40">交易前积分</th>
		            <th data-options="field:'usingCode',width:40">使用积分</th>
		            <th data-options="field:'orderSource',width:40">交易来源</th>
		            <th data-options="field:'b',width:40,formatter:function(v,r,i){if(r.tx){return statusFormat(r.tx.status);}}">状态</th>
		        </tr>  
		    </thead>  
		</table> 
			
	</fieldset>
</body>
</html>