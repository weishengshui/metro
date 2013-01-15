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
	
		
		function show(url,width,height){
			parent.parent.dialog("预览图片",url,width,height);
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
			deleteImage(path.name, path.id, spanId);
		}else{
			//ShowImage(path);
		}
	}
	function openDialog(){
		$('#dd').dialog('center');
		$('#dd').dialog('open');
	}
	function ShowImage(obj){
	    var suffix=obj.value.substring(obj.value.lastIndexOf(".")+1).toLowerCase();            
	    var browserVersion= window.navigator.userAgent.toUpperCase();//浏览器版本信息           
	    
        if (window.navigator.appName=="Microsoft Internet Explorer"){//ie浏览器
            if(browserVersion.indexOf("MSIE 6.0")>-1){//ie6
                $("#imgHeadPhoto").attr("src",obj.value);
            }else{//ie7、ie8、ie9未测试
                //滤镜实现
                obj.select();
                var newPreview =document.getElementById("divNewPreview");
                if(newPreview==null){
                    newPreview =document.createElement("div");
                    newPreview.setAttribute("id","divNewPreview");
                    newPreview.style.width = 160;
                    newPreview.style.height = 170;
                    newPreview.style.border="solid 1px #d2e2e2";
                }
                newPreview.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='scale',src='" + document.selection.createRange().text + "')";                            
                $("#divPreview").attr("style","display:none");
                $("#divPreview").after(newPreview);
                //$("#divNewPreview").attr("style","display:''");
//                $('#divNewPreview').attr('style','display');
                style="display:none";
                document.getElementById('divNewPreview').style.display = "";
                /* document.getElementById('divNewPreview').style.display = "none";
                document.getElementById('divNewPreview').style.display = ""; */
             }
        }else if(browserVersion.indexOf("FIREFOX")>-1){ //火狐浏览器
        	$('#imgHeadPhoto').attr('width','160px');
    		$('#imgHeadPhoto').attr('height','170px');
            var firefoxVersion= parseFloat(window.navigator.userAgent.toLowerCase().match(/firefox\/([\d.]+)/)[1]);
            if(firefoxVersion<7){//firefox7.0以下版本
                $("#imgHeadPhoto").attr("src", obj.files[0].getAsDataURL());
            }else{//火狐7.0以上版本
                $("#imgHeadPhoto").attr("src", window.URL.createObjectURL(obj.files[0]));
            }
        }else if(obj.files){      
        	$('#imgHeadPhoto').attr('width','160px');
    		$('#imgHeadPhoto').attr('height','170px');
            //兼容chrome等，也可以兼容火狐，通过HTML5来获取路径                   
            if(typeof FileReader !== "undefined"){
                var reader = new FileReader(); 
                reader.onload = function(e){$("#imgHeadPhoto").attr("src",e.target.result);}  
                reader.readAsDataURL(obj.files[0]);
            }else if($.browser.safari){
                alert("暂时不支持Safari浏览器!");
            }
        }else{
        	$('#imgHeadPhoto').attr('width','160px');
    		$('#imgHeadPhoto').attr('height','170px');
            $("#imgHeadPhoto").attr("src",obj.value);//其他
        }         
	}
	function uploadImage(){
		if($('#logo2').val()==""){
			alert("请先添加图片");
			return ;
		}
		$('#fm2').form('submit',{
			success:function(result){
				/* alert($.toJSON(result));*/
				result = eval('('+result+')');
				//$('#imgHeadPhoto').attr('src','showGetthumbPic?fileName='+result.url);
				var width = 250;
				var height = 270;
				$('#aimage1').attr('href','javascript:show(\'brand/imageShow?fileName='+result.url+'\',\''+width+'\',\''+height+'\')');
				$('#image1').attr('src','showGetthumbPic?fileName='+result.url);
			}
		}); 
	}
	function imageDelete(){
		$('#span1').html("<input type=file name=logo2 id=logo2 accept=image/* onchange=check(this,'span1') />");
		$('#imgHeadPhoto').attr('width','0px');
		$('#imgHeadPhoto').attr('height','0px');
		var newPreview =document.getElementById("divNewPreview");
        if(newPreview!=null){
        	style="display:none";
        	document.getElementById('divPreview').style.display = "";
        	document.getElementById('divNewPreview').style.display = "none";
			// $('#divNewPreview').attr('style','display:none');// ie
        }
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
		$('#'+spanId).html("<input type=file name="+name+" id=logo2 accept=image/* onchange=check(this,'span1') />");
	}
</script>

</head>
<body>
	<div class="easyui-tabs" style="">
	    <div title="品牌信息新增" style="padding:20px;">
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
							<input type="hidden" id="imageUrl" name="imageUrl"/>
							<div id="divPreview" >
<!-- 								<img id="imgHeadPhoto" src=""/> -->
								<a id="aimage1" href="javascript:show('line/shopPicShow?path=${path }&fileName=${file.url }',250,270)"><img id="image1" src="showGetthumbPic?path=${path }&fileName=${file.url }"></a>
							</div>
						</td>
						<td width="200px" align="left">
							<span id="span1">
								<input type="file" name="logo2" id="logo2" accept="image/*" onchange="check(this,'span1')">
							</span>
						</td>
						<td width="80px">&nbsp;&nbsp;<button type="button"  onclick="uploadImage()">上传</button></td>
						<td width="80px">&nbsp;&nbsp;<button type="button"  onclick="imageDelete()">删除</button></td>
	<!-- 					<td width="80px">&nbsp;&nbsp;<button type="button"  onclick="previewImage('logo')">预览</button></td> -->
					</tr>
			    </table>
		    </form>
	    </div>
    </div>
</body>
</html>