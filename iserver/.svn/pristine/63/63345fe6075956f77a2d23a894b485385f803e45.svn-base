<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>课程管理</title>
<%@include file="/link/link.jsp"%>
<link href="${app}/ueditor/themes/default/css/umeditor.css"
	type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8"
	src="${app}/ueditor/umeditor.config.js"></script>
<script type="text/javascript" charset="utf-8"
	src="${app}/ueditor/umeditor.min.js"> </script>
<script type="text/javascript" charset="utf-8"
	src="${app}/ueditor/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.3"></script>
</head>
<body class="easyui-layout">
	<div data-options="region:'center',title:'课程列表',iconCls:'icon-ok'">
		<table id="courseid"
			data-options="toolbar:'#toolbar',loadMsg:'正在加载数据...',">
			<thead>
				<tr>
					<th data-options="field:'id' , align:'center'" width="20">id</th>
					<th data-options="field:'type' , hidden:'true'" width="40">type</th>
					<th data-options="field:'playAddressId' , hidden:'true'" width="40">playAddressId</th>
					<th data-options="field:'cover' , hidden:'true'" width="40">cover</th>
					<th data-options="field:'avatar' , hidden:'true'" width="40">avatar</th>
					<th data-options="field:'isVideo' , hidden:'true'" width="40">isVideo</th>
					<th data-options="field:'courseName' , align:'center'" width="200">课程名称</th>
					<th data-options="field:'typeStr' , align:'center'" width="50">课程类型</th>
					<th data-options="field:'playBeginTime' , align:'center'"
						width="90">直播开始时间</th>
					<th data-options="field:'playEndTime' , align:'center'" width="90">直播结束时间</th>
					<th data-options="field:'playAddress' , align:'center'" width="200">播放地址</th>
				</tr>
			</thead>
		</table>
		<div id="toolbar" style="padding: 5px; height: auto">
			<div style="padding: 5px;">
				<select class="easyui-combobox" id="courseType"
					data-options="editable:false" name="courseType"
					style="width: 150px;">
					<option value="0">线下课程</option>
					<option value="1">线上课程</option>
				</select> <a href="javascript:void(0);" class="easyui-linkbutton"
					iconCls="icon-search" onclick="findCourse()">查询</a>
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

	<!--文件窗口 -->
	<div id="fileWindow" class="easyui-window" title="文件上传"
		data-options="modal:true,minimizable:false,closed:true,iconCls:'icon-edit'"
		style="width: 400px; height: 320px; padding: 10px;">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'center'" style="padding: 10px;">
				<form id="fileForm" method="post">
					<table align="center" width="100%">
						<tr>
							<td>课程名称：</td>
							<td><input name="courseId" id="fileCourseId" type="hidden" />
								<input id="courseNameStr" readonly="readonly"
								class="easyui-validatebox" type="text"
								data-options="required:false" style="width: 150px;" /></td>
						</tr>
						<tr>
							<td>时长：</td>
							<td><input id="timeLength" name="timeLength"
								class="easyui-validatebox" type="text"
								data-options="required:false" style="width: 150px;" /></td>
						</tr>
						<tr>
							<td>选择文件：</td>
							<td><input type="file" name="videoFile" id="videoFile" /></td>
						</tr>
					</table>
				</form>
			</div>
			<div data-options="region:'south',border:false"
				style="text-align: right; padding: 5px 0 0;">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-save'" onclick="saveFile()">保存</a> <a
					href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-cancel'"
					onclick="$('#fileWindow').window('close')">取消</a>
			</div>
		</div>
	</div>
	<!--讲师编辑窗口 -->
	<div id="teacherWindow" class="easyui-window" title="讲师信息"
		data-options="modal:true,minimizable:false,closed:true,iconCls:'icon-edit'"
		style="width: 400px; height: 320px; padding: 10px;">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'center'" style="padding: 10px;">
				<table align="center" width="100%">
					<tr>
						<td>讲师姓名：</td>
						<td><textarea id="teacherName" readonly="readonly"
								style="width: 255px; height: 130px" class="easyui-validatebox"></textarea>
							<input type="hidden" id="teacherIds" /></td>
					</tr>
				</table>
				<table align="center" width="100%">
					<tr>
						<td>讲师列表：</td>
						<td><input id="teacherList" class="easyui-combobox"
							style="width: 150px" /> <a href="javascript:void(0);"
							class="easyui-linkbutton" iconCls="icon-reload"
							onclick="addTeacherName();">添加</a> <a href="javascript:void(0);"
							class="easyui-linkbutton" iconCls="icon-reload"
							onclick="removeTeacherName();">清空</a></td>
					</tr>
				</table>
			</div>
			<div data-options="region:'south',border:false"
				style="text-align: right; padding: 5px 0 0;">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-save'" onclick="saveTeacher();">保存</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-cancel'"
					onclick="$('#teacherWindow').window('close')">取消</a>
			</div>
		</div>
	</div>
	<div id="courseWindow" class="easyui-window" title="课程信息"
		data-options="modal:true,minimizable:false,closed:true,iconCls:'icon-edit'"
		style="width: 910px; height: 400px; padding: 10px;">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'center'" style="padding: 10px;">
				<form id="form" method="post">
					<table width="100%">
						<tr>
							<td align="right" width="200px">课程封面:</td>
							<td align="left">
								<div id="coverDiv">
									<img id="coverShow" width="200"
										onclick="$('#coverFile').click();" height="100" />
								</div> <input type="file" name="coverImg" id="coverFile"
								style="display: none;" />
							</td>
							<td></td>
						</tr>
						<tr>
							<td align="right">头图:</td>
							<td>
								<div id="avatarDiv">
									<img id="avatarShow" width="200"
										onclick="$('#avatarFile').click();" height="100" />
								</div> <input type="file" name="avatarImg" id="avatarFile"
								style="display: none;" />
							</td>
							<td></td>
						</tr>
						<tr>
							<td height="10px"></td>
						</tr>
						<tr>
							<td align="right" height="10px">课程名称：</td>
							<td><input type="hidden" name="courseId" id="courseId" /> <input
								id="courseName" name="courseName" class="easyui-validatebox"
								type="text" data-options="required:false" style="width: 200px;" />
							</td>
							<td></td>
						</tr>
						<tr>
							<td height="10px"></td>
						</tr>
						<tr>
							<td align="right">课程开始时间：</td>
							<td><input id="beginTime" name="beginTimeStr"
								class="easyui-datebox"
								data-options="formatter:myformatter,parser:myparser,editable:false"
								style="width: 200px" /></td>
						</tr>
						<tr>
							<td height="10px"></td>
						</tr>
						<tr>
							<td align="right">课程类型：</td>
							<td><select class="easyui-combobox" id="courseType1"
								name="courseType" style="width: 200px;">
									<option value="0">线下课程</option>
									<option value="1">线上课程</option>
							</select></td>
							<td></td>
						</tr>
						<tr>
							<td height="10px"></td>
						</tr>
						<tr id="studentNumTr">
							<td align="right">招生数量：</td>
							<td><input id="studentNum" name="studentNum"
								class="easyui-validatebox" type="text"
								data-options="required:false" style="width: 200px;" /></td>
							<td></td>
						</tr>
						<tr id="priceTr">
							<td align="right">价格：</td>
							<td><input id="price" name="price"
								class="easyui-validatebox" type="text"
								data-options="required:false" style="width: 200px;" /></td>
						</tr>
						<tr id="nullid6">
							<td height="10px"></td>
						</tr>
						<tr id="cityIdTr">
							<td align="right">所在城市：</td>
							<td><input id="cityId" class="easyui-combobox" name="cityId"
								style="width: 200px" /></td>
							<td></td>
						</tr>
						<tr id="teacherIdTr">
							<td align="right">讲师：</td>
							<td><input id="teacherNames" class="easyui-validatebox"
								readonly="readonly" type="text" data-options="required:false"
								style="width: 200px;" /> <input id="teacherId" name="teacherId"
								class="easyui-validatebox" type="hidden"
								data-options="required:false" style="width: 200px;" /> <a
								href="javascript:void(0);" class="easyui-linkbutton"
								iconCls="icon-search" onclick="showTeacherWindow()">编辑</a></td>
							<td></td>
						</tr>
						<tr id="nullid1">
							<td height="10px"></td>
						</tr>
						<tr id="addressTr">
							<td align="right">上课地址：</td>
							<td><input id="address" name="address"
								class="easyui-validatebox" type="text"
								data-options="required:false" style="width: 200px;"
								onchange="searchByStationName();" /></td>
						</tr>
						<tr id="lonTr">
							<td align="right">经度：</td>
							<td><input type="text" class="easyui-validatebox" id="lon"
								name="lon" style="width: 200px" /></td>
						</tr>
						<tr id="latTr">
							<td align="right">纬度：</td>
							<td><input type="text" class="easyui-validatebox" id="lat"
								name="lat" style="width: 200px" /></td>
						</tr>
						<tr>
							<td colspan="2" align="center">
								<div id="container"></div>
							</td>
						</tr>
						<tr id="nullid2">
							<td height="10px"></td>
						</tr>
						<tr id="offlineTypeTr">
							<td align="right">所属类型：</td>
							<td><input id="offlineTypeId" class="easyui-combobox"
								name="offlineType" style="width: 200px" /></td>
							<td></td>
						</tr>
						<tr id="nullid5">
							<td height="10px"></td>
						</tr>
						<tr id="courseTimeTr">
							<td align="right">上课时间：</td>
							<td><input id="courseTime" name="courseTime"
								class="easyui-validatebox" type="text"
								data-options="required:false" style="width: 200px;" /></td>
							<td></td>
						</tr>
						<tr id="nullid3">
							<td height="10px"></td>
						</tr>
						<tr id="playBeginTimeTr">
							<td align="right">直播开始时间：</td>
							<td><input id="playBeginTime" name="playBeginTimeStr"
								class="easyui-datetimebox" data-options="editable:false"
								style="width: 200px" /></td>
						</tr>
						<tr id="nullid4">
							<td height="10px"></td>
						</tr>
						<tr id="playEndTimeTr">
							<td align="right">直播结束时间：</td>
							<td><input id="playEndTime" name="playEndTimeStr"
								class="easyui-datetimebox" data-options="editable:false"
								style="width: 200px" /></td>
						</tr>
						<tr id="nullTr1">
							<td height="10px"></td>
						</tr>
						<tr id="playaddressTr">
							<td align="right">直播地址：</td>
							<td><input id="addressId" class="easyui-combobox"
								name="addressId" style="width: 200px" /></td>
							<td></td>
						</tr>
						<!-- 
				<tr id="isVideoTr">
					<td align="right">直播格式：</td>
					<td>
						<select class="easyui-combobox" id="isVideo" name="isVideo" style="width:200px;">
							<option value="0">音频</option>
			    			<option value="1" selected="selected">视频</option>
			    		</select>
					</td>
				</tr>
				 -->
						<tr>
							<td height="30px"></td>
						</tr>
						<tr id="infoTr">
							<td align="right">课程简介:</td>
							<td colspan="2"><script id="editor" type="text/plain"
									style="width: 700px; height: 300px;"></script></td>
						</tr>
						<tr>
							<td height="10px"><input type="hidden" name="courseContent"
								id="courseContent" /></td>
						</tr>
					</table>
				</form>
			</div>
			<div data-options="region:'south',border:false"
				style="text-align: right; padding: 5px 0 0;">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-save'" onclick="saveCourseInfo()">保存</a>
				<a href="javascript:void(0)" class="easyui-linkbutton"
					data-options="iconCls:'icon-cancel'"
					onclick="$('#courseWindow').window('close')">取消</a>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
