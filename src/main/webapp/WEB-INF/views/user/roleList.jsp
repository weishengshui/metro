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
<script>
	function addDialog(){
		$("#roleForm")[0].reset();
		$("#roleAdd").dialog({
			modal:true,
			title:'添加角色'
		});
	}
	function submitrole(){
		if(!$('#roleForm').form('validate')){return false;}
    	if(findRoleByName($("#name").val())){
    		$.messager.alert('提示','角色已经存在','warning');
    		return;
		};
		$.ajax({
            url:'saveRole',
            type:'post',
            data:$("#roleForm").serialize(),
            success:function(data){
                alert('保存成功');
                $('.easyui-datagrid').datagrid('reload');
		    	$("#roleForm")[0].reset();
				$("#roleAdd").dialog('close');
            },error:function(data){
                alert('保存失败');
            }
        });
		
	}
	function findRoleByName(roleName){
		var rs = '';
		$.ajax({
			url:'findRoleByName',
			async: false,
			type :'post',
			data:'roleName='+roleName+"&id="+$("#id").val(),
			success:function(data){
				rs = data;
			}
		});
		return rs == 'suc';
	}
	function edits(v,r,i){
		return '<a href="javascript:void(0)" onclick="edit(\''+v+'\',\''+r.name+'\',\''+r.desc+'\')">修改</a>&nbsp;&nbsp;'+
			   '<a href="javascript:void(0)" onclick="addAuthority(\''+v+'\',\''+r.name+'\',\''+r.desc+'\')">添加权限</a>';
    }
	function edit(id,name,desc){
		$("#id").val(id);
		$("#name").val(name);
		$("#desc").val(desc);
		$("#roleAdd").dialog({
			modal:true,
			title:'修改角色'
		});
	}
	function searchs(){
    	$('#role_table').datagrid('load',getForms("searchForm"));
    }
   	function addAuthority(id,name,desc){
   		parent.addTab(name+"——权限","user/roleAuthority?name="+name+"&id="+id);
   	} 
</script>
</head>
<body>
	<!-- 查询条件Table -->
	<form id="searchForm">
		<table style="font-size:13px;">
			<tr>
				<td>用户名:</td>
				<td><input type="text" name="name" size="18"/></td>
				<td>&nbsp;<a id="btn" href="javascript:void(0)" onclick="searchs()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></td>
				<td>
					<a id="btn" href="javascript:void(0)" onclick="addDialog()" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>
				</td>
			</tr>
		<table>
	</form>

	<!-- 显示列表Table -->
	<table id="role_table" class="easyui-datagrid" data-options="url:'findRoles',fitColumns:true,striped:true,loadMsg:'正在载入...',pagination:true,
		rownumbers:false,pageList:pageList,singleSelect:true">  
	    <thead>  
	        <tr>
	            <th data-options="field:'name',width:100">角色</th>  
	            <th data-options="field:'desc',width:100">描述</th>
	            <th data-options="field:'id',width:40,formatter:function(v,r,i){return edits(v,r,i)}">操作</th>
	        </tr>  
	    </thead>  
	</table> 
	
	<!-- 添加窗口 -->
	<div style="display: none;">
		<div id="roleAdd" style="width:300px;height:220px;padding:15px;">
			<form id="roleForm" method="post">
			  <input type="hidden" name="id" id="id" />
			  <table border="0">
				<tr>
					<td width="50"><label for="name">角色</label></td>
					<td>
						<input id="name" type='text' name='name' style="width:150px" maxlength="20" 
						class="easyui-validatebox" data-options="required:true"/>
					</td>
				</tr>
				<tr>
					<td>描述</td>
					<td>
						<textarea rows="3" cols="18" style="font-size:13px;" name="desc" id="desc"></textarea>
					</td>
				</tr>
				<tr>
					<td></td>
					<td height="50" align="center">
						<a id="btn" href="javascript:void(0)" onclick="submitrole()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>  
						<a id="btn" href="javascript:void(0)" onclick="$('#roleAdd').dialog('close')" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>  
					</td>
				</tr>
			  </table>
			</form>
		</div>
	<div>
</body>
</html>