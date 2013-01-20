//转化json数据的日期
function dateFormat(jsondate) {
	if(jsondate == null){return '';}
	jsondate = jsondate.toString();
    jsondate = jsondate.replace("/Date(", "").replace(")/", "");
    if (jsondate.indexOf("+") > 0) {
        jsondate = jsondate.substring(0, jsondate.indexOf("+"));
    } else if (jsondate.indexOf("-") > 0) {
        jsondate = jsondate.substring(0, jsondate.indexOf("-"));
    }
    var date = new Date(parseInt(jsondate, 10));
    var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
    var currentDate = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
    var hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
    var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
    var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds(); 
    return date.getFullYear() + "-" + month + "-" + currentDate + " " + hours + ":" + minutes + ":" + seconds;
}

//定义列表pageList
var pageList = [20,30,50,100];

//将表单里面的值转为JSON,如：{"name":"tset"},用于查询
function getForms(formId){
	var arr = $("#"+formId).serializeArray();
	var s = {};
	$.each(arr, function(i, field){
	    s[field.name] = field.value;
	});
	return s;
}

$(window).resize(function(){
	$('.easyui-datagrid').datagrid('resize');
});