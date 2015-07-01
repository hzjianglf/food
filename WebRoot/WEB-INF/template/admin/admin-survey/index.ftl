
<div class="seach">
	<form id="searchFrom" class="form-inline" role="form" name="searchFrom"
		method="get" action="">
		<div class="form-group">
			<label for="searchInteger_status" class="sr-only">状态</label>
			<@s.select list="statusList" name="searchInteger.status"
				headerKey="" headerValue="select" cssClass="form-control" />
		</div>

		<div class="form-group">
			<label for="searchString_name" class="sr-only">关键词</label>
			<@s.textfield name="searchString.name" cssClass="form-control" />

		</div>
		<div class="form-group">
			<button class="btn btn-success" type="submit" name="searchString.submit" value="search">
		  		查询
		  	</button>
		</div>
	</form>
</div>

<#include "../public/sniper_menu.ftl">

<table class="table table-hover">
	<thead>
		<tr>
			<th>ID</th>
			<th>名称</th>
			<th>时间</th>
			<th>参与人数</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<#if lists??>
		<#list lists as l>
			<tr>
				<td><input type="checkbox" name="list.id" value="${l.id}" /><a href="admin/admin-survey/update?id=${l.id}" target="_blank">${l.id}</a></td>
				<td><a href="admin/admin-survey/answer?id=${l.id}" title="查看问题列表" target="_blank">${l.title}</a>(<i class="fa fa-${l.locked?string("un", "")}lock"></i>)</td>
				<td>${l.cTime}</td>
				<td>${l.peopleNum}</td>
				<td>
				<a href="admin/admin-survey/survey?id=${l.id}" title="查看问题列表" target="_blank"><i class="fa fa-list-ol"></i></a> 
				<a href="admin/admin-survey/surveyresult?id=${l.id}" target="_blank" title="查看图片结果"><i class="fa fa-bar-chart-o"></i></a>
				<a href="admin/admin-survey/surveytext?id=${l.id}" target="_blank" title="查看文本结果"><i class="fa fa-file-text"></i></a>
				<a href="admin/admin-survey/surveyexport?id=${l.id}" target="_blank" title="导出填写结果"><i class="fa fa-share"></i></a> 
				</td>
			</tr>
		</#list>
		</#if>
	</tbody>
</table>
<div class="meneame">${pageHtml }</div>

<div class="alert alert-success">
	<p><i class="fa fa-list-ol"></i> 点击可以编辑问卷的问题及其答案。</p>
	<p><i class="fa fa-edit"></i> 点击可以编辑问卷的基本信息。</p>
	<p><i class="fa fa-bar-chart-o"></i> 点击查看图片调查结果。</p>
	<p><i class="fa fa-file-text"></i> 点击查看数字调查结果。</p>
	<p><i class="fa fa-share"></i> 数据导出。</p>
	<p>非管理员只能删除、审核自己的问卷,对于已经参加过的问卷不允许删除。</p>
</div>