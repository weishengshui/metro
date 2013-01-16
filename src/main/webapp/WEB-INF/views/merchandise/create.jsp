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
	<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/ueditor/editor_all.js"></script>
<script type="text/javascript">
		var baseURL = '<%=request.getContextPath()%>';
		var editor;
		
	$(function() {
		editor= new UE.ui.Editor();
		editor.render('description');
		editor.ready(function(){
		    //需要ready后执行，否则可能报错
		    editor.setContent("");
		});
	});
	
	$(document).ready(function() { 
	});
	
	function doSubmit(){
		$('#description2').val(editor.getContent());
		/* if($('#overview').val()==''){
			alert("请选择基本图片");
			return;
		} */
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
				result = eval('('+result+')'); 
				if(result.categoryId){
					var node = $('#tt2').tree('find',eval('('+result+')').categoryId);
					var fullParent = getFullCategory(node);
					alert('\"'+fullParent+'\"类别中已存在该排序值');
					return;
				}
				$('#_id').val(result.id);
				alert(result.msg);
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
		/* $('#tt2').tree({
			url:baseURL+'/category'+'/get_tree_nodes2',
			checkbox:true,
			onlyLeafCheck:true
		}); */
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
	function deleteImage(name, id, spanId){ // 清空input type=file 直接$('#'+imageId).val('');有浏览器不兼容的问题
		//$('#'+imageId).val('');
		$('#'+spanId).html("<input type=file name="+name+" id="+id+" accept=image/* onchange=check(this,'"+spanId+"') />");
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
				$('#selectCategorys').html('<table border="0" id="categoryTable"><tr><td width="auto">商品类别</td><td width="auto">上下架</td><td width="auto">类别排序</td>	<td width="auto">操作</td></tr></table>');
				for(var i = 0; i < nodes.length; i++){
					var displaySort = '<input name="displaySort" type="text" style="width:50px" ';
					var node = nodes[i];
					var categoryId = node.id;
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
		$('#'+spanId).html("<input type=file name="+ name +" id=logo2"+ id +" accept=image/* onchange=check(this,'"+spanId+"') />");
	}
	function uploadImage(formId,imageId,aimageId,imagePreId,divPreviewId){
		if($('#'+imageId).val()==""){
			alert("请先添加图片");
			return ;
		}
		$('#'+formId).form('submit',{
			success:function(result){
				result = eval('('+result+')');
				initImageSession(result.imageSessionName);
				$('#'+aimageId).attr('href','javascript:show(\'brand/imageShow?path=BRAND_IMAGE_BUFFER&contentType='+result.contentType+'&fileName='+result.url+'\',\''+result.width+'\',\''+result.height+'\')');
				$('#'+imagePreId).attr('src','showGetthumbPic?path=BRAND_IMAGE_BUFFER&contentType='+result.contentType+'&fileName='+result.url);
				style="display:none";
		    	document.getElementById(divPreviewId).style.display = "";
			}
		}); 
	}
	function deleteImage(imageId, divPreviewId){ // 清空input type=file 直接$('#'+imageId).val('');有浏览器不兼容的问题
		$.ajax({
			url:'deleteImage',
			data:'imageSessionName='+$('#imageSessionName_dataForm').val()+'&imageId='+imageId,
			dataType:'json',
			async:false,
			success:function(data){
				style="display:none";
		    	document.getElementById(divPreviewId).style.display = "none";
			}
		});
	}
	function initImageSession(imageSessionName){
		$('#imageSessionName_dataForm').val(imageSessionName);
		$('#imageSessionName_overview').val(imageSessionName);
		$('#imageSessionName_image1').val(imageSessionName);		
		$('#imageSessionName_image2').val(imageSessionName);
		$('#imageSessionName_image3').val(imageSessionName);
		$('#imageSessionName_image4').val(imageSessionName);
		$('#imageSessionName_image5').val(imageSessionName);
		$('#imageSessionName_image6').val(imageSessionName);
	}
</script>

</head>
<body>
	<div class="easyui-tabs" style="">
	    <div title="商品基本信息" style="padding:20px;">
			<form action="create" method="post" id="fm" enctype="multipart/form-data"> 
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
												<input id="code" name="code" type="text" style="width:150px" class="easyui-validatebox" data-options="required:true" /> 
											</td>
											<td width="20px"><span style="color: red;">*</span></td>
											<td width="80px">商品名称：</td>
											<td width="200px" align="left">
												<input id="name" name="name" type="text" style="width:150px" class="easyui-validatebox" data-options="required:true"> 
											</td>
											<td></td>
										</tr>
										<tr>
											<td width="20px"><span style="color: red;">*</span></td>
											<td width="120px">型号：</td>
											<td width="200px" align="left">
												<input id="model" name="model" type="text" style="width:150px" class="easyui-validatebox" data-options="required:true"> 
											</td>
											<td width="20px"><span style="color: red;">*</span></td>
											<td width="80px">采购价：</td>
											<td width="200px" align="left">
												<input type="hidden" name="parent.id" id="parentId"> 
												<input id="purchasePrice" name="purchasePrice" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.01,precision:2,required:true"> 
											</td>
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
										<td width="20px"><input type="checkbox" name="rmb" id="rmb" /></td>
										<td width="80px">正常售卖：</td>
										<td width="200px">
												<input id="rmbPrice" name="rmbPrice" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.01,precision:2">&nbsp;元<input type="hidden" name="rmbUnitId" value="0"> 
										</td>
									</tr>
									<tr>
										<td width="20px"><input type="checkbox" name="binke" id="binke"/></td>
										<td width="80px">积分兑换：</td>
										<td width="200px">
												<input id="binkePrice" name="binkePrice" type="text" style="width:150px" class="easyui-numberbox" data-options="min:0.01,precision:2">&nbsp;缤刻<input type="hidden" name="binkeUnitId" value="1"> 
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
						<td align="center"><button type="button" onclick="doSubmit()">保存</button></td>
					</tr>
				</table>
			</form>
	    </div>
	    <div title="图片维护" style="padding:20px;">
		    <table>
		    	<tr>
		    		<td>
		    			<form action="imageUpload" id="fm0" method="post" enctype="multipart/form-data">
					    	<table>
						    	<tr>
									<td width="20px"></td>
									<td width="80px">基本图片：</td>
									<td>
										<input type="hidden" name="imageSessionName" id="imageSessionName_overview">
										<div id="divPreview0" >
											<a id="aimage0" href=""><img id="imagePre0" src=""></a>
										</div>
									</td>
									<td width="200px" align="left">
										<span id="span0">
											<input type="file" name="overview" id="overview" accept="image/*" onchange="check(this,'span0')">
										</span>
									</td>
									<td width="80px">&nbsp;&nbsp;<button type="button"  onclick="uploadImage('fm0','overview','aimage0','imagePre0','divPreview0')">上传</button></td>
									<td width="80px">&nbsp;&nbsp;<button type="button"  onclick="deleteImage('overview', 'divPreview0')">删除</button></td>
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
									<td>
										<input type="hidden" name="imageSessionName" id="imageSessionName_image1">
										<div id="divPreview1" >
											<a id="aimage1" href=""><img id="imagePre1" src=""></a>
										</div>
									</td>
									<td width="200px" align="left">
										<span id="span1">
											<input type="file" name="image1" id="image1" accept="image/*" onchange="check(this,'span1')">
										</span>
									</td>
									<td width="80px">&nbsp;&nbsp;<button type="button"  onclick="uploadImage('fm1','image1','aimage1','imagePre1','divPreview1')">上传</button></td>
									<td width="80px">&nbsp;&nbsp;<button type="button"  onclick="deleteImage('image1', 'divPreview1')">删除</button></td>
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
									<td>
										<input type="hidden" name="imageSessionName" id="imageSessionName_image2">
										<div id="divPreview2" >
											<a id="aimage2" href=""><img id="imagePre2" src=""></a>
										</div>
									</td>
									<td width="200px" align="left">
										<span id="span2">
											<input type="file" name="image2" id="image2" accept="image/*" onchange="check(this,'span2')">
										</span>
									</td>
									<td width="80px">&nbsp;&nbsp;<button type="button"  onclick="uploadImage('fm2','image2','aimage1','imagePre2','divPreview2')">上传</button></td>
									<td width="80px">&nbsp;&nbsp;<button type="button"  onclick="deleteImage('image2', 'divPreview2')">删除</button></td>
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
									<td>
										<input type="hidden" name="imageSessionName" id="imageSessionName_image3">
										<div id="divPreview3" >
											<a id="aimage3" href=""><img id="imagePre3" src=""></a>
										</div>
									</td>
									<td width="200px" align="left">
										<span id="span3">
											<input type="file" name="image3" id="image3" accept="image/*" onchange="check(this,'span3')">
										</span>
									</td>
									<td width="80px">&nbsp;&nbsp;<button type="button"  onclick="uploadImage('fm3','image3','aimage3','imagePre3','divPreview3')">上传</button></td>
									<td width="80px">&nbsp;&nbsp;<button type="button"  onclick="deleteImage('image3', 'divPreview3')">删除</button></td>
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
									<td>
										<input type="hidden" name="imageSessionName" id="imageSessionName_image4">
										<div id="divPreview4" >
											<a id="aimage4" href=""><img id="imagePre4" src=""></a>
										</div>
									</td>
									<td width="200px" align="left">
										<span id="span4">
											<input type="file" name="image4" id="image4" accept="image/*" onchange="check(this,'span4')">
										</span>
									</td>
									<td width="80px">&nbsp;&nbsp;<button type="button"  onclick="uploadImage('fm4','image4','aimage4','imagePre4','divPreview4')">上传</button></td>
									<td width="80px">&nbsp;&nbsp;<button type="button"  onclick="deleteImage('image4', 'divPreview4')">删除</button></td>
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
									<td>
										<input type="hidden" name="imageSessionName" id="imageSessionName_image5">
										<div id="divPreview5" >
											<a id="aimage5" href=""><img id="imagePre5" src=""></a>
										</div>
									</td>
									<td width="200px" align="left">
										<span id="span5">
											<input type="file" name="image5" id="image5" accept="image/*" onchange="check(this,'span5')">
										</span>
									</td>
									<td width="80px">&nbsp;&nbsp;<button type="button"  onclick="uploadImage('fm5','image5','aimage5','imagePre5','divPreview5')">上传</button></td>
									<td width="80px">&nbsp;&nbsp;<button type="button"  onclick="deleteImage('image5', 'divPreview5')">删除</button></td>
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
									<td>
										<input type="hidden" name="imageSessionName" id="imageSessionName_image6">
										<div id="divPreview6" >
											<a id="aimage6" href=""><img id="imagePre6" src=""></a>
										</div>
									</td>
									<td width="200px" align="left">
										<span id="span6">
											<input type="file" name="image6" id="image6" accept="image/*" onchange="check(this,'span6')">
										</span>
									</td>
									<td width="80px">&nbsp;&nbsp;<button type="button"  onclick="uploadImage('fm6','image6','aimage6','imagePre6','divPreview6')">上传</button></td>
									<td width="80px">&nbsp;&nbsp;<button type="button"  onclick="deleteImage('image6', 'divPreview6')">删除</button></td>
								</tr>
						    </table>
					    </form>
		    		</td>
		    	</tr>
		    </table>
	    </div>
	</div>
	<div id="dd" class="easyui-dialog" title="选择商品类别" style="width:400px;height:400px;"  
        data-options="resizable:false,modal:true,closed:true">  
        <ul id="tt2" class="easyui-tree" style="margin-top:10px;margin-left:20px;" data-options="url:'<%=request.getContextPath()%>/category/get_tree_nodes2',checkbox:true,onlyLeafCheck:true"></ul>
        <div style="position: absolute;top: 350px;left:250px;text-align:right;">
        	<button type="button" onclick="selectCategory()">确定</button>&nbsp;&nbsp;&nbsp;&nbsp;<button type="button" onclick="javascript:$('#dd').dialog('close');">关闭</button>
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