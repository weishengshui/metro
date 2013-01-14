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
		

	$(document).ready(function() {
	});

	function saveCategory() {
		if ($.trim($('#id_').val()) == null
				|| $.trim($('#id_').val()) == "") {
			alert("请先选择一个节点");
			return;
		}
			$('#fm').form('submit',{  
				url:'create',
		        onSubmit: function(){  
		        	if(!confirm("确认修改?")){
		        		return false; 
		        	}
		        	return $(this).form('validate');  
		        },  
		        success: function(result){  
		        	var oldId = $('#oldParentId').val();
		        	var newId = $('#parentId').val();
		        	
		        	var newParent = $('#tt').tree('find', newId);
		        	if(oldId != newId){
		        		var oldParent = $('#tt').tree('find', oldId);
		        		$('#tt').tree('reload', oldParent.target);
			        	$('#tt').tree('expandTo', oldParent.target);
		        	}
		        	$('#tt').tree('reload', newParent.target);
		        	$('#tt').tree('expandTo', newParent.target);
		        	
		        	alert(eval('(' + result + ')').msg);
		        }  
		    });	
	}
	function deleteCategory(){
		if ($.trim($('#id_').val()) == null
				|| $.trim($('#id_').val()) == "") {
			alert("请先选择一个节点");
			return;
		}
		if (!$('#fm').form('validate')) {
			return ;
		}
			$('#fm').form('submit',{
				url: 'delete',
		        onSubmit: function(){
		        	if(!confirm("确认删除?")){
		        		return false; 
		        	}
		        	return $(this).form('validate');  
		        },  
		        success: function(result){  
		        	var oldId = $('#oldParentId').val();
		        	
		        	var oldParent = $('#tt').tree('find', oldId);
	        		$('#tt').tree('reload', oldParent.target);
		        	$('#tt').tree('expandAll', oldParent.target);
		        	
		        	alert(eval('(' + result + ')').msg);
		        }  
		    });	
	}
	function openDialog(){
		$('#tt2').tree({
			url:'get_tree_nodes'
		});
		
		if ($.trim($('#id_').val()) == null
				|| $.trim($('#id_').val()) == "") {
			alert("修改父节点之前，请先选择一个子节点");
			return;
		}
		$('#dd').dialog('open');
	}
	//选择父节点
	function selectParent(){
		var node = $('#tt2').tree('getSelected');
		if(node){
			var id = $('#id_').val();
			if(node.id == id){
				alert("选择的父节点无效");
				return ;
			}
			var valid = true;
			var parent = $('#tt2').tree('getParent', node.target);
			while(parent){
				if(id==parent.id){
					valid = false;
					break;
				}			
				parent = $('#tt2').tree('getParent', parent.target);
			}
			if(!valid){
				alert("选择的父节点无效");
				return ;
			}
			$('#parentId').val(node.id);
			$('#parentName').val(node.text);
			$('#dd').dialog('close');
		}else{
			alert("请选择一个节点");
		}
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
								$('#displaySort').numberbox('clear');
								var root = $('#tt').tree('getRoot');
								if(root.id == node.id){
									$('#tt').tree('reload', root.target); 
								}else{
									var par = $('#tt').tree('getParent', node.target);
									$('#parentId').val(par.id);
									$('#oldParentId').val(par.id)
									$('#parentName').val(par.text);
									$('#name').val(node.text);
									$('#id_').val(node.id);
									var data =(new Function('','return '+node.attributes))();
									$('#displaySort').numberbox('setValue', data.displaySort);
									
								}
							},
							onLoadSuccess: function(node, data){
								$('#displaySort').numberbox('clear');
								if(node){
									var root = $('#tt').tree('getRoot');
									//if(root.id == node.id){ // 显示默认节点
										if(data){
											if(data.length>0){
												$('#name').val(data[0].text);
												$('#id_').val(data[0].id);
												var d =(new Function('','return '+data[0].attributes))();
												$('#displaySort').numberbox('setValue', d.displaySort);
											}else{
												$('#name').val('');
												$('#id_').val('');
												$('#displaySort').numberbox('clear');
											}
										}
									//}
								}else{
									var root = $('#tt').tree('getRoot');
									if(root){
										$('#tt').tree('reload', root.target); 
										$('#parentId').val(root.id);
										$('#oldParentId').val(root.id)
										$('#parentName').val(root.text);
									}
								}
							}"></ul>
						<input type="hidden" id="opt" name="opt"> 
						<input type="hidden" id="id" name="id">
				</fieldset>
			</td>
			<td>
				<fieldset style="font-size: 14px;width:400px;height:800px;">
					<legend style="color: blue;">类别信息</legend>
					<form id="fm" action="create" method="post">
					<table>
						<tr>
							<td width="20px"></td>
							<td colspan="2">请从左边商品类别树选择商品类别</td>
						</tr>
						<tr>
							<td width="20px"><span style="color: red;">*</span></td>
							<td width="80px">父节点：</td>
							<td width="200px" align="left">
								<input type="hidden" name="oldParentId" id="oldParentId">
								<input type="hidden" name="parent.id" id="parentId"> 
								<input id="parentName" name="parentName" type="text" style="width:150px" disabled="disabled"> 
							</td>
							<td><button type="button" onclick="openDialog()">修改</button></td>
						</tr>
						<tr>
							<td width="20px"><span style="color: red;">*</span></td>
							<td width="80px">类别名称：</td>
							<td width="200px" align="left" colspan="2">
								<input type="hidden" name='id' id='id_' />
								<input id="name" type='text' name='name' style="width:150px" 
												class="easyui-validatebox" data-options="required:true"/>
							</td>
						</tr>
						<tr>
							<td width="20px"><span style="color: red;">*</span></td>
							<td width="80px">排序编号：</td>
							<td width="200px" align="left" colspan="2">
								<input id="displaySort" type='text' name='displaySort' style="width:150px"  
												class="easyui-numberbox" data-options="required:true"/>
							</td>
						</tr>
						<tr>
							<td colspan="4" align="center">
								<button type="button" onclick="saveCategory()" >保存</button>
								&nbsp;&nbsp;&nbsp;&nbsp;<button type="button" onclick="deleteCategory()">删除</button>
							</td>
						</tr>
					</table>
					</form>
				</fieldset>
			</td>
		</tr>
	</table>
	
	<div id="dd" class="easyui-dialog" title="选择父节点" style="width:400px;height:400px;"  
        data-options="resizable:false,modal:true,closed:true">  
        <ul id="tt2" class="easyui-tree" style="margin-top:10px;margin-left:20px;"></ul>
        <div style="position: absolute;top: 350px;left:250px;text-align:right;">
        	<button type="button" onclick="selectParent()">确定</button>&nbsp;&nbsp;&nbsp;&nbsp;<button type="button" onclick="javascript:$('#dd').dialog('close');">关闭</button>
        </div>
	</div> 

</body>
</html>