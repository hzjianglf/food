<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>

<style>
.fileList {
	list-style: none outside none;
	overflow: auto;
	margin: 0;
	padding: 0;
}
.fileList .li {
	font-weight: bolder;
	position: relative;
	float: left;
	margin: 5px;
	text-align: center;
}

.fileList img {
	max-height: 200px;
}

.fileList .li .del {
	right: -3px;
	top: -8px;
	font-size: 18px;
	position: absolute;
	z-index: 100;
	color: black;
	position: absolute;
	cursor: pointer;
}

.menuContent {
	background: none repeat scroll 0 0 #fff;
	z-index: 1000;
	overflow: auto;
}
</style>
<form:form method="POST" cssClass="form-horizontal" id="sniperForm"
	role="form" modelAttribute="pptFile">
	<input type="hidden" name="${_csrf.parameterName}"
		value="${_csrf.token}" />
	<form:hidden path="id" />
	
	<div class="row">
		<div class="col-md-9">
			<div class="form-group">
				<label for="name" class="col-sm-2 sr-only control-label text-danger">名称</label>
				<div class="col-sm-12">
					<form:input path="name" cssClass="form-control" placeholder="请填写标题" />
					<div class="help-block">
						<p class="text-warning"><form:errors path="name" /></p>
					</div>
				</div>
			</div>

			<div class="form-group">
				<label for="note" class="col-sm-2 sr-only control-label text-danger">简述</label>
				<div class="col-sm-12">
					<form:textarea path="note" placeholder="请填写内容简单描述" rows="5"
						cssClass="form-control" />
				</div>
			</div>

			<div class="form-group">
				<label for="tags" class="col-sm-1 sr-only control-label">标签</label>
				<div class="col-sm-12">
					<form:input path="tags" cssClass="form-control"
						placeholder="多个标签请用英文逗号（,）分开" />
					<div class="help-block">
						<p class="text-warning"><form:errors path="tags" /></p>
					</div>
				</div>
			</div>

			<div class="form-group">
				<label for="tags" class="col-sm-1 sr-only control-label">PPT文件</label>
				<div class="col-sm-12">
					<div id="queue_ppt"></div>
					<input id="file_upload_ppt" name="file_upload_ppt" type="file"
						multiple="false">
					<div class="help-block">
						<p class="text-warning"><form:errors path="ppt" /></p>
					</div>
				</div>
			</div>
			
			<div class="form-group">
				<ol class="fileList mt10" id="filePptList"></ol>
			</div>
			<form:hidden path="ppt.id" id="filePptId" />
			
			<div class="form-group">
				<label for="tags" class="col-sm-1 sr-only control-label">Flash文件</label>
				<div class="col-sm-12">
					<div id="queue_flash"></div>
					<input id="file_upload_flash" name="file_upload_flash" type="file"
						multiple="false">
					<div class="help-block">
						<p class="text-warning"><form:errors path="flash" /></p>
					</div>
				</div>
			</div>

			<div class="form-group">
				<ol class="fileList mt10" id="fileFlashList"></ol>
			</div>
			<form:hidden path="flash" id="fileFlashId" />

			<div class="form-group">
				<label for="tags" class="col-sm-1 sr-only control-label">图片</label>
				<div class="col-sm-12">
					<div id="queue"></div>
					<input id="file_upload" name="file_upload" type="file"
						multiple="true">
				</div>
			</div>

			<div class="form-group">
				<ol class="fileList mt10" id="filesList"></ol>
			</div>
			<input type="hidden" name="filesId" id="filesId" >

		</div>
		<div class="col-md-3">
			<div class="form-group">
				<div class="col-sm-8 col-md-offset-0">
					<button type="submit" class="btn btn-danger">保存</button>
				</div>
			</div>

			<div class="form-group">
				<label for="source" class="col-sm-3 control-label">来源</label>
				<div class="col-sm-9">
					<form:input path="source" cssClass="form-control" placeholder="来源" />
					<div class="help-block">
						<form:errors path="source" />
					</div>
				</div>
			</div>
			
			<div class="form-group">
				<label for="name" class="col-sm-3 control-label">排序</label>
				<div class="col-sm-9">
					<form:input path="sort" cssClass="form-control" />
					<div class="help-block">
						<form:errors path="sort" />
						倒序排列 3>2>1>0
					</div>
				</div>
			</div>

			<div class="form-group">
				<label for="name" class="col-sm-3 control-label">状态</label>
				<div class=" col-sm-9">
			     <div class="checkbox">
			       <label>
			       	 <form:checkbox path="enabled"/> 启用
			       </label>
			       <label>
			       	 <form:checkbox path="original" /> 原创
			       </label>
			     </div>
			   </div>
			</div>

			<form:hidden path="channel.id" id="postChannel" />
			<div class="form-group">
				<div class="col-sm-10 menuContent well well-sm col-md-offset-1">
					<ul id="postMap" class="ztree ztreeSniper"></ul>
				</div>
				<form:errors path="channel" />
			</div>
		</div>
	</div>
