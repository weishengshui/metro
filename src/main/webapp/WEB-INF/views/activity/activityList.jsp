<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery/themes/icon.css" />
<style type="text/css">
	body {
		font-family: 黑体、宋体、Arial;
		font-size: 12px;
	}
</style>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/common.js"></script>

<script type="text/javascript">
	function operates(i, o){
		var v ;
		if(stateCompera(v,o,i) == '已取消' || stateCompera(v,o,i) == '已结束'){
			$('#updateAct').attr("disabled","true"); 
			$('#cancerAct').attr("disabled","true"); 
		}else if(stateCompera(v,o,i) == '未开始'||stateCompera(v,o,i) == '进行中'){
			$('#updateAct').removeAttr("disabled"); 
			$('#cancerAct').removeAttr("disabled"); 
			$('#deleteAct').removeAttr("disabled");
		}
	} 
	
	$(document).ready(function(){
		
		$('#searchbtn').click(function(){
			$('#table1').datagrid('load',getForms("searchForm"));
		});
		
		$('#updateAct').click(function(){
			var rows = $('#table1').datagrid('getChecked');
			if(rows.length <= 0 || rows.length > 1){
				alert("请先选择一条要修改的活动信息！");
				return false; 	
			}
			parent.addTab('维护活动信息','activity/queryActivity?id='+rows[0].id);
		});
		
		$('#deleteAct').click(function(){
			var data ='';
			var rows = $('#table1').datagrid('getChecked');
			
			for(var i in rows){
				data += rows[i].id+',';
			}
			data = data.substring(0, data.length -1);
			if(rows.length == 0){
				alert("请先选择要删除的活动信息！");
				return false; 	
			}
			if(!confirm("确认是否删除?")){
	    		return false; 
	    	}
			$.ajax({
	            url:'deleteActivity',
	            type:'post',
	            async: false,
	            data:'ids='+data,
	            success:function(data){
	            	$('#table1').datagrid('reload');
	            }
	        });
		});
		
		$('#cancerAct').click(function(){
			var data ='';var v;var flag = false ;
			var rows = $('#table1').datagrid('getChecked');
			for(var i in rows){
				data += rows[i].id+',';
				if(stateCompera(v,rows[i],i) == '已取消' || stateCompera(v,rows[i],i) == '已结束'){
					flag = true ;
					break ;
				}
			}
			if(flag){
				$('#updateAct').attr("disabled","true"); 
				$('#cancerAct').attr("disabled","true"); 
				return false ;
			}
			data = data.substring(0, data.length -1);
			if(rows.length == 0){
				alert("请先选择要取消的活动信息！");
				return false; 	
			}
			if(!confirm("确认是否取消活动?")){
	    		return false; 
	    	}
			$.ajax({
	            url:'cancerActivity',
	            type:'post',
	            async: false,
	            data:'ids='+data,
	            success:function(data){
	            	$('#table1').datagrid('reload');
	            }
	        });
			
		});
		
	});
	
	var pD=function(s){
		var dt=s.split(/ /);
		var d=dt[0].split(/-/);
		var t;
		if(dt[1]){
			t=dt[1].split(/:/);
			t.push(0);
			t.push(0);
		}else{
			t=[0,0,0];
		}
		return new Date(d[0],d[1]-1,d[2],t[0],t[1],t[2]);
	};
	var pS=function(d){
		var Y=d.getFullYear();
		var M=d.getMonth()+1;
		(M<10)&&(M='0'+M);
		var D=d.getDate();
		(D<10)&&(D='0'+D);
		var h=d.getHours();
		(h<10)&&(h='0'+h);
		var m=d.getMinutes();
		(m<10)&&(m='0'+m);
		var s=d.getSeconds();
		(s<10)&&(s='0'+s);
		return Y+'-'+M+'-'+D+' '+h+':'+m+':'+s;
	};
	
	function stateCompera(v,o,i){
		//alert(o.tag);
		var date = new Date();
		now = date.getFullYear() + "-";
		now = now + (date.getMonth() + 1) + "-";  //取月的时候取的是当前月-1如果想取当前月+1就可以了
		now = now + date.getDate() + " ";
		now = now + date.getHours() + ":";
		now = now + date.getMinutes() + ":";
		now = now + date.getSeconds() + "";
		var startDate = dateFormat(o.startDate) ;
		var endDate = dateFormat(o.endDate) ;
		var d1=pD(now);
		var d2=pD(startDate);
		var d3=pD(endDate);
		var now_1=pS(d1);
		var startDate_1=pS(d2);
		var endDate_1=pS(d3);
		if(o.tag == 0){
			return "已取消";
		}
		if(now_1<startDate_1){
			return "未开始";
		}else if(now_1>endDate_1){
			return "已结束";
		}else{
			return "进行中";
		}
	}
</script>
</head>
<body>
	<form id="searchForm" method="post" >
	
		<fieldset>
			<legend style="color: blue;">查询条件</legend>
			
			<table>
				<tr>
					<td>活动名称：</td>
					<td><input id="activityName" name="activityName" type="text"/></td>
					<td style="margin-left: 8px;">开始时间：</td>
					<td><input id="startDate" name="startDate" type="text" style="width: 180px;" class="easyui-datetimebox" editable="false"/></td>
					<td style="margin-left: 8px;">结束时间：</td>
					<td><input id="endDate" name="endDate" type="text" style="width: 180px;" class="easyui-datetimebox" editable="false"/></td>
					<td>
						<a id="searchbtn" href="javascript:void(0)" onclick="" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
					</td>
				</tr>
			</table>
		</fieldset>
	</form>
	
	<!-- 显示列表Table -->
	
	<table  id="table1"  title="活动详情" style="width: 1460px" class="easyui-datagrid" data-options="url:'findActivities',fitColumns:true,striped:true,loadMsg:'正在载入...',pagination:true,
				rownumbers:true,pageList:pageList,singleSelect:false,onCheck:function(rowIndex, rowData){operates(rowIndex, rowData)}">    
	    <thead>  
	        <tr>  
	        	<th field="ck" checkbox="true"></th>
	        	<th data-options="field:'id',width:10">活动编号</th>
	            <th data-options="field:'activityName',width:30">活动名称</th>  
	            <th data-options="field:'startDate',width:30,formatter:function(v){return dateFormat(v)}">开始时间</th>
	            <th data-options="field:'endDate',width:50,formatter:function(v){return dateFormat(v)}">结束时间</th>
	            <th data-options="field:'status',width:20,formatter:function(v,o,i){ return stateCompera(v,o,i)}">状态</th>
	        </tr>  
	    </thead>  
	</table> 
</div>
<br><br>
<div>
	<a id="updateAct" href="javascript:void(0)" class="easyui-linkbutton" style="margin-left: 10px;">修改</a>
	<a id="deleteAct" href="javascript:void(0)" class="easyui-linkbutton" style="margin-left: 10px;">删除</a>
	<a id="cancerAct" href="javascript:void(0)" class="easyui-linkbutton" style="margin-left: 10px;">取消</a>
</div>
</body>
</html>