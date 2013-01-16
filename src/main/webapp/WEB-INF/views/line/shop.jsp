<%@page import="com.chinarewards.metro.core.common.Dictionary"%>
<%@ page language="java" contentType="text/html; charset=utf8" pageEncoding="utf8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%--"http://www.w3.org/TR/html4/loose.dtd" --%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery/themes/icon.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery.form.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/extend-easyui-validate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/PCASClass.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/constant.js"></script>
<style type="text/css">
	fieldset table tr{height: 35px;}
	fieldset table {font-size:14px}
	fieldset{margin-bottom:10px;margin:0px;width:600px;font-size:14px;}
	select{width:155px;height:20px;}
	.red{color:red;font-size:12px;}
</style>
<script type="text/javascript">
	function submits(){
		 $("#myform").ajaxSubmit({ 
			    type: "post",
			    dataType: "json",
			    beforeSubmit: function(formData, jqForm, options){
			    	if($("#activeDate").datebox('getValue') > $("#expireDate").datebox('getValue')){
				    	alert("请输入正确的有效时间");
				    	return false;
				    }
			    	var upl = $('#csv').val();
					if(upl != "" && $("#autoc").attr("checked")!='checked'){
						if(upl.toLowerCase().substring(upl.length-4,upl.length)!=".csv"){
							alert("文件格式不对，请选择CSV文件！");
							return false;
						}
					}
					var flag = false;
					if($('#myform').form('validate')){
						if($("#province").val()=='' || $("#city").val()=='' || $("#").val()==''){
				    		alert("请选择区域");
				    	}else{
				    		flag = true;
				    	}
			    	}
					return flag;
			    },success:  function (data) { 
			        $("#id").val(data);
			        alert("保存成功 ");
			   	}
		 }); 	
	}

	function checkMethod(n){
		if(n==1){
			$("#mspan").css("display","none");
			$("#myform").attr("action","saveShopWithOut");
		}else{
			$("#mspan").css("display","");
			$("#myform").attr("action","saveShop");
		}
	}
	function getId(){
		return $("#id").val();
	}
