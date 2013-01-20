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
	
	function formatterdate(val, row) {
        var date = new Date(val);
        var m = (date.getMonth()+1) < 10 ? "0"+(date.getMonth()+1):(date.getMonth()+1);
        var d = date.getDate() < 10 ? "0"+date.getDate() : date.getDate();
        return date.getFullYear() + '-' + m + '-' + d;
	}

	function doSearch(){  
		
	    $('#tt').datagrid('load',{  
	    	name:$('#name').val(),  
	    	code:$('#code').val(),
	    	model:$('#model').val(),
	    	unitId:$('#sellform').val()
	    });  
	}
	function deleteMerchandises(){
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
			
			if(!confirm("确认删除？")){
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
	function updateMerchandise(){
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
			var titile = '维护' + row.name + '的信息';
			parent.addTab(titile,'merchandise/show?id='+ row.id);
			
		}else{
			alert("请先选择要修改的行");			
		}
	}
	function formatSaleform(v, r, i){
		var saleFormVos = r.saleFormVos;
		var str = "";
		if(saleFormVos && saleFormVos.length){
			for(var i=0, length = saleFormVos.length; i< length; i++){
				var saleFormVo = saleFormVos[i];
				if(saleFormVo.unitId == '0'){
					str += "正常售卖/";
				}else{
					str += "积分兑换/";
				}
			}
		}
		if(str.length > 0){
			str = str.substring(0, str.length -1);
		}
		return str;
	}
	function formatPrice(v, r, i){
		var saleFormVos = r.saleFormVos;
		var str = "";
		if(saleFormVos && saleFormVos.length){
			for(var i=0, length = saleFormVos.length; i< length; i++){
				var saleFormVo = saleFormVos[i];
				str += saleFormVo.price + "/";
			}
		}
		if(str.length > 0){
			str = str.substring(0, str.length -1);
		}
		return str;
	}
	function formatPreferentialPrice(v, r, i){
		var saleFormVos = r.saleFormVos;
		var str = "";
		if(saleFormVos && saleFormVos.length){
			for(var i=0, length = saleFormVos.length; i< length; i++){
				var saleFormVo = saleFormVos[i];
				var preferentialPrice = saleFormVo.preferentialPrice;
				if(preferentialPrice){
					str += preferentialPrice + "/";	
				}else{
					str += "无/";
				}
				
			}
		}
		if(str.length > 0){
			str = str.substring(0, str.length -1);
		}
		return str;
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
											<option value="">请选择</option>
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
	                		<th data-options="field:'code'">商品编号</th>
	                		<th data-options="field:'brandName'" >品牌</th>
	                		<th data-options="field:'name'" >商品名称</th>
	                		<th data-options="field:'model'" >型号</th>
	                		<th data-options="field:'a',formatter:function(v,r,i){return formatSaleform(v, r, i);}">售卖形式</th>
	                		<th data-options="field:'b',formatter:function(v,r,i){return formatPrice(v, r, i);}">售价</th>
	                		<th data-options="field:'c',formatter:function(v,r,i){return formatPreferentialPrice(v, r, i);}">优惠价</th>
	                		<th data-options="field:'purchasePrice'">采购价</th>
				           	</tr>  
					       	</thead>  
				   		</table> 
				   		<div style="text-align:right;">
				   			<br>
				   			<button type="button" onclick="updateMerchandise()">修改</button>&nbsp;&nbsp;&nbsp;&nbsp;<button type="button" onclick="deleteMerchandises()">删除</button>&nbsp;&nbsp;&nbsp;&nbsp;
				   		</div>
					</fieldset>
				</td>
			</tr>
		</table>
		
		 		
	    
	    
	</body>
</html>