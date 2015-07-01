<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title><@s.text name="login.sign.in" /></title>
<base href="${request.scheme }://${request.serverName}:#{request.serverPort}${request.contextPath}/">
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
		<#assign errorNum =0>
		
		<#if request.getCookies()??>
		<#assign cookies = request.getCookies()>
		<#list cookies as cookie>
			<#if (cookie.name == "SPRING_SECURITY_LOGIN_ERROR_NUM")>
		    	<#assign errorNum = cookie.value >
		    </#if>
		</#list>
		</#if>
		<form data-status='<@s.text name="login.loading" />'  class="form-signin" role="form" name="login" action="login_check" method="post">
			<h2 class="form-signin-heading"><@s.text name="login.sign.in" /></h2>
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<#if Session?? && Parameters.error?exists && Parameters.error = "true">
				<div class="alert alert-warning alert-dismissible" role="alert">
					<button type="button" class="close" data-dismiss="alert">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					
					<#if Session.SPRING_SECURITY_LAST_EXCEPTION.message?exists> 
						${Session.SPRING_SECURITY_LAST_EXCEPTION.message}
					<#else>
						<@s.text name="login.message.error.fiald"/>
					</#if>
					
				</div>
			</#if>
			
			<div class="form-group input-group-lg">
				<label for="username"><@s.text name="login.username" /></label>
				<input type="text" id="username" value="${usernameKey!''}" name="username" class="form-control" placeholder="<@s.text name="login.username"/>" required autofocus>
			</div>
			<div class="form-group input-group-lg">
				<label for="password"><@s.text name="login.password" /></label>
				<input id="password" type="password" name="password" class="form-control" placeholder="<@s.text name="login.password"/>" required>
			</div>
			
			<#if errorNum?eval gt 3 >
			<div class="form-group input-group-lg">
				<label for="verifycode" class="col-sm-2 control-label sr-only">
					<@s.text name="login.message.verity.code" />
				</label>
				<input type="text" name="sessionVerifyName" style=" display: inline;width: 44%;  float: left;" placeholder="<@s.text name="login.message.verity.code"/>"
					id="verifycode" class="form-control">
					<img alt="<@s.text name="login.message.verity.alt"/>" style="cursor: pointer; margin-left:2%" src="<@s.url action="verify" namespace="/" />" class="fl">
			</div>
			</#if>
			
			<div class="form-group input-group-lg">
				<label class="">
				<input type="checkbox" name="_spring_security_remember_me"> Remember me
				</label>
			</div>

			<button class="btn btn-lg btn-primary btn-block" type="submit">
				<@s.text name="login.sign.name" />
			</button>
			<p class="text-muted pull-right"><a href="password/getPassword">忘记密码?</a></p>
		</form>
	</div>
	<!-- /container -->

	<div id="back-mask" class="back-mask"></div>
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
		
		//闪烁
		function shake(ele, cls, times) {
			var i = 0, t = false, o = ele.attr("class") + " ", c = "", times = times || 2;
			if (t)
				return;
			t = setInterval(function() {
				i++;
				c = i % 2 ? o + cls : o;
				ele.attr("class", c);
				if (i == 2 * times) {
					clearInterval(t);
					ele.removeClass(cls);
				}
			}, 200);
		};

		var images = [];

		$(images).each(function() {
			$('<img/>')[0].src = this;
		});
		var index = 0;
		$.backstretch(images[index], {
			speed : 800
		});
		setInterval(function() {
			index = (index >= images.length - 1) ? 0 : index + 1;
			$.backstretch(images[index]);
		}, 5000);
	</script>

</body>
</html>