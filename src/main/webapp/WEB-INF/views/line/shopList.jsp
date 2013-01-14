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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/PCASClass.js"></script>
<style>
	.table select{width:140px;height:22px;margin-right:20px;}
</style>
<script>
	function operates(v,o,i){
		return '<a href="javascript:void(0)" onclick="edit(\''+o.id+'\',\''+o.name+'\')">修改</a>&nbsp;'+
				'<a href="javascript:void(0)" onclick="del(\''+o.id+'\')">删除</a>&nbsp;';
	}

	function edit(id,name){
		parent.addTab("修改"+name,"line/updateShopPage?id="+id);
	}
	function del(id){
		if(window.confirm("确认要删除吗?")){
			$.ajax({
	        	url:'delShop',
	        	type:'post',
	        	dataType:'json',
	        	data:"shopId="+id,
	        	success:function(data){
	        		$('.easyui-datagrid').datagrid('reload');
	        	}
			});		
		}
	}
	function searchs(){
        //load 加载数据分页从第一页开始, reload 从当前页开始
    	$('#table').datagrid('load',getForms("searchForm"));
    }
    function getAddress(v,o,i){
        return (o.province == null ? '' : o.province) + 
         	   (o.city == null ? '' : o.city) + 
         	   (o.region == null ? '' : o.region ) + 
         	   (o.address == null ? '' : o.address);
    }
</script>
</script>
</head>
<body>
	<!-- 查询条件Table -->
	<form id="searchForm">
		<table class="table" id="#table" style="font-size:13px;">
			<tr>
				<td>总店编号:</td>
				<td><input type="text" name="num" style="width:100px;"/></td>
				<td>门店名称:</td>
				<td><input type="text" name="name" style="width:100px;"/></td>
				<td>联系人:</td>
				<td><input type="text" name="linkman" style="width:100px;"/></td>
				<td><a id="btn" href="javascript:void(0)" onclick="searchs()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></td>
			</tr>
			<tr>
				<td>区域:</td>
				<td colspan="8">
					<select name="province" style="width:100px;"></select>
	  				<select name="city" style="width:100px;"></select>
	  				<select name="region" style="width:100px;"></select>
				</td>
			</tr>
		<table>
	</form>

	<!-- 显示列表Table -->
	<table id="table" class="easyui-datagrid" data-options="url:'findShopList',fitColumns:true,striped:true,loadMsg:'正在载入...',pagination:true,
		rownumbers:true,pageList:pageList,singleSelect:true,onDblClickRow:function(rowIndex,rowData){edit(rowData.id,rowData.name);}">
	    <thead>  
	        <tr> 
	        	<th data-options="field:'id',hidden:true,width:30">id</th>
	            <th data-options="field:'num',width:30">总店编号</th>
	            <th data-options="field:'name',width:30">门店名称</th>
	            <th data-options="field:'region',width:30,formatter:function(v,o,i){return getAddress(v,o,i) }">区域</th>  
	            <th data-options="field:'linkman',width:30">联系人</th>
	            <th data-options="field:'workPhone',width:30">联系电话</th>
	            <th data-options="field:'siteName',width:30">所属站台</th>
	            <th data-options="field:'ids',width:10,formatter:function(v,o,i){return operates(v,o,i) }">操作</th>
	        </tr>  
	    </thead>  
	</table> 
	<script>
		new PCAS("province","city","region");
	</script>
</body>
</html>