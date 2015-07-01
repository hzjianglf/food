<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<title><c:if test="${pptFile.name == null }">上传PPT</c:if>${pptFile.name }</title>
<div class="home_main">
	<div class="home_main_nav">
		<a href="user/ppts/insert" class="home_main_nav_default">上传PPT</a>
		<a href="user/ppts/" class="home_main_nav_hove">PPT管理</a>
	</div>
<style type="text/css">
	#file_upload_ppt,#file_upload,#file_upload_flash{margin: 5px;}
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
	right: -3px;
	top: -8px;
	font-size: 18px;
	position: absolute;
	z-index: 100;
	color: black;
	position: absolute;
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

select optgroup{padding: 5px;}
select option{text-indent: 2em;}
</style>

<div class="home_main_xw">
	<form:form method="POST" cssClass="form-horizontal" id="sniperForm"
		role="form" modelAttribute="pptFile">
		<form:hidden path="id" />
		<table width="790" border="0" class="tab_1">
			<tbody>
				<tr>
					<td width="95" align="right">名称：</td>
					<td width="685" height="50"><form:input path="name"
							cssClass="form-control" />
							<p class="text-warning"><form:errors path="name" /></p></td>
				</tr>
				<tr>
					<td align="right">原作者：</td>
					<td height="50"><form:input path="source"
							cssClass="form-control" /></td>
				</tr>
				<tr>
					<td align="right">分类：</td>
					<td height="50">
						<select class="form-control" name="channel.id">
						<c:forEach items="${selectChannelFids }" var="sc">
							<optgroup label="${sc.value }">
							<c:forEach items="${selectChannelsChilds[sc.key] }" var="sv">
								<option value="${sv.id }" <c:if test="${sv.id == pptFile.channel.id }">selected="selected"</c:if> >${sv.name }</option>
							</c:forEach>
							</optgroup>
						</c:forEach>
						</select>
						
					</td>
				</tr>
				
				<tr>
					<td align="right">关键字：</td>
					<td height="50"><form:input path="tags"
							cssClass="form-control" /></td>
				</tr>
				
				<tr>
					<td align="right">上传PPT：</td>
					<td height="50">
						<input id="file_upload_ppt" style="margin: 5px 0" name="file_upload_ppt" type="file"
							multiple="false">
						<ol class="fileList mt10" id="filePptList"></ol>
						<form:hidden path="ppt.id" id="filePptId" />
					</td>
				</tr>
				<tr>
					<td align="right">上传FLASH：</td>
					<td height="50"><input id="file_upload_flash" style="margin: 5px 0" name="file_upload_flash" type="file"
							multiple="false">
						<ol class="fileList mt10" id="fileFlashList"></ol>
						<form:hidden path="flash" id="fileFlashId" />
					</td>
				</tr>
				
				<tr>
					<td align="right">上传图片：</td>
					<td height="50"><input id="file_upload" style="margin: 5px 0" name="file_upload" type="file"
							multiple="false">
						<ol class="fileList mt10" id="filesList"></ol>
						<input type="hidden" name="filesId" id="filesId" >
					</td>
				</tr>
				
				<tr>
					<td height="50" colspan="2" align="center"><input
						class="up_but" type="submit" name="button" id="button"
						value="确认上传"></td>
				</tr>
			</tbody>
		</table>
	</form:form>

</div>
<script src="myfiles/Plugin/uploadify/jquery.uploadify.min.js"
	type="text/javascript"></script>

<link rel="stylesheet" href="myfiles/Plugin/zTree_v3/css/metroStyle/metroSniperStyle.css" type="text/css">
<link rel="stylesheet" href="myfiles/Plugin/uploadify/uploadify.css" type="text/css" >
<link
	href="myfiles/Plugin/jquery-ui-1.10.3.custom/css/ui-lightness/jquery-ui-1.10.3.custom.min.css"
	media="screen" rel="stylesheet" type="text/css">
<link href="myfiles/Plugin/jQuery-Tags-Input/jquery.tagsinput.css"
	media="screen" rel="stylesheet" type="text/css">
<script type="text/javascript"
	src="myfiles/Plugin/zTree_v3/js/jquery.ztree.core-3.5.min.js"></script>
