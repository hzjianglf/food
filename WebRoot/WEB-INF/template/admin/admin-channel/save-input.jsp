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
	role="form" modelAttribute="channel">
	<form:hidden path="id" />

	<div class="form-group">
		<label for="keyName" class="col-sm-2 control-label">名称</label>
		<div class="col-sm-8">
			<form:input path="name" cssClass="form-control" placeholder="name" />
			<div class="help-block">
				<form:errors path="name" />
			</div>
		</div>
	</div>

	<div class="form-group" style="position: relative;">
		<label for="fidName" class="col-sm-2 control-label">父级</label>
		<div class="col-sm-4">
			<input type="text" value="" class="form-control" name="fidName"
				id="fidName">
			<form:hidden path="fid" /> 
			
		</div>
		<div class="help-block"><a href="javascript:setTop()">设为顶级</a> 点击输入框选择父级(空值为顶级栏目)</div>

		<div id="menuContent" class="menuContent well well-sm"
			style="display:none;">
			<ul id="rightMap" class="ztree"></ul>
		</div>
	</div>

	<div class="form-group">
		<label for="url" class="col-sm-2 control-label">Url</label>
		<div class="col-sm-8">
			<form:input path="url" cssClass="form-control" />
			<div class="help-block">
				<form:errors path="url" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="sort" class="col-sm-2 control-label">排序</label>
		<div class="col-sm-2">
			<form:input path="sort" cssClass="form-control" />
			<div class="help-block">
				<form:errors path="sort" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="status" class="col-sm-2 control-label">状态</label>
		<div class="col-sm-8">
			<form:checkbox path="status"  />
		</div>
	</div>
	
	<div class="form-group">
		<label for="showHome" class="col-sm-2 control-label">前台显示</label>
		<div class="col-sm-8">
			<form:checkbox path="showHome"  />
			<div class="help-block">有效范围前台左侧频道显示</div>
		</div>
	</div>

	<div class="form-group">
		<label for="showType" class="col-sm-2 control-label">类型</label>
		<div class="col-sm-4">
			<form:select path="showType" items="${sniperMenu.keys }"
				cssClass="form-control" />
		</div>
	</div>
	
	<div class="form-group">
		<label for="attachement" class="col-sm-2 control-label">附件(图片)</label>
		<div class="col-sm-10">
			<form:input path="attachement" cssClass="form-control" id="attachement" />
			<div class="help-block">附件绑定图片</div>
		</div>
	</div>

	<div class="form-group">
		<label for="note" class="col-sm-2 control-label">简介</label>
		<div class="col-sm-8">
			<form:textarea path="note" rows="5" cssClass="form-control" />
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
<script type="text/javascript" src="myfiles/js/jquery.zTree.js"></script>
<SCRIPT type="text/javascript">
	var depMenu = new Array(${sniperMenu.key});
	
	function setTop()
	{
		$("#fidName").val("");
		$("#fid").val(0);
	}

	$(document).ready(function() {
		$().zTreeMenu({
			zNodesRight : depMenu,
			valueType : 'all',
			valueId : '${channel.fid}'

		});
	});
	
	KindEditor
	.ready(function(K) {
		var editor1 = K
				.editor({
					uploadJson : 'upload?${_csrf.parameterName}=${_csrf.token}',
					fileManagerJson : 'admin/file-upload/htmlmanager',
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
					showRemote : false,
					imageUrl : K('#attachement').val(),
					clickFn : function(url, title) {
						K('#attachement').val(url);
						editor1.hideDialog();
					}
				});
			});
		});
	});
</SCRIPT>
