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

<#include "../public/sniper_menu.ftl">

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
		<#list lists as l>
		<tr id="sl_${l.id}">
			<td><input type="checkbox" name="list.id" value="${l.id}" />${l.id}</td>
			<td><a href="admin/admin-user/update?id=${l.id}" target="_blank">${l.name}</a>(${l.nickName})</td>
			<td><a href="mailto:${l.email}">${l.email}</a></td>
			<td>${l.enabled?string('yes','no')}</td>
			<td>${l.usernameExpired!''}</td>
			<td>${l.passwordExpired!''}</td>
			<td>${l.locked?string('yes','no')}</td>
		</tr>
		</#list>
	
	</tbody>
</table>
<div class="meneame">${pageHtml }</div>

