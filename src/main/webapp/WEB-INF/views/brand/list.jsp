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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/extend-easyui-validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/PCASClass.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/constant.js"></script>
<style type="text/css">
	fieldset table tr{height: 35px;}
	fieldset{margin-bottom:10px;margin-left:30px;margin-top:10px;}
	select{width:155px;height:20px;}
	.red{color:red;font-size:12px;}
</style>

<script type="text/javascript">
	
	
	function formatterdate(val, row) {
        var date = new Date(val);
        return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
	}

	function doSearch(){  
		
	    $('#tt').datagrid('load',{  
	    	name:$('#name').val(),  
	    	companyName:$('#companyName').val(),
	    	createStart:$('#createStart').datebox('getValue'),
	    	createEnd:$('#createEnd').datebox('getValue'),
	    	unionInvited:$('#unionInvited').val()
	    });  
	}
	function deleteBrands(){
		var rows = $('#tt').datagrid('getChecked');
		if(rows){
			if(rows.length == 0){
				alert("请先选择要删除的品牌");
				return;
			}
			var data = '';
			for(var i=0,length=rows.length; i < length; i++){
				var row = rows[i];
				data += 'ids='+row.id+'&';
			}	
			data = data.substring(0, data.length -1);
			
			$.ajax({
				url:'delete',
				type:'post',
				data:data,
				success: function(data){
					alert(data.msg);
					$('#tt').datagrid('reload');
				}
			}); 
		}else{
			alert("请先选择要删除的品牌");			
		}
	}
	function update(v, r, i){
		return '<a href="javascript:void(0)" onclick="edit(\''+r.id+'\',\''+r.name+'\')">修改</a>';
	}
	function edit(id,name){
		//parent.addTab('修改'+name+'信息','brand/edit?id='+id);
		parent.addTab('品牌修改','brand/edit?id='+id);
	}
</script>

</head>
	<body>
		<table border="0">
			<tr>
				<td>
					<fieldset style="font-size: 14px;width:auto;height:auto;">
						<legend style="color: blue;">查询条件</legend>
						<form action="" >
							<table border="0">
								<tr>
									<td width="140px">品牌名称：</td>
									<td width="200px" align="left">
										<input id="name" name="name" type="text" style="width:150px"/> 
									</td>
									<td width="120px">公司名称：</td>
									<td width="200px" align="left">
										<input id="companyName" name="companyName" type="text" style="width:150px"> 
									</td>
								</tr>
								<tr>
									<td width="140px">创建时间：</td>
									<td width="200px" align="left">
										<input id="createStart" name="createStart" type="text" style="width:150px" class="easyui-datebox" editable="false"/>
									</td>
									<td width="80px">至</td>
									<td width="200px" align="left">
										<input id="createEnd" name="createEnd" type="text" style="width:150px" class="easyui-datebox" editable="false"/>
									</td>
								</tr>
								<tr>
									<td width="140px">联合会员申请状态：</td>
									<td>
										<select id="unionInvited" name="unionInvited" style="width:150px">
											<option value="">请选择</option>
											<option value="ON">已申请</option>
											<option value="OFF">未申请</option>
										</select>
									<td>
									</td>
									<td>
										<button type="button" onclick="doSearch()">查询</button>
									</td>
								</tr>
							</table>
						</form>
					</fieldset>
				</td>
			</tr>
			<tr>
				<td>
					<fieldset style="font-size: 14px;width:auto;height:auto;">
						<legend style="color: blue;">查询结果</legend>
						<table id="tt" class="easyui-datagrid" width="100%" height="100%"  
	           				url="list" rownumbers="true" pagination="true">  
	       					<thead>  
	           				<tr>  
		           				<th field="id" checkbox="true"></th> 
		                		<th data-options="field:'name'">品牌名称</th>
		                		<th data-options="field:'companyName'">公司名称</th>
		                		<th data-options="field:'createdAt',formatter:function(v,r,i){return dateFormat(v);}" >创建时间</th>
		                		<th data-options="field:'unionInvited',formatter:function(v,r,i){if(v){return '已申请';}else{return '未申请';}}">联合会员申请</th>
		                		<th data-options="field:'5',formatter:function(v,r,i){return update(v,r,i);}">操作</th>
				           	</tr>  
					       	</thead>  
				   		</table> 
				   		<div style="text-align:right;">
				   			<br>
				   			<button type="button" onclick="deleteBrands()">删除</button>&nbsp;&nbsp;&nbsp;&nbsp;
				   		</div>
					</fieldset>
				</td>
			</tr>
		</table>
	</body>
</html>