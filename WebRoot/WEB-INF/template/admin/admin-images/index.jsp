<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="seach">
	<form:form action="" role="form" cssClass="form-inline" method="get"
		modelAttribute="search">
		
		<div class="form-group">
			<label for="" class="sr-only">name</label>
			<form:input path="name" cssClass="form-control" />
		</div>
		<div class="form-group">
			<button class="btn btn-success" type="submit" name="submit" value="search">
		  		查询
		  	</button>
		</div>
	</form:form>
</div>

<c:import url="../public/sniper_menu.jsp" />
<div id="layer-photos" class="row">
 <c:forEach var="l" items="${lists }">
 	<div class="col-sm-6 col-md-4">
      <img class="img-thumbnail" layer-pid="${l.id }" layer-src="${systemConfig.imagePathPrefx}${l.newPath }" src="${systemConfig.imagePathPrefx}${l.newPath }" alt="${l.oldName }">
 		<div class="caption">
        <h3>${l.oldName }</h3>
        <p>${l.tags }</p>
        <p><a href="#" class="btn btn-primary" role="button">编辑</a></p>
      </div>
 	</div>
 </c:forEach>
</div>
<div class="meneame">${pageHtml }</div>

<script src="myfiles/Plugin/layer-v1.9.3/layer/layer.js"></script>
<script type="text/javascript">

//加载扩展模块
layer.config({
    extend: 'extend/layer.ext.js'
});


//页面一打开就执行，放入ready是为了layer所需配件（css、扩展模块）加载完毕
layer.ready(function(){ 
    
    //使用相册
    layer.photos({
        photos: '#layer-photos'
    });
});
  
</script>
