<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>课程图片管理</title>
<%@include file="/link/link.jsp"%>
</head>
<body class="easyui-layout">
	<div data-options="region:'center',title:'图片列表',iconCls:'icon-ok'">
		<table id="imageid"
			data-options="toolbar:'#toolbar',loadMsg:'正在加载数据...',">
			<thead>
				<tr>
					<th data-options="field:'id' ,align:'center'" width="80">id</th>
					<th data-options="field:'url',hidden:'true'">url</th>
					<th data-options="field:'image',align:'center'" width="100">图片</th>
					<th data-options="field:'courseName',align:'center'" width="150">所属课程</th>
				</tr>
			</thead>
		</table>
		<div id="toolbar" style="padding: 5px; height: auto">
			<div>
				<c:forEach items="${buttonList}" var="button">
					<a href="javascript:void(0);" class="easyui-linkbutton"
						iconCls="icon-${button.icon}" plain="true"
						onclick="${button.function}">${button.name}</a>
				</c:forEach>
			</div>
		</div>
	</div>
	<!--图片窗口 -->
	<div id="imageWindow" class="easyui-window" title="图片信息"
		data-options="modal:true,minimizable:false,closed:true,iconCls:'icon-edit'"
		style="width: 400px; height: 320px; padding: 10px;">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'center'" style="padding: 10px;">
				<form id="form" method="post">
					<table align="center" width="100%">
						<tr>
							<td>图片:</td>
							<td><input type="hidden" name="id" id="imgid" /> <input
								type="hidden" name="courseId" id="courseId" />

								<div id="imageDiv">
									<img id="imageShow" width="200"
										onclick="$('#imageFile').click();" height="100" />
								</div> <input type="file" name="imageFile" id="imageFile"
								style="display: none;" /></td>
							<td></td>
						</tr>
					</table>
				</form>
			</div>
			<div data-options="region:'south',border:false"
				style="text-align: right; padding: 5px 0 0;">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-save'" onclick="saveImage()">保存</a> <a
					href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-cancel'"
					onclick="$('#imageWindow').window('close')">取消</a>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
var courseId = '${courseId}';

$(function(){
	findImage();
});

//打开新增图片窗口
function showimageWindow(){
	$('#form').form('clear');
	new uploadPreview({ UpBtn: "imageFile", DivShow: "imageDiv", ImgShow: "imageShow" });
	$("#courseId").val(courseId);
	$("#imageShow").attr("src","");
	$("#imageWindow").window("open");
}

//打开修改图片窗口
function editRecord(){
	record = $("#imageid").datagrid("getSelected");
	if(record==null){
		$.messager.alert("系统提示","请选择记录!","warning");
		return;
	}
	new uploadPreview({ UpBtn: "imageFile", DivShow: "imageDiv", ImgShow: "imageShow" });
	$("#imgid").val(record.id);
	$("#courseId").val(courseId);
	$("#imageShow").attr("src",record.url);
	$("#imageWindow").window("open");
}
//保存图片
function saveImage(){
	var imageFile = $("#imageFile").val();
	if(imageFile == ""){
		$.messager.alert("系统提示","请选择图片","warning");
		return;
	}
	var options ={
			url:"${app}/admin/json/saveCourseImage",
			success : function(data) {
				if(checkData(data)){
					$("#imageWindow").window('close');
					findImage();
				}
			}
		};
	$("#form").ajaxSubmit(options);		
	//清除选中行
	$('#imageid').datagrid('clearSelections');
}
//删除图片
function deleteImage(){
	var record = $('#imageid').datagrid('getSelected');
	if(record==null){
		$.messager.alert("系统提示","请选择记录!","warning");
		return;
	}
	$.messager.confirm('系统提示', '确定要删除吗?', function(r){
		if (r){
			$.getJSON("${app}/admin/json/deleteImage?id="+record.id,function(data){
				if(checkData(data)){
					findImage();
				}
			});			
		}
	});
	$('#imageid').datagrid('clearSelections');
}
//查询图片
function findImage(){
	 $('#imageid').datagrid({
	        height: 'auto',  
	        nowrap: false,  
	        striped: true,  
	        border: true,  
	        collapsible:false,//是否可折叠的  
	        fit: true,//自动大小  
	        url:'${app}/admin/json/findCourseImage?courseId='+courseId,  
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
	    var p = $('#imageid').datagrid('getPager');  
	    $(p).pagination({  
	        beforePageText: '第',//页数文本框前显示的汉字  
	        afterPageText: '页    共 {pages} 页',  
	        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',  
	    });  
	    $('#imageid').datagrid('clearSelections');
}
</script>
</html>