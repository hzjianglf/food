<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title><sitemesh:write property="title" /> ${adminUser.name}的个人中心 | ${systemConfig.webName }</title>
<base href="${baseHref.baseHref }">
<link href="favicon.ico" rel="shortcut icon" type="image/vnd.microsoft.icon">
<link rel="stylesheet" type="text/css" href="myfiles/ppt/css/css.css">
<link rel="stylesheet" type="text/css" href="myfiles/ppt/css/style.css">
<link rel="stylesheet" type="text/css" href="myfiles/ppt/css/bootstrap.css">
<link rel="stylesheet" type="text/css" href="myfiles/ppt/css/font-awesome.min.css">
<script src="myfiles/ppt/js/jquery-1.11.3.min.js"></script>
<script src="myfiles/ppt/js/huli.js"></script>
<sitemesh:write property='head' />
</head>

<body>
<div class="top">
  <h1 class="logo"><a href="./">${systemConfig.webName }</a></h1>
  <div class="soft">
  	<form action="user/ppts" method="get">
	  	<input type="text" class="soft_text" name="key" value='<c:if test="${key != null }">${key }</c:if>'>
	  	<input type="submit" value="搜索" class="soft_bot">
	  	<a href="types">按分类查找</a>
  	</form>
  </div>
  <div class="login">
   	<a href="user">${adminUser.name }</a>
   	<a href="logout?service=<c:out value="${systemConfig.casLogoutPage }" />">退出</a>
  </div>
  <div class="nav">
	<ul>
    	<li><a href="./"><i class="fa fa-home"></i><br>首页</a></li>
    	<li><a href="types"><i class="fa fa-th-list"></i><br>分类检索</a></li>
    	<c:if test="${channelsLeftTop != null }">
    	<c:forEach items="${channelsLeftTop }" var="ct">
    	<li><a href="javascript:;"><i class="fa fa-comments"></i><br>${ct.name}</a>
        	<div class="subnav">
            	<div class="subnav_title">
                  <c:choose>
				  	<c:when test="${ct.attachement != null}">
				  		<img title="${ct.name}" src="${ct.attachement}">
				  	</c:when>
				  	<c:otherwise>
				   	<h2>${ct.name}</h2>
                  		 <p>${ct.note}</p>
				    </c:otherwise>
			  	</c:choose>
                </div>
                <ul>
                	<c:if test="${channelsLeft!= null }">
                	<c:set var="kid" >
                		<c:out value="${ct.id }" />
                	</c:set>
                	<c:set var="clts" value="${channelsLeft[kid] }" />
                	<c:forEach items="${clts }" var="clt">
                	<li><a href="ppt?id=${clt.id}">${clt.name}</a></li>
                	</c:forEach>
                	</c:if>
                </ul>
        	 </div>
        </li>
        </c:forEach>
        </c:if>
        <li><a href="user/ppts/insert"><i class="fa fa-file-o"></i><br>上传PPT</a></li>
        <li><a href="user/ppts"><i class="fa fa-user"></i><br>我的PPT</a></li>
    </ul>
  </div>
</div>
<div class="main">
	<div class="user_data">
    	<span class="user_hs"><img src="myfiles/ppt/images/touxiang.jpg" width="145" height="145" alt=""/></span>
        <ul class="user_data_editor">
        	<li><span><img src="myfiles/ppt/images/user1.jpg" width="16" height="16" alt=""/></span><a href="#">修改资料</a></li>
        	<li><span><img src="myfiles/ppt/images/user2.jpg" width="16" height="16" alt=""/></span><a href="#">修改头像</a></li>
        </ul>
        <ul class="user_data_list">
        	<li><span class="user_name">用户名</span></li>
        	<li><span class="user_grade">14</span>积分</li>
        </ul>
    </div>
    <div class="home_banner">
    	<img src="myfiles/ppt/images/home_banner.jpg" width="750" height="275" alt=""/>
    </div>
	<sitemesh:write property='body' />
</div>
</body>
</html>