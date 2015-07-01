<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<base href="<%=basePath%>">

<title>404</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

</head>
<style type="text/css">
BODY {
	background-color: #bfbfbf;
}
</style>
<body>
	<center>
		<a href="./"><img src="myfiles/images/404/404.gif" border="0"
			style="position:relative; top:100px;"></a>
	</center>
</body>
</html>
