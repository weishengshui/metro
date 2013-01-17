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
		if($('#myform').form('validate')){
			$.ajax({
	        	url:$("#id").val()==''?'saveBirthRule':'updateBirthRule',
	        	type:'post',
	        	dataType:'json',
	        	data:$("#myform").serialize(),
	        	success:function(data){
	        		if($("#id").val()=='')$("#id").val(id);
			    	alert('保存成功');
	        	}
			});	
		}
    }
</script>
</head>
<body>
	<form id="myform">
	<div class="easyui-panel" title="设置生日积分倍数"   
	        style="width:500px;height:230px;padding:50px;background:#fafafa;"  
	        data-options="iconCls:'icon-pencil',closable:false,  
	                collapsible:true,minimizable:false,maximizable:false">
	    <input type="hidden" value="${id}" name="id" id="id"/>  
	    <table>
	    	<tr>
	    		<td>会员生日积分倍数：<input type="text" name="times" maxlength="4" value="${times }" id="times" class="easyui-numberbox" data-options="precision:2,required:true" /></td>
	    	</tr>
	    	<tr align="right" height="60">
	    		<td><a id="btn" href="javascript:void(0)" onclick="save()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a></td>
	    	</tr>
	    </table>
	</div>
	</form>  
</body>
</html>