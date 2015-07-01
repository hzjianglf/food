<title>${pptFile.name!''}</title>

<div class="main">
	<div class="way">
        <ol class="breadcrumb">
          <li><a href="./">首页</a></li>
          <#if channelTop??>
          <li><a href="types">${channelTop.name}</a></li>
          </#if>
          <#if channel??>
          <li class="active"><a href="ppt?id=${channel.id}">${channel.name}</a></li>
          </#if>
        </ol>
    </div>
    <div class="arc_thumbnail"><!--缩略图-->
    
	  <object id="FlashID" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="720" height="577">
		  <param name="movie" value="${pptFile.flash!''}">
		  <param name="quality" value="high">
		  <param name="wmode" value="opaque">
		  <param name="swfversion" value="8.0.35.0">
		  <!-- 此 param 标签提示使用 Flash Player 6.0 r65 和更高版本的用户下载最新版本的 Flash Player。如果您不想让用户看到该提示，请将其删除。 -->
		  <param name="expressinstall" value="Scripts/expressInstall.swf">
		  <!-- 下一个对象标签用于非 IE 浏览器。所以使用 IECC 将其从 IE 隐藏。 -->
		  <!--[if !IE]>-->
		  <object type="application/x-shockwave-flash" data="${pptFile.flash!''}" width="720" height="577">
		    <!--<![endif]-->
		    <param name="quality" value="high">
		    <param name="wmode" value="opaque">
		    <param name="swfversion" value="8.0.35.0">
		    <param name="expressinstall" value="Scripts/expressInstall.swf">
		    <!-- 浏览器将以下替代内容显示给使用 Flash Player 6.0 和更低版本的用户。 -->
		    <div>
		      <h4>此页面上的内容需要较新版本的 Adobe Flash Player。</h4>
		      <p><a href="http://www.adobe.com/go/getflashplayer"><img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="获取 Adobe Flash Player" width="112" height="33" /></a></p>
	        </div>
		    <!--[if !IE]>-->
	    </object>
		  <!--<![endif]-->
	  </object>
    </div>
    <hr>
    <div class="arc_attribute"><!--属性-->
    	<h2 class="arc_title">${pptFile.name!''}</h2>
        <p><i class="fa fa-tag"></i>&nbsp;<strong>关键字：</strong>
        <#if pptFile.tags??>
        <#list pptFile.tags?split(",") as tag>
        	<a href="ppt?key=${tag?html}" target="_blank">${tag}</a>
        </#list>
        </#if>
        </p>
    </div>
    <a href="ppt/download/${pptFile.id!''}" class="down_buttom">
        <span><i class="fa fa-download"></i>&nbsp;立即下载</span>
        <br/>下载素材会消耗积分
    </a>
</div>
<script type="text/javascript">
swfobject.registerObject("FlashID");
swfobject.registerObject("FlashID");
</script>