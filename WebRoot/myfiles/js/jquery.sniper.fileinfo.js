/**
 * 文件操作类
 */
(function($) {
	$.fn.extend({
		"fileinfo" : function(options) {
			// 设置默认值
			options = $.extend({
				
			}, options);

			var titledialog = dialog();
			/**
			 * 根据文章获取附件列表
			 */
			var getFilesByPostID = function(id, callback){
				if(id == ''){
					titledialog.content('参数为空');
	            	titledialog.show();
					return false;
				}
				var sendData = {};
				sendData['id'] = id;
				$.post('file-info/getFilesByPostID',sendData,function(text, status){
					if(text.code != 200){
						callback(text);
					}
				})
				
			}
			/**
			 * 根据附件id获取附件信息
			 */
			var getFileByID = function(id, callback){
				if(id == ''){
					titledialog.content('参数为空');
	            	titledialog.show();
					return false;
				}
				var sendData = {};
				sendData['id'] = id;
				$.post('file-info/getFileByID',sendData,function(text, status){
					if(text.code != 200){
						callback(text);
					}
				})
			}
			/**
			 * 根据文件地址获取附件信息
			 */
			var getFileByUrl = function(url, callback){
				if(url == ''){
					titledialog.content('参数为空');
	            	titledialog.show();
					return false;
				}
				var sendData = {};
				sendData['url'] = url;
				$.post('file-info/getFileByUrl',sendData,function(text, status){
					if(text.code != 200){
						callback(text);
					}
				})
			}
			/**
			 * 根据附件id删除附件
			 */
			var deleteFileByID = function(id, callback){
				if(url == ''){
					titledialog.content('参数为空');
	            	titledialog.show();
					return false;
				}
				var sendData = {};
				sendData['id'] = id;
				$.post('file-info/deleteFileByID',sendData,function(text, status){
					if(text.code != 200){
						callback(text);
					}
				})
			}
			
			
			
		}
	});
})(jQuery);
