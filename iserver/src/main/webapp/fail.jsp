<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="UTF-8">
<title>404</title>
<link rel="stylesheet" type="text/css"
	href="${app}/css/cmstop-error.css" media="all">
</head>
<body class="body-bg">
	<div class="main">
		<p class="title">
			<c:choose>
				<c:when test="${msg!=null}">${msg}</c:when>
				<c:when test="${msg==null}">非常抱歉，您要查看的页面没有办法找到。</c:when>
			</c:choose>
		</p>
		<a href="javascript:void(0);" onclick="javascript:history.go(-1);"
			class="btn">后退</a>
	</div>
</body>
</html>