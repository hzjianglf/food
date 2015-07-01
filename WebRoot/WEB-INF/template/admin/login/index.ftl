<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>登录</title>
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
	max-width: 330px;
	padding: 15px;
}
</style>

</head>
<body>
	<div class="container">
		
		<form data-status=''  class="form-signin" role="form" name="login" action="login_check" method="post">
			<h2 class="form-signin-heading">登录</h2>
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<#if loginError??>
				<div class="alert alert-warning alert-dismissible" role="alert">
					<button type="button" class="close" data-dismiss="alert">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					
					${loginError}
					
				</div>
			</#if>
			<div class="form-group input-group-lg">
				<label for="username">用户名</label>
				<input type="text" id="username" value="${lastLoginName!''}" name="username" class="form-control" placeholder="" required autofocus>
			</div>
			<div class="form-group input-group-lg">
				<label for="password">密码</label>
				<input id="password" type="password" name="password" class="form-control" placeholder="" name="" required>
			</div>
			
			<#if loginNum?? && loginNum?eval gt 300 >
			<div class="form-group input-group-lg">
				<label for="verifycode" class="col-sm-2 control-label sr-only">
					验证码
				</label>
				<input type="text" name="sessionVerifyName" style=" display: inline;width: 44%;  float: left;" placeholder=""
					id="verifycode" class="form-control">
					<img alt="" style="cursor: pointer; margin-left:2%" src="verify" class="fl">
			</div>
			</#if>
			
			<div class="form-group input-group-lg">
				<label class="">
				<input type="checkbox" name="remember-me"> Remember me
				</label>
			</div>

			<button class="btn btn-lg btn-primary btn-block" type="submit">
				登录
			</button>
			<p class="text-muted pull-right"><a href="password/getPassword">忘记密码?</a></p>
		</form>
	</div>
	<!-- /container -->

	<script language="javascript">

	$(function() {
			$('form img').click(function() {
				fleshVerify();
			});
		});

		
		function fleshVerify() {
			var timenow = new Date().getTime();
			var src = $('form img').attr("src");
			var indexof = src.indexOf("?");
			if (indexof != -1) {
				src = src.substring(0, src.indexOf("?"));
			}
			$('form img').attr("src", src + '?d=' + timenow);
		}
		
		
	</script>

</body>
</html>