<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
<title></title>
</head>

<body>
	<div class="seach">
		<form:form action="" role="form" cssClass="form-inline" method="get"
			modelAttribute="groupSearch">
			<div class="form-group">
				<label for="" class="sr-only">${ sniperMenu.getKeyValue("theShow")}</label>
				<form:select title="${ sniperMenu.getKeyValue('theShow')}"
					path="isShow" cssClass="form-control"
					items="${sniperMenu.params.theShow }" />
			</div>
			<div class="form-group">
				<label for="" class="sr-only">${ sniperMenu.getKeyValue("theMenu")}</label>
				<form:select title="${sniperMenu.getKeyValue('theMenu')}"
					path="isMenu" cssClass="form-control"
					items="${sniperMenu.params.theMenu }"/>
			</div>
			<div class="form-group">
				<label for="" class="sr-only">名称</label>
				<form:input title="名称" path="groupName" cssClass="form-control" />
			</div>

			<div class="form-group">
				<label for="" class="sr-only">url</label>
				<form:input title="url" path="url" cssClass="form-control" />
			</div>

			<div class="form-group">
				<button class="btn btn-success" type="submit" name="submit"
					value="search">查询</button>
			</div>

		</form:form>
	</div>
	<c:import url="../public/sniper_menu.jsp" />
	<table class="table table-hover">
		<thead>
			<tr>
				<th>id</th>
				<th>name</th>
				<th>url</th>
				<th>sort</th>
				<th>theMenu</th>
				<th>thePublic</th>
				<th>theShow</th>
				<th>fid</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="l" items="${lists }">
				<tr id="sl_${l.id}">
					<td><input type="checkbox" name="list.id" value="${l.id}" /><a
						href="admin/admin-right/update?id=${l.id}" target="_blank">${l.id}</a></td>
					<td data-type="name">${l.name}</td>
					<td data-type="url">${l.url}</td>
					<td data-type="sort">${l.sort}</td>
					<td data-type="theMenu">${l.theMenu}</td>
					<td data-type="thePublic">${l.thePublic}</td>
					<td data-type="theShow">${l.theShow}</td>
					<td data-type="fid">${l.fid}</td>
				</tr>
			</c:forEach>

		</tbody>
	</table>
	<div class="meneame">${pageHtml }</div>
</body>
</html>