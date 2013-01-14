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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/extend-easyui-validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/PCASClass.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/json2.js"></script>
<style type="text/css">
	fieldset{margin-bottom:10px;margin:0px;width:500px;}
	.red{color:red;font-size:12px;}
	textarea{font-size:13px;}
</style>
<script type="text/javascript">
	function addDialog(){
		$("#site").dialog({
			height:320,
			width:500,
			modal:true,
			resizable:true,
			title:"选择线路"
		});
	}
	function addStoreDialog(){
		$("#store").dialog({
			height:350,
			width:600,
			modal:true,
			resizable:true,
			title:"选择门店"
		});
	}
	function selectSite(){
		var rows = $('#table').datagrid('getSelections');
		for(var i=0; i<rows.length; i++){
			append(rows[i].id,rows[i].name);
		}
		$("#site").dialog("close");
	}
	
	function selectShop(){
		var rows = $('#shopTable').datagrid('getSelections');//getSelected选一个
		for(var i=0; i<rows.length; i++){
			append1(rows[i].id,rows[i].name);
		}
		$("#store").dialog("close");
	}
	function submitLine(){
		if($('#siteForm').form('validate')){
			accept();
			accept1();

			var lineinserted = JSON.stringify($('#dg').datagrid('getChanges', "inserted"));
			var linedeleted = JSON.stringify($('#dg').datagrid('getChanges', "deleted"));
			var lineupdated = JSON.stringify($('#dg').datagrid('getChanges', "updated"));
			
			var shopinserted = JSON.stringify($('#dg1').datagrid('getChanges', "inserted"));
			var shopdeleted = JSON.stringify($('#dg1').datagrid('getChanges', "deleted"));
			var shopupdated = JSON.stringify($('#dg1').datagrid('getChanges', "updated"));
			
	        var param = $("#siteForm").serialize();
	        if(lineinserted=='[]'){lineinserted = '';};
	        if(linedeleted=='[]'){linedeleted = '';};
	        if(lineupdated=='[]'){lineupdated = '';};

	        if(shopinserted=='[]'){shopinserted = '';};
	        if(shopdeleted=='[]'){shopdeleted = '';};
	        if(shopupdated=='[]'){shopupdated = '';};
	        
	        json = "&lineinserted="+lineinserted+"&linedeleted="+linedeleted+"&lineupdated="+lineupdated+
	        	   "&shopinserted="+shopinserted+"&shopdeleted="+shopdeleted+"&shopupdated="+shopupdated;
	        $.post("updateMetroSite?"+param+json, function(rsp) {
	           	$('#dg').datagrid('acceptChanges');
	           	alert('保存成功!');
	        }, "JSON").error(function() {
	            alert("保存错误了！");
	        });
		}
    }
	function getAddress(v,o,i){
        return (o.province == null ? '' : o.province) + 
         	   (o.city == null ? '' : o.city) + 
         	   (o.region == null ? '' : o.region ) + 
         	   (o.address == null ? '' : o.address);
    }
	function searchs(){
        //load 加载数据分页从第一页开始, reload 从当前页开始
    	$('#table').datagrid('load',getForms("searchForm"));
    }
	function searchss(){
    	$('#shopTable').datagrid('load',getForms("searchForms"));
    }
