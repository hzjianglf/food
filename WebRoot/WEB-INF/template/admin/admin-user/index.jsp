<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="seach">
	<form id="searchFrom" class="form-inline" role="form" name="searchFrom" method="get" action="">
		
		<div class="form-group">
			<label for="" class="sr-only">name</label>
		</div>
		<div class="form-group">
			<button class="btn btn-success" type="submit" name="submit" value="search">
		  		查询
		  	</button>
		</div>
	</form>
</div>

<c:import url="../public/sniper_menu.jsp" />

<table class="table table-hover">
	<thead>
		<tr>
			<th>ID</th>
			<th>username(nickname)</th>
			<th>email</th>
			<th>enabled</th>
			<th>usernameExpired</th>
			<th>passwordExpired</th>
			<th>locked</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="l" items="${lists }">
		<tr id="sl_${l.id}">
			<td><input type="checkbox" name="list.id" value="${l.id}" />${l.id}</td>
			<td><a href="admin/admin-user/update?id=${l.id}" target="_blank">${l.name}</a>(${l.nickName})</td>
			<td><a href="mailto:${l.email}">${l.email}</a></td>
			<td>${l.enabled}</td>
			<td>${l.usernameExpired}</td>
			<td>${l.passwordExpired}</td>
			<td>${l.locked}</td>
		</tr>
		</c:forEach>
	
	</tbody>
</table>
<div class="meneame">${pageHtml }</div>

