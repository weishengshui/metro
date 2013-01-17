<%@ page language="java" contentType="text/html; charset=utf8" pageEncoding="utf8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery/themes/icon.css" />
<style type="text/css">
	fieldset table tr{height: 35px;}
	fieldset{margin-bottom:10px;margin-left:30px;margin-top:10px;}
	select{width:155px;height:20px;}
	.red{color:red;font-size:12px;}
</style>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/common.js"></script>

<script type="text/javascript">
	
	$(document).ready(function(){
		// when double click a cell, begin editing and make the editor get focus
		$('#tt').datagrid({
			onDblClickRow: function(rowIndex,rowData){
					var titile = '维护' + rowData.merchandise.name + '的信息';
					parent.addTab(titile,'<%=request.getContextPath()%>/merchandise/show?id='+ rowData.id);			
			}
		});	  
	});
	
	function formatterdate(val, row) {
        var date = new Date(val);
        return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
	}

	function doSearch(){  
		
	    $('#tt').datagrid('load',{  
	    	name:$('#name').val(),  
	    	code:$('#code').val(),
	    	model:$('#model').val(),
	    	unitId:$('#sellform').val()
	    });  
	}
	function deleteMerchandCata(){
		var rows = $('#tt').datagrid('getChecked');
		if(rows){
			if(rows.length == 0){
				alert("请先选择要删除的行");
				return;
			}
			var data = '';
			for(var i=0,length=rows.length; i < length; i++){
				var row = rows[i];
				data += 'id='+row.id+'&';
			}	
			data = data.substring(0, data.length -1);
			
			if(!confirm("确认删除？","删除商品")){
				return;
			}
			$.ajax({
				url:'delete',
				data:data,
				success: function(data){
					alert(data.msg);
					$('#tt').datagrid('reload');
				}
			}); 
		}else{
			alert("请先选择要删除的行");			
		}
	}
	function updateMerchandCata(){
		var rows = $('#tt').datagrid('getChecked');
		if(rows){
			if(rows.length == 0){
				alert("请先选择要修改的行");
				return;
			}
			if(rows.length > 1){
				alert("修改数据，一次只能选择一行");
				return;
			}
			var row = rows[0];
			var titile = '维护' + row.merchandise.name + '的信息';
			parent.addTab(titile,'merchandise/show?id='+ row.id);
			
		}else{
			alert("请先选择要修改的行");			
		}
	}
</script>

</head>
	<body>
		<table border="0">
			<tr>
				<td>
					<fieldset style="font-size: 14px;width:auto;height:auto;">
						<legend style="color: blue;">查询条件</legend>
						<form action="#" >
							<table border="0">
								<tr>
									<td width="80px">商品编号：</td>
									<td width="200px" align="left">
										<input id="code" name="code" type="text" style="width:150px"/> 
									</td>
									<td width="80px">商品名称：</td>
									<td width="200px" align="left">
										<input id="name" name="name" type="text" style="width:150px"> 
									</td>
								</tr>
								<tr>
									<td width="80px">售卖形式：</td>
									<td width="200px" align="left">
										<select id="sellform" name="sellform" style="width:150px">
											<option value=""></option>
											<option value="0">正常售卖</option>
											<option value="1">积分兑换</option>
										</select>
									</td>
									<td width="80px">型号：</td>
									<td width="200px" align="left">
										<input id="model" name="model" type="text" style="width:150px"> 
									</td>
								</tr>
								<tr>
									<td></td>
									<td></td>
									<td>
										<button type="button" onclick="doSearch()">查询</button>
									</td>
									<td>
										<button type="reset">重置</button>
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
						<legend style="color: blue;">商品列表</legend>
						<table id="tt" class="easyui-datagrid" width="100%" height="100%"  
	           				url="list" rownumbers="true" pagination="true">  
	       					<thead>  
	           				<tr>  
	           				<th field="id" checkbox="true"></th> 
	                		<th data-options="field:'1',formatter:function(v,o,i){return o.merchandise.code}">商品编号</th>
	                		<th data-options="field:'2',formatter:function(v,o,i){return o.merchandise.name}" >商品名称</th>
	                		<th data-options="field:'3',formatter:function(v,o,i){return o.merchandise.model}" >型号</th>
	                		<th data-options="field:'unitId',formatter:function(v,o,i){if(v=='0'){return '正常售卖';}else{return '积分兑换';}}">售卖形式</th>
	                		<th field="price">售价</th>
	                		<th data-options="field:'5',formatter:function(v,o,i){return o.merchandise.purchasePrice}">采购价</th>
<!-- 	                		<th data-options="field:'5',formatter:function(v,o,i){return o.merchandise.purchasePrice}">操作</th> -->
	                		  
<!-- 	                		<th data-options="field:'catagory',formatter:function(v,r,i){if(v!=null){return v.name;}}" >商品类别</th>   -->
<!-- 	                		<th field="code" align="right">商品代码</th>   -->
<!-- 	                		<th field="description" align="right">商品描述</th> -->
<!-- 	                		<th field="model" align="right">商品型号</th> -->
<!-- 	                		<th field="purchasePrice" align="right">采购价</th> -->
<!-- 			                <th field="createdBy" >创建人</th>   -->
<!-- 			                <th field="createdAt" formatter="formatterdate" align="center">创建时间</th>   -->
				           	</tr>  
					       	</thead>  
				   		</table> 
				   		<div style="text-align:right;">
				   			<br>
				   			<button type="button" onclick="updateMerchandCata()">修改</button>&nbsp;&nbsp;&nbsp;&nbsp;<button type="button" onclick="deleteMerchandCata()">删除</button>&nbsp;&nbsp;&nbsp;&nbsp;
				   		</div>
<!-- 					   	 <div id="tb" style="padding:5px;height: 60px;">   -->
<!-- 				    		<span>商品名称:</span>   -->
<!-- 				    		<input id="name" style="line-height:26px;border:1px solid #ccc">   -->
<!-- 				    		<span>商品代码:</span>   -->
<!-- 				    		<input id="code" style="line-height:26px;border:1px solid #ccc">  <br /> -->
<!-- 				    		<span>商品类别:</span>   -->
<!-- 				    		<input id="catagoryName" style="line-height:26px;border:1px solid #ccc"> -->
<!-- 				    		<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch()">Search</a> -->
<!-- 						</div> -->
					</fieldset>
				</td>
			</tr>
		</table>
		
		 		
	    
	    
	</body>
</html>