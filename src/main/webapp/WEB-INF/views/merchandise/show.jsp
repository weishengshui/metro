<%@ page language="java" contentType="text/html; charset=utf8"
	pageEncoding="utf8"%>
<%@ page import="com.chinarewards.metro.domain.merchandise.MerchandiseStatus" %>	
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/js/jquery/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/js/jquery/themes/icon.css" />
<style type="text/css">
	fieldset table tr{height: 35px;}
	fieldset{margin-bottom:10px;margin-left:30px;margin-top:10px;}
	select{width:155px;height:20px;}
	.red{color:red;font-size:12px;}
</style>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery/jquery-1.8.0.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/common.js"></script>
	<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/ueditor/editor_config.js"></script>
	<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/ueditor/editor_all.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.json-2.4.js"></script>	
<script type="text/javascript" src="<%=request.getContextPath()%>/js/map.js"></script>
<script type="text/javascript">
		var baseURL = '<%=request.getContextPath()%>';
		var editor;
		var map = new Map();
		var imagePreIdMap = new Map();
		
		function show(url,width,height){
			width = (width> 500 ? 500: width);
			height = (height> 700 ? 500: height);
			$("#openIframe").attr("src",url);
			$("#divDialog").dialog({
				height:height,
				width:width,
				modal:true,
				resizable:true,
				title:'预览图片'
			});
		}
		
	$(function() {
		editor= new UE.ui.Editor();
		editor.render('description');
		editor.ready(function(){
		    //需要ready后执行，否则可能报错
		    editor.setContent('${merchandise.description }');
		});
		
		for(var i = 0; i < 7; i++){
			var perArray = new  Array();
			perArray[0] = 'key'+i;
			perArray[1] = 'image'+i;
			perArray[2] = 'aimage'+i;
			perArray[3] = 'divPreview'+i;
			imagePreIdMap.put(i, perArray);
		}
		style="display:none";
		for(var i = 0; i < 7; i++){
	    	document.getElementById('divPreview'+i).style.display = "none";
	    }

    	var images = $.toJSON(${images});
    	//alert(images);
		images = eval('('+images+')');
		if(images){
			var index = 1;
			for(var i in images){
				var preArray = imagePreIdMap.get(index);
				var image = images[i];
				if(image){
					//alert('image.imageType is '+image.imageType);
					if(image.imageType == "OVERVIEW"){
						preArray = imagePreIdMap.get(0);
						//alert('image.url is ' + image.url);
						$('#'+preArray[0]).val(i);
						$('#'+preArray[2]).attr('href','javascript:show(\''+baseURL+'/archive/imageShow?path=MERCHANDISE_IMAGE_DIR&contentType='+image.mimeType+'&fileName='+image.url+'\',\''+image.width+'\',\''+image.height+'\')');
						$('#'+preArray[1]).attr('src',baseURL+'/archive/showGetthumbPic?path=MERCHANDISE_IMAGE_DIR&contentType='+image.mimeType+'&fileName='+image.url);
						style="display:none";
				    	document.getElementById(preArray[3]).style.display = "";
				    	continue;
					}else{
						//alert('image.url is ' + image.url);
						$('#'+preArray[0]).val(i);
						$('#'+preArray[2]).attr('href','javascript:show(\''+baseURL+'/archive/imageShow?path=MERCHANDISE_IMAGE_DIR&contentType='+image.mimeType+'&fileName='+image.url+'\',\''+image.width+'\',\''+image.height+'\')');
						$('#'+preArray[1]).attr('src',baseURL+'/archive/showGetthumbPic?path=MERCHANDISE_IMAGE_DIR&contentType='+image.mimeType+'&fileName='+image.url);
						style="display:none";
				    	document.getElementById(preArray[3]).style.display = "";
				    	index++;
					}
				}else{
					continue;
				}
			}
		}
	});
	
	$(document).ready(function(){
		var categoryVos = '${categoryVos}';
		categoryVos = eval('('+categoryVos+')');
		var timeParam = Math.round(new Date().getTime()/1000);
		$('#selectCategorys').html('<table border="0" id="categoryTable"><tr><td width="auto">商品类别</td><td width="auto">上下架</td><td width="auto">类别排序</td>	<td width="auto">操作</td></tr></table>');
		if(categoryVos && categoryVos.length > 0){
			for(var i = 0, length = categoryVos.length; i < length; i++){
				var displaySort = '<input name="displaySort" type="text" style="width:50px" ';
				
				var categoryVo = categoryVos[i];
				var categoryId = categoryVo.categoryId;
				var deleteBut='<button type="button" onclick="deleteCategory(this,\''+categoryId+'\')">删除</button>';
				map.put(categoryId, '');
				
				var categId = '<input type="hidden" name="categId" value='+ categoryId +' />';
				var categoryName = categoryVo.fullName;
				var displaySortId = 'displaySort'+timeParam+i;
				displaySort += ' id="' + displaySortId +'"/>';
				var status= '';
				if(categoryVo.status == '<%=MerchandiseStatus.ON.toString()%>'){
					status='<select name="status" style="width:50px" ><option value="ON" selected="selected">上架</option><option value="OFF">下架</option></select>';
				}else if(categoryVo.status == '<%=MerchandiseStatus.OFF.toString()%>'){
					status='<select name="status" style="width:50px" ><option value="ON">上架</option><option value="OFF" selected="selected">下架</option></select>';
				}
				
				
				var str = '<tr>';
				str += '<td  width="auto">' + categId +categoryName +'</td><td  width="auto">'+status +'</td><td  width="auto">' + displaySort +'</td><td width="auto">'+deleteBut+'</td>';
				str += '</tr>';
				$(str).appendTo($('#categoryTable'));
				$('#' + displaySortId).numberbox({precision:0});
				$('#' + displaySortId).numberbox('setValue',categoryVo.displaySort);
				$('#' + displaySortId).validatebox({required:true});
			}
		}
	});
	
	function doSubmit(){
		$('#description2').val(editor.getContent());
		$('#rmbPrice').validatebox({required: false});
		$('#binkePrice').validatebox({required: false});
		
		if($('#rmb').attr('checked')){
			$('#rmbPrice').validatebox({required: true});
			
			if($('#binke').attr('checked')){
				$('#binkePrice').validatebox({required: true});
			}
		}else if($('#binke').attr('checked')){
			$('#binkePrice').validatebox({required: true});
			
			if($('#rmb').attr('checked')){
				$('#rmbPrice').validatebox({required: true});
			}
		}else{
			alert("至少选择一种售卖形式");
			return;
		}
		$('#fm').form('submit',{
			url: 'create',
			onSubmit: function(){  
	            return $(this).form('validate');  
	        }, 
			success: function(result){
				if(eval('('+result+')').categoryId){
					var node = $('#tt2').tree('find',eval('('+result+')').categoryId);
					var fullParent = getFullCategory(node);
					alert(fullParent+'类别中已存在该排序值');
					return;
				}
				alert(eval('('+result+')').msg);
			},
			error:function(result){
				alert('error');
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
	function selectCategory(){
		var nodes = $('#tt2').tree('getChecked');
		if(nodes){
			if(nodes.length == 0 ){
				alert("请选择一个类别");
			}else{
				if($('#tt2').tree('isLeaf', $('#tt2').tree('getRoot').target)){
					alert("当前还没有类别，请先添加类别");
					return;
				}
				for(var i = 0; i < nodes.length; i++){
					var node = nodes[i];
					var categoryId = node.id;
					if(map.containsKey(categoryId)){
						var category = getFullCategory(node);
						alert('类别\"' + category + '\"已存在');
						return;
					}
				}
				var status='<select name="status" style="width:50px"><option value="ON">上架</option><option value="OFF">下架</option></select>';
				var timeParam = Math.round(new Date().getTime()/1000);
				//$('#selectCategorys').html('<table border="0" id="categoryTable"><tr><td width="auto">商品类别</td><td width="auto">上下架</td><td width="auto">类别排序</td>	<td width="auto">操作</td></tr></table>');
				for(var i = 0; i < nodes.length; i++){
					var displaySort = '<input name="displaySort" type="text" style="width:50px" ';
					var node = nodes[i];
					var categoryId = node.id;
					map.put(categoryId, '');
					var deleteBut='<button type="button" onclick="deleteCategory(this,\''+categoryId+'\')">删除</button>';
					var categId = '<input type="hidden" name="categId" value='+ categoryId +' />';
					var category = getFullCategory(node);
					var displaySortId = 'displaySort'+timeParam+i;
					displaySort += ' id="' + displaySortId +'"/>';
					
					var str = '<tr>';
					str += '<td  width="auto">' + categId +category +'</td><td  width="auto">'+status +'</td><td  width="auto">' + displaySort +'</td><td width="auto">'+deleteBut+'</td>';
					str += '</tr>';
					$(str).appendTo($('#categoryTable'));
					$('#' + displaySortId).numberbox({precision:0});
					$('#' + displaySortId).validatebox({required:true});
				}
				$('#dd').dialog('close');
				
			}
			
		}
	}
	function deleteCategory(butt, cateId){
		map.remove(cateId);
		butt.parentNode.parentNode.parentNode.removeChild(butt.parentNode.parentNode);
	}
	//返回完整的类别名称
	function getFullCategory(node){
		var str = "";
		if(node){
			var parent = $('#tt2').tree('getParent', node.target);
			str = node.text;
			while(parent){
				if(parent.id==$('#tt2').tree('getRoot').id){
					break;
				}
				str = parent.text +"/" + str;
				parent = $('#tt2').tree('getParent', parent.target);
			}
			return str;
		}else{
			return str;
		}
	}
	function deleteInputFile(name, id, spanId){
		$('#'+spanId).html("<input type=file name="+ name +" id="+ id +" accept=image/* onchange=check(this,'"+spanId+"') />");
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
				if($('#imageSessionName_dataForm').val() == ''){
					initImageSession(result.imageSessionName);
					
				}
				$('#'+aimageId).attr('href','javascript:show(\''+baseURL+'/archive/imageShow?path=MERCHANDISE_IMAGE_BUFFER&contentType='+result.contentType+'&fileName='+result.url+'\',\''+result.width+'\',\''+result.height+'\')');
				$('#'+imageId).attr('src',baseURL+'/archive/showGetthumbPic?path=MERCHANDISE_IMAGE_BUFFER&contentType='+result.contentType+'&fileName='+result.url);
				style="display:none";
		    	document.getElementById(divPreviewId).style.display = "";
			}
		}); 
	}
	function deleteImage(keyId, divPreviewId){ 
		$.ajax({
			url:baseURL+'/archive/deleteImage',
			data:'imageSessionName='+$('#imageSessionName_dataForm').val()+'&key='+$('#'+keyId).val(),
			dataType:'json',
			async:false,
			success:function(data){
			}
		});
		style="display:none";
    	document.getElementById(divPreviewId).style.display = "none";
	}
	function initImageSession(imageSessionName){
		$('#imageSessionName_dataForm').val(imageSessionName);
		$('#imageSessionName_image0').val(imageSessionName);
		$('#imageSessionName_image1').val(imageSessionName);		
		$('#imageSessionName_image2').val(imageSessionName);
		$('#imageSessionName_image3').val(imageSessionName);
		$('#imageSessionName_image4').val(imageSessionName);
		$('#imageSessionName_image5').val(imageSessionName);
		$('#imageSessionName_image6').val(imageSessionName);
	}
	function openAddIamgeDialog(){
		for(var i =0; i < length; i++){
			var name;
			if(i==0){
				name = 'overview';
			}else{
				name = 'others';
			}
			deleteInputFile(name,'file'+i,'span'+i);
		}
		$('#addImage').dialog('center');
		$('#addImage').dialog('open');
	}
	function closeAddIamgeDialog(){
		$('#addImage').dialog('close');
	}
</script>

</head>
<body>
	<div class="easyui-tabs" style="">
	    <div title="商品基本信息" style="padding:20px;">
			<form action="create" method="post" id="fm"> 
				<table border="0">
					<tr>
						<td>
							<fieldset style="font-size: 14px;width:1100px;height:auto;">
								<legend style="color: blue;">商品信息</legend>
									<table border="0">
										<tr>
											<td width="20px"><span style="color: red;">*</span></td>
											<td width="120px">商品编号：</td>
											<td width="200px" align="left">
												<input name="id" type="hidden" value="${merchandise.id }">
												<input type="hidden" name="imageSessionName" id="imageSessionName_dataForm" value="${imageSessionName }" />
												<input id="code" name="code" value="${merchandise.code }" type="text" style="width:150px" class="easyui-validatebox" data-options="required:true" maxlength="20"/> 
											</td>
											<td width="20px"><span style="color: red;">*</span></td>
											<td width="80px">商品名称：</td>
											<td width="200px" align="left">
												<input id="name" name="name" value="${merchandise.name }" type="text" style="width:150px" class="easyui-validatebox" data-options="required:true" maxlength="20"> 
											</td>
											<td></td>
										</tr>
										<tr>
											<td width="20px"><span style="color: red;">*</span></td>
											<td width="120px">型号：</td>
											<td width="200px" align="left">
												<input id="model" name="model" value="${merchandise.model }" type="text" style="width:150px" class="easyui-validatebox" data-options="required:true"  maxlength="20"> 
											</td>
											<td width="20px"><span style="color: red;">*</span></td>
											<td width="80px">采购价：</td>
											<td width="200px" align="left">
												<input type="hidden" name="parent.id" id="parentId"> 
												<input id="purchasePrice" name="purchasePrice" value="${merchandise.purchasePrice }" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.01,precision:2,required:true"  maxlength="20"> 
											</td>
											<td></td>
										</tr>
										<tr>
											<td width="20px"><span style="color: red;">*</span></td>
											<td width="120px">商品介绍：</td>
											<td align="left" colspan="5">
												<textarea id="description"></textarea>
												<input type="hidden" name="description" id="description2">
											</td>
										</tr>
									</table>
							</fieldset>
						</td>
					</tr>
					<tr>
						<td>
							<fieldset style="font-size: 14px;width:1100px;;height:auto;">
								<legend style="color: blue;">售卖形式</legend>
								<c:choose>
									<c:when test="${(!empty catalogs) && (fn:length(catalogs) > 0)}">
										<table border="0">
											<c:if test="${fn:length(catalogs) == 2 }">
												<c:forEach items="${catalogs }" var="catalog">
													<c:choose>
														<c:when test="${catalog.unitId =='0' }">
															<tr>
																<td width="20px"><input type="checkbox" name="rmb" id="rmb" checked="checked" /></td>
																<td width="80px">正常售卖：</td>
																<td width="200px">
																		<input id="rmbPrice" name="rmbPrice" value="${catalog.price }" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.01,precision:2"  maxlength="20">&nbsp;元<input type="hidden" name="rmbUnitId" value="0"> 
																</td>
															</tr>
														</c:when>
														<c:otherwise>
															<tr>
																<td width="20px"><input type="checkbox" name="binke" id="binke" checked="checked"/></td>
																<td width="80px">积分兑换：</td>
																<td width="200px">
																		<input id="binkePrice" name="binkePrice" value="${catalog.price }" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.01,precision:2"  maxlength="20">&nbsp;缤刻<input type="hidden" name="binkeUnitId" value="1"> 
																</td>
															</tr>			
														</c:otherwise>
													</c:choose>										
												</c:forEach>
											</c:if>
											<c:if test="${fn:length(catalogs) == 1 }">
												<c:forEach items="${catalogs }" var="catalog">
													<c:choose>
														<c:when test="${catalog.unitId =='0' }">
															<tr>
																<td width="20px"><input type="checkbox" name="rmb" id="rmb" checked="checked" /></td>
																<td width="80px">正常售卖：</td>
																<td width="200px">
																		<input id="rmbPrice" name="rmbPrice" value="${catalog.price }" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.01,precision:2" maxlength="20">&nbsp;元<input type="hidden" name="rmbUnitId" value="0"> 
																</td>
															</tr>
															<tr>
																<td width="20px"><input type="checkbox" name="binke" id="binke"/></td>
																<td width="80px">积分兑换：</td>
																<td width="200px">
																		<input id="binkePrice" name="binkePrice" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.01,precision:2" maxlength="20">&nbsp;缤刻<input type="hidden" name="binkeUnitId" value="1"> 
																</td>
															</tr>
														</c:when>
														<c:otherwise>
															<tr>
																<td width="20px"><input type="checkbox" name="rmb" id="rmb" /></td>
																<td width="80px">正常售卖：</td>
																<td width="200px">
																		<input id="rmbPrice" name="rmbPrice" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.01,precision:2" maxlength="20">&nbsp;元<input type="hidden" name="rmbUnitId" value="0"> 
																</td>
															</tr>
															<tr>
																<td width="20px"><input type="checkbox" name="binke" id="binke" checked="checked"/></td>
																<td width="80px">积分兑换：</td>
																<td width="200px">
																		<input id="binkePrice" name="binkePrice" value="${catalog.price }" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.01,precision:2" maxlength="20">&nbsp;缤刻<input type="hidden" name="binkeUnitId" value="1"> 
																</td>
															</tr>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</c:if>
										</table>
									</c:when>
									<c:otherwise>
										<table border="0">
											<tr>
												<td width="20px"><input type="checkbox" name="rmb" id="rmb" /></td>
												<td width="80px">正常售卖：</td>
												<td width="200px">
														<input id="rmbPrice" name="rmbPrice" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.001,precision:2" maxlength="20">&nbsp;元<input type="hidden" name="rmbUnitId" value="0"> 
												</td>
											</tr>
											<tr>
												<td width="20px"><input type="checkbox" name="binke" id="binke"/></td>
												<td width="80px">积分兑换：</td>
												<td width="200px">
														<input id="binkePrice" name="binkePrice" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.001,precision:2" maxlength="20">&nbsp;缤刻<input type="hidden" name="binkeUnitId" value="1"> 
												</td>
											</tr>
										</table>		
									</c:otherwise>
								</c:choose>
								
							</fieldset>
						</td>
					</tr>
					<tr>
						<td width="1200px">
							<fieldset style="font-size: 14px;width:1100px;height:auto;">
								<legend style="color: blue;">商品类别选择</legend>
								<table border="0">
									<tr>
										<td width="20px"></td>
										<td><button type="button" onclick="openDialog()">选择类别</button></td>
									</tr>
									<tr>
										<td width="20px"></td>
										<td>
											<div id="selectCategorys">
													
											</div>
										</td>
									</tr>
								</table>
							</fieldset>
						</td>
					</tr>
					<tr>
						<td align="center"><button type="button" onclick="doSubmit()">保存</button></td>
					</tr>
				</table>
			</form>
	    </div>
	    <div title="图片维护" style="padding:20px;text-align:center;">
	    	<table>
	    		<tr>
	    			<td>
	    				<table>
	    					<tr><td align="center">基本图片</td></tr>
				    		<tr>
				    			<td align="center" style="height:120px;">
				    				<div id="divPreview0" >
										<a id="aimage0" href=""><img id="image0" src=""></a><br>
										<button type="button"  onclick="deleteImage('key0','divPreview0')">删除</button>
									</div>
				    			</td>
				    		</tr>			
	    				</table>
	    			</td>
	    			<td>
	    				<table>
	    					<tr><td align="center">图片一</td></tr>
				    		<tr>
				    			<td align="center" style="height:120px;">
				    				<div id="divPreview1" >
										<a id="aimage1" href=""><img id="image1" src=""></a><br>
										<button type="button"  onclick="deleteImage('key1','divPreview1')">删除</button>
									</div>
				    			</td>
				    		</tr>			
	    				</table>
	    			</td>
	    			<td>
	    				<table>
	    					<tr><td align="center">图片二</td></tr>
				    		<tr>
				    			<td align="center" style="height:120px;">
				    				<div id="divPreview2" >
										<a id="aimage2" href=""><img id="image2" src=""></a><br>
										<button type="button"  onclick="deleteImage('key2','divPreview2')">删除</button>
									</div>
				    			</td>
				    		</tr>			
	    				</table>
	    			</td>
	    			<td>
	    				<table>
	    					<tr><td align="center">图片三</td></tr>
				    		<tr>
				    			<td align="center" style="height:120px;">
				    				<div id="divPreview3" >
										<a id="aimage3" href=""><img id="image3" src=""></a><br>
										<button type="button"  onclick="deleteImage('key3','divPreview3')">删除</button>
									</div>
				    			</td>
				    		</tr>			
	    				</table>
	    			</td>
	    			<td>
	    				<table>
	    					<tr><td align="center">图片四</td></tr>
				    		<tr>
				    			<td align="center" style="height:120px;">
				    				<div id="divPreview4" >
										<a id="aimage4" href=""><img id="image4" src=""></a><br>
										<button type="button"  onclick="deleteImage('key4','divPreview4')">删除</button>
									</div>
				    			</td>
				    		</tr>			
	    				</table>
	    			</td>
	    			<td>
	    				<table>
	    					<tr><td align="center">图片五</td></tr>
				    		<tr>
				    			<td align="center" style="height:120px;">
				    				<div id="divPreview5" >
										<a id="aimage5" href=""><img id="image5" src=""></a><br>
										<button type="button"  onclick="deleteImage('key5','divPreview5')">删除</button>
									</div>
				    			</td>
				    		</tr>			
	    				</table>
	    			</td>
	    			<td>
	    				<table>
	    					<tr><td align="center">图片六</td></tr>
				    		<tr>
				    			<td align="center" style="height:120px;">
				    				<div id="divPreview6" >
										<a id="aimage6" href=""><img id="image6" src=""></a><br>
										<button type="button"  onclick="deleteImage('key6','divPreview6')">删除</button>
									</div>
				    			</td>
				    		</tr>			
	    				</table>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td align="center" colspan="6">
	    				<button type="button" onclick="openAddIamgeDialog()">上传图片</button>
	    			</td>
	    		</tr>
	    	</table>
	    	<div id="addImage" class="easyui-dialog" title="上传图片" style="width:600px;height:400px;"  
		        data-options="resizable:true,modal:true,inline:false,closed:true">
			        <div style="text-align:center;">
			        	<table>
							<tr>
								<td>
									<form action="imageUpload" id="fm0" method="post" enctype="multipart/form-data">
								    	<table>
									    	<tr>
												<td width="20px"></td>
												<td width="80px">基本图片：</td>
												<td width="200px" align="left">
													<input type="hidden" name="path" value="MERCHANDISE_IMAGE_BUFFER">
													<input type="hidden" name="key" id="key0" />
													<input type="hidden" name="imageSessionName" id="imageSessionName_image0" value="${imageSessionName }">
													<span id="span0">
														<input type="file" name="overview" id="file0" accept="image/*" onchange="check(this,'span0')">
													</span>
												</td>
												<td align="right">&nbsp;&nbsp;<button type="button"  onclick="uploadImage('file0','fm0', 'key0', 'aimage0','image0','divPreview0')">上传</button></td>
											</tr>
										</table>
									</form>
								</td>
							</tr>
							<tr>
								<td>
									<form action="imageUpload" id="fm1" method="post" enctype="multipart/form-data">
								    	<table>
									    	<tr>
												<td width="20px"></td>
												<td width="80px">图片一：</td>
												<td width="200px" align="left">
													<input type="hidden" name="path" value="MERCHANDISE_IMAGE_BUFFER">
													<input type="hidden" name="key" id="key1" />
													<input type="hidden" name="imageSessionName" id="imageSessionName_image1" value="${imageSessionName }">
													<span id="span1">
														<input type="file" name="others" id="file1" accept="image/*" onchange="check(this,'span1')">
													</span>
												</td>
												<td align="right">&nbsp;&nbsp;<button type="button"  onclick="uploadImage('file1','fm1', 'key1', 'aimage1','image1','divPreview1')">上传</button></td>
											</tr>
										</table>
									</form>
								</td>
							</tr>
							<tr>
								<td>
									<form action="imageUpload" id="fm2" method="post" enctype="multipart/form-data">
								    	<table>
									    	<tr>
												<td width="20px"></td>
												<td width="80px">图片二：</td>
												<td width="200px" align="left">
													<input type="hidden" name="path" value="MERCHANDISE_IMAGE_BUFFER">
													<input type="hidden" name="key" id="key2" />
													<input type="hidden" name="imageSessionName" id="imageSessionName_image2" value="${imageSessionName }">
													<span id="span2">
														<input type="file" name="others" id="file2" accept="image/*" onchange="check(this,'span2')">
													</span>
												</td>
												<td align="right">&nbsp;&nbsp;<button type="button"  onclick="uploadImage('file2','fm2', 'key2', 'aimage2','image2','divPreview2')">上传</button></td>
											</tr>
										</table>
									</form>
								</td>
							</tr>	
							<tr>
								<td>
									<form action="imageUpload" id="fm3" method="post" enctype="multipart/form-data">
								    	<table>
									    	<tr>
												<td width="20px"></td>
												<td width="80px">图片三：</td>
												<td width="200px" align="left">
													<input type="hidden" name="path" value="MERCHANDISE_IMAGE_BUFFER">
													<input type="hidden" name="key" id="key3" />
													<input type="hidden" name="imageSessionName" id="imageSessionName_image3" value="${imageSessionName }">
													<span id="span3">
														<input type="file" name="others" id="file3" accept="image/*" onchange="check(this,'span3')">
													</span>
												</td>
												<td align="right">&nbsp;&nbsp;<button type="button"  onclick="uploadImage('file3','fm3', 'key3', 'aimage3','image3','divPreview3')">上传</button></td>
											</tr>	
										</table>
									</form>
								</td>
							</tr>	
							<tr>
								<td>
									<form action="imageUpload" id="fm4" method="post" enctype="multipart/form-data">
								    	<table>
									    	<tr>
												<td width="20px"></td>
												<td width="80px">图片四：</td>
												<td width="200px" align="left">
													<input type="hidden" name="path" value="MERCHANDISE_IMAGE_BUFFER">
													<input type="hidden" name="key" id="key4" />
													<input type="hidden" name="imageSessionName" id="imageSessionName_image4" value="${imageSessionName }">
													<span id="span4">
														<input type="file" name="others" id="file4" accept="image/*" onchange="check(this,'span4')">
													</span>
												</td>
												<td align="right">&nbsp;&nbsp;<button type="button"  onclick="uploadImage('file4','fm4', 'key4', 'aimage4','image4','divPreview4')">上传</button></td>
											</tr>	
										</table>
									</form>
								</td>
							</tr>	
							<tr>
								<td>
									<form action="imageUpload" id="fm5" method="post" enctype="multipart/form-data">
								    	<table>
									    	<tr>
												<td width="20px"></td>
												<td width="80px">图片五：</td>
												<td width="200px" align="left">
													<input type="hidden" name="path" value="MERCHANDISE_IMAGE_BUFFER">
													<input type="hidden" name="key" id="key5" />
													<input type="hidden" name="imageSessionName" id="imageSessionName_image5" value="${imageSessionName }">
													<span id="span5">
														<input type="file" name="others" id="file5" accept="image/*" onchange="check(this,'span5')">
													</span>
												</td>
												<td align="right">&nbsp;&nbsp;<button type="button"  onclick="uploadImage('file5','fm5', 'key5', 'aimage5','image5','divPreview5')">上传</button></td>
											</tr>	
										</table>
									</form>
								</td>
							</tr>
							<tr>
								<td>
									<form action="imageUpload" id="fm6" method="post" enctype="multipart/form-data">
								    	<table>
									    	<tr>
												<td width="20px"></td>
												<td width="80px">图片六：</td>
												<td width="200px" align="left">
													<input type="hidden" name="path" value="MERCHANDISE_IMAGE_BUFFER">
													<input type="hidden" name="key" id="key6" />
													<input type="hidden" name="imageSessionName" id="imageSessionName_image6" value="${imageSessionName }">
													<span id="span6">
														<input type="file" name="others" id="file6" accept="image/*" onchange="check(this,'span6')">
													</span>
												</td>
												<td align="right">&nbsp;&nbsp;<button type="button"  onclick="uploadImage('file6','fm6', 'key6', 'aimage6','image6','divPreview6')">上传</button></td>
											</tr>	
										</table>
									</form>
								</td>
							</tr>	
							<tr>
								<td align="right">
									<button type="button" onclick="closeAddIamgeDialog()">关闭</button>
								</td>
							</tr>	        		
			        	</table>
		        </div>
			</div>
		    <div id="divDialog">
		    	<iframe scrolling="auto" id='openIframe' name="openIframe" frameborder="0"  src="" style="width:100%;height:100%;overflow: hidden;"></iframe> 
			</div>
	    </div>
	</div>
	<div id="imageDia" class="easyui-dialog" title="图片预览" style="width:400px;height:400px;"  
        data-options="iconCls:'icon-add',resizable:true,modal:true,inline:false,closed:true">
	        <div style="text-align:center;">
	        	<img id="perviewImage" name="perviewImage" src="" />
	        </div>
	</div> 
	<div id="dd" class="easyui-dialog" title="选择商品类别" style="width:400px;height:400px;"  
        data-options="resizable:false,modal:true,closed:true">  
        <ul id="tt2" class="easyui-tree" style="margin-top:10px;margin-left:20px;" data-options="url:'<%=request.getContextPath()%>/category/get_tree_nodes2',checkbox:true,onlyLeafCheck:true"></ul>
        <div style="position: absolute;top: 350px;left:250px;text-align:right;">
        	<button type="button" onclick="selectCategory()">确定</button>&nbsp;&nbsp;&nbsp;&nbsp;<button type="button" onclick="javascript:$('#dd').dialog('close');">关闭</button>
        </div>
	</div> 
</body>
</html>