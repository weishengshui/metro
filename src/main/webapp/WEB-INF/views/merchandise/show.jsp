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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/map.js"></script>
<script type="text/javascript">
		var baseURL = '<%=request.getContextPath()%>';
		var editor;
		var map = new Map();
		
	$(function() {
		editor= new UE.ui.Editor();
		editor.render('description');
		editor.ready(function(){
		    //需要ready后执行，否则可能报错
		    editor.setContent('${merchandise.description }');
		});
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
	
	function check(path,spanId){ // 图片是作为整体修改的
		//必须先添加基本图片，才能添加其它图片，确保图片信息的完整性
		if(path.name != 'overview'){
			var overview = document.getElementById('overview');
			if(overview.value == ''){
				deleteImage(path.name, path.id, spanId);
				alert("请先添加基本图片");
				return;
			}
		}
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
		if(imageId == 'overview'){
			width = '346px';
			height = '346px';
		}else{
			width = '146px';
			height = '146px';
		}
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
	function deleteImage(imageId){
		$('#'+imageId).val('');
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
	
	function deleteImage(name, id, spanId){ // 清空input type=file 直接$('#'+imageId).val('');有浏览器不兼容的问题
		//$('#'+imageId).val('');
		$('#'+spanId).html("<input type=file name="+name+" id="+id+" accept=image/* onchange=check(this,'"+spanId+"') />");
	}
</script>

</head>
<body>
	<form action="create" method="post" id="fm" enctype="multipart/form-data"> 
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
										<input id="code" name="code" value="${merchandise.code }" type="text" style="width:150px" class="easyui-validatebox" data-options="required:true" readonly="readonly"/> 
									</td>
									<td width="20px"><span style="color: red;">*</span></td>
									<td width="80px">商品名称：</td>
									<td width="200px" align="left">
										<input id="name" name="name" value="${merchandise.name }" type="text" style="width:150px" class="easyui-validatebox" data-options="required:true"> 
									</td>
									<td></td>
								</tr>
								<tr>
									<td width="20px"><span style="color: red;">*</span></td>
									<td width="120px">型号：</td>
									<td width="200px" align="left">
										<input id="model" name="model" value="${merchandise.model }" type="text" style="width:150px" class="easyui-validatebox" data-options="required:true" readonly="readonly"> 
									</td>
									<td width="20px"><span style="color: red;">*</span></td>
									<td width="80px">采购价：</td>
									<td width="200px" align="left">
										<input type="hidden" name="parent.id" id="parentId"> 
										<input id="purchasePrice" name="purchasePrice" value="${merchandise.purchasePrice }" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.01,precision:2,required:true"> 
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
																<input id="rmbPrice" name="rmbPrice" value="${catalog.price }" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.01,precision:2">&nbsp;元<input type="hidden" name="rmbUnitId" value="0"> 
														</td>
													</tr>
												</c:when>
												<c:otherwise>
													<tr>
														<td width="20px"><input type="checkbox" name="binke" id="binke" checked="checked"/></td>
														<td width="80px">积分兑换：</td>
														<td width="200px">
																<input id="binkePrice" name="binkePrice" value="${catalog.price }" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.01,precision:2">&nbsp;缤刻<input type="hidden" name="binkeUnitId" value="1"> 
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
																<input id="rmbPrice" name="rmbPrice" value="${catalog.price }" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.01,precision:2">&nbsp;元<input type="hidden" name="rmbUnitId" value="0"> 
														</td>
													</tr>
													<tr>
														<td width="20px"><input type="checkbox" name="binke" id="binke"/></td>
														<td width="80px">积分兑换：</td>
														<td width="200px">
																<input id="binkePrice" name="binkePrice" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.01,precision:2">&nbsp;缤刻<input type="hidden" name="binkeUnitId" value="1"> 
														</td>
													</tr>
												</c:when>
												<c:otherwise>
													<tr>
														<td width="20px"><input type="checkbox" name="rmb" id="rmb" /></td>
														<td width="80px">正常售卖：</td>
														<td width="200px">
																<input id="rmbPrice" name="rmbPrice" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.01,precision:2">&nbsp;元<input type="hidden" name="rmbUnitId" value="0"> 
														</td>
													</tr>
													<tr>
														<td width="20px"><input type="checkbox" name="binke" id="binke" checked="checked"/></td>
														<td width="80px">积分兑换：</td>
														<td width="200px">
																<input id="binkePrice" name="binkePrice" value="${catalog.price }" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.01,precision:2">&nbsp;缤刻<input type="hidden" name="binkeUnitId" value="1"> 
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
												<input id="rmbPrice" name="rmbPrice" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.001,precision:2">&nbsp;元<input type="hidden" name="rmbUnitId" value="0"> 
										</td>
									</tr>
									<tr>
										<td width="20px"><input type="checkbox" name="binke" id="binke"/></td>
										<td width="80px">积分兑换：</td>
										<td width="200px">
												<input id="binkePrice" name="binkePrice" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.001,precision:2">&nbsp;缤刻<input type="hidden" name="binkeUnitId" value="1"> 
										</td>
									</tr>
								</table>		
							</c:otherwise>
						</c:choose>
						
					</fieldset>
				</td>
			</tr>
			<tr>
				<td>
					<fieldset style="font-size: 14px;width:1100px;;height:auto;">
						<legend style="color: blue;">商品图片</legend>
						<table border="0">
							<tr>
								<td width="20px"></td>
								<td width="80px">基本图片：</td>
								<td width="200px" align="left">
									<span id="span0">
										<input type="file" name="overview" id="overview" accept="image/*" onchange="check(this,'span0')">
									</span>
								</td>
								<td width="80px">&nbsp;&nbsp;<button type="button"  onclick="previewImage('overview')">预览</button> </td>
								<td width="80px"><button type="button" onclick="deleteImage('overview', 'overview', 'span0')">删除</button></td>
							</tr>
							<tr>
								<td width="20px"></td>
								<td width="80px">图一：</td>
								<td width="200px" align="left">
									<span id="span1">
										<input type="file" name="others" id="image1" accept="image/*" onchange="check(this,'span1')">
									</span>
								</td>
								<td width="80px">&nbsp;&nbsp;<button type="button" onclick="previewImage('image1')">预览</button> </td>
								<td width="80px"><button type="button" onclick="deleteImage('image1', 'image1', 'span1')">删除</button></td>
							</tr>
							<tr>
								<td width="20px"></td>
								<td width="80px">图二：</td>
								<td width="200px" align="left">
									<span id="span2">
										<input type="file" name="others" id="image2" accept="image/*" onchange="check(this,'span2')">
									</span>
								</td>
								<td width="80px">&nbsp;&nbsp;<button type="button" onclick="previewImage('image2')">预览</button> </td>
								<td width="80px"><button type="button" onclick="deleteImage('image2', 'image2', 'span2')">删除</button></td>
							</tr>
							<tr>
								<td width="20px"></td>
								<td width="80px">图三：</td>
								<td width="200px" align="left">
									<span id="span3">
										<input type="file" name="others" id="image3" accept="image/*" onchange="check(this,'span3')">
									</span>
								</td>
								<td width="80px">&nbsp;&nbsp;<button type="button" onclick="previewImage('image3')">预览</button> </td>
								<td width="80px"><button type="button" onclick="deleteImage('image3', 'image3', 'span3')">删除</button> </td>
							</tr>
							<tr>
								<td width="20px"></td>
								<td width="80px">图四：</td>
								<td width="200px" align="left">
									<span id="span4">
										<input type="file" name="others" id="image4" accept="image/*" onchange="check(this,'span4')">
									</span>
								</td>
								<td width="80px">&nbsp;&nbsp;<button type="button" onclick="previewImage('image4')">预览</button> </td>
								<td width="80px"><button type="button" onclick="deleteImage('image4', 'image4', 'span4')">删除</button></td>
							</tr>
							<tr>
								<td width="20px"></td>
								<td width="80px">图五：</td>
								<td width="200px" align="left">
									<span id="span5">
										<input type="file" name="others" id="image5" accept="image/*" onchange="check(this,'span5')">
									</span>
								</td>
								<td width="80px">&nbsp;&nbsp;<button type="button" onclick="previewImage('image5')">预览</button> </td>
								<td width="80px"><button type="button" onclick="deleteImage('image5', 'image5', 'span5')">删除</button></td>
							</tr>
							<tr>
								<td width="20px"></td>
								<td width="80px">图六：</td>
								<td width="200px" align="left">
									<span id="span6">
										<input type="file" name="others" id="image6" accept="image/*" onchange="check(this,'span6')">
									</span>
								</td>
								<td width="80px">&nbsp;&nbsp;<button type="button" onclick="previewImage('image6')">预览</button> </td>
								<td width="80px"><button type="button" onclick="deleteImage('image6', 'image6', 'span6')">删除</button></td>
							</tr>
						</table>
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