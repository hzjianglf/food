<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class="seach">
	<form:form action="" role="form" cssClass="form-inline" method="get"
		modelAttribute="channelSearch">
		<div class="form-group">
			<label for="" class="sr-only">状态</label>
			<form:select path="status" cssClass="form-control" >
				<form:option value=""></form:option>
				<form:options items="${sniperMenu.getMapValue('status') }"/>
			</form:select>
		</div>

		<div class="form-group">
			<label for="" class="sr-only">类型</label>
			<form:select path="type" cssClass="form-control">
				<form:option value=""></form:option>
				<form:options items="${sniperMenu.getMapValue('type') }"/>
			</form:select>
		</div>

		<div class="form-group">
			<label for="" class="sr-only">名称</label>
			<form:input title="名称" path="name" cssClass="form-control" />
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
			<th>名称(FID)</th>
			<th>状态</th>
			<th>首页左侧显示</th>
			<th>类型</th>
			<th>排序</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="l" items="${lists }">
			<tr id="sl_${l.id}">
				<td><input type="checkbox" name="list.id" value="${l.id}" /><a
					href="admin/admin-channel/update?id=${l.id}" target="_blank">${l.id}</a></td>
				<td data-type="name">${l.name}(${l.fid})</td>
				<td data-type="status">${l.status}</td>
				<td data-type="status">${l.showHome}</td>
				<td data-type="showType">${html.getKeyValue(l.showType)}</td>
				<td data-type="sort">${l.sort}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div class="meneame">${pageHtml }</div>
