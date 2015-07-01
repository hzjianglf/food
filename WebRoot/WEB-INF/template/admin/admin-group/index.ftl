<#include "../public/sniper_menu.ftl">

<table class="table table-hover">
	<thead>
		<tr>
			<th>ID</th>
			<th>名称</th>
			<th>值</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<#list lists as l>
			<tr id="sl_${l.id}">
				<td><input type="checkbox" name="list.id" value="${l.id}" />${l.id}</td>
				<td><a href="admin/admin-group/update?id=${l.id}" target="_blank">${l.name}</a></td>
				<td>${l.value}</td>
			</tr>
			</#list>
	</tbody>
</table>
<div class="meneame">${pageHtml }</div>
