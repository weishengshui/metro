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
	var roleAuths = '';
	function save(){
		var nodes = $('#tt').tree('getChecked');  
		var s = '';  
        for(var i=0; i<nodes.length; i++){
            if(nodes[i].id == undefined)  continue;
            if (s != '') s += ',';
            s += nodes[i].id;
        }
		alert('userId='+$("#userId").val()+"&resourceIds="+s+"&old="+roleAuths.substring(0, roleAuths.length-1));
        $.ajax({
            url:'createUserAuthority',
            type:'post',
            data:'userId='+$("#userId").val()+"&resourceIds="+s+"&old="+roleAuths.substring(0, roleAuths.length-1),
            success:function(){
				alert('保存成功!');
            }
        });
	}
</script>
</head>
<body>
    <div class="easyui-panel" title="${user.userName }—权限" style="height:auto">
    	<input type="hidden" id="userId" value="${user.id }"/>
    	<br/>
    	&nbsp;&nbsp;
    	<a href="javascript:void(0)" onclick="save()">保存</a>
    	&nbsp;&nbsp;
    	<a href="javascript:void(0)" onclick="collapseAll()" id="ex">收缩</a>
    	&nbsp;&nbsp;<span style="font-size:12px;color:gray">( 提示: 为用户单独授权后用户的角色授权失效 )</span>
    	<br/><br/>
        <ul id="tt" class="easyui-tree" data-options="animate:true,checkbox:true">
	    	<c:forEach items="${list }" var="res"><!-- 循环所有资源 -->
	    		<c:choose>
		    		<c:when test="${res.type == 0 }"><!-- 父节点 -->
		    			<li>  
			            	<span>${res.name }</span>
			            	<ul id="_${res.id }"></ul>
				        </li>
		    		</c:when>
		    		<c:otherwise>
		    			<c:choose>
		    				<c:when test="${empty userAuth }"><!-- 如果该用户没有权限资源,直接添加 -->
		    					<script>
					        		var pid = '${res.type}';
					        		$("#_"+pid).append("<li id='${res.id}'>${res.name}</li>");
					        		var authValue = eval('${authValue}');//所有权限值
					        		if('${res.rights}' != ''){
							    		var rights = '${res.rights}'.split(',');
							    		$("#${res.id}").append("<span>${res.name}</span>");
							    		var authValue = eval('${authValue}');//所有权限值
							    		$("#${res.id}").append("<ul id='ul_${res.id}'></ul>");
							    		for(var i=0;i<rights.length;i++){
											for(var n=0;n<authValue.length;n++){
												if(authValue[n].key == rights[i]){
													var li = "<li id='${res.id}_"+rights[i]+"'>"+authValue[n].value+"</li>";
													$("#ul_${res.id}").append(li);
												}
											}
								        }
							    	}
					        	</script>
		    				</c:when>
		    				<c:otherwise><!-- 如果该角色有权限,则循环判断并且勾选 -->
		    					
		    					<!-- flag 判断是否拥有该权限,初始值0,如果拥有该权限下面代码会将值设置为1 -->
		    					<c:set var="flag">0</c:set> 
		    					<c:forEach items="${userAuth }" var="ra"><!-- 判断Role拥有的资源 -->
				        			<c:if test="${ra.resourceId==res.id }">
				        				<c:set var="flag">1</c:set>
				        				<script>
				        					roleAuths += '${res.id}' + ',';
							        		var pid = '${res.type}';
							        		if('${res.rights}' == ''){
							        			$("#_"+pid).append("<li id='${res.id}' data-options='checked:true'>${res.name}</li>");
							        		}else{
							        			$("#_"+pid).append("<li id='${res.id}'>${res.name}</li>");
								        	}
							        		/****************判断权限值 start*********/
							        		if('${res.rights}' != ''){
									    		var rights = '${res.rights}'.split(',');
									    		$("#${res.id}").append("<span>${res.name}</span>");
									    		var authValue = eval('${authValue}');//所有权限值
									    		$("#${res.id}").append("<ul id='ul_${res.id}'></ul>");
									    		for(var i=0;i<rights.length;i++){
													for(var n=0;n<authValue.length;n++){
														if(authValue[n].key == rights[i]){
															//判断权限值
															var v = Math.pow(2,parseInt(rights[i]));
															var li = '';
															var y = parseInt('${ra.rights}') & v;
															if(y == v){
																li = "<li id='${res.id}_"+rights[i]+"' data-options='checked:true'>"+authValue[n].value+"</li>";
															}else{
																li = "<li id='${res.id}_"+rights[i]+"'>"+authValue[n].value+"</li>";
															}
															$("#ul_${res.id}").append(li);
														}
													}
										        }
									    	}
							        		/**************** END *********/
									    	
							        	</script>
				        			</c:if>
					        	</c:forEach>
					        	<c:if test="${flag==0}">
			        				<script>
						        		var pid = '${res.type}';
						        		$("#_"+pid).append("<li id='${res.id}'>${res.name}</li>");
						        		/****************判断权限值 (和上面一样)start*********/ 
						        		if('${res.rights}' != ''){
						            		var rights = '${res.rights}'.split(',');
						            		$("#${res.id}").append("<span>${res.name}</span>");
						            		var authValue = eval('${userAuth}');//所有权限值
						            		$("#${res.id}").append("<ul id='ul_${res.id}'></ul>");
						            		for(var i=0;i<rights.length;i++){
						        				for(var n=0;n<authValue.length;n++){
						        					if(authValue[n].key == rights[i]){
						        						//判断权限值
						        						var v = Math.pow(2,parseInt(rights[i]));
						        						var li = '';
						        						var y = parseInt('${ra.rights}') & v;
						        						if(y == v){
						        							li = "<li id='${res.id}_"+rights[i]+"' data-options='checked:true'>"+authValue[n].value+"</li>";
						        						}else{
						        							li = "<li id='${res.id}_"+rights[i]+"'>"+authValue[n].value+"</li>";
						        						}
						        						$("#ul_${res.id}").append(li);
						        					}
						        				}
						        	        }
						            	}
						        		/**************** END *********/
						        		
						        	</script>
					        	</c:if>
		    				</c:otherwise>
		    			</c:choose>
		    		</c:otherwise>
	    		</c:choose>
	    	</c:forEach>
	    </ul>   
	    <br/><br/>
    </div>
    <script>
	    function collapseAll(){
		    /*
	    	$('#tt').tree();
		    $('#tt > li').each(function(){
		    	var _target = $(this).children('.tree-node').get(0);
		    	$('#tt').tree('collapse',_target);
		    });*/
	    	$("#tt").tree('collapseAll');
	    	$("#ex").html('展开');
	    	$("#ex").attr('onclick','expandAll()');
		}
		function expandAll(){
			$("#tt").tree('expandAll');
			$("#ex").html('收缩');
	    	$("#ex").attr('onclick','collapseAll()');
		}
    </script>
</body>
</html>