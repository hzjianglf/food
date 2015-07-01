<title>PPT管理</title>
<div class="home_main">
	<div class="home_main_nav">
		<a href="user/ppts/insert" class="home_main_nav_hove">上传PPT</a>
		<a href="user/ppts/" class="home_main_nav_default">PPT管理</a>
	</div>
    <div class="home_main_xw">
		<ul class="material_manage">
			
			<#if lists??>
			<#list lists as ppt>
        	<li>
            	<a href="ppt/show/${ppt.id}" class="material_manage_img" target="_blank">
	            	<#assign imgs = ppt.files>
		    		<#if imgs??>
		    		<#list imgs as img>
		    		<img width="311" height="195" src="${systemConfig.imagePathPrefx }${img.newPath}" alt="${ppt.name}"/>
		    		<#break>
		    		</#list>
		    		<#else>
		    		<img src="myfiles/ppt/images/2015-05-11_165005.png" width="311" height="195" alt=""/>
		    		</#if>
	    		</a>
                <span class="material_manage_title">
                	<a href="ppt/show/${ppt.id}" title="${ppt.name}" class="material_manage_title_text"><#if ppt.name?length gt 18>${ppt.name?substring(0,18)}<#else>${ppt.name}</#if></a>
                    <a href="user/ppts/update?id=${ppt.id}" class="material_manage_but"><i class="fa fa-pencil"></i></a>
                </span>
                </li>
            	</#list>
            	</#if>
        </ul>
    	<div class="next"><div class="pagination">${pageHtml }</div></div>
	</div>
</div>