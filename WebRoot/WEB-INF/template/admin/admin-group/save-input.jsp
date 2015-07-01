<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script type="text/javascript"
	src="myfiles/Plugin/zTree_v3/js/jquery.ztree.excheck-3.5.js"></script>
<form:form method="POST" cssClass="form-horizontal" id="sniperForm"
	role="form" modelAttribute="adminGroup">

	<form:hidden path="id" />
	<input type="hidden" name="${_csrf.parameterName}"
		value="${_csrf.token}" />
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
		<label for="value" class="col-sm-2 control-label">值</label>
		<div class="col-sm-10">
			<form:input path="value" cssClass="form-control" placeholder="value" />
			<div class="help-block">
				一般以ROLE_开头,比如ROLE_ADMIN,ROLE_USER,ROLE_NAME等
				<br>
				<form:errors path="value" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="note" class="col-sm-2 control-label">简介</label>
		<div class="col-sm-10">
			<form:textarea path="note" rows="5" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-10 col-md-offset-2">
			<button type="submit" class="btn btn-danger">保存</button>
		</div>
	</div>
	<c:set var="fromRightSet"><c:forEach varStatus="varStatus" var="at" items="${adminGroup.adminRight }">${at.id }<c:if test="${!varStatus.last}">,</c:if></c:forEach></c:set>
	<input name="fromRight" id="fromRight" type="hidden" value="${fromRightSet }">
	
	<div class="form-group" style="position: relative;">
		<label for="adminGroup" class="col-sm-2 control-label">权限列表</label>
		<div class="col-sm-9 well well-sm">
			<ul id="rightGroupMap" class="ztree"></ul>
		</div>
	</div>
</form:form>

<SCRIPT type="text/javascript">
<!--
	var settingGroup = {
		view: {
			selectedMulti: false
		},
		check: {
			enable: true,
			chkStyle: "checkbox",
			chkboxType: { "Y": "ps", "N": "ps" }
		},
		
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			onCheck: onClickRight
		}
	};
		
	var zNodesGroup =[
		${html.key}
	];
	
	function checked()
	{
		var treeObjGroup = $.fn.zTree.getZTreeObj("rightGroupMap");
		var nodes =  treeObjGroup.transformToArray(treeObjGroup.getNodes());
		var checked = new Array(${fromRightSet });
		zTree_Menu = $.fn.zTree.getZTreeObj("rightGroupMap");
		for(var i = 0; i<checked.length; i++){
			
			var selected = zTree_Menu.getNodeByParam("id", checked[i], null)
			treeObjGroup.checkNode(selected, true, false);
			
		}
	}
	
	function onClickRight(e, treeId, treeNode) {
		
		var zTree = $.fn.zTree.getZTreeObj("rightGroupMap"),
		nodes = zTree.getSelectedNodes(),
		v = "";
		
		var checkCount = zTree.getCheckedNodes(true);		
		nodes.sort(function compare(a,b){return a.id-b.id;});
		for (var i=0, l = checkCount.length; i<l; i++) {
			//可以多选
			v += checkCount[i].id + ",";
			
		}
		//多选字符串截取
		if (v.length > 0 ) v = v.substring(0, v.length-1);
					
		$("#fromRight").val(v);
		return true;
	}
	
	
	$(document).ready(function(){
		$.fn.zTree.init($("#rightGroupMap"), settingGroup, zNodesGroup);
		checked();
		
	});
	//-->
</SCRIPT>

<div class="alert alert-success alert-dismissible" role="alert">
	<button type="button" class="close" data-dismiss="alert">
		<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
	</button>
	<ul>
		<li>权限修改完毕之后请清理缓存,<a href="admin/admin-config/cache">清理缓存</a></li>
	</ul>
</div>
