<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>用户管理</title>
<%@include file="/link/link.jsp"%>
</head>
<body class="easyui-layout">
	<div data-options="region:'center',title:'用户列表',iconCls:'icon-ok'">
		<table id="userid"
			data-options="toolbar:'#toolbar',loadMsg:'正在加载数据...',">
			<thead>
				<tr>
					<th data-options="field:'id' , hidden:'true'">id</th>
					<th data-options="field:'roleId',align:'center',hidden:'true'"
						width="150">RoleId</th>
					<th data-options="field:'account',align:'center'" width="150">账号</th>
					<th data-options="field:'roleName',align:'center'" width="150">角色</th>
					<th data-options="field:'realName',align:'center'" width="150">真实姓名</th>
					<th data-options="field:'password',align:'center',hidden:'true'"
						width="150">登录密码</th>
					<th data-options="field:'remark',align:'center'" width="150">备注</th>
				</tr>
			</thead>
		</table>
		<div id="toolbar" style="padding: 5px; height: auto">
			<div style="padding: 5px;">
				<font>真实姓名：</font> <input id="userRealName" name="userRealName"
					class="easyui-validatebox" type="text" style="width: 200px;" /> <font>账号：</font>
				<input id="userAccount" name="userAccount"
					class="easyui-validatebox" type="text" style="width: 200px;" /> <a
					href="javascript:void(0);" class="easyui-linkbutton"
					iconCls="icon-search" onclick="findUser()">查询</a>
			</div>
			<div>
				<c:forEach items="${buttonList}" var="button">
					<a href="javascript:void(0);" class="easyui-linkbutton"
						iconCls="icon-${button.icon}" plain="true"
						onclick="${button.function}">${button.name}</a>
				</c:forEach>
			</div>
		</div>
	</div>
	<!--用户窗口 -->
	<div id="userWindow" class="easyui-window" title="用户信息"
		data-options="modal:true,minimizable:false,closed:true,iconCls:'icon-edit'"
		style="width: 400px; height: 320px; padding: 10px;">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'center'" style="padding: 10px;">
				<form id="form" method="post">
					<table align="center" width="100%">
						<tr>
							<td>登录名：</td>
							<td><input type="hidden" name="id" id="uid" /> <input
								id="name" name="account" class="easyui-validatebox" type="text"
								data-options="required:false" style="width: 150px;" /></td>
						</tr>
						<tr>
							<td>登录密码：</td>
							<td><input id="pwd" name="password"
								class="easyui-validatebox" type="text" style="width: 150px;"></input></td>
						</tr>
						<tr>
							<td>角色：</td>
							<td><input id="role" class="easyui-combobox" name="roleId"
								style="width: 155px" /></td>
						</tr>
						<tr>
							<td>真实姓名：</td>
							<td><input id="realName" name="realName"
								class="easyui-validatebox" type="text" style="width: 150px;" />
							</td>
						</tr>
						<tr>
							<td>备注：</td>
							<td><textarea id="remark" name="remark"
									style="width: 150px;" class="easyui-validatebox"></textarea></td>
						</tr>
					</table>
				</form>
			</div>
			<div data-options="region:'south',border:false"
				style="text-align: right; padding: 5px 0 0;">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-save'" onclick="saveuser()">保存</a> <a
					href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-cancel'"
					onclick="$('#userWindow').window('close')">取消</a>
			</div>
		</div>

	</div>
