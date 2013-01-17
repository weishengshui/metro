<%@ page language="java" contentType="text/html; charset=utf8"
	pageEncoding="utf8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/js/jquery/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/js/jquery/themes/icon.css" />
<style type="text/css">
fieldset table tr {
	height: 35px;
}

fieldset {
	margin-bottom: 10px;
	margin: 10px;
}

select {
	width: 155px;
	height: 20px;
}

.red {
	color: red;
	font-size: 12px;
}
</style>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery/jquery-1.8.0.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/common.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery/ajaxfileupload.js"></script>

<script type="text/javascript">
		var baseURL = '<%=request.getContextPath()%>';
	
	function saveCategory() {
		if ($('#parentName').val() == "") {
			alert("请先选择一个父节点");
			return;
		}
		if ($.trim($('#name').val()) == null
				|| $.trim($('#name').val()) == "") {
			alert("请输入类别名称");
			return;
		}
			$('#fm').form('submit',{  
		        onSubmit: function(){  
		        	return $(this).form('validate');  
		        },  
		        success: function(result){  
		        	var node = $('#tt').tree('getSelected');
		        	if(node){
		        		$('#tt').tree('reload', node.target);
		        		$('#tt').tree('expandTo', node.target);
		        	}
		        	alert(eval('(' + result + ')').msg);
		        	
		            		        	
		        }  
		    });	
	}
</script>

</head>
<body>
	<table border="0">
		<tr>
			<td>
				<fieldset style="font-size: 14px;width:400px;height:800px;">
					<legend style="color: blue;">商品类别树</legend>
						选择商品类别：
						<ul id="tt" class="easyui-tree" url="get_tree_nodes" data-options="
							url:'get_tree_nodes',
							onClick: function(node){
								$('#parentId').val(node.id);
								$('#parentName').val(node.text);
							} 
							"></ul>
				</fieldset>
			</td>
			<td>
				<fieldset style="font-size: 14px;width:300px;height:800px;">
					<legend style="color: blue;">类别信息</legend>
					<form id="fm" action="create" method="post">
					<table>
						<tr>
							<td width="20px"></td>
							<td colspan="2">请从左边商品类别树选择父节点</td>
						</tr>
						<tr>
							<td width="20px"><span style="color: red;">*</span></td>
							<td width="80px">父节点：</td>
							<td width="200px" align="left">
								<input type="hidden" name="parent.id" id="parentId"> 
								<input id="parentName" name="parentName" type="text" style="width:150px" disabled="disabled"> 
							</td>
						</tr>
						<tr>
							<td width="20px"><span style="color: red;">*</span></td>
							<td width="80px">类别名称：</td>
							<td width="200px" align="left">
								<input id="name" type='text' name='name' style="width:150px" 
												class="easyui-validatebox" data-options="required:true" maxlength="20"/>
							</td>
						</tr>
						<tr>
							<td width="20px"><span style="color: red;">*</span></td>
							<td width="80px">排序编号：</td>
							<td width="200px" align="left">
								<input id="displaySort" type='text' name='displaySort' style="width:150px"  
												class="easyui-numberbox" data-options="required:true" maxlength="20"/>
							</td>
						</tr>
						<tr>
							<td colspan="3" align="center">
								<button type="button" onclick="saveCategory()" >保存</button>
								&nbsp;&nbsp;&nbsp;&nbsp;<button type="reset">重置</button>
							</td>
						</tr>
					</table>
					</form>
				</fieldset>
			</td>
		</tr>
	</table>

</body>
</html>