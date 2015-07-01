<div class="seach">
	<form id="searchFrom" class="form-inline" role="form" name="searchFrom" method="get" action="">
		<div class="form-group">
			<label for="" class="sr-only">name</label>
			<@s.textfield name="searchString.name" cssClass="form-control" placeholder="name搜索"/>
		</div>
		
		<div class="form-group">
			<button class="btn btn-success" type="submit" name="searchString.submit" value="search">
		  		查询
		  	</button>
		</div>
	</form>
</div>

<#include "../public/sniper_menu.ftl" >

<table class="table table-hover">
	<thead>
		<tr>
			<th>ID</th>
			<th>Key</th>
			<th>Value</th>
			<th>AutoLoad</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<#list lists as l>
			<tr id="sl_${l.id}">
				<td><input type="checkbox" name="list.id" value="${l.id}" /><a href="admin/admin-config/update?id=${l.id}" target="_blank">${l.id}</a></td>
				<td>${l.keyName}(${l.keyInfo})</td>
				<td>${l.keyValue}</td>
				<td>${l.autoload?string('yes','no')}</td>
			</tr>
			</#list>

	</tbody>
</table>
<div class="meneame">${pageHtml }</div>
