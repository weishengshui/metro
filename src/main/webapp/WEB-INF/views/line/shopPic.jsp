<%@page import="com.chinarewards.metro.core.common.Dictionary"%>
<%@ page language="java" contentType="text/html; charset=utf8" pageEncoding="utf8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html><%--"http://www.w3.org/TR/html4/loose.dtd" --%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery/themes/icon.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery.form.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/extend-easyui-validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/PCASClass.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/constant.js"></script>

<!-- Bootstrap CSS Toolkit styles -->
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css">
<!-- Generic page styles -->
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
<!-- Bootstrap styles for responsive website layout, supporting different screen sizes -->
<link rel="stylesheet" href="http://blueimp.github.com/cdn/css/bootstrap-responsive.min.css">
<!-- Bootstrap CSS fixes for IE6 -->
<!--[if lt IE 7]><link rel="stylesheet" href="http://blueimp.github.com/cdn/css/bootstrap-ie6.min.css"><![endif]-->
<!-- Bootstrap Image Gallery styles -->
<link rel="stylesheet" href="http://blueimp.github.com/Bootstrap-Image-Gallery/css/bootstrap-image-gallery.min.css">
<!-- CSS to style the file input field as button and adjust the Bootstrap progress bars -->
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery.fileupload-ui.css">
<!-- Shim to make HTML5 elements usable in older Internet Explorer versions -->
<!--[if lt IE 9]><script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
<script type="text/javascript">
	function show(url,width,height){
		parent.parent.dialog("查找图片",url,width,height);
	}
	function checks(){
		var id = parent.getId();
		
		if(id == 0){
			$("#startupload").attr("disabled",true);
			$(".btn-primary").attr("disabled",true);
		}else{
			$("#id").val(id);
			$("#startupload").attr("disabled",false);
			$(".btn-primary").attr("disabled",false);
		}
	}
</script>
</head>
<body style="padding-top: 20px;" onmouseover="checks()">
<div class="container" style="margin-left:10px;">
    <form id="fileupload" action="shopPicUpload" method="POST" enctype="multipart/form-data" >
    	<input type="hidden" name="id" id="id" />
        <div class="row fileupload-buttonbar" style="display: block;height: 30px;">
            <div class="span7">
                <span class="btn fileinput-button">
                    <i class="icon-plus icon-white"></i>
                    <span>添加图片</span>
                    <input type="file" name="files" id="aa" multiple >
                </span>
                <button type="button" class="btn start" id="startupload">
                    <i class="icon-upload icon-white"></i>
                    <span>开始上传</span>
                </button>
                <button type="reset" class="btn cancel">
                    <i class="icon-ban-circle icon-white"></i>
                    <span>取消上传</span>
                </button>
                <button type="button" class="btn delete">
                    <i class="icon-trash icon-white"></i>
                    <span>删除</span>
                </button>
                <input type="checkbox" class="toggle">
            </div>
            <div class="span5 fileupload-progress fade">
                <div class="progress progress-success progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
                    <div class="bar" style="width:0%;"></div>
                </div>
                <div class="progress-extended">&nbsp;</div>
            </div>
        </div>
        <!-- The loading indicator is shown during file processing -->
        <div class="fileupload-loading"></div>
        <br>
        <!-- The table listing the files available for upload/download -->
        <table role="presentation" class="table table-striped">
        	<tbody class="files" data-toggle="modal-gallery" data-target="#modal-gallery">
        		<c:forEach items="${list }" var="file">
        		<tr class="template-download fade in">
		            <td class="preview">
		                <a download="" rel="gallery" title="${file.originalFilename }" href="javascript:show('line/shopPicShow?path=${path }&fileName=${file.url }',250,270)"><img src="showGetthumbPic?path=${path }&fileName=${file.url }"></a>
		            </td>
		            <td class="name">
		                <a download="060828381f30e9240278c8d44c086e061c95f773.jpg" rel="gallery" title="060828381f30e9240278c8d44c086e061c95f773.jpg" href="javascript:show('line/shopPicShow?path=${path }&fileName=${file.url }',250,270)">${file.originalFilename }</a>
		            </td>
		            <td class="size"><span>756.08 KB</span></td>
		            <td colspan="2"></td>
		        
		        <td class="delete">
		            <button data-url="delShopPic?id=${file.id }&fileName=${file.url}" data-type="GET" class="btn btn-danger">
		                <i class="icon-trash icon-white"></i>
		                <span>删除</span>
		            </button>
		            <input type="checkbox" value="1" name="delete">
		        </td>
		    </tr>
		     </c:forEach>
        	</tbody>
        </table>
    </form>
</div>

