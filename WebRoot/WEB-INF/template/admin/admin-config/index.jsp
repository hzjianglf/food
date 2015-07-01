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
				<label for="" class="sr-only">${ sniperMenu.getKeyValue("autoload")}</label>
				<form:select title="${ sniperMenu.getKeyValue('autoload')}"
					path="autoload" cssClass="form-control"
					items="${sniperMenu.params.autoload }" />
			</div>

			<div class="form-group">
				<label for="" class="sr-only">名称</label>
				<form:input title="名称" path="groupName" cssClass="form-control" />
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
				<th>ID</th>
				<th>调用的KEY</th>
				<th>值</th>
				<th>自动加载</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="l" items="${lists }">
				<tr id="sl_${l.id}">
					<td><input type="checkbox" name="list.id" value="${l.id}" /><a
						href="admin/admin-config/update?id=${l.id}" target="_blank">${l.id}</a></td>
					<td data-type="name">${l.keyName}</td>
					<td data-type="url">${l.keyValue}</td>
					<td data-type="autoload">${l.autoload}</td>
				</tr>
			</c:forEach>

		</tbody>
	</table>
	<div class="meneame">${pageHtml }</div>
</body>
</html>