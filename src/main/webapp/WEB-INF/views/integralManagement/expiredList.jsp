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

<script type="text/javascript">
	
	function doSearch(){  
	    $('#tt').datagrid('load',{  
	    	opt:$('#opt').val(),  
	    	from:$('#from').val,
	    	to:$('#to').val()
	    });  
	}
</script>
</head>
	<body>
		<table border="0">
			<tr>
				<td>
					<fieldset style="font-size: 14px;width:auto;height:auto;">
						<legend style="color: blue;">查询条件</legend>
						<form action="" >
							<table border="0">
								<tr>
									<td width="140px">操作员：</td>
									<td width="200px" align="left">
										<input id="opt" name="opt" type="text" style="width:150px"/> 
									</td>
								</tr>
								<tr>
									<td width="140px">时间段：</td>
									<td width="200px" align="left">
										<input id="from" name="from" type="text" style="width:150px" class="easyui-datebox" editable="false"/>
									</td>
									<td width="80px">至</td>
									<td width="200px" align="left">
										<input id="to" name="to" type="text" style="width:150px" class="easyui-datebox" editable="false"/>
									</td>
									<td>
										<button type="button" onclick="doSearch()">查询</button>
									</td>
								</tr>
							</table>
						</form>
					</fieldset>
				</td>
			</tr>
			<tr>
				<td>
					<fieldset style="font-size: 14px;width:auto;height:auto;">
						<legend style="color: blue;">查询结果</legend>
						<table id="tt" class="easyui-datagrid" width="100%" height="100%"  
	           				url="expired_list" rownumbers="true" pagination="true">  
	       					<thead>  
	           				<tr>  
		                		<th data-options="field:'transactionNo'">批次号</th>
		                		<th data-options="field:'operator'">操作员</th>
		                		<th data-options="field:'transactionDate',formatter:function(v,r,i){return dateFormat(v);}">操作时间</th>
		                		<th data-options="field:'countMembers'">会员人数</th>
		                		<th data-options="field:'amountPoints'">积分总数</th>
		                		<th data-options="field:'transactionStatus'" >状态</th>
		                		<th data-options="field:'price'">操作</th>
				           	</tr>  
					       	</thead>  
				   		</table> 
					</fieldset>
				</td>
			</tr>
		</table>
	</body>
</html>