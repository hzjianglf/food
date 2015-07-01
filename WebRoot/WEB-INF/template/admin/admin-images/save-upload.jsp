<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

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

</style>
<form id="sniperForm" class="form-horizontal" role="form" action="" method="POST">
	<div class="form-group">
		<label for="name" class="col-sm-1 control-label">时间</label>
		<div class="col-sm-4">
			<input name="eventdate" class="form-control Wdate" placeholder="时间">
		</div>
	</div>
	
	<div class="form-group">
		<label for="name" class="col-sm-1 control-label">标签</label>
		<div class="col-sm-10">
			<input name="tags" class="form-control" placeholder="标签">
			<br>
			<button type="button" class="btn btn-danger clearTags" >清空标签</button>
			<div class="help-block">
				<p class="text-success">第一步:选择上传的图片</p>
				<p class="text-success">第二步:填写你的标签</p>
				<p class="text-success">第三步:点击"开始上传"结束</p>
				<p class="text-success">填写的标签会和你所有的图片绑定</p>
				<p class="text-success">填写标签回车确定</p>
			</div>
		</div>
	</div>
		
	<div class="form-group">
		<label for="tags" class="col-sm-1 control-label">
			<button type="button" class="btn btn-danger" onclick="javascript:$('#file_upload').uploadify('upload','*')">开始上传</button>
		</label>
		<div class="col-sm-10">
			<input id="file_upload" name="file_upload" type="file"
				multiple="true">
		</div>
	</div>
	<div class="form-group">
		<ol class="fileList mt10" id="filesList"></ol>
	</div>
</form>

<link
	href="myfiles/Plugin/jquery-ui-1.10.3.custom/css/ui-lightness/jquery-ui-1.10.3.custom.min.css"
	media="screen" rel="stylesheet" type="text/css">
<link href="myfiles/Plugin/jQuery-Tags-Input/jquery.tagsinput.css"
	media="screen" rel="stylesheet" type="text/css">
<script type="text/javascript"
	src="myfiles/Plugin/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script>
<script type="text/javascript"
	src="myfiles/Plugin/jQuery-Tags-Input/jquery.tagsinput.min.js"></script>
<script src="myfiles/Plugin/uploadify/jquery.uploadify.min.js"
	type="text/javascript"></script>
<link rel="stylesheet" type="text/css"
	href="myfiles/Plugin/uploadify/uploadify.css">
<script type="text/javascript" src="myfiles/js/jquery-jtemplates.js"></script>
<script type="text/javascript"
	src="${baseHref.baseHref }myfiles/Plugin/My97DatePicker/WdatePicker.js"></script>
<script type="text/template" id="tempfileValue">
<li class="li" id="{$T.id}">
	<a href="admin/admin-images/update?id={$T.id}" target="_blank"><img src="{$T.filePath}" title="{$T.oldName}" class="img-thumbnail"></a>
	<a href="javascript:;" onclick="unBindfile({$T.id}, 'filesId')" class="del"><i class="fa fa-times-circle"></i></a>
</li>
</script>

<div id="tempdata" style="display: none;"></div>
<script type="text/javascript">

	$('.Wdate').bind('click', function() {
		WdatePicker({
			dateFmt : 'yyyy-MM-dd'
		})
	});
	/**
	 * 设置图片
	 */
	function setImage(data, div) {

		if (data.filePath == null || data.filePath == '') {
			return false;
		}

		var filePath = data.filePath;
		if (data.filePath.substring(0, 4) != "http") {
			filePath = '${systemConfig.imagePathPrefx}' + filePath;
		}
		data.filePath = filePath;

		$('#tempdata').setTemplate($('#tempfileValue').html())
				.processTemplate(data);
		html = $('#tempdata').html();

		$('#' + div).append(html);
	}
	
	//删除文件
	function unBindfile(id, div) {
	
		if (!id) {
			return false;
		}
		
		var d = dialog({
		    title: '提示',
		    content: '确定删除图片?',
		    okValue: '确定',
		    ok: function () {
		    	$.post('file-info/deleteFileByID?${_csrf.parameterName}=${_csrf.token}',{id : id}, 
		    			function(data, textStatus) {
							if (data.code == 200) {
								$('#' + id).remove();
							}else{
								alert("删除失败");
							}
						}
		    	, 'json');
		        return true;
		    },
		    cancelValue: '取消',
		    cancel: function () {}
		});
		d.show();
	}

	$(function() {
		
		$("input[name='tags']").tagsInput({
			width : '100%',
			height : '100px',
			defaultText : '添加标签', //默认文字
			autocomplete_url : 'public/sendAjaxTags',
			onAddTag:function(tag){
                 $.post('public/addAjaxTags?${_csrf.parameterName}=${_csrf.token}',{term: tag},function(){});
            }
		});
		
		$(".clearTags").click(function(){
			$("input[name='tags']").importTags('');
		})
		
		$('#file_upload')
				.uploadify(
						{
							'formData' : {
								'timestamp' : '${_csrf.parameterName}',
								'token' : '${_csrf.token}'
							},
							'auto': false,
							'fileObjName' : 'imgFile',
							'debug' : false,
							'buttonText' : '选择图片',
							'fileTypeDesc' : 'Image Files',
							'fileTypeExts' : '*.gif; *.jpg; *.png; *.gif; *.tiff',
							'swf' : 'myfiles/Plugin/uploadify/uploadify.swf',
							'uploader' : '${baseHref.baseHref }upload?album=true&${_csrf.parameterName}=${_csrf.token}${firefox}',
							'onUploadSuccess' : function(file, data, response) {
								var data = eval("(" + data + ")");
								setImage(data, "filesList");
							},
							'onUploadError' : function(file, errorCode,
									errorMsg, errorString) {
								alert('The file ' + file.name
										+ ' could not be uploaded: '
										+ errorString);
							},
							onUploadStart:function(file){
								//动态传参数
								var element = {};
								element.tags = $("input[name='tags']").val();
								element.eventdate = $("input[name='eventdate']").val();
								
								/* if(element.tags == ''){
									 if(confirm("标签为空,是否取消上传?")){
										 $("#file_upload").uploadify('upload', '*');
										 return false;
									}
							     }  */
								
								$("#file_upload").uploadify("settings","formData",element);
							}
						});

	});
</script>

