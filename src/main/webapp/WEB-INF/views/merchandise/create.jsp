<%@ page language="java" contentType="text/html; charset=utf8"
	pageEncoding="utf8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.json-2.4.js"></script>
	<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/ueditor/editor_all.js"></script>
<script type="text/javascript">
		var baseURL = '<%=request.getContextPath()%>';
		var editor;
		var length = 7;
		var fileArray = new Array();
		var formArray = new Array();
		var keyArray = new Array();
		var aimageArray = new Array();
		var imageArray = new Array();
		var divPreviewArray = new Array();
	
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
			//parent.parent.dialog("预览图片",url,width,height);
		}
	$(function() {
		editor= new UE.ui.Editor();
		editor.render('description');
		editor.ready(function(){
		    //需要ready后执行，否则可能报错
		    editor.setContent("");
		});
		style="display:none";
		for(var i = 0; i < length; i++){
			document.getElementById('divPreview'+i).style.display = "none";
			fileArray[i]='file'+i;
			formArray[i]='fm'+i;
			keyArray[i]='key'+i;
			aimageArray[i]='aimage'+i;
			imageArray[i]='image'+i;
			divPreviewArray[i] = 'divPreview'+i;
		}
	});
	
	$(document).ready(function() { 
	});
	
	function doSubmit(){
		$('#description2').val(editor.getContent());
		/* if($('#overview').val()==''){
			alert("请选择基本图片");
			return;
		} */
		if($('#binke').attr('checked')){
			$('#binkePrice').validatebox({required: true});
		}
		if($('#rmb').attr('checked')){
			$('#rmbPrice').validatebox({required: true});
		}
		if($('#rmbPreferential').attr('checked')){
			$('#rmbPreferentialPrice').validatebox({required: true});
		}
		if($('#binkePreferential').attr('checked')){
			$('#binkePreferentialPrice').validatebox({required: true});
		}
		if(!$('#rmb').attr('checked') && !$('#binke').attr('checked')){
			alert("至少选择一种售卖形式");
			return;
		}
		$('#fm').form('submit',{
			url: 'create',
			onSubmit: function(){  
	            return $(this).form('validate');  
	        }, 
			success: function(result){
				result = eval('('+result+')'); 
				$('#_id').val(result.id);
				alert(result.msg);
			},
			error:function(result){
				alert('error');
			}
		});
	}
	function checkCheckbox(obj, priceId){
		if(obj.checked){
			$('#'+priceId).validatebox({required: true});
		}else{
			$('#'+priceId).validatebox({required: false});
		}
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
		/* $('#tt2').tree({
			url:baseURL+'/category'+'/get_tree_nodes2',
			checkbox:true,
			onlyLeafCheck:true
		}); */
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
				/* if(nodes[0].id = $('#tt2').tree('getRoot').id && $('#tt2').tree('isLeaf', nodes[0].target)){
				} */
				var status='<select name="status" style="width:50px"><option value="ON">上架</option><option value="OFF">下架</option></select>';
				var deleteBut='<button type="button" onclick="deleteCategory(this)">删除</button>';
				//var categoryTable = '';
				var timeParam = Math.round(new Date().getTime()/1000);
				$('#selectCategorys').html('<table border="0" id="categoryTable"><tr><td width="auto"  align="center">商品类别</td><td width="auto" align="center">上下架</td><td width="auto" align="center">类别排序</td><td width="auto" align="center">上下架时间</td><td width="auto" align="center">操作</td></tr></table>');
				for(var i = 0; i < nodes.length; i++){
					var displaySort = '<input name="displaySort" type="text" style="width:50px" ';
					var node = nodes[i];
					var categoryId = node.id;
					var categId = '<input type="hidden" name="categId" value='+ categoryId +' />';
					var category = getFullCategory(node);
					var displaySortId = 'displaySort'+timeParam+i;
					displaySort += ' id="' + displaySortId +'"/>';
					
					var str = '<tr>';
					str += '<td  width="auto">' + categId +category +'</td><td  width="auto">'+status +'</td><td  width="auto">' + displaySort +'</td><td width="auto"><input type=text name=on_offTime readonly="readonly" value=\"'+getCurrentTime()+'\" /></td><td width="auto">'+deleteBut+'</td>';
					str += '</tr>';
					$(str).appendTo($('#categoryTable'));
					$('#' + displaySortId).numberbox({precision:0});
					$('#' + displaySortId).validatebox({required:true});
				}
				$('#dd').dialog('close');
				
			}
			
		}
	}
	function getCurrentTime(){
		var currentTime = new Date();
		var year = currentTime.getFullYear();
		var month = currentTime.getMonth() + 1 < 10 ? "0" + (currentTime.getMonth() + 1) : currentTime.getMonth() + 1;
		var date = currentTime.getDate() < 10 ? "0" + currentTime.getDate() : currentTime.getDate();
		var hour = currentTime.getHours()<10?"0"+currentTime.getHours() : currentTime.getHours();
		var minute = currentTime.getMinutes()<10? "0" +currentTime.getMinutes(): currentTime.getMinutes();
		var second = currentTime.getSeconds() <10?"0"+currentTime.getSeconds():currentTime.getSeconds();
		return year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second;
	}
	function deleteCategory(butt){
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
				if($('#imageSessionName_dataForm').val() == ""){
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
			data:'imageSessionName='+$('#imageSessionName_imageForm').val()+'&key='+$('#'+keyId).val(),
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
	function addBrandDialog(){
		$('#brandDialog').dialog('center');
		$('#brandDialog').dialog('open');
	}
	function selectBrand(){
		var row = $('#tt').datagrid('getSelected');
		if(row){
			$('#brandId').val(row.id);
			$('#brandName').html(row.name);
			$('#brandDialog').dialog('close');
		}else{
			alert("请选择一个品牌");
			return;
		}
	}
	function doSearch(){  
		    $('#tt').datagrid('load',{  
		    	name:$('#brandSearchName').val() 
		    });  
		}
</script>

</head>
<body>
	<div class="easyui-tabs" style="">
	    <div title="商品基本信息" style="padding:20px;">
			<form action="create" method="post" id="fm"> 
				<table border="0">
					<tr>
						<td width="1200px">
							<fieldset style="font-size: 14px;width:1100px;height:auto;">
								<legend style="color: blue;">商品信息</legend>
									<table border="0">
										<tr>
											<td width="20px"><span style="color: red;">*</span></td>
											<td width="120px">商品编号：</td>
											<td width="200px" align="left">
												<input type="hidden" name="id" id="_id" />
												<input type="hidden" name="imageSessionName" id="imageSessionName_dataForm" />
												<input id="code" name="code" type="text" style="width:150px" class="easyui-validatebox" data-options="required:true" maxlength="20"/> 
											</td>
											<td width="20px"><span style="color: red;">*</span></td>
											<td width="80px">商品名称：</td>
											<td width="200px" align="left">
												<input id="name" name="name" type="text" style="width:150px" class="easyui-validatebox" data-options="required:true" maxlength="20"> 
											</td>
											<td></td>
										</tr>
										<tr>
											<td width="20px"><span style="color: red;">*</span></td>
											<td width="120px">型号：</td>
											<td width="200px" align="left">
												<input id="model" name="model" type="text" style="width:150px" class="easyui-validatebox" data-options="required:true" maxlength="20"> 
											</td>
											<td width="20px"><span style="color: red;">*</span></td>
											<td width="80px">采购价：</td>
											<td width="200px" align="left">
												<input type="hidden" name="parent.id" id="parentId"> 
												<input id="purchasePrice" name="purchasePrice" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.01,precision:2,required:true" maxlength="10"> 
											</td>
											<td ></td>
										</tr>
										<tr>
											<td width="20px"><span style="color: red;">*</span></td>
											<td width="80px">运费：</td>
											<td width="200px" align="left">
												<input id="freight" name="freight" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.01,precision:2,required:true" maxlength="10">  
											</td>
											<td ></td>
											<td ></td>
											<td ></td>
											<td ></td>
										</tr>
										<tr>
											<td width="20px"><span style="color: red;">*</span></td>
											<td width="120px">商品介绍：</td>
											<td align="left" colspan="5">
		<!-- 										<div id="description" style="width:200px;"></div> -->
												<textarea id="description" ></textarea>
												<input type="hidden" name="description" id="description2">
											</td>
										</tr>
									</table>
							</fieldset>
						</td>
					</tr>
					<tr>
						<td width="1200px">
							<fieldset style="font-size: 14px;width:1100px;height:auto;">
								<legend style="color: blue;">售卖形式</legend>
								<table border="0">
									<tr>
										<td width="20px"><input type="checkbox" name="rmb" id="rmb" onclick="checkCheckbox(this, 'rmbPrice')"/></td>
										<td width="80px">正常售卖：</td>
										<td width="200px">
												<input id="rmbPrice" name="rmbPrice" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.01,precision:2" maxlength="10">&nbsp;元<input type="hidden" name="rmbUnitId" value="0"> 
										</td>
										<td width="20px"></td>
										<td width="20px"><input type="checkbox" name="rmbPreferential" id="rmbPreferential" onclick="checkCheckbox(this,'rmbPreferentialPrice')" /></td>
										<td width="120px">优惠价格：</td>
										<td width="200px">
												<input id="rmbPreferentialPrice" name="rmbPreferentialPrice" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.01,precision:2" maxlength="10">&nbsp;元 
										</td>
									</tr>
									<tr>
										<td width="20px"><input type="checkbox" name="binke" id="binke" onclick="checkCheckbox(this,'binkePrice')"/></td>
										<td width="80px">积分兑换：</td>
										<td width="200px">
												<input id="binkePrice" name="binkePrice" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.01,precision:2" maxlength="10">&nbsp;缤刻<input type="hidden" name="binkeUnitId" value="1"> 
										</td>
										<td width="20px"></td>
										<td width="20px"><input type="checkbox" name="binkePreferential" id="binkePreferential" onclick="checkCheckbox(this,'binkePreferentialPrice')" /></td>
										<td width="120px">优惠兑换积分：</td>
										<td width="200px">
												<input id="binkePreferentialPrice" name="binkePreferentialPrice" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.01,precision:2" maxlength="10">&nbsp;缤刻
										</td>
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
						<td width="1200px">
							<fieldset style="font-size: 14px;width:1100px;height:auto;">
								<legend style="color: blue;">所属品牌</legend>
								<table border="0">
									<tr>
										<td width="20px"></td>
										<td width="100px">品牌名称：</td>
										<td width="150px" align="left">
											<input id="brandId" name="brand.id" type="hidden">
											<span id="brandName" ></span>
										</td>
										<td><button type="button" onclick="addBrandDialog()">选择</button></td>
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
													<input type="hidden" name="imageSessionName" id="imageSessionName_image0">
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
													<input type="hidden" name="imageSessionName" id="imageSessionName_image1">
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
													<input type="hidden" name="imageSessionName" id="imageSessionName_image2">
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
													<input type="hidden" name="imageSessionName" id="imageSessionName_image3">
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
													<input type="hidden" name="imageSessionName" id="imageSessionName_image4">
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
													<input type="hidden" name="imageSessionName" id="imageSessionName_image5">
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
													<input type="hidden" name="imageSessionName" id="imageSessionName_image6">
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
	<div id="dd" class="easyui-dialog" title="选择商品类别" style="width:400px;height:400px;"  
        data-options="resizable:false,modal:true,closed:true">  
        <ul id="tt2" class="easyui-tree" style="margin-top:10px;margin-left:20px;" data-options="url:'<%=request.getContextPath()%>/category/get_tree_nodes2',checkbox:true,onlyLeafCheck:true"></ul>
        <div style="position: absolute;top: 350px;left:250px;text-align:right;">
        	<button type="button" onclick="selectCategory()">确定</button>&nbsp;&nbsp;&nbsp;&nbsp;<button type="button" onclick="javascript:$('#dd').dialog('close');">关闭</button>
        </div>
	</div>
	<div id="brandDialog" class="easyui-dialog" title="新增商品-品牌选择" style="width:600px;height:550px;text-align:center;"  
        data-options="resizable:false,modal:true,closed:true">  
        <table border="0">
			<tr>
				<td>
					<fieldset style="font-size: 14px;width:auto;height:auto;">
						<legend style="color: blue;">查询条件</legend>
						<form action="" >
							<table border="0">
								<tr>
									<td width="140px">品牌名称：</td>
									<td width="200px" align="left">
										<input id="brandSearchName" type="text" style="width:150px"/> 
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
						<table id="tt" class="easyui-datagrid" width="100%" height="100%" 
	           				url="<%=request.getContextPath()%>/brand/list" rownumbers="true" pagination="true" singleSelect="true">   
	       					<thead>  
	           				<tr>  
		           				<th field="id" checkbox="true"></th> 
		                		<th data-options="field:'name',width:100">品牌名称</th>
		                		<th data-options="field:'companyName',width:100">公司名称</th>
		                		<th data-options="field:'createdAt',width:120,formatter:function(v,r,i){return dateFormat(v);}" >创建时间</th>
				           	</tr>  
					       	</thead>  
				   		</table> 
					</fieldset>
				</td>
			</tr>
		</table>
        <div style="position: absolute;top: 500px;left:450px;text-align:right;">
        	<button type="button" onclick="selectBrand()">确定</button>&nbsp;&nbsp;&nbsp;&nbsp;<button type="button" onclick="javascript:$('#brandDialog').dialog('close');">关闭</button>
        </div>
	</div>
	<div id="imageDia" class="easyui-dialog" title="图片预览" style="width:400px;height:400px;"  
		        data-options="iconCls:'icon-add',resizable:true,modal:true,inline:false,closed:true">
			        <div style="text-align:center;">
			        	<img id="perviewImage" name="perviewImage" src="" />
			        </div>
	</div> 
</body>
</html>