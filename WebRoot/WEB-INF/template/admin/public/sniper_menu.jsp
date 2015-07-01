<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="s"%>

<div id="sniper_menu" class="btn-group" data-spy="affix"
	data-offset-top="100">
	<div class="btn-group">
		<button type="button" class="btn btn-default dropdown-toggle"
			data-toggle="dropdown">
			<i class="fa fa-angle-double-down"></i>
		</button>
		<ul class="dropdown-menu">
			<li><a href="javascript:;" data-value="1" data-type="select">全选</a></li>
			<li><a href="javascript:;" data-value="2" data-type="select">不选</a></li>
			<li><a href="javascript:;" data-value="3" data-type="select">反选</a></li>
		</ul>
	</div>
	<div class="btn-group">
		<button type="button" class="btn btn-danger" data-click="on" data-value="delete"
			data-type="delete">
			<i class="fa fa-trash-o"></i>
		</button>
	</div>

	<s:if test="${sniperMenu != null }">
		<s:set value="${sniperMenu.params }" var="sniperMenu1" scope="page"></s:set>
		<s:forEach var="myKey" items="${sniperMenu1 }">
			<div class="btn-group">
				<button type="button" class="btn btn-default dropdown-toggle"
					data-toggle="dropdown">
					${ sniperMenu.getKeyValue(myKey.key)} <span class="caret"></span>
				</button>
				<ul class="dropdown-menu">
					<s:forEach var="key" items="${myKey.value }">
						<li><a href="javascript:;" data-value='${key.key}'
							data-type='${myKey.key}'>${key.value }</a>
					</s:forEach>
				</ul>
			</div>
		</s:forEach>
	</s:if>

	<div class="btn-group">
		<button type="button" class="btn btn-info" data-value="none"
			data-type="none">${endTime}</button>
	</div>
</div>

<!-- 调用 -->
<script type="text/javascript" src="myfiles/js/jquery.sniper.menu.js"></script>
<script type="text/javascript">
	$(function() {
		$().snipermenu({
			url : '${sniperUrl}?${_csrf.parameterName}=${_csrf.token}'
		});
	});
</script>