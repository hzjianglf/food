<title>首页</title>

<div class="main">
    <div class="banner">
    </div>
	<div class="column">
    	<span class="column_title"><a href="index.html">最新发布</a></span><span class="column_more"><a href="ppt">更多</a></span>
    </div>
	<ul class="list">
		<#if pptFiles??>
		<#list pptFiles as ppt>
    	<li>
    		<!-- 处理图片 -->
    		<a href="ppt/show/${ppt.id}" target="_blank">
    		<#assign imgs = ppt.files>
    		<#if imgs??>
    		<#list imgs as img>
    		 <img src="${systemConfig.imagePathPrefx!'' }${imageHelpUtil.show(img.newPath,310,195)!''}" alt="${ppt.name}"/>
    		<#break>
    		</#list>
    		<#else>
    		 <img src="myfiles/ppt/images/2015-05-11_165005.png" width="310" height="195" alt=""/>
    		</#if>
    		</a>
            <a href="ppt/show/${ppt.id}" target="_blank" class="list_title"><#if ppt.name?length gt 18>${ppt.name?substring(0,18)}<#else>${ppt.name}</#if></a>
            <div class="list_icon list_down">
            	<ul>
                    <li><a href="ppt/download/${ppt.id}" target="_blank"><i class="fa fa-download"></a></i></li>
                </ul>
            </div>
        </li>
        </#list>
        </#if>
    </ul>
</div>