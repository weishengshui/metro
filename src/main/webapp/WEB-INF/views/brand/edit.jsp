<%@ page language="java" contentType="text/html; charset=utf8"
	pageEncoding="utf8"%>
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
		
	function doSubmit(){
		$('#fm').form('submit',{
			success:function(result){
				alert(eval('('+result+')').msg);
			}
		}); 
	}
	
	function doSearch(){  
	    $('#tt2').datagrid('load',{ 
	    	brandId: $('#_id').val(),
	    	memberName:$('#memberName').val(),  
	    	cardNumber:$('#cardNumber').val(),
	    	joinedStart:$('#joinedStart').datebox('getValue'),
	    	joinedEnd:$('#joinedEnd').datebox('getValue'),
	    });  
	}
	
	function exportUnionMember(){
		
		window.location.href = 'exportUnionMember?brandId='+$('#_id').val()+'&memberName='+$('#memberName').val()+'&cardNumber='+$('#cardNumber').val()+'&joinedStart='+$('#joinedStart').datebox('getValue')+'&joinedEnd='+$('#joinedEnd').datebox('getValue');
	}
	
	function check(path,spanId){
		var filepath=path.value;
		filepath=filepath.substring(filepath.lastIndexOf('.')+ 1,filepath.length);
		filepath = filepath.toLocaleLowerCase();
		if(filepath != 'jpg' && filepath != 'gif' && filepath!='jpeg' && filepath !='bmp' && filepath!='png'){
			alert("只能上传JPG, GIF, JPEG, BMP, PNG 格式的图片");
			deleteImage(path.name, path.id, spanId);
		}
	}
	function openDialog(){
		$('#dd').dialog('center');
		$('#dd').dialog('open');
	}
	function previewImage(imageId){
    	
		if($('#'+imageId).val()==''){
			alert("请先添加图片");
			return;
		}
		var width, height;
		width = '346px';
		height = '346px';
		var input = document.getElementById(imageId);
		var imgPre = document.getElementById('perviewImage');
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
	                    $('#perviewImage').attr('src', e.target.result);
	                    $('#perviewImage').attr('width', width);
	                    $('#perviewImage').attr('height', height);
	                };
	                reader.readAsDataURL(input.files[0]);
	        } 
		}
		$('#imageDia').resizable({  
		    maxWidth:20,  
		    maxHeight:100  
		}); 
		$('#imageDia').dialog('center');
		$('#imageDia').dialog('open');
	}
	function deleteImage(name, id, spanId){ // 清空input type=file 直接$('#'+imageId).val('');有浏览器不兼容的问题
		//$('#'+imageId).val('');
		$('#'+spanId).html("<input type=file name="+name+" id="+id+" accept=image/* onchange=check(this,'"+spanId+"') />");
	}
</script>

