<%@ page language="java" contentType="text/html; charset=utf8" pageEncoding="utf8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/extend-easyui-validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
	<script type="text/javascript">
		function check(path,spanId){
			var filepath=path.value;
			filepath=filepath.substring(filepath.lastIndexOf('.')+ 1,filepath.length);
			filepath = filepath.toLocaleLowerCase();
			if(filepath != 'jpg' && filepath != 'gif' && filepath!='jpeg' && filepath !='bmp' && filepath!='png'){
				alert("只能上传JPG, GIF, JPEG, BMP, PNG 格式的图片");
				deleteImage(path.name, path.id, spanId);
			}
		}
		
		function previewImage(imageId){
	    	
			if($('#'+imageId).val()==''){
				alert("请先添加图片");
				return;
			}
			var width, height;
			if(imageId == 'overview'){
				width = '346px';
				height = '346px';
			}else{
				width = '246px';
				height = '246px';
			}
			var input = document.getElementById(imageId);
			var imgPre = document.getElementById('img1');
			if($.browser.msie){
				input.select();
				var url = document.selection.createRange().text;
				var imgDiv = document.createElement("div");
				imgDiv.setAttribute("id",imgPre.id);
				var parent = imgPre.parentNode;
				parent.appendChild(imgDiv);
				parent.removeChild(imgPre);
			    imgDiv.style.width = width;    
				imgDiv.style.height = height;
			    imgDiv.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod = scale)";   
			    imgDiv.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = url;
			}else {
				if (input.files && input.files[0]) {
		        	var reader = new FileReader();
		            reader.onload = function (e) {
		                    $('#img1').attr('src', e.target.result);
		                    $('#img1').attr('width', width);
		                    $('#img1').attr('height', height);
		                };
		                reader.readAsDataURL(input.files[0]);
		        } 
			}
		}
		
		function getId(){
			return $("#id").val();
		}
		
		$(document).ready(function(){
			$('#searchbtn1').click(function(){
				$('#table1').datagrid('load',getForms("searchForm1"));
			});
			
			$('#searchbtn2').click(function(){
				$('#table2').datagrid('load',getForms("searchForm2"));
			});
			$('#updateAct').click(function(){
				$('#win').window('open');
				$('#table2').datagrid('reload');
			});
			
			$('#updatePos').click(function(){
				$('#winPos').window('open');
				$('#table3').datagrid('reload');
			});
			
			$('#updateActivity').click(function(){
				var startDate = $("input[name='startDate']").val();
				var endDate = $("input[name='endDate']").val();
				
				if(startDate == ''){
					alert("请选择开始时间！");
					return false ;
				}
				
				if(endDate == ''){
					alert("请选择结束时间！");
					return false ;
				}
				
				$('#updateForm').form('submit', {
				    url:'update',
				    success:function(data){ 
				    	if(data == 1){
				    		alert("修改成功！");
				    	}
				    },
				    error:function(data){
				    	//alert('保存失败');
					}
				});	
			});
			
			$('#delAct').click(function(){
				var data ='';
				//var rows = $('#table1').datagrid('getSelections');
				var rows = $('#table1').datagrid('getChecked');
				for(var i in rows){
					data += rows[i].gid+',';
				}
				data = data.substring(0, data.length -1);
				if(rows.length == 0){
					alert("请先选择要删除的品牌");
					return false; 	
				}
				if(!confirm("确认是否删除?")){
	        		return false; 
	        	}
				$.ajax({
		            url:'delActAndBran',
		            type:'post',
		            async: false,
		            data:'ids='+data,
		            success:function(data){
		            	$('#table1').datagrid('reload');
		            }
		        }); 
				
			});
			
			
			$('#actSure').click(function(){
				var data ='';
				var rows = $('#table2').datagrid('getChecked');
				for(var i in rows){
					data += rows[i].id+',';
				}
				data = data.substring(0, data.length -1);
				if(rows.length == 0){
					alert("请先选择要参加活动的品牌");
					return false; 	
				}
				$.ajax({
		            url:'addBrandAct',
		            type:'post',
		            async: false,
		            data:{
		            	ids:data,
		            	actId:getId()
		            },
		            beforeSend:function(){
		            	if(getId()==""){
		            		alert("请先添加活动信息！");
		            		$('#win').window('close');
		            		$('#tab').tabs('enableTab', 0);
		            		return false ;
		            	}
		            },
		            success:function(data){
		            	alert("品牌成功参加活动！");
		            	$('#table2').datagrid('reload');
		            	$('#win').window('close');
		            	$('#table1').datagrid('reload');
		            }
		        });
				
			});
			
			
			$('#delPos').click(function(){
				var data ='';
				var rows = $('#table3').datagrid('getChecked');
				for(var i in rows){
					data += rows[i].id+',';
				}
				data = data.substring(0, data.length -1);
				if(rows.length == 0){
					alert("请先选择要删除的POS机");
					return false; 	
				}
				if(!confirm("确认是否删除?")){
	        		return false; 
	        	}
				$.ajax({
		            url:'delPosBand',
		            type:'post',
		            async: false,
		            data:'ids='+data,
		            success:function(data){
		            	$('#table3').datagrid('reload');
		            }
		        }); 
				
			});
			
			
			$('#savePos').click(function(){
				var code = $("#code").val();
				var bindDate = $("input[name='bindDate']").val();
				$.ajax({
		            url:'savePos',
		            type:'post',
		            async: false,
		            data:{
		            	code:code,
		            	actId:getId(),
		            	bindDate:bindDate
		            },
		            beforeSend:function(){
		            	var flag ;
		            	$.ajax({
		    	            url:'checkPosBand',
		    	            type:'post',
		    	            async: false,
		    	            data:{
		    	            	code:code
		    	            },
		    	            success:function(data){
		    	            	flag = data ;
		    	            }
		    	        });
		            	if(flag > 0){
		            		alert("POS机编号为"+code+"已经绑定了,请重新输入！");
		            		return false ;
		            	}
		            	if(getId()==""){
		            		alert("请先添加活动信息！");
		            		$('#winPos').window('close');
		            		$('#tabAct').tabs('enableTab', 0);
		            		return false ;
		            	}
		            },
		            success:function(data){
		            	alert('保存成功');
		            	$('#winPos').window('close');
		            	$('#table3').datagrid('reload');
		            }
		        });
			});
			
		});
	</script>