var ue = UM.getEditor('editor');
function removeTeacherName(){
	$("#teacherName").val('');
	$("#teacherIds").val('');
}

$(function(){
	$("#courseType").combobox('select',1);
	findCourse();
	$("#container").hide();
});
//修改时
function editRecord(){
	record = $("#courseid").datagrid("getSelected");
	if(record==null){
		$.messager.alert("系统提示","请选择记录!","warning");
		return;
	}
	
	$("#courseId").val(record.id);
	$.getJSON("${app}/admin/json/findAllCity",function(data){
		$('#cityId').combobox({
			data:data,
            valueField:'id', 
            textField:'value',
            editable:false,
            onLoadSuccess: function () { //加载完成后,设置选中第一项
            	 $(this).combobox("select", record.cityId);
            }
        });
	});
	$.getJSON("${app}/admin/json/findAllPlayAddress",function(data){
		$('#addressId').combobox({
			data:data,
            valueField:'id', 
            textField:'value',
            editable:false,
            onLoadSuccess: function () { //加载完成后,设置选中第一项
            	$(this).combobox("select", record.playAddressId);
            }
        });
	});
	$.getJSON("${app}/admin/json/findDictByCode?code=OFFLINECOURSETYPE",function(data){
		$('#offlineTypeId').combobox({
			data:data,
            valueField:'id', 
            textField:'value',
            editable:false,
            onLoadSuccess: function () {
            	$(this).combobox("select", record.offlineTypeId);
            }
        });
	});

	$("#coverFile").attr("value","");
	$("#avatarFile").attr("value","");
	$("#avatarShow").attr("src",record.avatar);
	$("#coverShow").attr("src",record.cover);
	new uploadPreview({ UpBtn: "coverFile", DivShow: "coverDiv", ImgShow: "coverShow" });
	new uploadPreview({ UpBtn: "avatarFile", DivShow: "avatarDiv", ImgShow: "avatarShow" });
	
	changeType(record.type);
	$("#courseName").val(record.courseName);
	$("#beginTime").datebox('setValue',record.beginTime);
	$("#playBeginTime").datetimebox('setValue',record.playBeginTime);
	$("#playEndTime").datetimebox('setValue',record.playEndTime);
	$("#isVideo").combobox('setValue',record.isVideo);
	$("#courseType1").combobox('setValue',1);
	$("#courseWindow").window("open");
}