<script type="text/javascript"
	src="myfiles/Plugin/zTree_v3/js/jquery.ztree.exhide-3.5.js"></script>
<script type="text/javascript"
	src="myfiles/Plugin/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script>
<script type="text/javascript"
	src="myfiles/Plugin/jQuery-Tags-Input/jquery.tagsinput.min.js"></script>
<script type="text/javascript"
	src="myfiles/Plugin/zTree_v3/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="myfiles/js/jquery-jtemplates.js"></script>


<script type="text/template" id="tempfileValue">
<li class="li" id="{$T.id}">
	<a href="{$T.filePath}" target="_blank"><img src="{$T.filePath}" title="{$T.oldName}" class="img-thumbnail"></a>
	<a href="javascript:;" onclick="unBindfile({$T.id}, 'filesId')" class="del"><i class="fa fa-times-circle"></i></a>
</li>
</script>

<script type="text/template" id="tempfileValuePpt">
<li class="li" id="{$T.id}">
	<img class="" src="myfiles/images/webicon/ppt-file.png" title="{$T.oldName}">
	<a href="javascript:;" onclick="unBindfile({$T.id}, 'filePptId')" class="del"><i class="fa fa-times-circle"></i></a>
</li>
</script>

<script type="text/template" id="tempfileValueflash">
<li class="li" id="flashID">
	<img class="" src="myfiles/images/webicon/flash-file.png">
	<a href="javascript:;" onclick="unBindfile('flashID', 'fileFlashId')" class="del"><i class="fa fa-times-circle"></i></a>
</li>
</script>