</head>
<body>
<div id="tabAct" class="easyui-tabs" style="width:650px;height:550px">  
        <div title="活动信息新增" style="padding:10px">  
            <form id="updateForm" method="post" enctype="multipart/form-data">
            <fieldset>
			<legend style="color: blue;">活动基本信息</legend>
		     <table style="width: 600px;" >
		        	<tr>
		        	<input type="hidden" id="id" name="id" value="${activity.id }"/>
		        		<td><span style="font-weight: bolder;color: red;">*</span>活动名称：</td>
		        		<td><input id="activityName" value="${activity.activityName }" class="easyui-validatebox" data-options="required:true" name="activityName" type="text" style="width: 211px;"/></td>
		        		<td rowspan="8" colspan="2" >
		        			<img name="img1" id="img1" src="<%=request.getContextPath()%>/images/blank.jpg" />
		        		</td>
		        	</tr>
		        	<tr>
		        		<td><span style="font-weight: bolder;color: red;">*</span>开始时间：</td>
		        		<td><input id="startDate" value="${activity.startDate }" name="startDate" type="text" style="width: 215px;" class="easyui-datetimebox" editable="false"/></td>
		        	</tr>
		        	<tr>
		        		<td><span style="font-weight: bolder;color: red;">*</span>结束时间：</td>
		        		<td><input id="endDate" value="${activity.endDate }" name="endDate" type="text" style="width: 215px;" class="easyui-datetimebox" editable="false"/></td>
		        	</tr>
		        	<tr>
		        		<td><span style="font-weight: bolder;color: red;">*</span>描述：</td>
		        		<td>
		        			<textarea id="description"  class="easyui-validatebox" data-options="required:true" name="description" rows="3" cols="24">${activity.description}</textarea>
		        		</td>
		        	</tr>
		        	<tr>
		        		<td>&nbsp;举办方：</td>
		        		<td><input id="hoster" name="hoster" value="${activity.hoster }" type="text" style="width: 211px;"/></td>
		        	</tr>
		        	<tr>
		        		<td>&nbsp;活动网址：</td>
		        		<td><input id="activityNet" name="activityNet" value="${activity.activityNet }" class="easyui-validatebox" data-options="validType:'url'" type="text" style="width: 211px;"/></td>
		        	</tr>
		        	<tr>
		        		<td>&nbsp;联系人：</td>
		        		<td><input id="contacts" name="contacts" type="text" value="${activity.contacts }" style="width: 211px;"/></td>
		        	</tr>
		        	<tr>
		        		<td>&nbsp;联系电话：</td>
		        		<td><input id="conTel" name="conTel" type="text" value="${activity.conTel }" style="width: 211px;" class="easyui-validatebox" data-options="validType:'phoneNumber'"/></td>
		        	</tr>
		        	<tr>
		        		<td>&nbsp;图片：</td>
		        		<td><span id="pic"><input id="picture" value="${activity.picture }" name="picture" type="file" style="width: 211px;" onchange="check(this,'pic')"/></span></td>
		        		<td></td>
		        		<td><a id="lookbtn" href="javascript:void(0)" onclick="previewImage('picture')" class="easyui-linkbutton" >浏览</a></td>
		        	</tr>
		        	<tr></tr>
		        </table>
		        </fieldset>
		        <br>
		        <fieldset>
				<legend style="color: blue;">优惠信息</legend>
					<table style="width: 600px;" >
						<tr>
							<td>标题：</td>
							<td><input id="title" name="title" type="text" value="${discount.title }" style="width: 211px;"/></td>
						</tr>
						<tr>
							<td>描述</td>
							<td><textarea id="descr"  name="descr" rows="3" cols="24">${discount.descr }</textarea></td>
						</tr>
					</table>
				</fieldset>
				<br><br>
				<div align="center"><a id="updateActivity" href="javascript:void(0)" class="easyui-linkbutton" >保存</a></div>
	        </form> 
        </div>  
        <div title="参与品牌" style="padding:10px">  
            
			<form id="searchForm1" method="post" >
				<fieldset>
					<legend style="color: blue;">查询条件</legend>
					
					<table style="" id="search1">
						<tr>
							<td>品牌名称：</td>
							<td><input id="name" name="name"  type="text"/></td>
							<td>
								<a id="searchbtn1" href="javascript:void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							</td>
						</tr>
					</table>
				</fieldset>
			</form>

			<!-- 显示列表Table -->
			<table style="height:350px" id="table1"  title="参与活动的品牌" style="" class="easyui-datagrid" data-options="url:'query_actBands',queryParams:{id:${activity.id }},fitColumns:true,striped:true,loadMsg:'正在载入...',pagination:true,
				rownumbers:true,pageList:pageList,singleSelect:false">
			    <thead>  
			        <tr>  
			        	<th checkbox="true"></th>
			        	<th data-options="field:'gid',width:30">活动编号</th>
			            <th data-options="field:'name',width:30">品牌名称</th>  
			            <th data-options="field:'companyName',width:30">公司名称</th>
			            <th data-options="field:'joinTime',width:30,formatter:function(v){return dateFormat(v);}">加入时间</th>
			        </tr>  
			    </thead>  
			</table> 
			<br>
			<a id="updateAct" href="javascript:void(0)" onclick="" class="easyui-linkbutton" >新增</a>
			<a id="delAct" href="javascript:void(0)" onclick="" class="easyui-linkbutton" >删除</a>
        </div>  
        <div title="POS机维护" style="padding:10px">  

			<!-- 显示列表Table -->
			<table style="height:400px" id="table3"  title="所绑定活动的Pos机信息" style="" class="easyui-datagrid" data-options="url:'query_posBands',queryParams:{id:${activity.id }},fitColumns:true,striped:true,loadMsg:'正在载入...',pagination:true,
				rownumbers:true,pageList:pageList,singleSelect:false">
			    <thead>  
			        <tr>  
			        	<th checkbox="true"></th>
			        	<th data-options="field:'id',width:30">POS机编号</th>
			        	<th data-options="field:'code',width:30">POS机号</th>
			            <th data-options="field:'bindDate',width:30,formatter:function(v){return dateFormat(v);}">绑定时间</th>
			        </tr>  
			    </thead>  
			</table>  
			<br>
			<a id="updatePos" href="javascript:void(0)" onclick="" class="easyui-linkbutton" >新增</a>
			<a id="delPos" href="javascript:void(0)" onclick="" class="easyui-linkbutton" >删除</a>
        </div>  
    </div> 
	<div id="win" class="easyui-window" title="选择参与活动的品牌" style="width:600px;height:350px"  
	        data-options="modal:true,closed:true,collapsible:false,minimizable:false,maximizable:false,">  
	    	<form id="searchForm2" method="post" >
				<fieldset>
					<legend style="color: blue;">查询条件</legend>
					
					<table style="" id="search">
						<tr>
							<td>品牌名称：</td>
							<td><input id="name" name="name" type="text"/></td>
							<td>公司名称：</td>
							<td><input id="companyName" name="companyName" type="text"/></td>
							<td>
								<a id="searchbtn2" href="javascript:void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							</td>
						</tr>
					</table>
				</fieldset>
			</form>

			<!-- 显示列表Table -->
			<table id="table2" style="height:200px" idField="name" style="" class="easyui-datagrid" data-options="url:'findBrandNotBandAct',queryParams:{id:${activity.id }},fitColumns:true,striped:true,loadMsg:'正在载入...',pagination:true,
				rownumbers:true,pageList:pageList,singleSelect:false,onDblClickRow:function(rowIndex,rowData){edit(rowData.id,rowData.name);}">
			    <thead>  
			        <tr>  
			        	<th field="ck" checkbox="true"></th>
			        	<th data-options="field:'id',width:30">品牌编号</th> 
			            <th data-options="field:'name',width:30">品牌名称</th>  
			            <th data-options="field:'companyName',width:30">公司名称</th>
			        </tr>  
			    </thead>  
			</table> 
			
			<br>
			<a id="actSure" href="javascript:void(0)" onclick="" class="easyui-linkbutton" >确定</a>  
	</div>
	
	</table>
	<div id="winPos" class="easyui-window" title="添加绑定的POS机" style="width:250px;height:140px"  
        data-options="modal:true,closed:true,collapsible:false,minimizable:false,maximizable:false,"> 
        <br>
        <form id="posForm">
        	<table>
        		<tr>
        			<td>绑定POS机：</td>
        			<td>
        				<input type="text" name="code" id="code" />
        			</td>
        		</tr>
        		<tr>
        			<td>绑定日期：</td>
        			<td>
        				<input type="text" name="bindDate" style="width: 155px" id="bindDate" class="easyui-datetimebox" />
        			</td>
        		</tr>
        		<tr>
        			<td colspan="2" align="right">
        				<a id="savePos" href="javascript:void(0)" onclick="" class="easyui-linkbutton" >保存</a>
        			</td>
        		</tr>
        	</table>
        </form> 
    </div>  
</div> 
</body>
</html>