<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<style>
.fileList {
	list-style: none outside none;
	border: 1px dotted #CCCCCC;
	overflow-y: scroll;
	margin: 0;
	padding: 0;
}

.fileList .li {
	border: 1px solid #CCCCCC;
	height: 100px;
	font-weight: bolder;
	width: 200px;
	position: relative;
	float: left;
	margin: 3px;
	background: none repeat scroll 0 0 ghostwhite;
	text-align: center;
}

.fileList img {
	max-height: 100px;
	max-width: 200px;
}

.fileList .li .info {
	FILTER: alpha(opacity = 50);
	opacity: 0.5;
	height: 20px;
	color: #ffffff;
	line-height: 20px;
	text-align: left;
	bottom: 10px;
	background: none repeat scroll 0 0 #000000;
	left: 3px;
	position: absolute;
	z-index: 100;
	right: 3px;
	padding: 2px;
}

.fileList .li .del {
	right: 3px;
	position: absolute;
	z-index: 100;
	color: #ffffff;
}

.fileList .li .alertpath {
	left: 3px;
	position: absolute;
	z-index: 100;
	color: #ffffff;
}

.menuContent {
	background: none repeat scroll 0 0 #fff;
	z-index: 1000;
	overflow: auto;
}
</style>
<form:form method="POST" cssClass="form-horizontal" id="sniperForm"
	role="form" modelAttribute="post">
	<input type="hidden" name="${_csrf.parameterName}"
		value="${_csrf.token}" />
	<form:hidden path="id" />
	<div class="row">
		<div class="col-md-9">
			<div class="form-group">
				<label for="name" class="col-sm-2 sr-only control-label text-danger">名称</label>
				<div class="col-sm-12">
					<form:input path="name" cssClass="form-control"
						placeholder="请填写文章标题" />
					<div class="help-block">
						<form:errors path="name" />
					</div>
				</div>
			</div>

			<div class="form-group">
				<label for="note" class="col-sm-2 sr-only control-label text-danger">简述</label>
				<div class="col-sm-12">
					<form:textarea path="note" placeholder="请填写文章内容简单描述" rows="5"
						cssClass="form-control" />
				</div>
			</div>

			<div class="form-group">
				<label for="url" class="col-sm-1 sr-only control-label">地址</label>
				<div class="col-sm-12">
					<form:input path="url" cssClass="form-control"
						placeholder="用于第三方页面跳转" />
					<div class="help-block">
						<form:errors path="url" />
					</div>
				</div>
			</div>
			
			<div class="form-group">
				<label for="tags" class="col-sm-1 sr-only control-label">标签</label>
				<div class="col-sm-12">
					<form:input path="tags" cssClass="form-control"
						placeholder="多个标签请用英文逗号（,）分开" />
					<div class="help-block">
						<form:errors path="tags" />
					</div>
				</div>
			</div>

			<div class="form-group">
				<label for="note" class="col-sm-2 sr-only control-label">内容</label>
				<div class="col-sm-12">
					<form:textarea path="postValue.value" rows="6"
						cssClass="form-control" cssStyle="height:400px" />
				</div>
			</div>
			
			<div class="form-group">
				<ol class="fileList mt10" id="fileValue"></ol>
			</div>
			<form:hidden path="postValue.id" />
			<input type="hidden" name="postFiles" value="${postFiles }">
			
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
						倒序排列,4>3>2>1>0
					</div>
				</div>
			</div>

			<div class="form-group">
				<label for="target" class="col-sm-3 control-label">目标</label>
				<div class="col-sm-9">
					<form:select path="target" cssClass="form-control"
						items="${targets }" />
				</div>
			</div>
			<div class="form-group">
				<label for="enabled" class="col-sm-3 control-label">状态</label>
				<div class="col-sm-9">
					<form:select path="status" cssClass="form-control"
						items="${statusList }" />
				</div>
			</div>

			<input type="hidden" id="postChannel" value="${postChannel }"
				name="postChannel">
			<div class="form-group">
				<div class="col-sm-10 menuContent well well-sm col-md-offset-1">
					<ul id="postMap" class="ztree"></ul>
				</div>
				<form:errors path="channels" />
			</div>
		</div>
	</div>
</form:form>

<link href="myfiles/Plugin/jquery-ui-1.10.3.custom/css/ui-lightness/jquery-ui-1.10.3.custom.min.css"
	media="screen" rel="stylesheet" type="text/css">
<link href="myfiles/Plugin/jQuery-Tags-Input/jquery.tagsinput.css"
	media="screen" rel="stylesheet" type="text/css">
<script type="text/javascript"
	src="myfiles/Plugin/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script>
<script type="text/javascript"
	src="myfiles/Plugin/jQuery-Tags-Input/jquery.tagsinput.min.js"></script>
<script type="text/javascript"
	src="myfiles/Plugin/zTree_v3/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript"
	src="myfiles/Plugin/kindeditor/kindeditor-all-min.js"></script>
<script type="text/javascript"
	src="myfiles/Plugin/kindeditor/lang/zh_CN.js"></script>
<script type="text/javascript">

function onAddTag(tag) {
	alert("Added a tag: " + tag);
}
function onRemoveTag(tag) {
	alert("Removed a tag: " + tag);
}

function onChangeTag(input,tag) {
	alert("Changed a tag: " + tag);
}

