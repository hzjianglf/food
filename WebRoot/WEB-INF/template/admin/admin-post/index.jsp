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

<table class="table table-hover">
	<thead>
		<tr>
			<th>编辑</th>
			<th>标题</th>
			<th>作者</th>
			<th>状态</th>
			<th>栏目</th>
			<th>发布时间</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="l" items="${lists }">
		<tr id="sl_${l.id}">
			<td><input type="checkbox" name="list.id" value="${l.id}" /><a href="admin/admin-post/update?id=${l.id}" target="_blank">${l.id}</a></td>
			<td>${l.name}</td>
			<td>${l.adminUser }</td>
			<td>${l.status }</td>
			<td>${l.channels }</td>
			<td>${l.stime }</td>
		</tr>
		</c:forEach>
	
	</tbody>
</table>
<div class="meneame">${pageHtml }</div>

