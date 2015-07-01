<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>更改密码</title>
<base href="${baseHref.baseHref}">
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

<script type="text/javascript" src="myfiles/js/jquery.backstretch.min.js"></script>
<style type="text/css">
.form-signin {
	margin: 0 auto;
	max-width: 450px;
	padding: 15px;
}
</style>

</head>
<body>
	<div class="container">
		<div class="page-header">
		  <h1>找回密码</h1>
		</div>
		<div class="row">
		<#if messages??>
			<div class="alert alert-warning alert-dismissible" role="alert">
				<button type="button" class="close" data-dismiss="alert">
					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>
				<ul>
				<#list messages as am>
				<li>${am!''}</li>
				</#list>
				</ul>
			</div>
		<#else>
		<form class="form-signin" role="form" name="login" action="" method="post">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			
			<div class="form-group input-group-lg">
				<label for="password">新密码</label>
				<input type="password" id="password" name="password" class="form-control" required autofocus>
			</div>
			<div class="form-group input-group-lg">
				<label for="passwordC">确认新密码</label>
				<input id="passwordC" type="password" name="passwordC" class="form-control" required>
			</div>
			
			<button class="btn btn-lg btn-primary btn-block" type="submit">
				更改密码
			</button>
		</form>
		</#if>
		<div>
	</div>
	<!-- /container -->
</body>
</html>