<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>main</title>
<%@include file="/link/link.jsp"%>
<style type="text/css">
a {
	color: #ffffff;
	text-decoration: none;
}

a:HOVER {
	color: #ffffff;
	text-decoration: underline;
}

.bg {
	background: url('${app}/images/top.jpg');
	filter:
		"progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='scale')";
	-moz-background-size: 100% 100%;
	background-size: 100% 100%;
}
</style>
</head>
<body class="easyui-layout">
	<!-- 头部标题 -->
	<div data-options="region:'north',border:false,split:true,maxHeight:60"
		class="bg" style="overflow: hidden;">
		<table width="100%" height="60px" border="0" cellpadding="0"
			cellspacing="0">
			<tr height="20px">
				<td rowspan="2"><img src="${app}/images/logo.gif" /></td>
				<td width="200px" valign="middle"><label style="color: black">当前用户：</label>
					<label style="color: black">${realName}</label></td>
				<td width="80px" valign="middle"><a
					href="javascript:uploadPwd();" style="color: black;">修改密码</a></td>
				<td width="60px" valign="middle"><a
					href="javascript:reLogin();" style="color: black;">注销</a></td>
			</tr>
			<tr height="20px">
				<td colspan="4"></td>
			</tr>
		</table>
	</div>

	<!-- 左侧菜单 -->
	<div data-options="region:'west',split:true,title:'系统功能'"
		style="width: 200px; padding: 10px; background-color: #eef1f0">
		<ul id="tree" class="easyui-tree" data-options="animate:true">
		</ul>
	</div>

	<!-- 页脚信息 -->
	<div data-options="region:'south',border:false"
		style="height: 20px; background: #F3F3F3; padding: 2px; vertical-align: middle;">
		<span style="float: right; width: 50px">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
		<span id="nowTime" style="float: right;"></span> <span id="sysVersion"
			style="float: left;"> 系统版本：V1.0 </span>
	</div>

	<!-- 内容tab -->
	<div id="tabs" data-options="region:'center'" class="easyui-tabs">
		<div title="首页" style="padding: 0px; display: block; overflow: hidden"
			data-options="fit:'true'" width="100%" height="100%">
			<img src="${app}/images/bg.png" width="100%" height="100%" />
		</div>
	</div>

	<!-- 修改密码窗口 -->
	<div id="uploadPwdWindow" class="easyui-window" title="修改密码"
		data-options="modal:true,minimizable:false,closed:true,iconCls:'icon-edit'"
		style="width: 400px; height: 200px; padding: 10px;">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'center'" style="padding: 10px;">
				<table align="center" width="100%">
					<tr>
						<td>原密码：</td>
						<td><input id="oldPwd" type="password" style="width: 200px;">
						</td>
					</tr>
					<tr>
						<td>新密码：</td>
						<td><input id="pwd1" type="password" style="width: 200px;">
						</td>
					</tr>
					<tr>
						<td>确认密码：</td>
						<td><input id="pwd2" type="password" style="width: 200px;">
						</td>
					</tr>
				</table>
			</div>
			<div data-options="region:'south',border:false"
				style="text-align: right; padding: 5px 0 0;">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-save'" onclick="savePwd()">确定</a> <a
					href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-cancel'"
					onclick="$('#uploadPwdWindow').window('close')">取消</a>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">

	$(function() {
		getTree();
		//系统时间显示
		setInterval("document.getElementById('nowTime').innerHTML=new Date().toLocaleString()+' 星期'+'日一二三四五六'.charAt(new Date().getDay());",1000);
	});

	function getTree() {
		$('#tree').tree({
			url : '${app}/admin/json/findMenuTree',
			onClick : function(node) {
				if($('#tree').tree('isLeaf',node.target)){
					tab(node.text,node.attributes.url); 
				}
			}
		});
	}
	
	function tab(text, url) {
		if ($("#tabs").tabs('exists', text)) { //若选项卡已存在，选择该选项卡
			$("#tabs").tabs('select',text);
		} else {
			
			var content = "<iframe id='myFrame' frameborder='0' scrolling='auto' style='width:100%;height:100%' src=" + url + "></iframe>";
			$("#tabs").tabs('add', { //生成新的选项卡，
				title : text,
				closable : true,
				content : content,
			});
			$("#tabs").tabs('getSelected').css('width','auto');
		}
	}

	//修改密码窗口
	function uploadPwd() {
		$("#oldPwd").val("");
		$("#pwd1").val("");
		$("#pwd2").val("");
		$("#uploadPwdWindow").window("open");
	}

	//修改密码
	function savePwd() {
		var oldPwd = $("#oldPwd").val();
		var pwd1 = $("#pwd1").val();
		var pwd2 = $("#pwd2").val();
		if (oldPwd == "") {
			$.messager.alert('系统提示', '请输入原密码!', 'warning');
			return;
		} else if (pwd1 == "") {
			$.messager.alert('系统提示', '请输入新密码!', 'warning');
			return;
		} else if (pwd1 != pwd2) {
			$.messager.alert('系统提示', '两次输入密码不正确!', 'warning');
			return;
		}
		$.getJSON("${app}/admin/json/updatePassword?newPasswrod=" + pwd1 + "&oldPassword=" + oldPwd, function(data) {
			if(checkData(data)){
				$("#uploadPwdWindow").window('close');
			}
		});
	}
	
	//注销
	function reLogin() {
		$.messager.confirm('系统提示', '确定要注销吗?', function(r) {
			if (r) {
				$.getJSON("${app}/admin/json/logout", function(data) {
					window.location.href="${app}/admin/login";
				});
			}
		});
	}
</script>
</html>