$(function(){
//	InitLeftMenu();
})
//初始化左侧
function InitLeftMenu() {
    $(".west-menu").empty();
    var menulist = "";
    $.each(_menus.menus, function(i, n) {
        menulist += '<div title="'+n.menuname+'"  icon="'+n.icon+'" style="overflow:auto;">';
		menulist += '<ul>';
        $.each(n.menus, function(j, o) {
			menulist += '<li><div><a href="javascript:void(0)" onclick="addTab(\''+o.menuname+'\',\'' + o.url + '\')"><span class="icon '+o.icon+'" ></span>' + o.menuname + '</a></div></li> ';
        })
        menulist += '</ul></div>';
    })
	$(".west-menu").append(menulist);
	
	$('.west-menu li a').click(function(){
		/*
		var tabTitle = $(this).text();
		var url = $(this).attr("href");
		addTab(tabTitle,url);
		$('.west-menu li div').removeClass("selected");
		$(this).parent().addClass("selected");*/
	}).hover(function(){
		$(this).parent().addClass("hover");
	},function(){
		$(this).parent().removeClass("hover");
	});
	
	$(".west-menu").accordion({
		animate:false,
		width:'auto'
	});
}

//添加tab
function addTab(subtitle,url){
	if(!$('#tabs').tabs('exists',subtitle)){
		$('#tabs').tabs('add',{
			title:subtitle,
			content:createFrame(url),
			closable:true,
			width:$('#mainPanle').width()-10,
			height:$('#mainPanle').height()-26
		});
	}else{
		$('#tabs').tabs('select',subtitle);
	}
	tabClose();
}

function createFrame(url){
	var s = '<iframe name="'+url+'" scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
	return s;
}
/*双击关闭TAB选项卡*/
function tabClose(){
	$(".tabs-inner").dblclick(function(){
		var subtitle = $(this).children("span").text();
		$('#tabs').tabs('close',subtitle);
	})
	/*
	$(".tabs-inner").bind('contextmenu',function(e){
		$('#mm').menu('show', {
			left: e.pageX,
			top: e.pageY
		});
		var subtitle =$(this).children("span").text();
		$('#mm').data("currtab",subtitle);
		return false;
	});*/
}