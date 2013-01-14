<%@ page language="java" contentType="text/html; charset=utf8"
	pageEncoding="utf8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.json-2.4.js"></script>
<style type="text/css">
	fieldset table tr{height: 35px;}
	fieldset{margin-bottom:10px;margin-left:30px;margin-top:10px;}
	select{width:155px;height:20px;}
	.red{color:red;font-size:12px;}
</style>
<script type="text/javascript">
		var baseURL = '<%=request.getContextPath()%>';
		
	function doSubmit(){
		
		$('#fm').form('submit',{
			success:function(result){
				alert(eval('('+result+')').msg);
			}
		}); 
	}
	
</script>

</head>
<body>
	
	<form action="create" method="post" id="fm" enctype="multipart/form-data"> 
		<table border="0">
			<tr>
				<td>
					<fieldset style="font-size: 14px;width:auto;height:auto;">
						<legend style="color: blue;">积分信息</legend>
							<table border="0">
								<tr>
									<td width="80px">积分名称：</td>
									<td width="200px" align="left">
										<input type="hidden" name="unitId" id="_id" value="${unit.unitId }">
										<input id="displayName" name="displayName" value="${unit.displayName }" type="text" style="width:150px" class="easyui-validatebox" data-options="required:true" /> 
									</td>
									<td></td>
								</tr>
								<tr>
									<td width="80px">有效期：</td>
									<td width="200px" align="left">
										<input id="available" name="available" value="${unit.available }" type="text" style="width:150px" class="easyui-numberbox" data-options="precision:0,required:true">&nbsp;&nbsp;月 
									</td>
									<td></td>
								</tr>
								<tr>
									<td width="80px">1积分&nbsp;=&nbsp;</td>
									<td width="200px" align="left">
										<input id="price" name="price" value="${unit.price }" type="text" style="width:150px" class="easyui-numberbox" data-options="precision:2,required:true">&nbsp;&nbsp;元 
									</td>
								</tr>
								<tr>
									<td colspan="2" align="center">
										<a id="btn" href="javascript:void(0)" onclick="doSubmit()" class="easyui-linkbutton" data-options="iconCls:'icon-add'">保存</a>
									</td>
								</tr>
							</table>
					</fieldset>
				</td>
			</tr>
			
		</table>
	</form>
</body>
</html>