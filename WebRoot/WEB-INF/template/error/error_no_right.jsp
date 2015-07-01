<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ page trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>Access Denied</title>
<base href="${pageContext.request.scheme }://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<script type="text/javascript" src="myfiles/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="myfiles/Plugin/Bootstrap/js/bootstrap.min.js"></script>
<link href="myfiles/Plugin/Bootstrap/css/bootstrap.min.css" media="screen" rel="stylesheet" type="text/css">
<link href="myfiles/Plugin/Bootstrap/css/bootstrap-theme.min.css" media="screen" rel="stylesheet" type="text/css">

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
	<script src="myfiles/Plugin/Bootstrap/js/html5shiv.min.js"></script>
<![endif]-->
<style type="text/css">

</style>
</head>
<body>
<div class="container">
	<div class="alert alert-warning" role="alert">
		<security:authentication property="name"/>
		<security:csrfInput/>
		<p>警告没有权限访问.</p>
		<p>更换帐号 <a href="admin/login">登录</a></p>
		<p>如果管理员给了你权限,但是你无法查看,请让管理员清空缓存.位置:系统管理->缓存管理->第一行选中->点击执行ok.</p>
		<h1>Sorry, access is denied</h1>
	</div>
</div><!-- /container -->

</body>
</html>