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
	function addDialog(){
		//parent.dialog("积分规则","integralRule/ruleCreatePage",550,380);
		parent.addTab('积分规则','integralRule/ruleCreatePage');
	}
	function submitIntegral(){
		$('#IntegralForm').form('submit', {
		    url:'createIntegalContract',
		    dataType:'json',
		    onSubmit: function(){
			    if($("#organizationId").val()==null){ $.messager.alert('提示','请选择会员系统!','warning');return false;}
			    if($("#money").val()==null){ $.messager.alert('提示','请选择消费金额!','warning');return false;}
			    if($("#shape").val()==2 && $("#bfbvalue").val()){$.messager.alert('提示','线性值不能大于100% !','warning');return false;}
			    if($("#startTime").val()=='');
			    return false;
			},
		    success:function(data){
		    	$('.easyui-datagrid').datagrid('reload');
		    	$("#IntegralForm")[0].reset();
				$("#IntegralAdd").dialog('close');
		    }  
		});
    }
    function searchs(){
    	$('#Integral_table').datagrid('load',getForms("searchForm"));
    }

	function del(){
		var rows = $('#Integral_table').datagrid('getSelections');//getSelected选一个
		var ids = '';
		for(var i=0; i<rows.length; i++){
			if(ids != '') ids += ",";
		    ids += rows[i].id;
		}
		if(ids == ''){
			alert('请选择要删除的记录');return false;
		}
		if(window.confirm("确定要删除选中的记录吗 ?")){
			$.ajax({
	        	url:'removeRule',
	        	type:'post',
	        	dataType:'json',
	        	data:"ids="+ids,
	        	success:function(data){
	        		$('.easyui-datagrid').datagrid('reload');
	        	}
			});
		}  
	}
	
    function edits(v,o,i){
		return '<a href="javascript:void(0)" onclick="edit(\''+v+'\',\''+o.ruleName+'\')">修改</a>';
    }
    function edit(id,ruleName){
    	parent.addTab("修改"+ruleName+"规则","integralRule/findRuleById?id="+id);
    }
    function getSex(v){
        var sex = ${sexJson};
        for(var i=0;i<sex.length;i++){
            if(v == sex[i].key){
            	return sex[i].value;
            };
        };
    }
</script>
</script>
</head>
<body>
	<!-- 查询条件Table -->
	<form id="searchForm">
		<table id="searchTable" style="font-size:13px;">
			<tr>
				<td>规则名称:</td>
				<td><input type="text" name="ruleName" /></td>
				<td>积分倍数:</td>
				<td><input type="text" name="times" /></td>
				<td>时间段:</td>
				<td>
					<input name="rangeFrom" type="text" class="easyui-datebox" />
					至
					<input name="rangeTo" type="text" class="easyui-datebox" />
				</td>
				<td>&nbsp;<a id="btn" href="javascript:void(0)" onclick="searchs()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></td>
				<td>
					<a id="btn" href="javascript:void(0)" onclick="addDialog()" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>
				</td>
				<td>
					<a id="btn" href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>
				</td>
			</tr>
		<table>
	</form>

	<!-- 显示列表Table -->
	<table id="Integral_table" class="easyui-datagrid" data-options="url:'findRules',fitColumns:true,striped:true,loadMsg:'正在载入...',pagination:true,
		rownumbers:true,pageList:pageList,singleSelect:false,onDblClickRow:function(rowIndex,rowData){edit(rowData.id,rowData.ruleName);}">  
	    <thead>
	        <tr>
	        	<th field="ck" checkbox="true"></th>   
	            <th data-options="field:'ruleName',width:80">规则名称</th>
	            <th data-options="field:'times',width:100">积分倍数</th>
	            <th data-options="field:'rangeFrom',width:100">开始时间</th>
	            <th data-options="field:'rangeTo',width:100">结束时间</th>
	         	<th data-options="field:'rangeAgeFrom',width:100">年龄起</th>
	         	<th data-options="field:'rangeAgeTo',width:100">年龄止</th>
	         	<th data-options="field:'amountConsumedFrom',width:100">消费金额起</th>
	         	<th data-options="field:'amountConsumedTo',width:100">消费金额止</th>
	         	<th data-options="field:'gender',width:50,formatter:function(v){return getSex(v)}">性别</th>
	         	<th data-options="field:'id',width:30,formatter:function(v,o,i){return edits(v,o,i)}">操作</th>
	        </tr>
	    </thead>  
	</table> 
</body>
</html>