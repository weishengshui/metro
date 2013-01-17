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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/constant.js"></script>
<script>
	function addDialog(){
		parent.addTab('注册会员','member/memberPage');
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
</script>
</head>
<body>
	<!-- 查询条件Table -->
	<form id="searchForm">
		<table style="font-size:13px;">
			<tr>
				<td>会员名:</td>
				<td><input type="text" name="name" size="18"/></td>
				<td>状态:</td>
				<td>
					<select name="status" style="width:100px;height:22px;">
						<option value=""></option>
						<c:forEach items="${status }" var="s">
							<option value="${s.key }">${s.value }</option>
						</c:forEach>
					</select>	
				</td>
				<td>&nbsp;<a id="btn" href="javascript:void(0)" onclick="searchs()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></td>
				<td>
					<a id="btn" href="javascript:void(0)" onclick="addDialog()" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加会员</a>
				</td>
			</tr>
		<table>
	</form>

	<!-- 显示列表Table -->
	<table id="table" class="easyui-datagrid" data-options="url:'findMemebers',fitColumns:true,striped:true,loadMsg:'正在载入...',pagination:true,
		rownumbers:true,pageList:pageList,singleSelect:false">
	    <thead>  
	        <tr>  
	            <th data-options="field:'name',width:30,formatter:function(v,o,i){return getName(v,o,i);}">会员名</th>  
	            <th data-options="field:'phone',width:30">电话</th>
	            <th data-options="field:'sex',width:10,formatter:function(v){return v == <%=Dictionary.MEMBER_SEX_MALE %> ? '男' : '女';}">性别</th>    
	            <th data-options="field:'email',width:50">邮箱</th>
	            <th data-options="field:'status',width:20,formatter:function(v){return getStatus(v) }">状态</th>
	            <th data-options="field:'profession',width:30,formatter:function(v){return getProfession(v)}">职业</th>
	            <th data-options="field:'salary',width:30,formatter:function(v){return getSalary(v)}">年薪</th>
	            <th data-options="field:'address',width:80,formatter:function(v,o,i){return getAddress(v,o,i)}">地址</th>
	            <th data-options="field:'createDate',width:50">注册时间</th>
	        </tr>  
	    </thead>  
	</table> 
</body>
</html>