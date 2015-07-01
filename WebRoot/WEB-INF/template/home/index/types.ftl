
<title>频道列表</title>

<div class="main">
	<#if channelsTop??>
   	<#list channelsTop as ct>
	<div class="nav_class">
    	<h3 class="nav_class_title"><a href="#">${ct.name}</a></h3>
        <ul>
        	<#if channels??>
        	<#assign kid = ct.id>
        	<#assign clts = channels[kid?c]>
        	<#list clts as clt>
        	<li><a href="ppt?id=${clt.id}" target="_blank">${clt.name}</a></li>
        	</#list>
            </#if>
        </ul>
    </div>
    </#list>
    </#if>
</div>