<div id="tempdata" style="display: none;"></div>
<script type="text/javascript">
	function getFilesPost(div) {
		var filesid = new Array();
		var filesPost = $('#' + div).val();
		if (filesPost != '') {
			filesid = filesPost.split(',');
		}
		return filesid;
	}

	function setFilesPost(data, div) {
		filesid = getFilesPost(div);
		var j = 0;
		for (var i = 0; i < filesid.length; i++) {
			if (filesid[i] == data.id) {
				j++;
			}
		}

		if (j == 0) {
			filesid.push(data.id);
		}
		$('#' + div).val(filesid.join(','));
	}

	function delFilesPost(fileid, div) {
		filesid = getFilesPost(div);
		for (var i = 0; i < filesid.length; i++) {
			if (filesid[i] == fileid) {
				//删除数组
				filesid.splice(i, 1);
			}
		}

		$('#' + div).val(filesid.join(','));
	}

	/**
	 * 设置图片
	 */
	function setImage(data, div) {
		
		if(data.filePath == null ||  data.filePath == ''){
			return false;
		}

		var filePath = data.filePath;
		if (data.filePath.substring(0, 4) != "http") {
			filePath = '${systemConfig.imagePathPrefx}' + filePath;
		}
		
		data.filePath = filePath;
		switch(div){
		
		case "filesList":
			setFilesPost(data, 'filesId');
			$('#tempdata').setTemplate($('#tempfileValue').html()).processTemplate(data);
			html = $('#tempdata').html();
			break;
		case "filePptList":
			$('#tempdata').setTemplate($('#tempfileValuePpt').html()).processTemplate(data);
			html = $('#tempdata').html();
			$('#' + div).empty();
			break;
		case "fileFlashList":
			$('#tempdata').setTemplate($('#tempfileValueflash').html()).processTemplate(data);
			html = $('#tempdata').html();
			$('#' + div).empty();
			break;
		}
		
		$('#' + div).append(html);
	}
	
	//删除文件
	function delfile(id) {
		if (!id) {
			return false;
		}

		var dialog = art
				.dialog({
					title : '附件删除',
					content : '图片删除不可恢复,与其相关联的文章内容图片将无法显示，请及时删除文章内容图片！',
					okVal : '删除图片',
					ok : function() {
						$.post(
										'file-info/deleteFileById?${_csrf.parameterName}=${_csrf.token}',
										{
											id : id
										}, function(data, textStatus) {
											if (data.message == 1) {
												//$('#' + id).remove();
												delFilesPost(id, '');
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
	function unBindfile(id, div) {
		
		if (!id) {
			return false;
		}
		$('#' + id).remove();
		if(div == "fileFlashId"){
			$('#' + div).val('');
		}
		delFilesPost(id, div);
	}

	$(document).ready(function() {
		
		$('#sniperForm').submit(function() {
			 if(confirm("确定提交?")){
				return true;
			}
	        return false; 
	     }); 
		
		$("input[name='tags']").tagsInput({
			width : '100%',
			height : '60px',
			defaultText : '添加标签', //默认文字
			autocomplete_url : 'public/sendAjaxTags'
		});
		
		<c:if test="${tempfileValue != null}">
		var tempfileValue = ${tempfileValue };
		if (tempfileValue) {
			for ( var i in tempfileValue) {
				setImage(tempfileValue[i], 'filesList');
			}
		}
		</c:if>
		
		<c:if test="${tempfileValuePpt != null  }">
		var tempfileValuePpt = ${tempfileValuePpt };
		setImage(tempfileValuePpt, 'filePptList');
		</c:if>
		
		<c:if test="${pptFile.flash != '' && pptFile.flash != null  }">
		var flashTemp = {};
		flashTemp.filePath = '${pptFile.flash}';
		setImage(flashTemp, 'fileFlashList');
		</c:if>
		
		
	});

	$(function() {
		$('#file_upload')
				.uploadify(
						{
							'formData' : {
								'timestamp' : '${_csrf.parameterName}',
								'token' : '${_csrf.token}'
							},
							'fileObjName' : 'imgFile',
							'debug' : false,
							'buttonText' : '选择图片',
							'fileTypeDesc' : 'Image Files',
							'fileTypeExts' : '*.gif; *.jpg; *.png; *.gif; *.tiff',
							'swf' : 'myfiles/Plugin/uploadify/uploadify.swf',
							'uploader' : '${baseHref.baseHref }upload?${_csrf.parameterName}=${_csrf.token}',
							'onUploadSuccess' : function(file, data, response) {
								var data = eval("(" + data + ")");
								setImage(data, 'filesList');
							},
							'onUploadError' : function(file, errorCode,
									errorMsg, errorString) {
								alert('The file ' + file.name
										+ ' could not be uploaded: '
										+ errorString);
							}
						});

		$('#file_upload_ppt')
				.uploadify(
						{
							'formData' : {
								'timestamp' : '${_csrf.parameterName}',
								'token' : '${_csrf.token}'
							},
							'fileObjName' : 'imgFile',
							'debug' : false,
							'fileTypeExts' : '*.ppt; *.pptx',
							'buttonText' : '选择PPT文件',
							'swf' : 'myfiles/Plugin/uploadify/uploadify.swf',
							'uploader' : '${baseHref.baseHref }upload?dir=file&${_csrf.parameterName}=${_csrf.token}',
							'onUploadSuccess' : function(file, data, response) {
								var data = eval("(" + data + ")");
								setImage(data, 'filePptList');
								$("#filePptId").val(data.id);
							},
							'onUploadError' : function(file, errorCode,
									errorMsg, errorString) {
								alert('The file ' + file.name
										+ ' could not be uploaded: '
										+ errorString);
							}
						});
		
		$('#file_upload_flash')
		.uploadify(
				{
					'formData' : {
						'timestamp' : '${_csrf.parameterName}',
						'token' : '${_csrf.token}'
					},
					'fileObjName' : 'imgFile',
					'debug' : false,
					'fileTypeExts' : '*.swf; *.flv; *.ppt',
					'buttonText' : '选择Flash文件',
					'swf' : 'myfiles/Plugin/uploadify/uploadify.swf',
					'uploader' : '${baseHref.baseHref }upload?dir=flash&${_csrf.parameterName}=${_csrf.token}',
					'onUploadSuccess' : function(file, data, response) {
						var data = eval("(" + data + ")");
						setImage(data, 'fileFlashList');
						$("#fileFlashId").val(data.fileShotPath);
					},
					'onUploadError' : function(file, errorCode,
							errorMsg, errorString) {
						alert('The file ' + file.name
								+ ' could not be uploaded: '
								+ errorString);
					}
				});

	});
</script>
	</div>
</div>