</form:form>

<script src="myfiles/Plugin/uploadify/jquery.uploadify.min.js"
	type="text/javascript"></script>
<link rel="stylesheet" type="text/css"
	href="myfiles/Plugin/uploadify/uploadify.css">
<link
	href="myfiles/Plugin/jquery-ui-1.10.3.custom/css/ui-lightness/jquery-ui-1.10.3.custom.min.css"
	media="screen" rel="stylesheet" type="text/css">
<link href="myfiles/Plugin/jQuery-Tags-Input/jquery.tagsinput.css"
	media="screen" rel="stylesheet" type="text/css">
<script type="text/javascript"
	src="myfiles/Plugin/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script>
<script type="text/javascript"
	src="myfiles/Plugin/jQuery-Tags-Input/jquery.tagsinput.min.js"></script>
<script type="text/javascript"
	src="myfiles/Plugin/zTree_v3/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="myfiles/js/jquery-jtemplates.js"></script>
<script type="text/javascript" src="myfiles/js/jquery.sniper.ppt.js"></script>
<link rel="stylesheet" href="myfiles/Plugin/zTree_v3/css/metroStyle/metroSniperStyle.css" type="text/css">


<div id="tempdata" style="display: none;"></div>
<script type="text/javascript">

	 
	//栏目选择
	var settingGroup = {
		view : {
			selectedMulti : false
		},
		check : {
			enable : true,
			chkStyle : "radio",
			radioType: "all"
		},

		data : {
			simpleData : {
				enable : true
			}
		},
		callback : {
			onCheck : onClickRight
		}
	};
	
	var zNodesGroup = [ ${treePostMap} ];

	function checked() {
		var treeObjGroup = $.fn.zTree.getZTreeObj("postMap");
		var nodes = treeObjGroup.transformToArray(treeObjGroup.getNodes());
		var checked = [ ${pptFile.channel.id} ];
		for (var i = 0; i < checked.length; i++) {
			var selected = treeObjGroup.getNodeByParam("id", checked[i], null)
			treeObjGroup.checkNode(selected, true, false);
			treeObjGroup.selectNode(selected);
		}
		
	}

	function onClickRight(e, treeId, treeNode) {

		var zTree = $.fn.zTree.getZTreeObj("postMap"), nodes = zTree
				.getSelectedNodes(), v = "";

		var checkCount = zTree.getCheckedNodes(true);
		nodes.sort(function compare(a, b) {
			return a.id - b.id;
		});
		for (var i = 0, l = checkCount.length; i < l; i++) {
			//可以多选
			v += checkCount[i].id + ",";
		}
		//多选字符串截取
		if (v.length > 0)
			v = v.substring(0, v.length - 1);

		$("#postChannel").val(v);
		return true;
	}

	$(document).ready(function() {
		$.fn.zTree.init($("#postMap"), settingGroup, zNodesGroup);
		<c:if test="${treePostMap != ''  }">
		checked();
		</c:if>
		$("input[name='tags']").tagsInput({
			width : '100%',
			height : '60px',
			defaultText : '添加标签', //默认文字
			autocomplete_url : 'public/sendAjaxTags',
			onAddTag:function(tag){
                $.post('public/addAjaxTags?${_csrf.parameterName}=${_csrf.token}',{term: tag},function(){});
           }
		});
		
		
		$().sniperppt({
			baseHref : '${baseHref.baseHref }',
			imagePathPrefx : '${systemConfig.imagePathPrefx}',
			tempfileValue : '${tempfileValue }',
			tempfileValuePpt : '${tempfileValuePpt}',
			flashTemp: '${pptFile.flash}',
			token : '${_csrf.token}'
		 });
		
		
		$('#sniperForm').submit(function() {
			 if(confirm("确定提交?")){
				return true;
			}
	        return false; 
	    });
		 
		

		
	});
</script>