</head>
<body>
	
	<div id="tt" class="easyui-tabs" style="width:800;">  
       <div title="品牌基本信息" style="padding:20px;">
       		<form action="create" method="post" id="fm" enctype="multipart/form-data"> 
				<table border="0">
					<tr>
						<td>
							<fieldset style="font-size: 14px;width:auto;height:auto;">
								<legend style="color: blue;">修改品牌信息</legend>
									<table border="0">
										<tr>
											<td width="20px"><span style="color: red;">*</span></td>
											<td width="80px">品牌名称：</td>
											<td width="200px" align="left">
												<input type="hidden" name="id" id="_id" value="${brand.id }">
												<input id="name" name="name" value="${brand.name }" type="text" style="width:150px" class="easyui-validatebox" data-options="required:true" /> 
											</td>
											<td></td>
										</tr>
										<tr>
											<td width="20px"><span style="color: red;">*</span></td>
											<td width="80px">公司名称：</td>
											<td width="200px" align="left">
												<input id="companyName" name="companyName" value="${brand.companyName }" type="text" style="width:150px" class="easyui-validatebox" data-options="required:true"> 
											</td>
											<td></td>
										</tr>
										<tr>
											<td width="20px"><span style="color: red;">*</span></td>
											<td width="80px">公司网址：</td>
											<td width="200px" align="left">
												<input id="companyWebSite" name="companyWebSite" value="${brand.companyWebSite }" type="text" style="width:150px" class="easyui-validatebox" data-options="required:true,validType:'url'"> 
											</td>
											<td></td>
										</tr>
										<tr>
											<td width="20px"><span style="color: red;">*</span></td>
											<td width="80px">联系人：</td>
											<td width="200px" align="left">
												<input id="contact" name="contact" value="${brand.contact }" type="text" style="width:150px" class="easyui-validatebox" data-options="required:true"> 
											</td>
											<td></td>
										</tr>
										<tr>
											<td width="20px"><span style="color: red;">*</span></td>
											<td width="80px">联系电话：</td>
											<td width="200px" align="left">
												<input id="phoneNumber" name="phoneNumber" value="${brand.phoneNumber }" type="text" style="width:150px" validType="phoneNumber" maxlength="13" class="easyui-validatebox" data-options="required:true,validType:'phoneNumber'"> 
											</td>
											<td></td>
										</tr>
										<tr>
											<td width="20px"></td>
											<td width="80px">描述：</td>
											<td width="200px" align="left">
												<textarea rows="5" cols="15" name="description" id="description">${brand.description }</textarea>
											</td>
											<td></td>
										</tr>
										<tr>
											<td width="20px"></td>
											<td width="80px">图片：</td>
											<td width="200px" align="left">
												<span id="span1">
													<input type="file" name="logo2" id="logo" accept="image/*" onchange="check(this,'span1')">
												</span>
											</td>
											<td width="80px">&nbsp;&nbsp;<button type="button"  onclick="previewImage('logo')">预览</button> </td>
										</tr>
										<tr>
											<td></td><td colspan="2">
												<c:choose>
													<c:when test="${(! empty brand) && brand.unionInvited==true }">
														<input type="checkbox" name="unionInvited" id="unionInvited" checked="checked">联合会员申请
													</c:when>
													<c:otherwise>
														<input type="checkbox" name="unionInvited" id="unionInvited">联合会员申请
													</c:otherwise>
												</c:choose>
											</td><td></td>
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
       <div title="联合会员" style="padding:20px;">
			<table border="0">
				<tr>
					<td>
						<fieldset style="font-size: 14px;width:auto;height:auto;">
							<legend style="color: blue;">查询条件</legend>
							<form action="#" >
								<table border="0">
									<tr>
										<td width="80px">会员名称：</td>
										<td width="200px" align="left">
											<input id="memberName" name="memberName" type="text" style="width:150px"/> 
										</td>
										<td width="80px">会员卡号：</td>
										<td width="200px" align="left">
											<input id="cardNumber" name="cardNumber" type="text" style="width:150px"> 
										</td>
									</tr>
									<tr>
										<td width="80px">加入时间：</td>
										<td width="200px" align="left">
											<input id="joinedStart" name="joinedStart" type="text" style="width:150px" class="easyui-datebox" editable="false"/>
										</td>
										<td width="80px">至</td>
										<td width="200px" align="left">
											<input id="joinedEnd" name="joinedEnd" type="text" style="width:150px" class="easyui-datebox" editable="false"/>
										</td>
									</tr>
									<tr>
										<td width="80px"></td>
										<td>
										<td>
										</td>
										<td>
											<button type="button" onclick="doSearch()">查询</button>
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
							<legend style="color: blue;">查询结果</legend>
							<table id="tt2" class="easyui-datagrid" width="100%" height="100%"  
		           				url="listUnionMember" rownumbers="true" pagination="true">  
		       					<thead>  
		           				<tr>  
<!-- 		           				<th field="id" checkbox="true"></th>  -->
		                		<th data-options="field:'1',width:100,formatter:function(v,r,i){return r.member.name;}">会员名称</th>
		                		<th data-options="field:'2',width:100, formatter:function(v, r, i){return r.member.card.cardNumber;}">会员卡号</th>
		                		<th data-options="field:'joinedDate',formatter:function(v,r,i){return dateFormat(v);},width:150" >加入时间</th>
					           	</tr>  
						       	</thead>  
					   		</table> 
					   		<div style="text-align:right;">
					   			<br>
					   			<input type="button" onclick="exportUnionMember()" value="导出EXCEL">
					   		</div>
						</fieldset>
					</td>
				</tr>
			</table>
       </div>
	</div>	
</body>
</html>