</script>
</head>
<body style="padding:10px;">
	  <fieldset style="font-size:14px;">
	  	<legend style="color: blue;">站台信息</legend>
	  	<form id="siteForm" method="post">
	  	<input type="hidden" value="${site.id }" name="id" />
	  	<table>
	  		<tr>
	  			<td><span class="red">*</span></td>
	  			<td>站台名称：</td>
	  			<td>
	  				<input id="name" type='text' name='name' value="${site.name}" style="width:200px" maxlength="100" 
				class="easyui-validatebox" data-options="required:true" />
	  			</td>
	  		</tr>
	  		<tr>
	  			<td></td>
	  			<td>站台描述：</td>
	  			<td>
	  				<textarea rows="3" cols="25" name="descs">${site.descs}</textarea>
	  			</td>
	  		</tr>
	  	</table>
	  	</form>
	  	
	  	<br/>
	  	<table id="dg" class="easyui-datagrid" style="width:500px;height:auto;"  
		            data-options="
		                singleSelect: true,  
		                toolbar: '#tb', 
		                url: 'findLineBySiteId?id=${site.id }',
		                onClickCell: onClickCell,
		                fitColumns:true,
		                height:220
		            ">
        <thead>
            <tr>
            	<th data-options="field:'id',hidden:true">id</th>
                <th data-options="field:'name',width:250,align:'left'">线路名称</th>          
                <th data-options="field:'orderNo',width:180,editor:'numberbox'">线路排序编号</th>
                <th data-options="field:'op',align:'center',width:50,styler:cellStyler">操作</th>  
            </tr>
      	  </thead>  
    	</table>
    	
    	<br/>
	  	<table id="dg1" class="easyui-datagrid" style="width:500px;height:auto;"  
		            data-options="  
		                singleSelect: true,  
		                toolbar: '#tb1',  
		                url: 'findShopBySiteId?id=${site.id }',
		                onClickCell: onClickCell1,
		                fitColumns:true,
		                height:220
		            ">
        <thead>
            <tr>
            	<th data-options="field:'siteId',hidden:true">id</th>
                <th data-options="field:'name',width:250,align:'left'">门店名称</th>          
                <th data-options="field:'orderNo',width:180,editor:'numberbox'">门店排序编号</th>
                <th data-options="field:'op',align:'center',width:50,styler:cellStyler">操作</th>  
            </tr>
      	  </thead>  
    	</table>
    	
    	<div align="right" style="padding-top:8px;">
			<a id="btn" href="javascript:void(0)" onclick="resets()" class="easyui-linkbutton" data-options="iconCls:'icon-redo'">重置</a>
			<a id="btn" href="javascript:void(0)" onclick="submitLine()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>    	
    	</div>
    	
	  </fieldset>
	  <br/>
	  
	  <div id="tb" style="height:auto">  
	      <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addDialog()">所属线路选择</a>
	  </div>  
	  <div id="tb1" style="height:auto">  
	      <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addStoreDialog()">所有门店选择</a>
	  </div>
	
	<div style="display: none;">
		<div id="site">
			<div style="margin-top:5px;margin-bottom: 5px;">
			<form id="searchForm">
			&nbsp;线路编号：<input type="text" style="width: 100px;" name="numno" />
			线路名称：<input type="text" style="width: 100px;" name="name"/>
			<a id="btn" href="javascript:void(0)" onclick="searchs()" class="easyui-linkbutton">搜索</a>
			<a id="btn" href="javascript:void(0)" onclick="selectSite()" class="easyui-linkbutton">确定</a>
			</form>
			</div>
			<table id="table" class="easyui-datagrid" data-options="url:'findLines',fitColumns:true,striped:true,loadMsg:'正在载入...',pagination:true,
				rownumbers:true,pageList:pageList,singleSelect:false,height:250" >
			    <thead>
			        <tr>
			        	<th field="ck" checkbox="true"></th>  
			        	<th data-options="field:'id',hidden:true"></th>
			            <th data-options="field:'numno',width:30">线路编号</th>
			            <th data-options="field:'name',width:30">线路名称</th>
			            <th data-options="field:'descs',width:30">线路描述</th>
			        </tr>  
			    </thead>
			</table>
		</div>
	</div>
	
	<div style="display: none;">
		<div id="store">
			<div style="margin-top:5px;margin-bottom: 5px;">
			<form id="searchForms">
				&nbsp;总店编号：<input type="text" style="width:100px;" name="num"/>
				总店名称：<input type="text" style="width:100px;" name="name"/>
				联系人：<input type="text" style="width:100px;" name="linkman"/>
				<div style="margin-top: 7px;">
					<select name="province"></select>
					<select name="city"></select>
					<select name="area"></select>
					<a id="btn" href="javascript:void(0)" onclick="searchss()" class="easyui-linkbutton">搜索</a>
					<a id="btn" href="javascript:void(0)" onclick="selectShop()" class="easyui-linkbutton">确定</a>
				</div>
			</form>
			</div>
			<table id="shopTable" class="easyui-datagrid" data-options="url:'findShopList',fitColumns:true,striped:true,loadMsg:'正在载入...',pagination:true,
				rownumbers:true,pageList:pageList,singleSelect:false,height:250" >
			    <thead>
			        <tr>
			        	<th field="ck" checkbox="true"></th>  
			        	<th data-options="field:'id',hidden:true"></th>
			            <th data-options="field:'num',width:30">总店编号</th>
			            <th data-options="field:'name',width:30">门店名称</th>
			            <th data-options="field:'region',width:30">区域</th>
			            <th data-options="field:'descss',width:30">联系人</th>
			            <th data-options="field:'descsss',width:30">联系电话</th>
			        </tr>
			    </thead>  
			</table> 
		</div>
	</div>
	
	<script type="text/javascript">
		var editIndex = undefined;
        function append(id,name){
            var rows = $('#dg').datagrid('getRows');
            if(rows != ''){
                for(var i=0;i<rows.length;i++){
                    if(rows[i]['name'] == name){
                        return ;break;
                    }
                }
            }
    		$('#dg').datagrid('appendRow',{id:id,name: name});
    		editIndex = $('#dg').datagrid('getRows').length-1;  
          	$('#dg').datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
          	accept(); 
        }
        function accept(){  
        	var ed = $('#dg').datagrid('getEditor', {index:editIndex,field:'orderNo'});
        	if(ed==null){return;}
            $('#dg').datagrid('getRows')[editIndex]['orderNo'] = '33';
            $('#dg').datagrid('endEdit', editIndex);
        }
        function deleteAll(){
        	var indexs = $('#dg').datagrid('getRows');
        	if(indexs != ''){
                for(var i=indexs.length-1;i>=0;i--){
            		$('#dg').datagrid('cancelEdit',i).datagrid('deleteRow', i);
                }
        	}
      	}
        function onClickCell(rowIndex, field, value){
            if(field == 'op'){
            	$('#dg').datagrid('cancelEdit', rowIndex).datagrid('deleteRow', rowIndex);
            }
            if(field == 'orderNo'){
            	$('#dg').datagrid('selectRow', rowIndex).datagrid('beginEdit', rowIndex);
            	accept();
            	editIndex = rowIndex;
            }
        }
        function cellStyler(value,row,index){  
           return 'background:url(<%=request.getContextPath()%>/js/jquery/themes/icons/cancel.png) no-repeat center;cursor:pointer';  
        } 
		<%----------             门店选择                    ------------%>
		var editIndex1 = undefined;
        function append1(id,name){
            var rows = $('#dg1').datagrid('getRows');
            if(rows != ''){
                for(var i=0;i<rows.length;i++){
                    if(rows[i]['name'] == name){
                        return ;break;
                    }
                }
            }
    		$('#dg1').datagrid('appendRow',{id:id,name: name});
    		editIndex1 = $('#dg1').datagrid('getRows').length-1;  
          	$('#dg1').datagrid('selectRow', editIndex1).datagrid('beginEdit', editIndex1);
          	accept1(); 
        }
        function accept1(){  
        	var ed = $('#dg1').datagrid('getEditor', {index:editIndex1,field:'num'});
        	if(ed==null){return;}
            $('#dg1').datagrid('getRows')[editIndex1]['num'] = '';
            $('#dg1').datagrid('endEdit', editIndex1);
        }
        function deleteAll1(){
        	var indexs = $('#dg1').datagrid('getRows');
        	if(indexs != ''){
                for(var i=indexs.length-1;i>=0;i--){
            		$('#dg1').datagrid('cancelEdit',i).datagrid('deleteRow', i);
                }
        	}
      	}
        function onClickCell1(rowIndex, field, value){
            if(field == 'op'){
            	$('#dg1').datagrid('cancelEdit', rowIndex).datagrid('deleteRow', rowIndex);
            }
            if(field == 'num'){
            	$('#dg1').datagrid('selectRow', rowIndex).datagrid('beginEdit', rowIndex);
            	accept1();
            	editIndex1 = rowIndex;
            }
        }
        function cellStyler1(value,row,index){  
           return 'background:url(<%=request.getContextPath()%>/js/jquery/themes/icons/cancel.png) no-repeat center;cursor:pointer';  
        } 
    	new PCAS("province","city","area","","","");
    </script>  
</body>
</html>