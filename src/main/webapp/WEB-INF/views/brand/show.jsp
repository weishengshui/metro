<%@ page language="java" contentType="text/html; charset=utf8"
	pageEncoding="utf8"%>
<%@ page import="com.chinarewards.metro.core.common.Constants" %>	
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/constant.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.json-2.4.js"></script>
<style type="text/css">
	fieldset table tr{height: 35px;}
	fieldset{margin-bottom:10px;margin-left:30px;margin-top:10px;}
	select{width:155px;height:20px;}
	.red{color:red;font-size:12px;}
</style>
<script type="text/javascript">
		var baseURL = '<%=request.getContextPath()%>';
	
		$(function(){
			style="display:none";
	    	document.getElementById('divPreview').style.display = "none";
		});
		
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
			//parent.parent.dialog("预览图片",url,width,height);
		}
		function doSubmit(){
			$('#fm').form('submit',{
				success:function(result){
					result = eval('(' + result + ')');
					alert(result.msg);
					$('#_id').val(result.id);		
				}
			}); 
		}
	
	function check(path,spanId){
		var filepath=path.value;
		filepath=filepath.substring(filepath.lastIndexOf('.')+ 1,filepath.length);
		filepath = filepath.toLocaleLowerCase();
		if(filepath != 'jpg' && filepath != 'gif' && filepath!='jpeg' && filepath !='bmp' && filepath!='png'){
			alert("只能上传JPG, GIF, JPEG, BMP, PNG 格式的图片");
			deleteInputFile(path.name, path.id, spanId);
		}
	}
	function openDialog(){
		$('#dd').dialog('center');
		$('#dd').dialog('open');
	}
	function uploadImage(fileId,formId, keyId, aimageId,imageId,divPreviewId){
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
				$('#'+aimageId).attr('href','javascript:show(\''+baseURL+'/archive/imageShow?path=BRAND_IMAGE_BUFFER&contentType='+result.contentType+'&fileName='+result.url+'\',\''+result.width+'\',\''+result.height+'\')');
				$('#'+imageId).attr('src',baseURL+'/archive/showGetthumbPic?path=BRAND_IMAGE_BUFFER&contentType='+result.contentType+'&fileName='+result.url);
				style="display:none";
		    	document.getElementById(divPreviewId).style.display = "";
			}
		}); 
	}
	function deleteImage(keyId, divPreviewId){ // 清空input type=file 直接$('#'+imageId).val('');有浏览器不兼容的问题
		$.ajax({
			url:'deleteImage',
			data:'imageSessionName='+$('#imageSessionName_imageForm').val()+'&key='+$('#'+keyId).val(),
			dataType:'json',
			async:false,
			success:function(data){
				
			}
		});
		style="display:none";
    	document.getElementById(divPreviewId).style.display = "none";
		//$('#span1').html("<input type=file name=logo2 id=logo2 accept=image/* onchange=check(this,'span1') />");
	}
	function deleteInputFile(name, id, spanId){
		$('#'+spanId).html("<input type=file name="+ name +" id="+ id +" accept=image/* onchange=check(this,'"+spanId+"') />");
	}
</script>

</head>
<body>
	<div class="easyui-tabs" style="">
	    <div title="品牌基本信息" style="padding:20px;">
	    	<form action="create" method="post" id="fm"> 
				<table border="0">
					<tr>
						<td>
							<fieldset style="font-size: 14px;width:auto;height:auto;">
								<legend style="color: blue;">品牌新增</legend>
									<table border="0">
										<tr>
											<td width="20px"><span style="color: red;">*</span></td>
											<td width="80px">品牌名称：</td>
											<td width="200px" align="left">
												<input type="hidden" name="id" id="_id">
												<input type="hidden" name="imageSessionName" id="imageSessionName_dataForm">
												<input id="name" name="name" type="text" style="width:150px" class="easyui-validatebox" data-options="required:true" /> 
											</td>
											<td></td>
										</tr>
										<tr>
											<td width="20px"><span style="color: red;">*</span></td>
											<td width="80px">公司名称：</td>
											<td width="200px" align="left">
												<input id="companyName" name="companyName" type="text" style="width:150px" class="easyui-validatebox" data-options="required:true"> 
											</td>
											<td></td>
										</tr>
										<tr>
											<td width="20px"><span style="color: red;">*</span></td>
											<td width="80px">公司网址：</td>
											<td width="200px" align="left">
												<input id="companyWebSite" name="companyWebSite" type="text" style="width:150px" class="easyui-validatebox" data-options="required:true,validType:'url'"> 
											</td>
											<td></td>
										</tr>
										<tr>
											<td width="20px"><span style="color: red;">*</span></td>
											<td width="80px">联系人：</td>
											<td width="200px" align="left">
												<input id="contact" name="contact" type="text" style="width:150px" class="easyui-validatebox" data-options="required:true"> 
											</td>
											<td></td>
										</tr>
										<tr>
											<td width="20px"><span style="color: red;">*</span></td>
											<td width="80px">联系电话：</td>
											<td width="200px" align="left">
												<input id="phoneNumber" name="phoneNumber" type="text" style="width:150px" validType="phoneNumber" maxlength="13" class="easyui-validatebox" data-options="required:true,validType:'phoneNumber'"> 
											</td>
											<td></td>
										</tr>
										<tr>
											<td width="20px"></td>
											<td width="80px">描述：</td>
											<td width="200px" align="left">
												<textarea rows="5" cols="15" name="description" id="description"></textarea>
											</td>
											<td></td>
										</tr>
										
										<tr>
											<td></td><td width="80px" colspan="2"><input type="checkbox" name="unionInvited" id="unionInvited">联合会员申请</td><td></td>
										</tr>
										<tr>
											<td align="center" colspan="4"><button type="button" onclick="doSubmit()">保存</button></td>
										</tr>
									</table>
							</fieldset>
						</td>
					</tr>
					
				</table>
			</form>
			<div id="imageDia" class="easyui-dialog" title="图片预览" style="width:400px;height:400px;"  
		        data-options="resizable:true,modal:true,inline:false,closed:true">
			        <div style="text-align:center;">
			        	<img id="perviewImage" name="perviewImage" src="" />
			        </div>
			</div> 
	    </div>
	    <div title="图片维护" style="padding:20px;">
		    <form action="imageUpload" id="fm2" method="post" enctype="multipart/form-data">
		    	<table>
			    	<tr>
						<td width="20px"></td>
						<td width="80px">图片：</td>
						<td>
							<input type="hidden" name="path" value="BRAND_IMAGE_BUFFER">
							<input type="hidden" name="key" id="key" />
							<input type="hidden" name="imageSessionName" id="imageSessionName_imageForm">
							<div id="divPreview" >
<!-- 								<img id="imgHeadPhoto" src=""/> -->
								<a id="aimage1" href=""><img id="image1" src=""></a>
							</div>
						</td>
						<td width="200px" align="left">
							<span id="span1">
								<input type="file" name="file" id="file1" accept="image/*" onchange="check(this,'span1')">
							</span>
						</td>
						<td width="80px">&nbsp;&nbsp;<button type="button"  onclick="uploadImage('file1','fm2', 'key', 'aimage1','image1','divPreview')">上传</button></td>
						<td width="80px">&nbsp;&nbsp;<button type="button"  onclick="deleteImage('key','divPreview')">删除</button></td>
					</tr>
			    </table>
		    </form>
	    </div>
    </div>
    <div id="divDialog">
    	<iframe scrolling="auto" id='openIframe' name="openIframe" frameborder="0"  src="" style="width:100%;height:100%;overflow: hidden;"></iframe> 
	</div>
</body>
</html>