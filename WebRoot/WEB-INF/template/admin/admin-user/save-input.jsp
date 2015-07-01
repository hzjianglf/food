<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<style type="text/css">
.menuContent {
	background: none repeat scroll 0 0 #fff;
	position: absolute;
	left: 0;
	top: 0;
	width: auto;
	z-index: 1000;
	overflow: auto;
	max-height: 600px;
}
</style>
<c:if test="${adminUser.signCode != null }">
	<div class="alert alert-success" role="alert">
		KEY:<br>${adminUser.signCode }</div>
</c:if>

<form:form method="POST" cssClass="form-horizontal" id="sniperForm"
	role="form" modelAttribute="sniperAdminUser">
	<input type="hidden" name="${_csrf.parameterName}"
		value="${_csrf.token}" />
	<form:hidden path="id" />

	<div class="form-group">
		<label for="name" class="col-sm-2 control-label">登录名称</label>
		<div class="col-sm-4">
			<form:input path="name" cssClass="form-control" placeholder="name" />
			<div class="help-block">
				只允许使用a-zA-Z0-9字符
				<form:errors path="name" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="nickName" class="col-sm-2 control-label">显示昵称</label>
		<div class="col-sm-4">
			<form:input path="nickName" cssClass="form-control"
				placeholder="nickName" />
			<div class="help-block">
				<form:errors path="nickName" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="email" class="col-sm-2 control-label">Email</label>
		<div class="col-sm-4">
			<form:input path="email" cssClass="form-control" />
			<div class="help-block">
				<form:errors path="email" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="password_old" class="col-sm-2 control-label">密码</label>
		<div class="col-sm-4">
			<form:password path="password" cssClass="form-control" />
			<div class="help-block">
				密码最少六位
				<form:errors path="password" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="password_c" class="col-sm-2 control-label">确认密码</label>
		<div class="col-sm-4">
			<input id="password_c" name="password_c" class="form-control"
				type="password" value="">
			<div class="help-block">
				<form:errors path="password" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="adminGroup" class="col-sm-2 control-label">用户组</label>
		<div class="col-sm-10">
			
			<c:forEach var="ag" items="${adminGroups }" varStatus="varStatus">
			<span>
			<input id="adminGroup${varStatus.index }" name="adminGroups" type="checkbox" value="${ag.id }" 
			<c:forEach items="${sniperAdminUser.adminGroup }" var="aag">
			<c:if test="${aag.id == ag.id }">checked="checked"</c:if>
			</c:forEach> >
			<label for="adminGroup${varStatus.index }">${ag.name }</label>
			</span>
			</c:forEach>
		</div>
	</div>

	<div class="form-group">
		<label for="enabled" class="col-sm-2 control-label">启用</label>
		<div class="col-sm-2">
			<form:checkbox path="enabled" />
		</div>
	</div>

	<div class="form-group">
		<label for="locked" class="col-sm-2 control-label">锁定</label>
		<div class="col-sm-2">
			<form:checkbox path="locked" />
		</div>
	</div>

	<div class="form-group">
		<label for="usernameExpired" class="col-sm-2 control-label">用户名过期时间</label>
		<div class="col-sm-3">
			<form:input path="usernameExpired" cssClass="form-control Wdate" />
		</div>
	</div>

	<div class="form-group">
		<label for="passwordExpired" class="col-sm-2 control-label">密码过期时间</label>
		<div class="col-sm-3">
			<form:input path="passwordExpired" cssClass="form-control Wdate" />
		</div>
	</div>

	<div class="form-group">
		<label for="enabled" class="col-sm-2 control-label">启用数据导出登录</label>
		<div class="col-sm-4">
			<input id="sign" name="sign"
				<c:if test="${adminUser.signCode != null }">checked="checked"</c:if>
				type="checkbox" value="true">
			<div class="help-block">生成的KEY会在顶部显示</div>
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-10 col-md-offset-2">
			<button type="submit" class="btn btn-danger">保存</button>
		</div>
	</div>
</form:form>

<script type="text/javascript"
	src="${baseHref.baseHref }myfiles/Plugin/My97DatePicker/WdatePicker.js"></script>
<SCRIPT type="text/javascript">
	$('.Wdate').bind('click', function() {
		WdatePicker({
			dateFmt : 'yyyy-MM-dd HH:mm:ss'
		})
	});
	
</SCRIPT>
