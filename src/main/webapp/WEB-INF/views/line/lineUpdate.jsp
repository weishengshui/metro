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
			title:"选择站台"
		});
	}
	function selectSite(){
		var rows = $('#table').datagrid('getSelections');//getSelected选一个
		for(var i=0; i<rows.length; i++){
			append(rows[i].id,rows[i].name);
		}
		$("#site").dialog("close");
	}
	function submitLine(){
		if($('#lineForm').form('validate')){
			var flag = false;
			$.ajax({
	            url:'findMetroLineByNum',
	            type:'post',
	            async: false,
	            data:'numno='+$("#numno").val()+"&id="+$("#id").val(),
	            success:function(data){
	            	if(data == 1){
	                	alert('编号已经存在');
	                	flag = true;
	                }
	            }
	        });
			if(flag){return false;}
			$.ajax({
	            url:'findMetroLineByName',
	            type:'post',
	            async: false,
	            data:'name='+$("#name").val()+"&id="+$("#id").val(),
	            success:function(data){
	            	if(data == 1){
	                	alert('名称已经存在');
	                	flag = true;
	                }
	            }
	        });
			if(flag){return false;}
			accept();
			var inserted = JSON.stringify($('#dg').datagrid('getChanges', "inserted"));
			var deleted = JSON.stringify($('#dg').datagrid('getChanges', "deleted"));
			var updated = JSON.stringify($('#dg').datagrid('getChanges', "updated"));
	        var param = $("#lineForm").serialize();
	        if(inserted=='[]'){inserted='';};
	        if(deleted=='[]'){deleted='';};
	        if(updated=='[]'){updated='';};
	        $.post("updateMetroLine?"+param+"&inserted="+inserted+"&deleted="+deleted+"&updated="+updated, function(rsp) {
	           	$('#dg').datagrid('acceptChanges');
	           	alert('保存成功!');
	        }, "JSON").error(function() {
	            alert("保存错误了！");
	        });
		}
    }
	function resets(){
		deleteAll();
		$('#lineForm').form('clear');
	}
	function siteNames(v,o,i){
		if(o.site  != undefined)
			return o.site.name;
		if(v != undefined)
			return v;
	}
	function searchs(){
        //load 加载数据分页从第一页开始, reload 从当前页开始
    	$('#table').datagrid('load',getForms("searchForm"));
    }
</script>
</head>
<body style="padding:10px;">
	  <input type="hidden" id="oldnum" value="${line.numno }" />
	  <input type="hidden" id="oldname" value="${line.name }" />
	  <fieldset style="font-size:14px;">
	  	<legend style="color: blue;">基本信息</legend>
	  	<form id="lineForm" method="post">
	  	<input type="hidden" value="${line.id }" name="id" id="id"/>
	  	<table>
	  		<tr>
	  			<td><span class="red">*</span></td>
	  			<td style="width: 80px;">线路编号：</td>
	  			<td>
	  				<input id="numno" type='text' name='numno' value="${line.numno}" style="width:200px" maxlength="20" 
				class="easyui-validatebox" data-options="required:true"/>
	  			</td>
	  		</tr>
	  		<tr>
	  			<td><span class="red">*</span></td>
	  			<td>线路名称：</td>
	  			<td>
	  				<input id="name" type='text' name='name' value="${line.name }" style="width:200px" maxlength="100" 
				class="easyui-validatebox" data-options="required:true" />
	  			</td>
	  		</tr>
	  		<tr>
	  			<td></td>
	  			<td>线路描述：</td>
	  			<td>
	  				<textarea rows="3" cols="25" name="descs">${line.descs }</textarea>
	  			</td>
	  		</tr>
	  	</table>
	  	</form>
	  	<br/>
	  	<table id="dg" class="easyui-datagrid" style="width:500px;height:auto"  
		            data-options="  
		                singleSelect: true,  
		                toolbar: '#tb',
		                url: 'findMetroLineSites?lindId=${line.id}',
		                onClickCell: onClickCell,
		                fitColumns:true,
		                height:400
		            ">
        <thead>
            <tr>
            	<th data-options="field:'id',hidden:true">id</th>
                <th data-options="field:'name',width:250,align:'left',formatter:function(v,o,i){return siteNames(v,o,i) }">站台名称</th>          
                <th data-options="field:'orderNo',width:180,editor:'numberbox'">线路排序编号</th>
                <th data-options="field:'op',align:'center',width:50,styler:cellStyler">操作</th>  
            </tr>
      	  </thead>  
    	</table>
    	<div align="right" style="padding-top:8px;">
			<a id="btn" href="javascript:void(0)" onclick="submitLine()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>    	
    	</div>
	  </fieldset>
	  <br/>
	  <div id="tb" style="height:auto">  
	      <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addDialog()">添加站台</a>
	  </div>  
	
	
	<div style="display: none;">
		<div id="site">
			<div style="margin-top:5px;margin-bottom: 5px;">
			<form id="searchForm">
			&nbsp;站台名：<input type="text" name="name"/>
			<a id="btn" href="javascript:void(0)" onclick="searchs()" class="easyui-linkbutton">搜索</a>
			<a id="btn" href="javascript:void(0)" onclick="selectSite()" class="easyui-linkbutton">确定</a>
			</form>
			</div>
			<table id="table" class="easyui-datagrid" data-options="url:'findSites',fitColumns:true,striped:true,loadMsg:'正在载入...',pagination:true,
				rownumbers:true,pageList:pageList,singleSelect:false,height:250" >
			    <thead>
			        <tr>
			        	<th field="ck" checkbox="true"></th>  
			        	<th data-options="field:'id',hidden:true"></th>
			            <th data-options="field:'name',width:30">站台名</th>
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
                    var n = '';
                	if(rows[i].site  != undefined){
                		n = rows[i].site['name'];
                    }else{
                        n = rows[i]['name'];
                    }
            		if(n == name){
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
    </script>  
</body>
</html>