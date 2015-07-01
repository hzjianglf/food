<form id="searchFrom" class="form-horizontal" role="form" name="searchFrom"
	method="post" action="">
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	<div class="form-group">
		<button class="btn btn-success" type="submit" name="submit" value="search">
	  		执行
	  	</button>
	</div>
	<#list keyMap.getKeyMap()?keys as key>
	<div class="checkbox">
	  <label>
	    <input type="checkbox" value="${key}" name="clearType">
	   ${keyMap.getValue(key)}
	  </label>
	</div>
	</#list>
	
</form>
<#if result?? && (result?size > 0) > 
<table class="table table-hover">
	<tbody>
		<#list result as r>
		<tr>
			<td>${r}</td>
		</tr>
		</#list>
		
	</tbody>
</table>
</#if>
