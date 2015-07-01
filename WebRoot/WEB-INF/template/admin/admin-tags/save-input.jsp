<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<style type="text/css">
.menuContent {
	background: none repeat scroll 0 0 #fff;
	position: absolute;
	left: 0;
	top: 0;
	width: auto;
	z-index: 1000;
}
</style>
<form:form method="POST" cssClass="form-horizontal" id="sniperForm"
	role="form" modelAttribute="tags">
	
	<input type="hidden" name="${_csrf.parameterName}"
		value="${_csrf.token}" />
	<form:hidden path="id" />

	<div class="form-group">
		<label for="keyName" class="col-sm-2 control-label">名称</label>
		<div class="col-sm-8">
			<form:input path="name" cssClass="form-control" />
			<div class="help-block">
				这将是它在站点上显示的名字。<br>
				<form:errors path="name" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="plug" class="col-sm-2 control-label">别名</label>
		<div class="col-sm-8">
			<form:input path="plug" cssClass="form-control" />
			<div class="help-block">
				“别名”是在URL中使用的别称，它可以令URL更美观。通常使用小写，只能包含字母，数字和连字符（-）。
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="description" class="col-sm-2 control-label">简介</label>
		<div class="col-sm-8">
			<form:textarea path="description" rows="5" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-10 col-md-offset-2">
			<button type="submit" class="btn btn-danger">保存</button>
		</div>
	</div>
</form:form>

