<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<form:form method="POST" cssClass="form-horizontal" id="sniperForm"
	role="form" modelAttribute="survey">
	<input type="hidden" name="${_csrf.parameterName}"
		value="${_csrf.token}" />
	<form:hidden path="id" />
	<div class="form-group">
		<label for="title" class="col-sm-2 control-label text-danger">名称</label>
		<div class="col-sm-9">
			<form:input path="title" cssClass="form-control"
				placeholder="问卷标题,必填" />
			<span class="help-block"><p class="text-danger">
					<form:errors path="title"></form:errors>
				</p></span>
		</div>
	</div>

	<div class="form-group">
		<label for="peopleMaxNum" class="col-sm-2 control-label">最大人数</label>
		<div class="col-sm-4">
			<form:input path="peopleMaxNum" cssClass="form-control"
				placeholder="允许填写的最大人数" />
		</div>
	</div>

	<div class="form-group">
		<label for="password" class="col-sm-2 control-label">设置密码</label>
		<div class="col-sm-4">
			<form:input path="password" cssClass="form-control"
				placeholder="设置密码,只有输入密码才能提交数据" />
		</div>
	</div>


	<div class="form-group">
		<label for="target" class="col-sm-2 control-label">问题前缀</label>
		<div class="col-sm-2">
			<form:select path="listStyle" items="${listStyles }"
				cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="page" class="col-sm-2 control-label">启用分页</label>
		<div class="col-sm-4">
			<form:checkbox path="page" />
			<div class="help-block">当拿起用分页的时候,问卷的每一个段落都会作为一页显示</div>
		</div>
			
	</div>

	<div class="form-group">
		<label for="locked" class="col-sm-2 control-label text-danger">锁定</label>
		<div class="col-sm-4">
			<form:checkbox path="locked" />
			<div class="help-block">锁定的时候不允许填写问卷</div>
		</div>
	</div>

	<div class="form-group">
		<label for="locked" class="col-sm-2 control-label">启用验证码</label>
		<div class="col-sm-4">
			<form:checkbox path="verifyCode" />
			<div class="help-block">用户提交问卷的时候需要填写验证码</div>
		</div>
	</div>

	<div class="form-group">
		<label for="locked" class="col-sm-2 control-label">IP限制</label>
		<div class="col-sm-4">
			<form:input path="verifyIpNum" cssClass="form-control" />
			<div class="help-block">一个ip只能填写的次数</div>
		</div>
	</div>

	<div class="form-group">
		<label for="locked" class="col-sm-2 control-label">手机限制</label>
		<div class="col-sm-4">
			<form:input path="verifyPhone" cssClass="form-control" />
			<div class="help-block">同一电脑/手机填写次数</div>
		</div>
	</div>

	<div class="form-group">
		<label for="startDate" class="col-sm-2 control-label">问卷开始时间</label>
		<div class="col-sm-4">
			<form:input path="startDate" cssClass="form-control Wdate" />
			<div class="help-block">问卷开始时间</div>
		</div>
	</div>

	<div class="form-group">
		<label for="entDate" class="col-sm-2 control-label">问卷结束时间</label>
		<div class="col-sm-4">
			<form:input path="entDate" cssClass="form-control Wdate" />
			<div class="help-block">问卷结束时间</div>
		</div>
	</div>

	<div class="form-group">
		<label for="note" class="col-sm-2 control-label">简介</label>
		<div class="col-sm-9">
			<form:textarea path="note" rows="6" cssClass="form-control"
				cssStyle="height:200px" />
		</div>
	</div>

	<div class="form-group">
		<label for="submitName" class="col-sm-2 control-label">问卷提交按钮名称</label>
		<div class="col-sm-4">
			<form:input path="submitName" cssClass="form-control"
				placeholder="默认是完成问卷" />
			<span class="help-block">默认是完成问卷</span>
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-10 col-md-offset-1">
			<button type="submit" class="btn btn-danger">保存</button>
		</div>
	</div>
</form:form>

<script type="text/javascript"
	src="myfiles/Plugin/kindeditor/kindeditor-min.js"></script>
<script type="text/javascript"
	src="myfiles/Plugin/kindeditor/lang/zh_CN.js"></script>
<script type="text/javascript"
	src="${baseHref.baseHref }myfiles/Plugin/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	$(function() {
		var editor = KindEditor
				.create(
						'textarea[name="note"]',
						{
							uploadJson : 'admin/file-upload/upload?${_csrf.parameterName}=${_csrf.token}',
							fileManagerJson : 'admin/file-upload/htmlmanager',
							allowFileManager : true,
							filterMode : false,
							domain : 'domain',
							afterBlur : function() {
								this.sync();
							},
						});
	});

	$('.Wdate').bind('click', function() {
		WdatePicker({
			dateFmt : 'yyyy-MM-dd HH:mm:ss'
		})
	});
</script>