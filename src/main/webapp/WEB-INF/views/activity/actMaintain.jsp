<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.chinarewards.metro.domain.activity.Token" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery/themes/icon.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script type="text/javascript">
		var baseURL = '<%=request.getContextPath()%>';
		function getId(){
			return $("#actId").val();
		}
		function show(url,width,height){
			width = width> 500 ? 500: width;
			height = height> 700 ? 500: height;
			$("#openIframe").attr("src",url);
			$("#divDialog").dialog({
				height:height,
				width:width,
				modal:true,
				resizable:true,
				title:'预览图片'
			});
		}
		$(function(){
			style="display:none";
	    	document.getElementById('divPreview').style.display = "none";
		});
	$(document).ready(function(){
		
		$('#saveActivity').click(function(){
			var name = $("input[name='activityName']").val();
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
			var tag = false ;
			$.ajax({
	            url:'checkActNameAndTime',
	            type:'post',
	            async: false,
	            data:{
	            	name:name,
	            	dTime:startDate
	            },
	            success:function(data){
	            	if(data == 1){
	            		tag = true ;
	            	}
	            }
	        });
			
			if(tag){
				alert("活动名称和开始日期不能相同！");
				return false ;
			}
			
			$('#activityForm').form('submit', {
			    url:'saveActivity',
			    success:function(data){
		    		$("#id").val(data);
			    	$("#actId").val(data);
			    	$("#hid").val(data);
		    		$('#table1').datagrid({
		    			queryParams: {
		    				id: data
		    			}
		    		});
		    		$('#table2').datagrid({
		    			queryParams: {
		    				id: data
		    			}
		    		});
		    		$('#table3').datagrid({
		    			queryParams: {
		    				id: data
		    			}
		    		});
		    		alert('保存成功');
			    },
			    error:function(data){
			    	//alert('保存失败');
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
	            	var flag = false;
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
		
		
			
		$('#searchbtn1').click(function(){
			$('#table1').datagrid('load',getForms("searchForm1"));
		});
		
		$('#searchbtn2').click(function(){
			$('#table2').datagrid('load',getForms("searchForm2"));
		});
		
		
		$('#addAct').click(function(){
			if(getId()==""){
        		alert("请先添加活动信息！");
        		$('#win').window('close');
        		return false ;
        	}
			$('#win').window('open');
			$('#table2').datagrid('reload');
		});
		
		$('#addPos').click(function(){
			if(getId()==""){
        		alert("请先添加活动信息！");
        		$('#win').window('close');
        		return false ;
        	}
			$('#winPos').window('open');
			$('#table3').datagrid('reload');
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
		
		$('#delAct').click(function(){
			var data ='';
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
		
	});
	
	function check(path,spanId){
		var filepath=path.value;
		filepath=filepath.substring(filepath.lastIndexOf('.')+ 1,filepath.length);
		filepath = filepath.toLocaleLowerCase();
		if(filepath != 'jpg' && filepath != 'gif' && filepath!='jpeg' && filepath !='bmp' && filepath!='png'){
			alert("只能上传JPG, GIF, JPEG, BMP, PNG 格式的图片");
			deleteInputFile(path.name, path.id, spanId);
		}
	}
	function uploadImage(fileId,formId, keyId, aimageId,imageId, divPreviewId){
		if($('#'+fileId).val()==""){
			alert("请先添加图片");
			return ;
		}
		$('#'+formId).form('submit',{
			success:function(result){
				result = eval('('+result+')');
				if(!result.key){
					alert("请检查图片是否是完好的");
					return;
				}
				$('#'+keyId).val(result.key);
				$('#imageSessionName_dataForm').val(result.imageSessionName);
				$('#imageSessionName_imageForm').val(result.imageSessionName);
				$('#'+aimageId).attr('href','javascript:show(\''+baseURL+'/archive/imageShow?path=ACTIVITY_IMAGE_BUFFER&contentType='+result.contentType+'&fileName='+result.url+'\',\''+result.width+'\',\''+result.height+'\')');
				$('#'+imageId).attr('src',baseURL+'/archive/showGetthumbPic?path=ACTIVITY_IMAGE_BUFFER&contentType='+result.contentType+'&fileName='+result.url);
				style="display:none";
		    	document.getElementById(divPreviewId).style.display = "";
			}
		}); 
	}
	function deleteImage(keyId, divPreviewId){ 
		$.ajax({
			url:baseURL+'/archive/deleteImage',
			data:'imageSessionName='+$('#imageSessionName_imageForm').val()+'&key='+$('#'+keyId).val(),
			dataType:'json',
			async:false,
			success:function(data){
			}
		});
		style="display:none";
    	document.getElementById(divPreviewId).style.display = "none";
	}
	function deleteInputFile(name, id, spanId){// 清空input type=file 直接$('#'+imageId).val('');有浏览器不兼容的问题
		$('#'+spanId).html("<input type=file name="+ name +" id="+ id +" accept=image/* onchange=check(this,'"+spanId+"') />");
	}
</script>
</head>
<body>

	<input type="hidden" value="" name="actId" id="actId" />

	<div id="tabAct" class="easyui-tabs" style="width:650px;height: 550px;">
	<div title="上传活动图片" style="padding:20px;text-align:center;"><!-- 图片维护start -->
			<div style="">
		        	<form action="<%=request.getContextPath()%>/archive/imageUpload" id="fm2" method="post" enctype="multipart/form-data">
				    	<table>
				    	<tr>
							<td width="20px"></td>
							<td width="80px">请选择图片：</td>
							<td width="200px" align="left">
								<input type="hidden" name="path" value="ACTIVITY_IMAGE_BUFFER">
								<input type="hidden" name="key" id="key" />
								<input type="hidden" name="imageSessionName" id="imageSessionName_imageForm">
								<span id="span1">
									<input type="file" name="file" id="file1" accept="image/*" onchange="check(this,'span1')">
								</span>
							</td>
							<td width="80px">&nbsp;&nbsp;
							<a href="javascript:void(0)" onclick="uploadImage('file1','fm2', 'key', 'aimage1','image1','divPreview')" class="easyui-linkbutton" >上传</a>
							</td>
						</tr>
				    </table>
			    </form>
	        </div><br><br>
			<div align="left">图片预览：</div>
	    	<div id="divPreview" >
				<a id="aimage1" href=""><img id="image1" src="" style="width: 400px;height: 250px;"></a><br><br><br>
				<a href="javascript:void(0)" onclick="deleteImage('key','divPreview')" class="easyui-linkbutton" >删除</a>
			</div>
		    <div id="divDialog">
		    	<iframe scrolling="auto" id='openIframe' name="openIframe" frameborder="0"  src="" style="width:100%;height:100%;overflow: hidden;"></iframe> 
			</div>
	    </div><!-- 图片维护end -->  
        <div title="活动信息新增" style="padding:10px">  
            <form id="activityForm" method="post" >
            <fieldset>
			<legend style="color: blue;">活动基本信息</legend>
			<%
			   Token t=Token.getInstance();
			   String token=t.getToken(request);
			%>
			<input type="hidden" name="imageSessionName" id="imageSessionName_dataForm">
		     <table style="width: 600px;" >
		        	<tr>
		        	<input type="hidden" value="" name="id" id="id" />
		        	<input type="hidden" name="goasin" value="<%=token %>"/>
		        		<td><span style="font-weight: bolder;color: red;">*</span>活动名称：</td>
		        		<td><input id="activityName" class="easyui-validatebox" data-options="required:true" name="activityName" type="text" style="width: 211px;"/></td>
		        		<td rowspan="8" colspan="2" >
		        			<img name="img1" id="img1" src="<%=request.getContextPath()%>/images/blank.jpg" />
		        		</td>
		        	</tr>
		        	<tr>
		        		<td><span style="font-weight: bolder;color: red;">*</span>开始时间：</td>
		        		<td><input id="startDate" name="startDate" type="text" style="width: 215px;" class="easyui-datetimebox" editable="false"/></td>
		        	</tr>
		        	<tr>
		        		<td><span style="font-weight: bolder;color: red;">*</span>结束时间：</td>
		        		<td><input id="endDate"  name="endDate" type="text" style="width: 215px;" class="easyui-datetimebox" editable="false"/></td>
		        	</tr>
		        	<tr>
		        		<td><span style="font-weight: bolder;color: red;">*</span>描述：</td>
		        		<td>
		        			<textarea id="description" class="easyui-validatebox" data-options="required:true" name="description" rows="3" cols="24"></textarea>
		        		</td>
		        	</tr>
		        	<tr>
		        		<td>&nbsp;举办方：</td>
		        		<td><input id="hoster" name="hoster" type="text" style="width: 211px;"/></td>
		        	</tr>
		        	<tr>
		        		<td>&nbsp;活动网址：</td>
		        		<td><input id="activityNet" name="activityNet" class="easyui-validatebox" data-options="validType:'url'" type="text" style="width: 211px;"/></td>
		        	</tr>
		        	<tr>
		        		<td>&nbsp;联系人：</td>
		        		<td><input id="contacts" name="contacts" type="text" style="width: 211px;"/></td>
		        	</tr>
		        	<tr>
		        		<td>&nbsp;联系电话：</td>
		        		<td><input id="conTel" name="conTel" type="text" style="width: 211px;" class="easyui-validatebox" data-options="validType:'phoneNumber'"/></td>
		        	</tr>
		        </table>
		        </fieldset>
		        <br>
		        <fieldset>
				<legend style="color: blue;">优惠信息</legend>
					<table style="width: 600px;" >
						<tr>
							<td>标题：</td>
							<td><input id="title" name="title" type="text" style="width: 211px;"/></td>
						</tr>
						<tr>
							<td>描述</td>
							<td><textarea id="descr"  name="descr" rows="3" cols="24"></textarea></td>
						</tr>
					</table>
				</fieldset>
				<br><br>
				<div align="center"><a id="saveActivity" href="javascript:void(0)" onclick="" class="easyui-linkbutton" >保存</a></div>
	        </form> 
        </div>  
        
        <div title="参与品牌" style="padding:10px">  
            
			<form id="searchForm1" method="post" >
				<fieldset>
					<legend style="color: blue;">查询条件</legend>
					
					<table style="" id="search">
						<tr>
							<td>品牌名称：</td>
							<td><input id="name" name="name" type="text"/></td>
							<input id="hid" name="id" type="hidden"/>
							<td>
								<a id="searchbtn1" href="javascript:void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							</td>
						</tr>
					</table>
				</fieldset>
			</form>

			<!-- 显示列表Table -->
			<table style="height:350px" id="table1"  title="参与活动的品牌" style="" class="easyui-datagrid" data-options="url:'query_actBands',fitColumns:true,striped:true,loadMsg:'正在载入...',pagination:true,
				rownumbers:true,pageList:pageList,singleSelect:false">
			    <thead>  
			        <tr>  
			        	<th checkbox="true"></th>
			        	<th data-options="field:'gid',width:30,hidden:true">活动编号</th>
			            <th data-options="field:'name',width:30">品牌名称</th>  
			            <th data-options="field:'companyName',width:30">公司名称</th>
			            <th data-options="field:'joinTime',width:30,formatter:function(v){return dateFormat(v);}">加入时间</th>
			        </tr>  
			    </thead>  
			</table> 
			<br>
			<a id="addAct" href="javascript:void(0)" onclick="" class="easyui-linkbutton" >新增</a>
			<a id="delAct" href="javascript:void(0)" onclick="" class="easyui-linkbutton" >删除</a>
        </div>  
        <div title="POS机维护" style="padding:10px">  

			<!-- 显示列表Table -->
			<table style="height:400px" id="table3"  title="所绑定活动的Pos机信息" style="" class="easyui-datagrid" data-options="url:'query_posBands',fitColumns:true,striped:true,loadMsg:'正在载入...',pagination:true,
				rownumbers:true,pageList:pageList,singleSelect:false">
			    <thead>  
			        <tr>  
			        	<th checkbox="true"></th>
			        	<th data-options="field:'id',width:30,hidden:true">POS机编号</th>
			        	<th data-options="field:'code',width:30">POS机号</th>
			            <th data-options="field:'bindDate',width:30,formatter:function(v){return v;}">绑定时间</th>
			        </tr>  
			    </thead>  
			</table>  
			<br>
			<a id="addPos" href="javascript:void(0)" onclick="" class="easyui-linkbutton" >新增</a>
			<a id="delPos" href="javascript:void(0)" onclick="" class="easyui-linkbutton" >删除</a>
        </div>  
    </div> 
	<div id="win" class="easyui-window" title="选择参与活动的品牌" style="width:600px;height:350px"  
	        data-options="modal:true,closed:true,collapsible:false,minimizable:false,maximizable:false">  
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
			<table id="table2" style="height:200px" idField="name" style="" class="easyui-datagrid" data-options="url:'findBrandNotBandAct',fitColumns:true,striped:true,loadMsg:'正在载入...',pagination:true,
				rownumbers:true,pageList:pageList,singleSelect:false">
			    <thead>  
			        <tr>  
			        	<th field="ck" checkbox="true"></th>
			        	<th data-options="field:'id',width:30,hidden:true">品牌编号</th> 
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
        data-options="modal:true,closed:true,collapsible:false,minimizable:false,maximizable:false"> 
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