<%@page import="com.chinarewards.metro.core.common.Dictionary"%>
<%@ page language="java" contentType="text/html; charset=utf8" pageEncoding="utf8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
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
		$("#userForm")[0].reset();
		$("#userAdd").dialog({
			modal:true,
			title:'添加用户'
		});
	}
	function submituser(){
		if(!$('#userForm').form('validate')){return false;}
    	if($("#password").val()!=$("#repassword").val()){
    		$.messager.alert('提示','两次密码不一致','warning');
			return;	
		}
    	if(findUserByName($("#userName").val())){
    		$.messager.alert('提示','用户名已经存在','warning');
    		return;
		};
		$.ajax({
            url:'save',
            type:'post',
            data:$("#userForm").serialize(),
            async: false,
            success:function(data){
                alert('保存成功');
            	$('.easyui-datagrid').datagrid('reload');
		    	$("#userForm")[0].reset();
				$("#userAdd").dialog('close');
            },error:function(data){
                alert('保存失败');
            }
        });
	}
	function findUserByName(userName){
		var rs = '';
		$.ajax({
			async:false,
			url:'findUserByName',
			type :'post',
			data:'userName='+userName,
			success:function(data){
				rs = data;
			}
		});
		return rs == 'suc';
	}
	function edits(v,r,i){
		var reset =  '<a href="javascript:void(0)" onclick="editPassword(\''+r.userName+'\',\''+v+'\')">重置密码</a>&nbsp;&nbsp;';
		var lock = '<a href="javascript:void(0)" onclick="lock(\''+v+'\',\'<%=Dictionary.USER_STATE_LOCKED%>\')">锁定</a>&nbsp;&nbsp;';
		if(r.disable == '<%=Dictionary.USER_STATE_LOCKED%>'){
			lock = '<a href="javascript:void(0)" onclick="lock(\''+v+'\',\'<%=Dictionary.USER_STATE_NORMAL%>\')">启动</a>&nbsp;&nbsp;';
		}
		var addRole = '<a href="javascript:void(0)" onclick="addRole(\''+v+'\')">添加角色</a>';
		return  reset+lock+addRole;
    }
	function lock(id,disable){
		var c = '锁定';
		if(disable == 0){
			c = '启动';
		}
		$.messager.confirm('确认框','确定要'+c+'这个用户吗 ?',function(r){  
		    if (r){
		    	$.ajax({
		        	url:'lockUser',
		        	type:'post',
		        	dataType:'json',
		        	data:"id="+id+"&disable="+disable,
		        	success:function(data){
		        		$('.easyui-datagrid').datagrid('reload');
		        	}
				});
		    }  
		});
	}
	function editPassword(userName,id){
		$.messager.confirm('确认框','确定要重置该用户密码 ?',function(r){  
		    if (r){
		    	$.ajax({
		        	url:'resetPassword',
		        	type:'post',
		        	dataType:'json',
		        	data:"id="+id+"&userName="+userName+"&password=123456",
		        	success:function(data){
			        	alert('密码重置为'+data);
		        		$('.easyui-datagrid').datagrid('reload');
		        	}
				});
		    }  
		});
	}
	function searchs(){
    	$('#user_table').datagrid('load',getForms("searchForm"));
    }

	function addRole(id){
   		$("#roleUser_table").datagrid('uncheckAll');
   	   	$("#userId").val(id);
		$("#roleAdd").dialog({
			modal:true,
			title:'给用户添加角色'
		});
		checked();
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
	function saveUserRole(){
   		var rows  = $("#roleUser_table").datagrid('getSelections');
   		var roleIds = '';
        for(var i=0;i<rows.length;i++){
			if(roleIds!='') roleIds+=',';
			roleIds += rows[i].id;	
        }
        $.ajax({
            url:'saveUserRole',
            type:'post',
            data:'roleIds='+roleIds+"&userId="+$("#userId").val(),
            success:function(){
                $("#user_table").datagrid('reload');
                $('#roleAdd').dialog('close');
				alert('保存成功!');
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
				<td><input type="text" name="userName" size="18"/></td>
				<td>&nbsp;<a id="btn" href="javascript:void(0)" onclick="searchs()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></td>
				<td>
					<a id="btn" href="javascript:void(0)" onclick="addDialog()" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>
				</td>
			</tr>
		<table>
	</form>

	<!-- 显示列表Table -->
	<table id="user_table" class="easyui-datagrid" data-options="url:'findUserInfos',fitColumns:true,striped:true,loadMsg:'正在载入...',pagination:true,
		rownumbers:false,pageList:pageList,singleSelect:true">  
	    <thead>  
	        <tr>
	            <th data-options="field:'userName',width:100">用户名</th>  
	            <th data-options="field:'disable',width:100,formatter:function(v){return v == '<%=Dictionary.USER_STATE_NORMAL %>' ? '启用' : '锁定';}">启用/锁定</th>
	            <th data-options="field:'userRole',width:100">用户角色</th>
	            <th data-options="field:'id',width:40,formatter:function(v,r,i){return edits(v,r,i)}">操作</th>
	        </tr>  
	    </thead>  
	</table> 
	
	<!-- 添加窗口 -->
	<div style="display: none;">
		<div id="userAdd" style="width:300px;height:240px;padding:15px;">
			<form id="userForm" method="post">
			  <input type="hidden" name="id" id="id" />
			  <table border="0">
				<tr>
					<td width="50"><label for="name">用户名</label></td>
					<td>
						<input id="userName" type='text' name='userName' style="width:150px" value="${user.name }" maxlength="20" 
						class="easyui-validatebox" data-options="required:true"/>
					</td>
				</tr>
				<tr>
					<td>密码</td>
					<td>
						<input type='password' name='password' id="password" style="width:150px" value="${user.name }" maxlength="20" 
						class="easyui-validatebox" data-options="required:true"/>
					</td>
				</tr>
				<tr>
					<td>重复密码</td>
					<td>
						<input type='password' id="repassword" style="width:150px" value="${user.name }" maxlength="20" 
						class="easyui-validatebox" data-options="required:true"/>
					</td>
				</tr>
				<tr>
					<td></td>
					<td height="50" align="center">
						<a id="btn" href="javascript:void(0)" onclick="submituser()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>  
						<a id="btn" href="javascript:void(0)" onclick="$('#userAdd').dialog('close')" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>  
					</td>
				</tr>
			  </table>
			</form>
		</div>
	<div>
	
	<!-- 添加角色 -->
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
			        </tr>  
			    </thead>  
			</table> 
		</div>
	<div>
</body>
</html>