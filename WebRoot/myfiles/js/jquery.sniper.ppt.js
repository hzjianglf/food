/**
 * 问卷动态添加
 */
(function($) {
	$.fn
			.extend({
				"sniperppt" : function(options) {
					// 设置默认值
					options = $.extend({
						baseHref : '',
						imagePathPrefx : '',
						// 图片列表存放id
						filesList : 'filesList',
						// 存放图片的id
						filesId : 'filesId',
						tempfileValue : [],
						tempfileValuePpt : {},
						flashTemp : '',
						parameterName : '_csrf',
						token : ''
					}, options);

					var tempfileValue = '<li class="li" id="{$T.id}">';
					tempfileValue += '<a href="{$T.filePath}" target="_blank"><img src="{$T.filePath}" title="{$T.oldName}" class="img-thumbnail"></a>';
					tempfileValue += '<p data-div="filesId" data-id="{$T.id}" class="del"><i class="fa fa-times-circle"></i></p>';
					tempfileValue += '</li>';

					var tempfileValuePpt = '<li class="li" id="{$T.id}">';
					tempfileValuePpt += '<img class="" src="myfiles/images/webicon/ppt-file.png" title="{$T.oldName}">';
					tempfileValuePpt += '<p data-div="filePptId" data-id="{$T.id}" class="del"><i class="fa fa-times-circle"></i></p>';
					tempfileValuePpt += '</li>';

					var tempfileValueflash = '<li class="li" id="flashID">';
					tempfileValueflash += '<img class="" src="myfiles/images/webicon/flash-file.png">';
					tempfileValueflash += '<p data-div="fileFlashId" data-id="flashID" class="del"><i class="fa fa-times-circle"></i></p>';
					tempfileValueflash += '</li>'

					var titledialog = dialog();
					var getFilesPost = function(div) {
						var filesid = new Array();
						var filesPost = $('#' + div).val();
						if (filesPost != '') {
							filesid = filesPost.split(',');
						}
						return filesid;
					}

					var setFilesPost = function(data, div) {
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

					var delFilesPost = function(fileid, div) {
						filesid = getFilesPost(div);
						for (var i = 0; i < filesid.length; i++) {
							if (filesid[i] == fileid) {
								// 删除数组
								filesid.splice(i, 1);
							}
						}

						$('#' + div).val(filesid.join(','));
					}

					/**
					 * 设置图片
					 */
					function setImage(data, div) {
						
						console.log(data);
						
						var filePath = data.filePath;
						if (data.filePath.substring(0, 4) != "http") {
							filePath = options.imagePathPrefx + filePath;
						}

						data.filePath = filePath;
						switch (div) {

						case "filesList":
							setFilesPost(data, 'filesId');
							$('#tempdata').setTemplate(tempfileValue)
									.processTemplate(data);
							html = $('#tempdata').html();
							break;
						case "filePptList":
							$('#tempdata').setTemplate(tempfileValuePpt)
									.processTemplate(data);
							html = $('#tempdata').html();
							$('#' + div).empty();
							break;
						case "fileFlashList":
							$('#tempdata').setTemplate(tempfileValueflash)
									.processTemplate(data);
							html = $('#tempdata').html();
							$('#' + div).empty();
							break;
						}

						$('#' + div).append(html);

					}

					// 删除文件
					var unBindfile = function(id, div) {

						if (!id) {
							return false;
						}

						var d = dialog({
							title : '提示',
							content : '确定删除图片?',
							okValue : '确定',
							ok : function() {
								$.post('file-info/deleteFileByID?'
										+ options.parameterName + '='
										+ options.token, {
									id : id
								}, function(data, textStatus) {
									if (data.code == 200) {
										$('#' + id).remove();
										if (div == "fileFlashId") {
											$('#' + div).val('');
										}
										delFilesPost(id, div);
									} else {
										alert("删除失败");
									}
								}, 'json');
								return true;
							},
							cancelValue : '取消',
							cancel : function() {
							}
						});
						d.show();
					}

					// 绑定删除操作
					$(".fileList").on("click", "p", function() {
						id = $(this).attr('data-id');
						div = $(this).attr('data-div');
						unBindfile(id, div);
					})
						
					//数据初始化
					if (options.tempfileValue) {
						var fileValue = eval("(" + options.tempfileValue + ")");
						for ( var i in fileValue) {
							console.log(fileValue[i]);
							setImage(fileValue[i], 'filesList');
						}
					}

				
					if (options.tempfileValuePpt) {
						var fileValuePpt = eval("(" + options.tempfileValuePpt + ")");
						setImage(fileValuePpt, 'filePptList');
					}

					if (options.flashTemp) {
						var flashPath = {};
						flashPath.filePath = options.flashTemp;
						setImage(flashPath, 'fileFlashList');
					}
					
					//三个上传
					$('#file_upload')
							.uploadify(
									{
										'formData' : {
											'timestamp' : options.parameterName,
											'token' : options.token
										},
										'fileObjName' : 'imgFile',
										'debug' : false,
										'buttonText' : '选择图片',
										'fileTypeDesc' : 'Image Files',
										'fileTypeExts' : '*.gif; *.jpg; *.png; *.gif; *.tiff',
										'swf' : 'myfiles/Plugin/uploadify/uploadify.swf',
										'uploader' : options.baseHref
												+ 'upload?'
												+ options.parameterName + '='
												+ options.token,
										'onUploadSuccess' : function(file,
												data, response) {
											var data = eval("(" + data + ")");
											setImage(data, 'filesList');
										},
										'onUploadError' : function(file,
												errorCode, errorMsg,
												errorString) {
											alert('The file '
													+ file.name
													+ ' could not be uploaded: '
													+ errorString);
										}
									});

					$('#file_upload_ppt')
							.uploadify(
									{
										'formData' : {
											'timestamp' : options.parameterName,
											'token' : options.token
										},
										'fileObjName' : 'imgFile',
										'debug' : false,
										'fileTypeExts' : '*.ppt; *.pptx',
										'buttonText' : '选择PPT文件',
										'swf' : 'myfiles/Plugin/uploadify/uploadify.swf',
										'uploader' : options.baseHref
												+ 'upload?'
												+ options.parameterName + '='
												+ options.token,
										'onUploadSuccess' : function(file,
												data, response) {
											var data = eval("(" + data + ")");
											setImage(data, 'filePptList');
											$("#filePptId").val(data.id);
										},
										'onUploadError' : function(file,
												errorCode, errorMsg,
												errorString) {
											alert('The file '
													+ file.name
													+ ' could not be uploaded: '
													+ errorString);
										}
									});

					$('#file_upload_flash')
							.uploadify(
									{
										'formData' : {
											'timestamp' : options.parameterName,
											'token' : options.token
										},
										'fileObjName' : 'imgFile',
										'debug' : false,
										'fileTypeExts' : '*.swf; *.flv; *.ppt',
										'buttonText' : '选择Flash文件',
										'swf' : 'myfiles/Plugin/uploadify/uploadify.swf',
										'uploader' : options.baseHref
												+ 'upload?'
												+ options.parameterName + '='
												+ options.token,
										'onUploadSuccess' : function(file,
												data, response) {
											var data = eval("(" + data + ")");
											setImage(data, 'fileFlashList');
											$("#fileFlashId").val(
													data.fileShotPath);
										},
										'onUploadError' : function(file,
												errorCode, errorMsg,
												errorString) {
											alert('The file '
													+ file.name
													+ ' could not be uploaded: '
													+ errorString);
										}
									});

				}
			});
})(jQuery);
