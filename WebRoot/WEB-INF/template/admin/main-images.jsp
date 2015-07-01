<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title><sitemesh:write property="title" />${baseHref.pageTitle}
	- | 后台管理</title>
<sitemesh:write property='head' />
<base href="${baseHref.baseHref }">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<link href="myfiles/Plugin/Bootstrap/css/bootstrap.min.css"
	media="screen" rel="stylesheet" type="text/css">
<link href="myfiles/Plugin/Bootstrap/css/bootstrap-theme.min.css"
	media="screen" rel="stylesheet" type="text/css">
<link href="myfiles/Plugin/font-awesome-4.3.0/css/font-awesome.min.css"
	media="screen" rel="stylesheet" type="text/css">
<link href="myfiles/css/admin.css" media="screen" rel="stylesheet"
	type="text/css">
<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
  <script src="${basePath }myfiles/Plugin/Bootstrap/js/html5shiv.min.js"></script>
  <script src="${basePath }myfiles/Plugin/Bootstrap/js/respond.min.js"></script>
<![endif]-->

<script type="text/javascript" src="myfiles/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="myfiles/js/js.config.js"></script>
<script type="text/javascript"
	src="myfiles/Plugin/Bootstrap/js/bootstrap.min.js"></script>
<style type="text/css">
.navbar {
	margin-bottom: 20px;
}

#bs-example-navbar-collapse-1 {
	background: url("myfiles/images/images/top.jpg") repeat scroll 0 0
		rgba(0, 0, 0, 0);
}

.navbar-default .navbar-brand {
	color: white
}

.navbar-default .navbar-nav>li>a {
	color: white
}

body {
	background: none repeat scroll 0 0 #f3f3f3;
}
</style>
</head>

<body>
	<nav class="navbar navbar-default" role="navigation">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#bs-example-navbar-collapse-1">
				<span class="sr-only">${systemConfig.webName }</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="${baseHref.baseHref }" target="_blank">${systemConfig.webName}</a>
		</div>

		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse"
			id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li class="active"><a href="javascript:;">${baseHref.adminRight.name }</a></li>
				<li ><a href="admin/admin-images/upload">上传</a></li>
			</ul>
			 <form class="navbar-form navbar-left" role="search">
		        <div class="form-group">
		          <input type="text" class="form-control" value="${key }" name="key" placeholder="Search">
		        </div>
		        <button type="submit" class="btn btn-default">查询</button>
		      </form>

			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown">Hello, <sec:authentication
							property="name" /> <b class="caret"></b></a>
					<ul class="dropdown-menu">
						<li><a href="admin/admin-user/changepassword">更改密码</a></li>
						<li class="divider"></li>
						<li><a href="logout?service=<c:out value="${systemConfig.casLogoutPage }" />">退出</a></li>
					</ul>
				</li>
			</ul>
		</div>
		<!-- /.navbar-collapse -->
	</nav>
	<div class="container">
		<!-- s -->
		<!-- 从被装饰页面获取body标签内容 -->
		<sitemesh:write property='body' />
		<!-- e -->
	</div>

	<div id="footer" class="bs-footer" role="contentinfo">
		<div class="container">
			<p class="text-right">Thanks for using(${endTime})</p>
			<p id="footer-upgrade" class="text-right">Version 1.0</p>
		</div>
	</div>
</body>
</html>