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
<script type="text/javascript">
	$(document).ready(function() {
		$('#table').datagrid({
			url:'attendBrands',
			queryParams:{
				id: $('#id_').val(),
			}	
		});
	}); 
</script>
</head>
<body style="padding:20px;">
	<input type="hidden" name="id" id="id_" value="${id }">
	<br/>
	<fieldset style="width:605px;text-align:center;">
		<table id="table" class="easyui-datagrid" style="width:600px;" data-options="url:'',fitColumns:true,striped:true,loadMsg:'正在载入...',pagination:true,
			rownumbers:true,pageList:pageList,singleSelect:true,">
			<thead>  
		        <tr>  
		            <th data-options="field:'brandName',width:80">品牌名称</th>  
		            <th data-options="field:'joinedDate',width:80,formatter:function(v,r,i){return dateFormat(v);}">加入时间</th>
		        </tr>  
		    </thead>  
		</table> 
			
	</fieldset>
</body>
</html>