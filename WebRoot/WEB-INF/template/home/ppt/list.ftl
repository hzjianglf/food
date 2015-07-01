<title>PPT列表</title>
<title><#if channelTop?? && channel??>${channel.name}/${channelTop.name}</#if><#if key??>${key}</#if></title>

<div class="main">
	
	<div class="way">
        <ol class="breadcrumb">
          <li><a href="./">首页</a></li>
          <#if channelTop??>
          <li><a href="types">${channelTop.name}</a></li>
          </#if>
          <#if channel??>
          <li class="active"><a href="ppt?id=${channel.id}" >${channel.name}</a></li>
          </#if>
          <#if key??><li class="active">${key}</li></#if>
        </ol>
    </div>
    
	<ul class="list">
    	<#if lists??>
		<#list lists as ppt>
    	<li>
    		<!-- 处理图片 -->
    		<#assign imgs = ppt.files>
    		<#if imgs??>
    		<#list imgs as img>
    		<a href="ppt/show/${ppt.id}" target="_blank"><img src="${systemConfig.imagePathPrefx }${img.newPath}" alt="${ppt.name}"/></a>
    		<#break>
    		</#list>
    		<#else>
    		 <img src="myfiles/ppt/images/2015-05-11_165005.png" width="430" height="315" alt=""/>
    		</#if>
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
	<div class="next"><div class="pagination">${pageHtml }</div></div>
</div>
