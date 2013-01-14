<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>上海地铁</title>
<link rel="stylesheet" type="text/css" href="js/jquery/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="js/jquery/themes/icon.css" />
<script type="text/javascript" src="js/jquery/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="js/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/base.js"></script>
<style type="text/css">
a{text-decoration: none;}
</style>
<script>
	
	function dialog(name,url,width,height){// 公用弹出框方法
		$("#openIframe").attr("src",url);
		$("#divDialog").dialog({
			height:height,
			width:width,
			modal:true,
			resizable:true,
			title:name
		});
	}
	
	function close(){
		$("#divDialog").dialog('close');
	}
	
	function getChild(){
		alert(window.frames['mainFrame'].length);
		if(window.frames['mainFrame'].document != null){
			return window.frames['mainFrame'];
		}else{
			return document.getElementById('mainFrame');
		}
	}
	function resetPassword(){
		$("#pform")[0].reset();
		$("#error").empty();
		$("#pdiv").dialog({
			modal:true,
			title:'修改密码'
		});
	}
	function updatePassword(){
		$("#error").empty();
		if($("#newpwd").val() =='' || $("#repwd").val()==''){
			$("#error").append("不能为空");
			return ;	
		}
		if($("#newpwd").val() != $("#repwd").val()){
			$("#error").append("输入密码不一致");
			$("#newpwd").focus();
			return ;
		}
		$.ajax({
			url:'user/validateOldPwd',
			data:'oldpwd='+$("#oldpwd").val()+"&newpwd="+$("#newpwd").val(),
			type:'post',
			async:false,
			success:function(data){
				if(data == ''){ 
					$("#error").append("旧密码输入错误");
				}else if(data == 'suc'){
					alert('修改成功');
					$("#pdiv").dialog('close');
				}
			}
		});
	}
</script>
</head>
<body class="easyui-layout">
	<div data-options="region:'north',border:false" style="height:68px;padding:0px;;">
		<table width="100%">
			<tr>
				<td><a href="">logo</a></td>
				<td align="right" valign="bottom">
					welcome <sec:authentication property="name" />&nbsp;&nbsp;
					<a href="javascript:void(0)" onclick="resetPassword()">修改密码</a>&nbsp;&nbsp;
					<a href="j_spring_security_logout">注销</a>&nbsp;&nbsp;	
				</td>
			</tr>
		</table>
	</div>
	
	<div class="west-menu" data-options="region:'west',split:true,title:'客户关系管理系统'" style="width:200px;padding:5px;">
		<ul class="easyui-tree" data-options="url:'user/findUserResources'"></ul> 
	</div>
	
	<div data-options="region:'south',border:false" style="height:20px;text-align: center;padding-top:4px;color:gray;font-size:12px;">
		技术支持：积享通信息有限公司
	</div>
	
	<div id="mainPanle" data-options="region:'center',title:'积享通'">
		 <div id="tabs" class="easyui-tabs"  fit="true" border="false" >
			<div title=" 首&nbsp;&nbsp;页 " style="padding:20px;overflow:hidden;" id="home">
				<h1>Welcome to platform</h1>
			</div>
		</div>
	</div>
	<div id="divDialog">
    	<iframe scrolling="auto" id='openIframe' name="openIframe" frameborder="0"  src="" style="width:100%;height:100%;overflow: hidden;"></iframe> 
	</div>
		<div style="display: none;">
		<div id="pdiv" style="width:280px;height: 200px;">
			<form id="pform">
			<table style="padding:20px;">
				<tr>
					<td>旧密码</td>
					<td><input type="password" id="oldpwd" /></td>
				</tr>
				<tr>
					<td>新密码</td>
					<td><input type="password" id="newpwd" /></td>
				</tr>
				<tr>
					<td>确认密码</td>
					<td><input type="password" id="repwd" /></td>
				</tr>
				<tr>
					<td colspan="2" align="right">
						<a id="btn" href="javascript:void(0)" onclick="updatePassword()" class="easyui-linkbutton">修改</a>
						<a id="btn" href="javascript:void(0)" onclick="$('#pdiv').dialog('close')" class="easyui-linkbutton">取消</a>
					</td>
				</tr>
				<tr><td colspan="2" align="center"><span id="error" style="color:red;font-size:12px;"></span></td></tr>
			</table>
			</form>
		</div>
	</div>
</body>
</html>