<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>角色管理</title>
<%@include file="/link/link.jsp"%>
</head>
<body class="easyui-layout">
	<div data-options="region:'center',title:'日志列表',iconCls:'icon-ok'">
		<table id="logid"
			data-options="toolbar:'#toolbar',loadMsg:'正在加载数据...',">
			<thead>
				<tr>
					<th data-options="field:'id' , hidden:'true'">id</th>
					<th data-options="field:'userId' , hidden:'true'">userId</th>
					<th data-options="field:'account',align:'center'" width="150">用户账号</th>
					<th data-options="field:'userName',align:'center'" width="150">用户名称</th>
					<th data-options="field:'ipAddress',align:'center'" width="150">IP地址</th>
					<th data-options="field:'uri',align:'center'" width="150">访问URI</th>
					<th data-options="field:'description',align:'center'" width="150">描述</th>
					<th data-options="field:'createAt',align:'center'" width="150">访问时间</th>
				</tr>
			</thead>
		</table>
		<div id="toolbar" style="padding: 0px; height: auto">
			<!-- <div style="padding:5px;">
				<font>角色名称：</font>
				<input id="rn" name="roleName" class="easyui-validatebox" type="text" style="width:200px;"/>
				<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" onclick="findLog()">查询</a>
			</div> -->
			<div>
				<c:forEach items="${buttonList}" var="button">
					<a href="javascript:void(0);" class="easyui-linkbutton"
						iconCls="icon-${button.icon}" plain="true"
						onclick="${button.function}">${button.name}</a>
				</c:forEach>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
$(function(){
	findLog();
});
//查询角色
function findLog(){
	var name = $("#rn").val();
	 $('#logid').datagrid({
	        height: 'auto',  
	        nowrap: false,  
	        striped: true,  
	        border: true,  
	        collapsible:false,//是否可折叠的  
	        fit: true,//自动大小  
	        url:'${app}/admin/json/findUserLog',  
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
	    var p = $('#logid').datagrid('getPager');  
	    $(p).pagination({  
	        beforePageText: '第',//页数文本框前显示的汉字  
	        afterPageText: '页    共 {pages} 页',  
	        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',  
	    });  
	    $('#logid').datagrid('clearSelections');
}
</script>
</html>