$(function() {
	$("input[name='tags']").tagsInput({
		width	: '100%',
		height	: '60px',
		autocomplete_url:'public/sendAjaxTags'
	});
});


	$(function() {
		var editor = KindEditor
				.create(
						'textarea[name="postValue.value"]',
						{
							uploadJson : 'upload?${_csrf.parameterName}=${_csrf.token}',
							fileManagerJson : 'file-upload/htmlmanager',
							allowFileManager : true,
							filterMode : false,
							filePostName : 'imgFile',
							domain : 'domain',
							extraFileUploadParams : {
							//${_csrf.parameterName} : '${_csrf.token}'
							},
							afterBlur : function() {
								this.sync();
							},
							afterUpload : function(url, data) {
								setImage(data);
							},
							afterSelectFile : function(url) {
								getImage(url);
							}
						});
	});

	function getFilesPost() {
		var filesid = new Array();
		var filesPost = $('input[name="postFiles"]').val();
		if (filesPost != '') {
			filesid = filesPost.split(',');
		}
		return filesid;
	}

	function setFilesPost(data) {
		filesid = getFilesPost();
		var j = 0;
		for (var i = 0; i < filesid.length; i++) {
			if (filesid[i] == data.id) {
				j++;
			}
		}

		if (j == 0) {
			filesid.push(data.id);
		}
		$('input[name="postFiles"]').val(filesid.join(','));
	}

	function delFilesPost(fileid) {
		//$('#'+id).remove();
		filesid = getFilesPost();
		for (var i = 0; i < filesid.length; i++) {
			if (filesid[i] == fileid) {
				//删除数组
				filesid.splice(i, 1);
			}
		}
		
		$('input[name="filesPost"]').val(filesid.join(','));
	}

	/**
	 * 设置图片
	 */
	function setImage(data) {
		//新版设置附件id
		setFilesPost(data);
		var filePath = data.filePath;

		if (data.filePath.substring(0, 4) != "http") {
			filePath = '${systemConfig.imagePathPrefx}' + filePath;
			console.log(data.filePath.substring(0, 4));
			console.log(filePath);
		}
		html = '<li class="li" id="'+data.id+'">';
		if (data.fileType.substring(0, 5) == 'image') {
			html += '<img src="';
			html+=filePath;
			html+='" title="'+data.oldName+'">';
		} else {
			html += '<img src="myfiles/images/webicon/attach_64.png" title="'+data.oldName+'">';
		}
		html += '<div class="info">';

		if (false && data.fileType.substring(0, 5) == 'image') {
			html += '<input type="checkbox" id="c' + data.id + '"';
			if (data.checked == 1) {
				html += ' checked="checked" ';
			}
			html += ' value="' + data.id + '" onclick="setPic(' + data.id
					+ ')"> 新闻图片';
		}
		html += '<a href="javascript:;" onclick="showpath(\'' + filePath
				+ '\')" class="alertpath">查看路径</a>';
		html += '<a href="javascript:;" onclick="unBindfile(' + data.id
				+ ')" class="del">解除绑定</a>';
		html += '</div>';
		html += '</li>';
		$('#fileValue').append(html);
	}

	//设置图片新闻
	function setPic(obj) {
		id = $('#c' + obj).val();
		checked = $('#c' + obj).attr("checked");
		if (checked == 'checked') {
			checked = 1;
		} else {
			checked = 0
		}
		//更新新闻图片属性的地址
		$.post('', {
			id : id,
			top : checked
		}, function(data, textStatus) {
			if (data.message == 1) {
				art.dialog({
					time : 2,
					content : '操作成功'
				});
			}
		}, 'json');
	}
	//设置图片新闻
	function getImage(value, type) {
		//csrf();
		if (!type)
			type = 'url';
		
		if (!value) {
			return false;
		}
		if (type == "url") {
			$.post('admin/file-info/getFileByUrl?${_csrf.parameterName}=${_csrf.token}', {
				url : value
			}, function(data, textStatus) {
				if (data.file) {
					setImage(data.file);
				}
			}, 'json');
		} else if (type == 'post-id') {
			$.post('admin/file-info/getFilesByPostID?${_csrf.parameterName}=${_csrf.token}', {
				id : value
			}, function(data, textStatus) {
				for ( var i in data.file) {
					if (data.file[i]) {
						setImage(data.file[i]);
					}
				}
			}, 'json');
		}
	}

	//弹出当前文件地址以便复制使用
	function showpath(path) {
		art.dialog({
			content : path,
			ok : true,
			okVal : '关闭'
		});
	}
	//删除文件
	function delfile_bak(id) {
		if (!id) {
			return false;
		}

		var dialog = art.dialog({
			title : '附件删除',
			content : '图片删除不可恢复,与其相关联的文章内容图片将无法显示，请及时删除文章内容图片！',
			okVal : '删除图片',
			ok : function() {
				$.post('file-info/deleteFileById?${_csrf.parameterName}=${_csrf.token}', {
					id : id
				}, function(data, textStatus) {
					if (data.message == 1) {
						//$('#' + id).remove();
						delFilesPost(id);
					}
				}, 'json');
				return true;
			},

			cancelVal : '取消',
			cancel : true
		//为true等价于function(){}
		});
	}

	//删除文件
	function unBindfile(id) {
		if (!id) {
			return false;
		}
		$('#' + id).remove();
		delFilesPost(id);
	}

	$(document).ready(function() {
		getImage('${post.id}', 'post-id');
	})

	//栏目选择
	var settingGroup = {
		view : {
			selectedMulti : false
		},
		check : {
			enable : true,
			chkStyle : "checkbox",
			chkboxType : {
				"Y" : "ps",
				"N" : "ps"
			}
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
		var checked = [ ${postChannel} ];
		for (var i = 0; i < checked.length; i++) {
			var selected = treeObjGroup.getNodeByParam("id", checked[i], null)
			treeObjGroup.checkNode(selected, true, false);
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
		checked();

	});
</script>