</body>
<script type="text/javascript">
$(function(){
	findUser();
});
//打开新增用户窗口
function showuserWindow(){
	$('#form').form('clear');
	$("#name").removeAttr('readonly');
	$("#pwd").removeAttr('readonly');
	$.getJSON("${app}/admin/json/findAllRole",function(data){
		$('#role').combobox({
			data:data,
            valueField:'id', 
            textField:'roleName',
            editable:false,
            onLoadSuccess: function () { //加载完成后,设置选中第一项
            	var val = $(this).combobox("getData");
                for (var item in val[0]) {
                    if (item == "id") {
                        $(this).combobox("select", val[0][item]);
                    }
                }
            }
        });
	});
	$("#userWindow").window("open");
}
//打开修改用户窗口
function editRecord(){
	record = $("#userid").datagrid("getSelected");
	if(record==null){
		$.messager.alert("系统提示","请选择记录!","warning");
		return;
	}
	$("#uid").val(record.id);
	$("#name").val(record.account);
	$("#name").attr('readonly','readonly');
	$("#realName").val(record.realName);
	$("#pwd").val('');
	$("#pwd").attr('readonly','readonly');
	$("#remark").val(record.remark);
	$.getJSON("${app}/admin/json/findAllRole",function(data){
		$('#role').combobox({ 
			data:data,
            valueField:'id', 
            textField:'roleName',
            editable:false,
            onLoadSuccess: function () { //加载完成后,设置选中第一项
            	var val = $(this).combobox("getData");
                for (var item in val[0]) {
                    if (item == "id") {
                    	$('#role').combobox("select",record.roleId);
                    }
                }
            }
        });   
	});
	$("#userWindow").window("open");
}

//保存用户
function saveuser(){
	var role = $("#role").combobox('getValue');
	if(role == ""){
		$.messager.alert("系统提示","请选择角色","warning");
		return;
	}
	var name = $("#name").val();
	if(name == ""){
		$.messager.alert("系统提示","请填写登录名","warning");
		return;
	}
	var realName = $("#realName").val();
	if(realName == ""){
		$.messager.alert("系统提示","请填写真实姓名","warning");
		return;
	}
	var pwd = $("#pwd").val();
	var uid = $("#uid").val();
	if(pwd == "" && uid == ""){
		$.messager.alert("系统提示","请填写登录密码","warning");
		return;
	}
	var options ={
			url:"${app}/admin/json/saveUser",
			success : function(data) {
				if(checkData(data)){
					$("#userWindow").window('close');
					findUser();
				}
			}
		};
	$("#form").ajaxSubmit(options);		
	//清除选中行
	$('#userid').datagrid('clearSelections');
}
//删除用户
function deleteUser(){
	var record = $('#userid').datagrid('getSelected');
	if(record==null){
		$.messager.alert("系统提示","请选择记录!","warning");
		return;
	}
	$.messager.confirm('系统提示', '确定要删除吗?', function(r){
		if (r){
			$.getJSON("${app}/admin/json/deleteUser?userId="+record.id,function(data){
				if(checkData(data)){
					findUser();
				}
			});			
		}
	});
	$('#userid').datagrid('clearSelections');
}
//查询用户
function findUser(){
	var userRealName = $("#userRealName").val();
	var userAccount = $("#userAccount").val();
	 $('#userid').datagrid({  
	        height: 'auto',  
	        nowrap: false,  
	        striped: true,  
	        border: true,  
	        collapsible:false,//是否可折叠的  
	        fit: true,//自动大小  
	        url:'${app}/admin/json/findUserList?account='+userAccount+'&realName='+userRealName,  
	        remoteSort:false, 
	        idField:'id',  
	        singleSelect : true,//是否单选  
			selectOnCheck: true,
			checkOnSelect: true,
			fitColumns: true, 
	        pagination:true,//分页控件  
	        pageSize:30,//设置分页默认条数
		    pageList: [30,50,100,150], 
	        rownumbers:true,//行号  
	        onLoadError:function(){
	        	$.messager.alert("系统提示","加载数据失败！","warning");
			}
	    });  
	 	//设置分页控件  
	    var p = $('#userid').datagrid('getPager');  
	    $(p).pagination({  
	        beforePageText: '第',//页数文本框前显示的汉字  
	        afterPageText: '页    共 {pages} 页',  
	        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',  
	    });  
	    $('#userid').datagrid('clearSelections');
}
</script>
</html>