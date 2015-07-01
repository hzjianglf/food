<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<form:form method="post" cssClass="form-horizontal" id="sniperForm"
	role="form" modelAttribute="links" action="">
	<input type="hidden" name="${_csrf.parameterName}"
		value="${_csrf.token}" />
	<form:hidden path="id" />
	<div class="form-group">
		<label for="name" class="col-sm-2 control-label">名称</label>
		<div class="col-sm-10">
			<form:input path="name" cssClass="form-control" placeholder="name" />
			<div class="help-block">
				<form:errors path="name" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="nameTitle" class="col-sm-2 control-label">别名(用于title)</label>
		<div class="col-sm-10">
			<form:input path="nameTitle" cssClass="form-control" placeholder="" />
			<div class="help-block">
				<form:errors path="nameTitle" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="url" class="col-sm-2 control-label">url</label>
		<div class="col-sm-10">
			<form:input path="url" cssClass="form-control" placeholder="" />
			<div class="help-block">
				<form:errors path="url" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="url" class="col-sm-2 control-label">组选择</label>
		<div class="col-sm-4">
			<form:select path="channel.id" items="${channels }" itemLabel="name"
				itemValue="id" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="attachement" class="col-sm-2 control-label">附件(图片)</label>
		<div class="col-sm-10">
			<form:input path="attachement.newPath" cssClass="form-control" id="attachement" />
			<div class="help-block">附件绑定图片</div>
		</div>
		<form:hidden path="attachement.id" id="attachement_id"/>
	</div>

	<div class="form-group">
		<label for="width" class="col-sm-2 control-label">图片宽度</label>
		<div class="col-sm-2">
			<form:input path="width" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="height" class="col-sm-2 control-label">图片高度</label>
		<div class="col-sm-2">
			<form:input path="height" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="sort" class="col-sm-2 control-label">Sort</label>
		<div class="col-sm-2">
			<form:input path="sort" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="enabled" class="col-sm-2 control-label">启用</label>
		<div class="col-sm-2">
			<form:checkbox path="enabled" />
		</div>
	</div>

	<div class="form-group">
		<label for="timeStart" class="col-sm-2 control-label">开始时间</label>
		<div class="col-sm-3">
			<form:input path="timeStart" cssClass="form-control Wdate" />
		</div>
	</div>

	<div class="form-group">
		<label for="timeEnd" class="col-sm-2 control-label">结束时间</label>
		<div class="col-sm-3">
			<form:input path="timeEnd" cssClass="form-control Wdate" />
		</div>
	</div>

	<div class="form-group">
		<label for="note" class="col-sm-2 control-label">备注</label>
		<div class="col-sm-10">
			<form:textarea path="note" rows="6" cssClass="form-control"
				cssStyle="height:100px" />
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-10 col-md-offset-2">
			<button type="submit" class="btn btn-danger">保存</button>
		</div>
	</div>
</form:form>

<link href="myfiles/Plugin/kindeditor/themes/default/default.css"
	media="screen" rel="stylesheet" type="text/css">

<script type="text/javascript"
	src="myfiles/Plugin/kindeditor/kindeditor-min.js"></script>
<script type="text/javascript"
	src="myfiles/Plugin/kindeditor/lang/zh_CN.js"></script>
<script type="text/javascript"
	src="myfiles/Plugin/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	$('.Wdate').bind('click', function() {
		WdatePicker({
			dateFmt : 'yyyy-MM-dd HH:mm:ss'
		});
	});

	KindEditor
			.ready(function(K) {
				var editor1 = K
						.editor({
							uploadJson : 'upload?${_csrf.parameterName}=${_csrf.token}',
							fileManagerJson : 'file-upload/htmlmanager',
							allowFileManager : true,
							allowImageUpload : true,
							urlType : 'domain',
							filePostName : 'imgFile',
							afterBlur : function() {
								this.sync();
							}

						});

				K('#attachement').click(function() {
					editor1.loadPlugin('image', function() {
						editor1.plugin.imageDialog({
							showLocal : true,
							showRemote : true,
							imageUrl : K('#attachement').val(),
							clickFn : function(url, title) {
								K('#attachement').val(url);
								editor1.hideDialog();
							},
							clickFns : function (data){
								$("#attachement_id").val(data.id);
							}
						});
					});
				});
			});
</script>