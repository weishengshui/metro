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
	var userOldRole = '';
	function edits(v,r,i){
		return '<a href="javascript:void(0)" onclick="addRole(\''+v+'\',\''+r.userName+'\',\''+r.desc+'\')">角色授权</a>&nbsp;&nbsp;'+
		   	   '<a href="javascript:void(0)" onclick="addAuthority(\''+v+'\',\''+r.userName+'\',\''+r.desc+'\')">用户授权</a>';
    }
	
	function searchs(){
    	$('#role_table').datagrid('load',getForms("searchForm"));
    }
    
   	function addRole(id,name,desc){
   		$("#roleUser_table").datagrid('unselectAll');
   	   	$("#userId").val(id);
		$("#roleAdd").dialog({
			modal:true,
			title:'给用户添加角色'
		});
		checked();
   	} 

   	function addAuthority(id,name,desc){
	   	parent.addTab(name+"-权限","user/userAuthority?userName="+name+"&id="+id);
   	}
   	
   	function saveUserRole(){
   		var rows  = $("#roleUser_table").datagrid('getSelections');
   		var roleIds = '';
        for(var i=0;i<rows.length;i++){
			if(roleIds!='') roleIds+=',';
			roleIds += rows[i].id;	
        }
        $.ajax({
            url:'createUserRole',
            type:'post',
            data:'roleIds='+roleIds+"&userId="+$("#userId").val(),
            success:function(){
                $("#role_table").datagrid('reload');
                $('#roleAdd').dialog('close');
				alert('保存成功!');
            }
        });
   	}
   	
   	function checked(){
   		$.ajax({
            url:'findUserRole',
            type:'post',
            data:"userId="+$("#userId").val(),
            success:function(data){
               	if(data.length > 0){
               		var rows = $("#roleUser_table").datagrid('getRows');
            	 	for(var i=0;i<rows.length;i++){
                	 	for(var n=0;n<data.length;n++){
                 	 		if(rows[i].id == data[n].roleId){
          						$("#roleUser_table").datagrid('selectRow',i);
          						break;
          					}
                        }
               	 	}
                }
            }
        });
   		
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
			</tr>
		<table>
	</form>

	<!-- 显示列表Table -->
	<table id="role_table" class="easyui-datagrid" data-options="url:'findUserRoles',fitColumns:true,striped:true,loadMsg:'正在载入...',pagination:true,
		rownumbers:false,pageList:pageList,singleSelect:true">  
	    <thead>  
	        <tr>
	            <th data-options="field:'userName',width:100">用户</th>  
	            <th data-options="field:'userRole',width:100">角色</th>
	            <th data-options="field:'a',width:100">授权方式</th>
	            <th data-options="field:'userId',width:40,formatter:function(v,r,i){return edits(v,r,i)}">操作</th>
	        </tr>  
	    </thead>  
	</table> 
	
	<!-- 添加窗口 -->
	<div style="display: none;">
		<div id="roleAdd" style="width:500px;height:320px;padding:15px;">
			<input type="hidden" id="userId" />
			<a id="btn" href="javascript:void(0)" onclick="saveUserRole()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			<a id="btn" href="javascript:void(0)" onclick="$('#roleAdd').dialog('close')" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
			<br/><br/>
			<table id="roleUser_table" class="easyui-datagrid" data-options="url:'findRoles',fitColumns:true,striped:true,loadMsg:'正在载入...',pagination:true,
				rownumbers:false,pageList:pageList,height:220">  
			    <thead>  
			        <tr>
			        	<th data-options="field:'id',checkbox:true"></th>
			            <th data-options="field:'name',width:100">角色</th>  
			            <th data-options="field:'desc',width:100">描述</th>
			            <th data-options="field:'d',width:100,formatter:function(v,r,i){return edits(v,r,i)}">优先级</th>
			        </tr>  
			    </thead>  
			</table> 
		</div>
	<div>
</body>
</html>