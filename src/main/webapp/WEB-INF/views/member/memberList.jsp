<%@page import="com.chinarewards.metro.core.common.Dictionary"%>
<%@ page language="java" contentType="text/html; charset=utf8" pageEncoding="utf8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery/themes/icon.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/PCASClass.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/constant.js"></script>
<style>
	.table select{width:140px;height:22px;margin-right:20px;}
</style>
<script>
	function operates(v,o,i){
		return '<a href="javascript:void(0)" onclick="edit(\''+v+'\',\''+o.name+'\')">修改</a>';
	}

	function edit(id,name){
		parent.addTab('维护'+name+'信息','member/updateMemberPage?id='+id);
	}

	function searchs(){
        //load 加载数据分页从第一页开始, reload 从当前页开始
    	$('#table').datagrid('load',getForms("searchForm"));
    }
    function getStatus(v){
        var status = ${statusJson};
        for(var i=0;i<status.length;i++){
            if(v == status[i].key){
            	return status[i].value;
            };
        };
    }
    function getProfession(v){
    	for(var i=0;i<Constant.profession.length;i++){
        	if(v==Constant.profession[i].id){
				return Constant.profession[i].text;	
			};
    	};
    }
    function getSalary(v){
    	for(var i=0;i<Constant.salary.length;i++){
        	if(v==Constant.salary[i].id){
				return Constant.salary[i].text;	
			};
    	};
    }
    function getAddress(v,o,i){
    	return o.province + o.city + o.area + o.address;
    }
    function getName(v,o,i){
        return (o.surname==null?'':o.surname)+(o.name==null?'':o.name);
    }
</script>
</head>
<body>
	<!-- 查询条件Table -->
	<form id="searchForm">
		<table class="table" style="font-size:13px;">
			<tr>
				<td>省:</td>
				<td><select name="province"></select></td>
				<td>姓名:</td>
				<td><input type="text" name="name" size="18"/></td>
				<td>手机号:</td>
				<td><input type="text" name="phone" size="18"/></td>
			</tr>
			<tr>
				<td>市:</td>
				<td><select name="city"></select></td>
				<td>卡号:</td>
				<td><input type="text" name="card.cardNumber" size="18"/></td>
				<td>E-mail:</td>
				<td><input type="text" name="email" size="18"/></td>
			</tr>
			<tr>
				<td>区:</td>
				<td><select name="area"></select></td>
				<td>状态:</td>
				<td>
					<select name="status">
						<option value=""></option>
						<c:forEach items="${status }" var="s">
							<option value="${s.key }">${s.value }</option>
						</c:forEach>
					</select>	
				</td>
				<td colspan="2" align="right">
					<a id="btn" href="javascript:void(0)" onclick="searchs()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
				</td>
			</tr>
		</table>
	</form>

	<!-- 显示列表Table -->
	<table id="table" class="easyui-datagrid" data-options="url:'findMemebers',fitColumns:true,striped:true,loadMsg:'正在载入...',pagination:true,
		rownumbers:true,pageList:pageList,singleSelect:true,onDblClickRow:function(rowIndex,rowData){edit(rowData.id,rowData.name);}">
	    <thead>  
	        <tr>  
	            <th data-options="field:'name',width:30,formatter:function(v,o,i){return getName(v,o,i);}">会员名</th>  
	            <th data-options="field:'phone',width:30">手机</th>
	            <th data-options="field:'email',width:50">邮箱地址</th>
	            <th data-options="field:'card',width:50,formatter:function(v,o,i){if(v.cardNumber){return v.cardNumber;}return '';}">会员卡号</th>
	            <th data-options="field:'address',width:80,formatter:function(v,o,i){return getAddress(v,o,i)}">会员地址</th>
	            <th data-options="field:'createDate',width:50">注册时间</th>
	            <th data-options="field:'status',width:20,formatter:function(v){return getStatus(v) }">状态</th>
	         	<th data-options="field:'id',width:10,formatter:function(v,o,i){return operates(v,o,i)}">操作</th>
	        </tr>  
	    </thead>  
	</table> 
</body>
<script>
new PCAS("province","city","area","","","");
</script>
</html>