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
	src="<%=request.getContextPath()%>/js/jquery/ajaxfileupload.js"></script>

<script type="text/javascript">
		var baseURL = '<%=request.getContextPath()%>';
	    $(function(){
	    });
</script>

</head>
<body>
	
	<form action="addSave" method="post" id="messageForm" target="suc"  enctype="multipart/form-data">  
	 
		<table  border="0" style="width: 500px;">
	       <tr>
				<td><span style="color: red;">*</span>任务名称:</td>
				<td>
				  <input id="taskName" name="taskName" type="text" style="width:150px" class="easyui-validatebox" data-options="required:true" /> 
				</td>
			</tr>
			
			<tr>
				<td><span style="color: red;">*</span>短信内容:</td>
				<td><textarea name="content" id="overFont" style="height: 80px; width: 380px;"  data-options="required:true" onKeyUp="javascript:checkWord(160,event)" onfocus="cancelCheckC()"></textarea></td>
			</tr>
			<tr>
				<td>
				    <input type="radio" name="sendType" value="1"/ checked="checked"> 立即发送
				</td>
			</tr>
			<tr>
				<td>
				    <input type="radio" name="sendType" value="2"/> 计划发送时间 
				</td>
			    <td>
			      <input id="planSendDate" name="planSendDate" type="text" style="width:150px" class="easyui-datetimebox" editable="false" />
			    </td>
			</tr>
			<tr>
				<td>
				     <span style="color: red;">*</span> 接受号码：
				</td>
			    <td>
			      <input type="file" name="telephoneFile" id="telephoneFile" style="width:150px"  onchange="check(this)" class="easyui-validatebox"  onfocus="cancelCheckF()">
			    </td>
			</tr>
			
			<tr>
				<td align="center" colspan="4">
				   <a id="btn" href="javascript:void(0)" onclick="doSubmit()" class="easyui-linkbutton" data-options="iconCls:'icon-add'">保存</a>
				</td>
			</tr>
		</table>
	  </fieldset>
	</form>
	<div id="fileDia" class="easyui-dialog" title="文件预览" style="width:400px;height:400px;"  
        data-options="resizable:true,modal:true,inline:false,closed:true">
	        <div style="text-align:center;">
	        	<img id="perviewFile" name="perviewFile" src="" />
	        </div>
	</div> 
	
<script type="text/javascript">
			String.prototype.trim = function() 
			{ 
			   return this.replace(/(^\s*)|(\s*$)/g, ""); 
			}
			function checkWord(len,evt){
				var overFont=document.getElementById("overFont");
				if(evt==null)
				{
				  evt = window.event;
				}
				var src = evt.srcElement? evt.srcElement : evt.target; 
				var str=src.value;
				myLen=0;
				i=0;
				otherLen=len;
				for(;(i<str.length)&&(myLen<=len);i++){
				  if(str.charCodeAt(i)>0&&str.charCodeAt(i)<128||str.charCodeAt(i)==160)
				{
			     	myLen++;
				}
				 else
				{
					myLen+=2;
					otherLen=len-20;
				}
				}
		
				if(myLen>len){
					alert("您输入超过限定长度");
					src.value=str.substring(0,i-1);
				}
		}
			
		function cancelCheckC(){
				$("#checkC").remove();
				
	   }
		function cancelCheckF(){
			$("#checkT").remove();
			
   }
	    function doSubmit(){
	    	var con=$("#overFont").val();
	    	if (con==null||con.length<1){
	    		$("#overFont").after("<tr  id='checkC'><td style='color: red;'>短信内容不能为空！</td></tr>");
	    		return;
	    	}
	    	var tf=$("#telephoneFile").val();
	    	if(tf==null||tf.length<1){
	    		$("#telephoneFile").after("<tr id='checkT'><td style='color: red;'>请上传号码文件！</td></tr>");
	    		return;
	    	}
			$('#messageForm').form('submit',{
				success:function(result){
					alert(eval('('+result+')').msg);
				    location.replace(location);
				}
			}); 
		}
	    
	    function check(path){
			var filepath=path.value;
			filepath=filepath.substring(filepath.lastIndexOf('.')+ 1,filepath.length);
			filepath = filepath.toLocaleLowerCase();
			if(filepath != 'csv' ){
				alert("只能上传csv 格式的文件");
				path.value="";
			}
		}
		function openDialog(){
			$('#dd').dialog('center');
			$('#dd').dialog('open');
		}
		function previewFile(fileId){
	    	
			if($('#'+fileId).val()==''){
				alert("请先添加文件");
				return;
			}
			var width, height;
			width = '346px';
			height = '346px';
			var input = document.getElementById(fileId);
			var imgPre = document.getElementById('perviewFile');
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
		                    $('#perviewFile').attr('src', e.target.result);
		                    $('#perviewFile').attr('width', width);
		                    $('#perviewFile').attr('height', height);
		                };
		                reader.readAsDataURL(input.files[0]);
		        } 
			}
			$('#fileDia').resizable({  
			    maxWidth:20,  
			    maxHeight:100  
			}); 
			$('#fileDia').dialog('center');
			$('#fileDia').dialog('open');
		}
		function deleteFile(fileId){
			$('#'+fileId).val('');
		}
</script>
	
</body>
</html>