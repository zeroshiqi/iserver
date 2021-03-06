<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<link rel="stylesheet" type="text/css"
	href="${app}/easyui/themes/gray/easyui.css" />
<link rel="stylesheet" type="text/css"
	href="${app}/easyui/themes/icon.css" />
<link rel="stylesheet" type="text/css"
	href="${app}/easyui/demo/demo.css">
<script type="text/javascript" src="${app}/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${app}/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${app}/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${app}/js/jquery.form.js"></script>
<script type="text/javascript" src="${app}/js/uploadPreview.min.js"></script>
<script type="text/javascript">
	//时间控件
	function myformatter(date) {
		var y = date.getFullYear();
		var m = date.getMonth() + 1;
		var d = date.getDate();
		return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d);
	}
	function myparser(s) {
		if (!s)
			return new Date();
		var ss = (s.split('-'));
		var y = parseInt(ss[0], 10);
		var m = parseInt(ss[1], 10);
		var d = parseInt(ss[2], 10);
		if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
			return new Date(y, m - 1, d);
		} else {
			return new Date();
		}
	}
	
	//验证数据
	function checkData(data){
		//未登录
		if(data.status == 300){
			window.location.href="${app}/admin/forward";
			return false;
		}
		//warning
		if(data.status == 600){
			$.messager.alert('系统提示', data.msg, 'warning');
			return false;
		}
		//error
		if(data.status == 500){
			$.messager.show({
				title : '系统提示',
				msg : data.msg,
				showType : 'show'
			});
			return false;
		}
		// ok
		if(data.status == 200){
			$.messager.show({
				title : '系统提示',
				msg : data.msg,
				showType : 'show'
			});
			return true;
		}
		return false;
	}
	
	
</script>
