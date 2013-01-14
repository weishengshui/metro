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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<style type="text/css">
#searchTable input{width:100px;}
</style>
<script>
	function save(){
		$('#myform').form('submit', {
		    url:'saveShopSite',
		    dataType:'json',
		    onSubmit: function(){
			    if(parent.getId()==0){
				    alert("请先保存门店");
				    return false;
				}else{
					$("#shopId").val(parent.getId());
				}
		    	return $('#myform').form('validate');
			},
		    success:function(id){
			    if($("#id").val()=='')$("#id").val(id);
		    	alert('保存成功');
		    }
		});
    }
	function addDialog(){
		$("#site").dialog({
			height:320,
			width:500,
			modal:true,
			resizable:true,
			title:"选择站台"
		});
	}
	function selectSite(){
		var row = $('#table').datagrid('getSelected');//getSelected选一个
		$("#siteName").val(row.name);
		$("#siteId").val(row.id);
		$("#site").dialog("close");
	}
</script>
</head>
<body>
	<form id="myform" style="margin: 20px;font-size:13px;">
	    <input type="hidden" name="shopId" id="shopId"/>
	    <input type="hidden" name="siteId" id="siteId" value="${site.id }"/>
	    <table>
	    	<tr>
	    		<td>站台名称：<input type="text" name="siteName" id="siteName" value="${site.name }" readonly="readonly" class="easyui-validatebox" data-options="required:true"/></td>
	    		<td>
	    			<input type="button" value="选择" onclick="addDialog()" />
	    		</td>
	    	</tr>
	    	<tr>
	    		<td>排序编号：<input type="text" name="orderNo" value="${orderNo }" class="easyui-numberbox" data-options="required:true"/></td>
	    		<td></td>
	    	</tr>
	    	<tr align="right" height="60">
	    		<td><a id="btn" href="javascript:void(0)" onclick="save()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a></td>
	    	</tr>
	    </table>
	</form>  
	<div style="display: none;">
		<div id="site">
			<div style="margin-top:5px;margin-bottom: 5px;">
			&nbsp;站台名：<input type="text" />
			<a id="btn" href="javascript:void(0)" onclick="" class="easyui-linkbutton">搜索</a>
			<a id="btn" href="javascript:void(0)" onclick="selectSite()" class="easyui-linkbutton">确定</a>
			</div>
			<table id="table" class="easyui-datagrid" data-options="url:'findSites',fitColumns:true,striped:true,loadMsg:'正在载入...',pagination:true,
				rownumbers:true,pageList:pageList,singleSelect:true,height:250" >
			    <thead>
			        <tr>
			        	<th field="ck" checkbox="true"></th>  
			        	<th data-options="field:'id',hidden:true"></th>
			            <th data-options="field:'name',width:30">站台名</th>
			        </tr>  
			    </thead>  
			</table> 
		</div>
	</div>
</body>
</html>