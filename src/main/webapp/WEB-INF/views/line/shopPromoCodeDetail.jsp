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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/extend-easyui-validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<style type="text/css">
</style>
<script type="text/javascript">
	function isReciveds(v){
		if(v==0){
			return "未使用";
		}else{
			return "已使用";
		}
	}
	function searchs(){
		$('#dg').datagrid('load',getForms("searchForm"));
	}
</script>
</head>
<body>
	<form id="searchForm" style="font-size:14px;">
	<table style="margin-bottom: 10px;">
		<tr>
			<td>优惠码：</td>
			<td><input type="text" name="num" /></td>
			<td><input type="button" value="查询" onclick="searchs()" /></td>
		</tr>
	</table>
	</form>
  	<table id="dg" class="easyui-datagrid" style="height:auto"  
	            data-options="  
	                singleSelect: false,  
	                url: 'findShopPromoCodeDetail?importDate=${importDate}',
	                height:400,
	                rownumbers:true,
	                pagination:true,
	                fitColumns:true,
	                pageList:pageList
	            ">
       <thead>
           <tr>
           	<th data-options="field:'id',hidden:true">id</th>
               <th data-options="field:'discountNum',width:300,align:'left'">优惠码</th>          
               <th data-options="field:'isRecived',width:300,formatter:function(v){return isReciveds(v) }">状态</th>
           </tr>
     	  </thead>
   	</table>
</body>
</html>