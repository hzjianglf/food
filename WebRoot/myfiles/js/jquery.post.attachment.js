/**
 * 处理新闻和附件之间的关系
 * 
 * @param $
 */
// 跨与访问jsonp
jQuery.support.cors = true;
(function ($) {
   $.fn.extend({
        "postattachement": function (options) {
            // 设置默认值
            options = $.extend({
            	// 根据url获取文件
            	getFileUrl		: 'admin/file-info/get-file-url',
            	// 根据id获取文件
            	getFileId		: 'admin/file-info/get-file-id',
            	getFilePostId	: 'admin/file-info/get-file-post-id',
            	// 根据id删除图片
            	deleteFileId	: 'admin/file-info/delete-file-id',
            	filePrefix		: 'postfile_',
            	// 相关文章
            	postId			: 0
            	
            	
            }, options);
            
            /**
			 * 设置图片
			 */
        	function setImage(data)
        	{
        		// 新版设置附件id
        		var filesid	= $('input[name="filesPost"]').val();
        		if(filesid !=''){
        			filesid	= filesid.split(',');
        		}else{
        			var filesid	= new Array();
        		}
        		filesid.push(data.id);
        		$('input[name="filesPost"]').val(filesid.join(','));

        		html='<li class="li" id="' + option.filePrefix + data.id + '">';
        		if(data.fileType.substring(0, 5) == 'image'){
        			html+='<img src="'+data.filePath+'" title="'+data.oldName+'">';
        		}else{
        			html+='<img src="myfiles/images/webicon/attach_64.png" title="'+data.oldName+'">';
        		}
        		html+='<div class="info">';
        		
        		if(false && data.fileType.substring(0, 5) == 'image' ){
        			html+='<input type="checkbox" id="c'+ data.id + '"';
        			if(data.checked == 1){
        				html+=' checked="checked" ';
        			}
        			html+=' value="' + data.id + '" onclick="setPic(' + data.id + ')"> 新闻图片';
        		}
        		
        		html+='<a href="javascript:showpath(\'' + data.url + '\')" class="alertpath">复制路径</a>';
        		html+='<a href="javascript:delfile(' + data.id + ')" class="del">删除</a>';
        		html+='</div>';
        		html+='</li>';
        		
        		$('#fileValue').append(html);
        	}

        	// 设置图片新闻
        	function setPic(obj)
        	{
        		id	= $('#c' + obj).val();
        		checked	= $('#c' + obj).attr("checked");
        		if(checked=='checked'){
        			checked	= 1;
        		}else{
        			checked	= 0;
        		}

        		$.post('', {id:id,top:checked},
        				function (data, textStatus){
        					if(data.message==1){
        						art.dialog({
        						    time: 2,
        						    content: '操作成功'
        						});
        					}
        		},'json');
        	}
        	// 设置图片新闻
        	function getImage(url, type)
        	{
        		if(!type) type='url';
        		if(!url)  { alert('出错'); return false;}
        		if(type == "url"){
        			$.post(options.getFileUrl, {filePath: url},
        					function (data, textStatus){
        						for(var i in data){
        							if(data[i]){
        								setImage(data[i]);
        							}
        						}
        				},'json');
        		}else if(type == 'post-id'){
        			$.post(options.getFileId, {id: url},
        					function (data, textStatus){
        						for(var i in data){
        							if(data[i]){
        								setImage(data[i]);
        							}
        						}
        				},'json');
        		}
        	}
        	
        	// 弹出当前文件地址以便复制使用
        	function showpath(path)
        	{
        		art.dialog({
        		    content: path,
        		    ok: true,
        		    okVal: '关闭'
        		});
        	}
        	// 删除文件
        	function delfile(id) {
        		if(!id)  { alert('出错'); return false;}

        		var dialog = art.dialog({
        		    title: '附件删除',
        		    content: '图片删除不可恢复,与其相关联的文章内容图片将无法显示，请及时删除文章内容图片！',
        		    okVal:'删除图片',
        		    ok: function(){
        		    	$.post(options.deleteFileId, {id:id },
        		    			function (data, textStatus){
        		    		   if(data.message){ $('#' + option.filePrefix+id).remove();}
        		    		},'json');
        		        return true;
        		    },
        			
        		    cancelVal: '取消',
        		    cancel: true // 为true等价于function(){}
        		});
        	}
           
           
           
            // end
        }
   	 });
})(jQuery);