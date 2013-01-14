<%@ page language="java" contentType="text/html; charset=utf8" pageEncoding="utf8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/jquery/themes/icon.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
<script>
	function save(){
		var nodes = $('#tt').tree('getChecked');  
		var s = '';  
        for(var i=0; i<nodes.length; i++){
            if(nodes[i].id == undefined || nodes[i].id == '')  continue;
            if (s != '') s += ',';  
            s += nodes[i].id;
        }
        if(s == ''){
            alert("请选择");return;
        }
        $.ajax({
            url:'saveRoleAuthority',
            type:'post',
            data:'roleId=${role.id }&resourceIds='+s+'&old='+$("#oldId").val(),
            success:function(newResourceIds){
				alert('保存成功!');
				$("#oldId").val(newResourceIds);
            }
        });
	}
	function exp(){
		if($("#exp").html()=="展开"){
			$(".easyui-tree").tree("expandAll");
			$("#exp").html('收缩');
		}else{
			$(".easyui-tree").tree("collapseAll");
			$("#exp").html('展开');
		}
	}
</script>
</head>
<body>
    <div id="tt" class="easyui-panel" title="${role.name }角色权限" style="height:auto">
    	<input type="hidden" id="oldId" value="${old }"/>
    	<br/><br/>&nbsp;&nbsp;
    	<a href="javascript:void(0)" onclick="save()">保存</a>
    	&nbsp;&nbsp;
    	<a href="javascript:void(0)" onclick="exp()" id="exp">展开</a>
    	&nbsp;&nbsp;
    	<br/><br/>
        <ul class="easyui-tree" data-options="url:'findResources?roleId=${role.id }',animate:true,checkbox:true"></ul>    
	    <br/><br/>
    </div>
</body>
</html>