//添加教师姓名
function addTeacherName(){
	var value = $('#teacherList').combobox('getValue');
	var text = $("#teacherList").combobox('getText');
	
	var values = $("#teacherIds").val();
	var texts = $("#teacherName").val();
	
	if(texts == ''){
		$("#teacherName").val(text);
	}else{
		$("#teacherName").val(texts + ","+ text);
	}
	
	if(values == ''){
		$("#teacherIds").val(value);
	}else{
		$("#teacherIds").val(values + "," + value);
	}
}

function showTeacherWindow(){
	$.getJSON("${app}/admin/json/findAllMember",function(data){
		$('#teacherList').combobox({
			data:data,
            valueField:'id', 
            textField:'value',
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
	$("#teacherIds").val($("#teacherId").val());
	$("#teacherName").val($("#teacherNames").val());
	$("#teacherWindow").window("open");
	
}
//保存选择的讲师
function saveTeacher(){
	var ids = $("#teacherIds").val();
	var names = $("#teacherName").val();
	
	$("#teacherId").val(ids);
	$("#teacherNames").val(names);
	$("#teacherWindow").window("close");
}

//百度地图
var map = new BMap.Map("container");
map.centerAndZoom("北京", 12);
map.enableScrollWheelZoom();    //启用滚轮放大缩小，默认禁用
map.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用
map.addControl(new BMap.NavigationControl());  //添加默认缩放平移控件
map.addControl(new BMap.OverviewMapControl()); //添加默认缩略地图控件
var localSearch = new BMap.LocalSearch(map);
localSearch.enableAutoViewport(); //允许自动调节窗体大小

//查询坐标 
function searchByStationName(){
	map.clearOverlays();//清空原来的标注
  	var keyword =$("#address").val();
  	localSearch.setSearchCompleteCallback(function (searchResult) {
      var poi = searchResult.getPoi(0);
      $("#lon").val(poi.point.lng);
      $("#lat").val(poi.point.lat);
      map.centerAndZoom(poi.point, 13);
      var marker = new BMap.Marker(new BMap.Point(poi.point.lng, poi.point.lat));  // 创建标注，为要查询的地址对应的经纬度
      map.addOverlay(marker);
  });
  localSearch.search(keyword);
}
//保存课程信息
function saveCourseInfo(){
	var courseName = $("#courseName").val();
	if(courseName == ""){
		$.messager.alert("系统提示","请填写课程名称!","warning");
		return;
	}
	var beginTime = $("#beginTime").datebox('getValue');
	if(beginTime == ""){
		$.messager.alert("系统提示","请选择课程开始时间!","warning");
		return;
	}
	var courseType1 = $("#courseType1").combobox('getValue');
	if(courseType1 != '1' && courseType1 != '0'){
		$.messager.alert("系统提示","请选择课程类型!","warning");
		return;
	}
	if(courseType1 == '0'){
		var price = $("#price").val();
		if(price == ""){
			$.messager.alert("系统提示","请选择课程开始时间!","warning");
			return;
		}
		if(!/^[0-9]+\.?[0-9]*/.test(price)){
			$.messager.alert("系统提示","价格格式不正确!","warning");
			return;
		}
		var cityId = $("#cityId").combobox('getValue');
		if(cityId == ''){
			$.messager.alert("系统提示","请选择城市!","warning");
			return;
		}
		var teacherId = $("#teacherId").val();
		if(teacherId == ''){
			$.messager.alert("系统提示","请选择讲师!","warning");
			return;
		}
		var address = $("#address").val();
		if(address == ''){
			$.messager.alert("系统提示","请输入地址!","warning");
			return;
		}
		var lat = $("#lat").val();
		var lon = $("#lon").val();
		if(lat == ''){
			$.messager.alert("系统提示","请输入经度!","warning");
			return;
		}
		if(lon == ''){
			$.messager.alert("系统提示","请输入纬度!","warning");
			return;
		}
		var offlineTypeId = $("#offlineTypeId").combobox('getValue');
		if(offlineTypeId == ''){
			$.messager.alert("系统提示","请选择课程类型!","warning");
			return;
		}
		if(!ue.hasContents()){
			$.messager.alert("系统提示","请输入课程简介!","warning");
			return;
		}
		$("#courseContent").val(ue.getContent());
	}else{
		var playBeginTime = $("#playBeginTime").datetimebox('getValue');
		var playEndTime = $("#playEndTime").datetimebox('getValue');
		if(playBeginTime == ''){
			$.messager.alert("系统提示","请选择直播开始时间!","warning");
			return;
		}
		if(playEndTime == ''){
			$.messager.alert("系统提示","请选择直播结束时间!","warning");
			return;
		}
		var addressId = $("#addressId").combobox('getValue');
		if(addressId == ''){
			$.messager.alert("系统提示","请选择直播地址!","warning");
			return;
		}
		/* var isVideo = $("#isVideo").combobox('getValue');
		if(isVideo == ''){
			$.messager.alert("系统提示","请选择直播类型!","warning");
			return;
		} */
	}
	var options ={
			url:"${app}/admin/json/saveCourseInfo",
			success : function(data) {
				if(checkData(data)){
					$("#courseWindow").window('close');
					findCourse();
				}
			}
		};
	$("#form").ajaxSubmit(options);		
	//清除选中行
	$('#courseid').datagrid('clearSelections');
}

$("#courseType1").combobox({
	editable:false,
	onChange:function(newValue,oldValue){
		changeType(newValue);
    }
});
$("#courseType").combobox({
	onChange:function(newValue,oldValue){
		if(newValue == 0){
			window.location.href="${app}/admin/offline";
		}
    }
});
function showCourseWindow(){
	$('#form').form('clear');
	$("#addressTr").hide();
	$("#offlineTypeTr").hide();
	$("#playBeginTimeTr").hide();
	$("#playEndTimeTr").hide();
	$("#nullid1").hide();
	$("#nullid2").hide();
	$("#nullid3").hide();
	$("#nullid4").hide();
	$("#nullid5").hide();
	$("#courseTimeTr").hide();
	$("#nullid6").hide();
	$("#cityIdTr").hide();
	$("#lonTr").hide();
	$("#latTr").hide();
	$("#priceTr").hide();
	$("#studentNumTr").hide();
	$("#teacherIdTr").hide();
	//$("#isVideoTr").hide();
	$("#infoTr").hide();
	$("#nullTr1").hide();
	$("#playaddressTr").hide();
	
	$("#avatarShow").attr("src","");
	$("#coverShow").attr("src","");
	
	
	$("#coverFile").attr("value","");
	$("#avatarFile").attr("value","");
	new uploadPreview({ UpBtn: "coverFile", DivShow: "coverDiv", ImgShow: "coverShow" });
	new uploadPreview({ UpBtn: "avatarFile", DivShow: "avatarDiv", ImgShow: "avatarShow" });
	
	$.getJSON("${app}/admin/json/findAllCity",function(data){
		$('#cityId').combobox({
			data:data,
            valueField:'id', 
            textField:'value',
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
	
	$.getJSON("${app}/admin/json/findAllPlayAddress",function(data){
		$('#addressId').combobox({
			data:data,
            valueField:'id', 
            textField:'value',
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
	$.getJSON("${app}/admin/json/findDictByCode?code=OFFLINECOURSETYPE",function(data){
		$('#offlineTypeId').combobox({
			data:data,
            valueField:'id', 
            textField:'value',
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
	$("#courseWindow").window("open");
}

function changeType(type){
	 if(type == '1'){
    	$("#addressTr").hide();
    	$("#offlineTypeTr").hide();
    	$("#playBeginTimeTr").show();
    	$("#playEndTimeTr").show();
    	$("#nullid3").show();
    	$("#nullid4").show();
    	$("#nullid1").hide();
    	$("#nullid2").hide();
    	$("#nullid5").hide();
    	$("#courseTimeTr").hide();
    	$("#nullid6").hide();
		$("#cityIdTr").hide();
		//$("#isVideoTr").show();
		$("#lonTr").hide();
		$("#latTr").hide();
		$("#priceTr").hide();
		$("#studentNumTr").hide();
		$("#teacherIdTr").hide();
		$("#infoTr").hide();
		$("#nullTr1").show();
		$("#playaddressTr").show();
    }else{
    	$("#addressTr").show();
    	$("#offlineTypeTr").show();
    	$("#playBeginTimeTr").hide();
    	$("#playEndTimeTr").hide();
    	$("#nullid3").hide();
    	$("#nullid4").hide();
    	$("#nullid1").show();
    	$("#nullid2").show();
    	$("#nullid5").show();
    	$("#courseTimeTr").show();
    	$("#nullid6").show();
		$("#cityIdTr").show();
		$("#lonTr").show();
		$("#latTr").show();
		$("#teacherIdTr").show();
		$("#studentNumTr").show();
		$("#priceTr").show();
		//$("#isVideoTr").hide();
		$("#infoTr").show();
		$("#nullTr1").hide();
		$("#playaddressTr").hide();
    }
}
//查询课程
function findCourse(){
	 $('#courseid').datagrid({
	        height: 'auto',  
	        nowrap: false,  
	        striped: true,  
	        border: true,  
	        collapsible:false,//是否可折叠的  
	        fit: true,//自动大小  
	        url:'${app}/admin/json/findCourseList?type=1',  
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
	    var p = $('#courseid').datagrid('getPager');  
	    $(p).pagination({  
	        beforePageText: '第',//页数文本框前显示的汉字  
	        afterPageText: '页    共 {pages} 页',  
	        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',  
	    });  
	    $('#courseid').datagrid('clearSelections');
}
function deleteCourse(){
	record = $("#courseid").datagrid("getSelected");
	if(record==null){
		$.messager.alert("系统提示","请选择记录!","warning");
		return;
	}
	$.messager.confirm('系统提示', '确定要删除吗?', function(r){
		if (r){
			$.getJSON("${app}/admin/json/deleteCourse?id="+record.id+'&type=1',function(data){
				if(checkData(data)){
					findCourse();
				}
			});			
		}
	});
}
function saveFile(){
	var videoFile = $("#videoFile").val();
	if(videoFile == null){
		$.messager.alert("系统提示","请选择文件!","warning");
		return;
	}
	var timeLength = $("#timeLength").val();
	if(timeLength == ''){
		$.messager.alert("系统提示","请添加时长!","warning");
		return;
	}
	var options ={
			url:"${app}/admin/json/saveCourseFile",
			success : function(data) {
				if(checkData(data)){
					$("#fileWindow").window('close');
					findCourse();
				}
			}
		};
	$("#fileForm").ajaxSubmit(options);		
	//清除选中行
	$('#courseid').datagrid('clearSelections');
}
function showFileWindow(){
	$('#fileForm').form('clear');
	record = $("#courseid").datagrid("getSelected");
	if(record==null){
		$.messager.alert("系统提示","请选择记录!","warning");
		return;
	}
	var endtime = new Date(record.playEndTime);
	if(endtime > new Date()){
		$.messager.alert("系统提示","此课程未未到结束时间!","warning");
		return;
	}
	$("#fileCourseId").val(record.id);
	$("#courseNameStr").val(record.courseName);
	$("#fileWindow").window("open");
}
</script>
</html>