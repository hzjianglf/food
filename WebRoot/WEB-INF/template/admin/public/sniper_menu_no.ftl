<div id="sniper_menu" class="btn-group" data-spy="affix" data-offset-top="10">
	<div class="btn-group">
		<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
  			<i class="fa fa-angle-double-down"></i>
  		</button>
  		<ul class="dropdown-menu">
			<li><a href="javascript:;" data-value="1" data-type="select">全选</a></li>
			<li><a href="javascript:;" data-value="2" data-type="select">不选</a></li>
			<li><a href="javascript:;" data-value="3" data-type="select">反选</a></li>
		</ul>
	</div>
	<div class="btn-group">
		<button type="button" title="删除所选" class="btn btn-danger" data-click="on" data-value="delete" data-type="delete"><i class="fa fa-trash-o"></i></button>  		
	</div>
	<#if sniperMenu??>
	<#assign sniperMenu1=sniperMenu.params!'null'>
	<#list sniperMenu1?keys as myKey>
	<#assign oper=myKey!'null'>
	<div class="btn-group">
		<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"> ${sniperMenu.getKeyValue(oper)} <span class="caret"></span>
  		</button>
  		<ul class="dropdown-menu">
  		<#assign value=sniperMenu.getMapValue(oper)!'null'>
  		<#list value?keys as v>
  			<li><a href="javascript:;" data-value='${v}' data-type='${oper}'>${sniperMenu.getMapValueString(oper,v)!''}</a>
  		</#list>
		</ul>
	</div>
	</#list>
	</#if>
	
	<div class="btn-group">
		<button type="button" class="btn btn-info" data-value="none" data-type="none">${endTime}</button>  		
	</div>
</div>

<!-- 调用 -->
<script type="text/javascript" src="${baseHref.baseHref }myfiles/js/jquery.sniper.menu.js"></script>