</script>
</head>
<body>
	<div class="easyui-tabs" style="">
	    <div title="门店信息新增" style="padding:20px;">
	        <form id="myform" method="post" action="saveShopWithOut" enctype="multipart/form-data">
	          <input type="hidden" value="${shop.id }" name="id" id="id" />
			  <fieldset style="font-size:14px;">
			  	<legend style="color: blue;">门店基本信息</legend>
			  	<table>
			  		<tr>
			  			<td><span class="red"></span></td>
			  			<td width="80">总编号：</td>
			  			<td width="200">
			  				<input id="name" type='text' value="${shop.num }" name='num' style="width:150px" maxlength="200"/>
			  			</td>
			  			<td width="80">&nbsp;&nbsp;网址：</td>
			  			<td>
			  				<input id="url" name="url" type="text" value="${shop.url }" style="width:150px" maxlength="200"/>
			  			</td>
			  		</tr>
			  		<tr>
			  			<td><span class="red">*</span></td>
			  			<td>门店名称：</td>
			  			<td>
			  				<input id="name" type='text' name='name' value="${shop.name }" style="width:150px" maxlength="200" class="easyui-validatebox" data-options="required:true"/>
			  			</td>
			  			<td><span class="red" style="margin-right:3px;">*</span>固定电话</td>
			  			<td>
			  				<input id="workPhone" name="workPhone" class="easyui-validatebox" data-options="validType:'phoneNumber',required:true" value="${shop.workPhone }" class="easyui-validatebox" data-options="required:true" maxlength="12" type='text' />
			  			</td>
			  		</tr>
			  		<tr>
			  			<td><span class="red">*</span></td>
			  			<td>门店地址：</td>
			  			<td>
			  				<input id="address" type='text' name='address' value="${shop.address }" style="width:150px" maxlength="200" class="easyui-validatebox" data-options="required:true" />
			  			</td>
			  			<td><span class="red" style="margin-right:3px;">*</span>区域：</td>
			  			<td>
			  				<select name="province" id="province" style="width:60px;"></select>
			  				<select name="city" id="city" style="width:60px;"></select>
			  				<select name="region" id="region" style="width:60px;"></select>
			  			</td>
			  		</tr>
			  		<tr>
			  			<td><span class="red">*</span></td>
			  			<td>联系人：</td>
			  			<td>
			  				<input id="linkman" type='text' name='linkman' value="${shop.linkman }" style="width:150px" maxlength="200"  class="easyui-validatebox" data-options="required:true" />
			  			</td>
			  			<td><span class="red" style="margin-right:3px;">*</span>电子邮件：</td>
			  			<td>
			  				<input id="email" name="email" type='text' value="${shop.email }" class="easyui-validatebox" data-options="required:true,validType:'email'"  />
			  			</td>
			  		</tr>
			  		<tr>
			  			<td><span class="red">*</span></td>
			  			<td>营业时间：</td>
			  			<td>
			  				<textarea name="businessHours" id="businessHours" rows="3" cols="15"  class="easyui-validatebox" data-options="required:true" >${shop.businessHours }</textarea>
			  			</td>
			  			<td>&nbsp;&nbsp;特色描述：</td>
			  			<td>
			  				<textarea id="features" name="features" rows="3" cols="15">${shop.features }</textarea>
			  			</td>
			  		</tr>
			  	</table>
			  </fieldset>
			  <br/>
			  <fieldset style="font-size:14px;">
			  	<legend style="color: blue;">优惠信息</legend>
			  	<table>
			  		<tr>
			  			<td></td>
			  			<td width="80">标题：</td>
			  			<td width="200">
			  				<Input name="privilegeTile" type="text" value="${shop.privilegeTile }" />
			  			</td>
			  		</tr>
			  		<tr>
			  			<td></td>
			  			<td width="80">描述：</td>
			  			<td width="200">
			  				<textarea name="privilegeDesc" rows="2" cols="52">${shop.privilegeDesc }</textarea>
			  			</td>
			  		</tr>
			  		<tr>
			  			<td></td>
			  			<td width="80">有效期：</td>
			  			<td width="200">
			  				<input type="text" name="activeDate" id="activeDate" value="${fn:substring(shop.activeDate,0,10)}" class="easyui-datebox" editable="false"/> 
			  				至
			  				<input type="text" name="expireDate" id="expireDate" value="${fn:substring(shop.expireDate,0,10)}" class="easyui-datebox" editable="false"/>
			  			</td>
			  		</tr>
			  	</table>
			  </fieldset>
			  <br/>
			  <fieldset style="font-size:14px;">
			  	<legend style="color: blue;">优惠码生成方式</legend>
			  	<table>
			  		<tr>
			  			<td></td>
			  			<td>
			  			  <label><input type="radio" id="autoc" name="discountModel" checked="checked" onclick="checkMethod(1)" value="<%=Dictionary.PROME_Code_AUTO %>" />系统生成</label>
			  			  <label><input type="radio" name="discountModel" ${shop.discountModel==1?'checked':'' } onclick="checkMethod(2)" value="<%=Dictionary.PROME_Code_IMPORT %>"/>文件导入</label>
			  			  <span style="display: none;" id="mspan">
			  			  	说明:<input type="text" name="note" style="width: 110px;" value="${shop.note }" />
			  			  	<input type="file" name="csv" id="csv" />
			  			  </span>
			  			</td>	
			  		</tr>
			  	</table>
			  </fieldset>
			  <br/>
			  <fieldset style="text-align: right;border: none;">
			  	<!-- 
				<a id="btn" href="javascript:void(0)" onclick="submit()" class="" data-options="iconCls:'icon-save'">保存</a>
				 -->
				<input type="button" onclick="submits()" value="保 存"/>
			  </fieldset>
			</form>
	    </div>  
	    <div title="图片维护">  
	        <iframe frameborder="0"  src="shopPic?shopId=${shop.id }" style="width:100%;height: 700px;" scrolling="auto" ></iframe>
	    </div>  
	    <div title="消费类型及POST机维护">  
	        <iframe frameborder="0"  src="typeAndPost?shopId=${shop.id }" style="width:100%;height: 700px;" scrolling="auto" ></iframe>
	    </div>  
	    <div title="所属站台维护">  
	        <iframe frameborder="0"  src="shopSite?id=${shop.siteId }&orderNo=${shop.orderNo}" style="width:100%;height: 700px;" scrolling="auto" ></iframe>
	    </div>  
	    <div title="品牌维护">  
	        <iframe frameborder="0"  src="shopBrand?shopId=${shop.id }" style="width:100%;height: 700px;" scrolling="auto" ></iframe>
	    </div> 
	    <div title="导入优惠码" style="padding:20px;">  
	        <iframe frameborder="0"  src="shopPromoCode?shopId=${shop.id }" style="width:100%;height: 700px;" scrolling="auto" ></iframe>
	    </div> 
	</div> 
</body>
<script type="text/javascript">
	for(var i=0;i<Constant.industry.length;i++){
		if(Constant.industry[i].id == '${member.industry}'){
			$("#industry").append("<option value="+Constant.industry[i].id+" selected='selected'>"+Constant.industry[i].text+"</option>");
		}else{
			$("#industry").append("<option value="+Constant.industry[i].id+">"+Constant.industry[i].text+"</option>");
		}
	}
	for(var i=0;i<Constant.profession.length;i++){
		if(Constant.profession[i].id == '${member.profession}'){
			$("#profession").append("<option value="+Constant.profession[i].id+" selected='selected'>"+Constant.profession[i].text+"</option>");
		}else{
			$("#profession").append("<option value="+Constant.profession[i].id+">"+Constant.profession[i].text+"</option>");
		}
	}
	for(var i=0;i<Constant.position.length;i++){
		if(Constant.position[i].id == '${member.position}'){
			$("#position").append("<option value="+Constant.position[i].id+" selected='selected'>"+Constant.position[i].text+"</option>");
		}else{
			$("#position").append("<option value="+Constant.position[i].id+">"+Constant.position[i].text+"</option>");
		}
	}
	for(var i=0;i<Constant.salary.length;i++){
		if(Constant.salary[i].id == '${member.salary}'){
			$("#salary").append("<option value="+Constant.salary[i].id+" selected='selected'>"+Constant.salary[i].text+"</option>");
		}else{
			$("#salary").append("<option value="+Constant.salary[i].id+">"+Constant.salary[i].text+"</option>");
		}
	}
	for(var i=0;i<Constant.education.length;i++){
		if(Constant.education[i].id == '${member.education}'){
			$("#education").append("<option value="+Constant.education[i].id+" selected='selected'>"+Constant.education[i].text+"</option>");
		}else{
			$("#education").append("<option value="+Constant.education[i].id+">"+Constant.education[i].text+"</option>");
		}
	}
	new PCAS("province","city","region","${shop.province}","${shop.city}","${shop.region}");
</script>
</html>