<!-- The template to display files available for upload -->
<script id="template-upload" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-upload fade">
        <td class="preview"><span class="fade"></span></td>
        <td class="name"><span>{%=file.name%}</span></td>
        <td class="size"><span>{%=o.formatFileSize(file.size)%}</span></td>
        {% if (file.error) { %}
            <td class="error" colspan="2"><span class="label label-important">{%=locale.fileupload.error%}</span> {%=locale.fileupload.errors[file.error] || file.error%}</td>
        {% } else if (o.files.valid && !i) { %}
            <td>
                <div class="progress progress-success progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"><div class="bar" style="width:0%;"></div></div>
            </td>
            <td class="start">{% if (!o.options.autoUpload) { %}
                <button class="btn btn-primary">
                    <i class="icon-upload icon-white"></i>
                    <span>{%=locale.fileupload.start%}</span>
                </button>
            {% } %}</td>
        {% } else { %}
            <td colspan="2"></td>
        {% } %}
        <td class="cancel">{% if (!i) { %}
            <button class="btn btn-warning">
                <i class="icon-ban-circle icon-white"></i>
                <span>{%=locale.fileupload.cancel%}</span>
            </button>
        {% } %}</td>
    </tr>
{% } %}
</script>
<!-- The template to display files available for download -->
<script id="template-download" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-download fade">
        {% if (file.error) { %}
            <td></td>
            <td class="name"><span>{%=file.name%}</span></td>
            <td class="size"><span>{%=o.formatFileSize(file.size)%}</span></td>
            <td class="error" colspan="2"><span class="label label-important">{%=locale.fileupload.error%}</span> {%=locale.fileupload.errors[file.error] || file.error%}</td>
        {% } else { %}
            <td class="preview">{% if (file.thumbnail_url) { %}
                <a href="{%=file.url%}" title="{%=file.name%}" rel="gallery" download="{%=file.name%}"><img src="{%=file.thumbnail_url%}"></a>
            {% } %}</td>
            <td class="name">
                <a href="{%=file.url%}" title="{%=file.name%}" rel="{%=file.thumbnail_url&&'gallery'%}" download="{%=file.name%}">{%=file.name%}</a>
            </td>
            <td class="size"><span>{%=o.formatFileSize(file.size)%}</span></td>
            <td colspan="2"></td>
        {% } %}
        <td class="delete">
            <button class="btn btn-danger" data-type="{%=file.delete_type%}" data-url="{%=file.delete_url%}">
                <i class="icon-trash icon-white"></i>
                <span>{%=locale.fileupload.destroy%}</span>
            </button>
            <input type="checkbox" name="delete" value="1">
        </td>
    </tr>
{% } %}
</script>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
<!-- The jQuery UI widget factory, can be omitted if jQuery UI is already included -->
<script src="<%=request.getContextPath()%>/js/jquery/fileupload/vendor/jquery.ui.widget.js"></script>
<!-- The Templates plugin is included to render the upload/download listings -->
<script src="http://blueimp.github.com/JavaScript-Templates/tmpl.min.js"></script>
<!-- The Load Image plugin is included for the preview images and image resizing functionality -->
<script src="http://blueimp.github.com/JavaScript-Load-Image/load-image.min.js"></script>
<!-- The Canvas to Blob plugin is included for image resizing functionality -->
<script src="http://blueimp.github.com/JavaScript-Canvas-to-Blob/canvas-to-blob.min.js"></script>
<!-- Bootstrap JS and Bootstrap Image Gallery are not required, but included for the demo -->
<script src="http://blueimp.github.com/cdn/js/bootstrap.min.js"></script>
<script src="http://blueimp.github.com/Bootstrap-Image-Gallery/js/bootstrap-image-gallery.min.js"></script>
<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
<script src="<%=request.getContextPath()%>/js/jquery/fileupload/jquery.iframe-transport.js"></script>
<!-- The basic File Upload plugin -->
<script src="<%=request.getContextPath()%>/js/jquery/fileupload/jquery.fileupload.js"></script>
<!-- The File Upload file processing plugin -->
<script src="<%=request.getContextPath()%>/js/jquery/fileupload/jquery.fileupload-fp.js"></script>
<!-- The File Upload user interface plugin -->
<script src="<%=request.getContextPath()%>/js/jquery/fileupload/jquery.fileupload-ui.js"></script>
<!-- The localization script -->
<script src="<%=request.getContextPath()%>/js/jquery/fileupload/locale.js"></script>
<!-- The main application script -->
<script src="<%=request.getContextPath()%>/js/jquery/fileupload/main.js"></script>
<!-- The XDomainRequest Transport is included for cross-domain file deletion for IE8+ -->
<!--[if gte IE 8]><script src="js/cors/jquery.xdr-transport.js"></script><![endif]-->
</body>
</html>