<%@ page language="java" contentType="text/html; charset=utf8" pageEncoding="utf8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
<style type="text/css">
	fieldset table tr{height: 35px;}
	fieldset{margin-bottom:10px;margin-left:30px;margin-top:10px;}
	select{width:155px;height:20px;}
	.red{color:red;font-size:12px;}
</style>

</head>
	<body>
		<table border="0">
			<tr>
				<td>
					<fieldset style="font-size: 14px;width:auto;height:auto;">
						<div>批次号</div>${transactionNo }
						<div>会员人数：${countMembers }积分总数：${amounts }</div>
						<table id="tt" class="easyui-datagrid" width="100%" height="100%"  
	           				url="expired_details" rownumbers="true" pagination="true">  
	       					<thead>  
	           				<tr>  
		                		<th data-options="field:'memberName'">会员名称</th>
		                		<th data-options="field:'memberCard',formatter:function(v,r,i){return dateFormat(v);}">卡号</th>
		                		<th data-options="field:'createdAt'" >获取时间</th>
		                		<th data-options="field:'units',formatter:function(v,r,i){return v+'个月';}">积分</th>
		                		<th data-options="field:'unitPrice'">积分价值</th>
				           	</tr>  
					       	</thead>  
				   		</table> 
					</fieldset>
				</td>
			</tr>
		</table>
	</body>
</html>