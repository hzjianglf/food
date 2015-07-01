<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<form:form method="POST" cssClass="form-horizontal" id="sniperForm"
	role="form" modelAttribute="config">
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	<form:hidden path="id" />

	<div class="form-group">
		<label for="keyName" class="col-sm-2 control-label">调用的key</label>
		<div class="col-sm-10">
			<form:input path="keyName" cssClass="form-control" placeholder="keyName" />
			<div class="help-block">
				<form:errors path="keyName" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="keyValue" class="col-sm-2 control-label">显示的值</label>
		<div class="col-sm-10">
			<form:input path="keyValue" cssClass="form-control" placeholder="keyValue" />
			<div class="help-block">
				<form:errors path="keyValue" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="autoload" class="col-sm-2 control-label">自动加载</label>
		<div class="col-sm-10">
			<form:checkbox path="autoload"/>
		</div>
	</div>

	<div class="form-group">
		<label for="keyInfo" class="col-sm-2 control-label">简介</label>
		<div class="col-sm-10">
			<form:textarea path="keyInfo" rows="5" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-10 col-md-offset-2">
			<button type="submit" class="btn btn-danger">保存</button>
		</div>
	</div>
</form:form>

