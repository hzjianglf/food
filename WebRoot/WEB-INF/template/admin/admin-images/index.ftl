
<#include "../public/sniper_menu_images.ftl">
<style>
#sniper_menu{z-index: 1000;}
h3{  word-wrap: break-word;  font-size: 18px; line-height: 25px;}
.row .thumbnail{ position: relative; height: 400px;}
.row .thumbnail .del {
	right: -3px;
	top: -8px;
	font-size: 18px;
	position: absolute;
	z-index: 100;
	color: black;
}
.row .thumbnail .download {
	right: 20px;
	top: -8px;
	font-size: 18px;
	position: absolute;
	z-index: 100;
	color: black;
}
.row .thumbnail .ipt {
	top: 5px;
	left: 10px;
	z-index: 100;
	position: absolute;
}
</style>
<div id="layer-photos" class="row">
 	<#if lists??>
 	<#list lists as l>
 	<div class="col-sm-3 col-md-3" id="sl_${l.id}">
	 	<div class="thumbnail">
	      <img alt="${l.oldName!'' }" title="${l.oldName!'' }" layer-pid="${l.id }" layer-src="${systemConfig.imagePathPrefx}${l.newPath }" src="${systemConfig.imagePathPrefx}${imageHelpUtil.show(l.newPath,300,270)!''}" alt="${l.oldName!''}">
	      <a href="javascript:;" onclick="unBindfile(${l.id})" class="del"><i class="fa fa-times-circle"></i></a>
	      <a href="file-info/download?id=${l.id}" target="_blank" class="download"><i class="fa fa-download"></i></a>
	      <input type="checkbox" name="list.id" value="${l.id}" class="ipt" />
	      <div class="caption">
	        <h3><a href="admin/admin-images/update?id=${l.id}" target="_blank">${l.oldName!'' }</a></h3>
	        <p><#if l.tags??>
	        <#list l.tags?split(",") as tag>
	        	<a href="admin/admin-images/?key=${tag?html}">${tag}</a>
	        </#list>
	        </#if>
	        </p>
	      </div>
	 	</div>
 	</div>
	</#list>
	</#if>
</div>
<div class="meneame">${pageHtml }</div>
<link href="myfiles/Plugin/artDialog/css/ui-dialog.css" media="screen" rel="stylesheet"
	type="text/css">
<script src="myfiles/Plugin/artDialog/dist/dialog-min.js"></script>
<script src="myfiles/Plugin/layer-v1.9.3/layer/layer.js"></script>
<script type="text/javascript">

//加载扩展模块
layer.config({
    extend: 'extend/layer.ext.js'
});


//页面一打开就执行，放入ready是为了layer所需配件（css、扩展模块）加载完毕
layer.ready(function(){ 
    
    //使用相册
    layer.photos({
        photos: '#layer-photos'
    });
});

$(function(){
	$().snipermenu({
		div : '#layer-photos',
		url:'${sniperUrl!''}?${_csrf.parameterName}=${_csrf.token}'
		
	 });
 });

//删除文件
function unBindfile(id) {
	
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
								$('#sl_' + id).remove();
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

</script>
