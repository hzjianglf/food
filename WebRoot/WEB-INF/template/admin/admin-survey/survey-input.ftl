<script type="text/javascript" src="myfiles/Plugin/jquery-ui/js/jquery-ui-1.10.4.custom.min.js"></script>
<script type="text/javascript" src="myfiles/js/jquery-jtemplates.js"></script>
<script type="text/javascript" src="myfiles/js/jquery.sniper.survey.js"></script>
<script type="text/javascript" src="myfiles/Plugin/nestedSortable/jquery.mjs.nestedSortable.js"></script>

<style>
.survey{list-style: none outside none;padding : 0}
.survey ul{list-style: none outside none;}
.input-group-addon{cursor:grabbing}
.placeholder {
			outline: 1px dashed #4183C4;
			/*-webkit-border-radius: 3px;
			-moz-border-radius: 3px;
			border-radius: 3px;
			margin: -1px;*/
		}
</style>

<#assign selectHtml> 
<select class="form-control" name="question.type">
<#if toHtml.getKeys()??>
<#list toHtml.getKeys()?keys as shKey>
<option value="${shKey}">${toHtml.getKeyValue(shKey)}</option>
</#list>
</#if>
</select>
</#assign>

<ul class="survey" data-url="admin/admin-survey-page/?${_csrf.parameterName}=${_csrf.token}">
	<#if survey.surveyPages??>
	<#assign pages=survey.surveyPages>
	<#list pages as p>
	<li id="page_${p.id}" class="page">
		<div class="panel panel-default">
			<div class="panel-heading">		
				<div class="input-group">
					<span class="input-group-addon">页面</span>
					<input type="input" name="page.name" class="form-control" value='${p.name}'>
					<input type="hidden" name="page.id"  value='${p.id}'>
					<input type="hidden" name="page.survey.id"  value='${survey.id}'>
					<span class="input-group-btn">
						<button class="btn btn-primary" type="button" data-type="page_check" title="更新页面"><i class="fa fa-hdd-o"></i> 保存</button>
						<button class="btn btn-danger" type="button" data-type="page_minus" title="删除页面"><i class="fa fa-trash-o"></i></button>
						<button class="btn btn-success" type="button" data-type="page_copy" title="复制页面"><i class="fa fa-files-o"></i></button>
						<button class="btn btn-success" type="button" data-type="page_up" title="页面上移"><i class="fa fa-arrow-up"></i></button>
						<button class="btn btn-success" type="button" data-type="page_down" title="页面下移"><i class="fa fa-arrow-down"></i></button>
						<button class="btn btn-success" type="button" data-type="page_open"  data-toggle="collapse" data-target="#page_n_${p.id}"><i class="fa fa-folder-open"></i></button>		
					</span>
				</div>
			</div>
			<div id="page_n_${p.id}" class="collapse in">
				<div class="panel-body">
					<textarea rows="2" placeholder="请填写页面简述(选填)" class="form-control" name="page.note">${p.note!''}</textarea>
					<input type="hidden" name="question.surveyPage.id" value='${p.id?c}'>
				</div>
				<ul class="panel-body">
					<#if p.sq??>
					<#assign sq=p.sq>
					<#list sq as q>
					<li id="question_${q.id}" class="question">
						<div class="panel panel-success">
							<div class="panel-heading">
								<div class="input-group col-md-12">
									<span class="input-group-addon">问题</span>
									<input type="input" name="question.name" class="form-control" value="${q.name}">
									<input type="hidden" name="question.id"  value='${q.id?c}'>
									<input type="hidden" name="question.page.id" value='${p.id?c}'>
									<input type="hidden" name="question.rules.id" value='<#if q.rules??>${q.rules.id!''}</#if>'>
									
									<span class="input-group-btn">
										<button class="btn btn-default" type="button" data-type="question_check" title="更新问题"><i class="fa fa-hdd-o"></i></button>
										<button class="btn btn-danger" type="button" data-type="question_minus" title="删除问题"><i class="fa fa-trash-o"></i></button>
										<button class="btn btn-success" type="button" data-type="question_copy" title="复制页面"><i class="fa fa-files-o"></i></button>
										<button class="btn btn-success" type="button" data-type="question_up" title="问题上移"><i class="fa fa-arrow-up"></i></button>
										<button class="btn btn-success" type="button" data-type="question_down" title="问题下移"><i class="fa fa-arrow-down"></i></button>
										<button class="btn btn-success" type="button" data-type="page_open"  data-toggle="collapse" data-target="#question_n_${q.id}"><i class="fa fa-folder-open"></i></button>		
									</span>
								</div>
							</div>
							<div id="question_n_${q.id}" class="collapse">
								<div class="panel-body">
									<ul class="list-group">
										<#if q.options??>
										<#assign os=q.options>
										<#list os as o>
										<li class="option list-group-item" id="option_${o.id}" >
											<div class="row">
												<div class="col-md-7">
													<div class="input-group col-md-12">
														<span class="input-group-addon">答案</span>
														<input type="input" name="option.name" class="form-control" value="${o.name}" >
														<input type="hidden" name="option.id"  value='${o.id}'>
														<input type="hidden" name="option.question.id"  value='${q.id}'>
														<span class="input-group-btn">
															<button class="btn btn-default" type="button" data-type="option_check" title="更新问题答案页面"><i class="fa fa-hdd-o"></i></button>
															<button class="btn btn-danger" type="button" data-type="option_minus" title="删除问题答案"><i class="fa fa-trash-o"></i></button>
															<button class="btn btn-success" type="button" data-type="option_copy" title="复制页面"><i class="fa fa-files-o"></i></button>
															<button class="btn btn-success" type="button" data-type="option_up" title="答案上移"><i class="fa fa-arrow-up"></i></button>
															<button class="btn btn-success" type="button" data-type="option_down" title="答案下移"><i class="fa fa-arrow-down"></i></button>
														</span>
													</div>
												</div>
												<div class="col-md-5">
													<i class="fa fa-terminal"></i>
													<input type="checkbox" title="此题允许用户填写" ${o.writed?string('checked="checked"','')} value="true" name="option.writed"> 可填空
													<input type="checkbox" title="默认被选择" ${o.checked?string('checked="checked"','')} value="true" name="option.checked"> 默认
													<button class="btn btn-success" type="button" data-type="option_magnet" title="绑定点击可以跳转到任意题"><i class="fa fa-magnet"></i></button>
												</div>
											</div>
										</li>
										</#list>
										</#if>
									</ul>
								</div>
								<div class="panel-footer">			
									<button type="button" class="btn btn-default" data-id="${q.id}" data-name="option.question.id" data-type="option_plus"  title="添加答案"><i class="fa fa-plus-circle"></i> 添加答案</button>
								</div>
							</div>
						</div>
						<ul class="list-group">
						  	<li class="list-group-item">
							  	<div class="row">
									<div class="col-md-2">
								  		<span>必须: <input type="checkbox" <#if q.rules??>${q.rules.required?string('checked="checked"','')}</#if> value="true" name="question.rules.required"></span>
								  	</div>
									<div class="col-md-2">
										<span>验证链接: <input type="checkbox" <#if q.rules??>${q.rules.url?string('checked="checked"','')}</#if> value="true" name="question.rules.url"></span>
									</div>
								  <div class="col-md-2">
								 	<span>验证Email: <input type="checkbox" <#if q.rules??>${q.rules.email?string('checked="checked"','')}</#if> value="true" name="question.rules.email"></span>
								  </div>
								  
								  <div class="col-md-1">类型:</div>
								  <div class="col-md-2">
									  <select class="form-control" name="question.type">
										<#if toHtml.keys??>
										<#list toHtml.keys?keys as shKey>
										<option value="${shKey}" <#if shKey == q.type?c>selected="selected"</#if> >${toHtml.getKeyValue(shKey)}</option>
										</#list>
										</#if>
									</select>
								   </div>
							  	</div>
						  	</li>
						  	<li class="list-group-item">
							  <div class="row">
								  <div class="col-md-2">
								  启用大小限制: 
								  <input type="checkbox" <#if q.rules??>${q.rules.size?string('checked="checked"','')}</#if> value="true" name="question.rules.size">
								  </div>
								  <div class="col-md-2">大小范围:</div>
								  <div class="col-md-2">
								  	<input type="input" value="<#if q.rules??>${q.rules.min}<#else>0</#if>" class="form-control" name="question.rules.minSize">
								  </div>
								  <div class="col-md-2">
								  	<input type="input" value="<#if q.rules??>${q.rules.max}<#else>0</#if>" class="form-control" name="question.rules.maxSize">
								  </div>
							  </div>
						  </li>
						  <li class="list-group-item">
							  <div class="row">
								  <div class="col-md-2">
								  启用长度限制: 
								  <input type="checkbox" <#if q.rules??>${q.rules.length?string('checked="checked"','')}</#if> value="true" name="rules.length">
								  </div>
								  <div class="col-md-2">长度范围:</div>
								  <div class="col-md-2">
								  	<input type="input" value="<#if q.rules??>${q.rules.minLength}<#else>0</#if>" class="form-control" name="rules.minLength">
								  </div>
								  <div class="col-md-2">
								 	<input type="input" value="<#if q.rules??>${q.rules.maxLength}<#else>0</#if>" class="form-control" name="rules.maxLength">
								  </div>
							  </div>
						  </li>
						</ul>
					</li>
				</#list>
				</#if>
				</ul>
				<div class="panel-footer">
					<button type="button" class="btn btn-default" data-id="${p.id?c}" data-name="question.page.id" data-type="question_plus" title="添加问题"><i class="fa fa-plus-circle"></i> 添加问题</button>
				</div>
			</div>
		</div>
	</li>
	</#list>
	</#if>
	<div class="panel-footer">
		<button class="btn btn-default" type="button"  data-id="${survey.id}" data-name="page.survey.id" data-type="page_plus" title="添加页面"><i class="fa fa-plus-circle"></i></button>
	</div>
</ul>

<div id="tempdata" style="display: none;"></div>

<div class="alert alert-danger mt10">
<ol>
  <li>本页面当你点击页面一行的保存时,他只保存页面有关信息,不影响下面的问题及其答案,当点击问题的保存时道理一样</li>
  <li>多选天空题: 类型解析,此题是一个可以多次填写的问答题一个下横线(_)表示此处用户可填写,举例:(公司名称_,年收入_.),填写问题标题处,不需要答案.</li>
</ol>
</div>
<script type="text/template" id="selectHtml">
${selectHtml}
</script>

<script type="text/template" id="pageAddHtml">
<li id="page_{$T.page.id}" class="page">
	<div class="panel panel-default">
		<div class="panel-heading">		
			<div class="input-group">
				<span class="input-group-addon">页面</span>
				<input type="input" value="{$T.page.name}" class="form-control" name="page.name">
				<input type="hidden" name="page.id"  value='{$T.page.id}'>
				<input type="hidden" name="page.survey.id"  value='${survey.id}'>
				<span class="input-group-btn">
					<button class="btn btn-primary" type="button" data-type="page_check" title="更新页面"><i class="fa fa-hdd-o"></i> 保存</button>
					<button class="btn btn-danger" type="button" data-type="page_minus" title="删除页面"><i class="fa fa-trash-o"></i></button>
					<button class="btn btn-success" type="button" data-type="page_copy" title="复制页面"><i class="fa fa-files-o"></i></button>
					<button class="btn btn-success" type="button" data-type="page_up" title="答案上移"><i class="fa fa-arrow-up"></i></button>
					<button class="btn btn-success" type="button" data-type="page_down" title="答案下移"><i class="fa fa-arrow-down"></i></button>
					<button data-target="#page_n_{$T.page.id}" data-toggle="collapse" data-type="page_open" type="button" class="btn btn-success collapsed"><i class="fa fa-folder-open"></i></button>		
				</span>
			</div>
		</div>
		<div class="collapse" id="page_n_{$T.page.id}">
			<ul class="panel-body"></ul>
			<div class="panel-footer">
				<button title="添加问题" data-id="{$T.page.id}" data-name="question.page.id" data-type="question_plus" class="btn btn-default" type="button"><i class="fa fa-plus-circle"></i> 添加问题</button>
			</div>
		</div>
	</div>
</li>
</script>

<script type="text/template" id="qustionAddHtml">

<li id="question_{$T.question.id}" class="question">
	<div class="panel panel-success">
		<div class="panel-heading">
			<div class="input-group col-md-12">
				<span class="input-group-addon">问题</span>
				<input type="input" name="question.name" class="form-control" value="{$T.question.name}">
				<input type="hidden" name="question.id"  value='{$T.question.id}'>
				<input type="hidden" name="question.page.id" value='{$T.question.page.id}'>
				<input type="hidden" name="question.rules.id" value='{$T.question.rules.id}'>
				
				<span class="input-group-btn">
					<button class="btn btn-default" type="button" data-type="question_check" title="更新问题"><i class="fa fa-hdd-o"></i></button>
					<button class="btn btn-danger" type="button" data-type="question_minus" title="删除问题"><i class="fa fa-trash-o"></i></button>
					<button class="btn btn-success" type="button" data-type="question_copy" title="复制页面"><i class="fa fa-files-o"></i></button>
					<button class="btn btn-success" type="button" data-type="question_up" title="答案上移"><i class="fa fa-arrow-up"></i></button>
					<button class="btn btn-success" type="button" data-type="question_down" title="答案下移"><i class="fa fa-arrow-down"></i></button>
					<button class="btn btn-success" type="button" data-type="page_open"  data-toggle="collapse" data-target="#question_n_{$T.question.id}"><i class="fa fa-folder-open"></i></button>		
				</span>
			</div>
		</div>
		<div id="question_n_{$T.question.id}" class="collapse">
			<div class="panel-body">
				<ul class="list-group"></ul>
			</div>
			<div class="panel-footer">			
				<button type="button" class="btn btn-default" data-id="{$T.question.id}" data-name="option.question.id" data-type="option_plus" title="添加答案"><i class="fa fa-plus-circle"></i> 添加答案</button>			
			</div>
		</div>
	</div>
	<ul class="list-group">
	  	<li class="list-group-item">
		  	<div class="row">
				<div class="col-md-2">
			  		<span>必须: <input type="checkbox" {#if $T.question.rules.required = 'true'}checked="checked"{#/if} value="true" name="question.rules.required"></span>
			  	</div>
				<div class="col-md-2">
					<span>验证链接: <input type="checkbox" {#if $T.question.rules.url = 'true'}checked="checked"{#/if} value="true" name="question.rules.url"></span>
				</div>
			  <div class="col-md-2">
			 	<span>验证Email: <input type="checkbox" {#if $T.question.rules.email = 'true'}checked="checked"{#/if} value="true" name="question.rules.email"></span>
			  </div>
			  <div class="col-md-1">类型:</div>
			  <div class="col-md-2">
				  ${selectHtml}
			   </div>
		  	</div>
	  	</li>
	  	<li class="list-group-item">
		  <div class="row">
			  <div class="col-md-2">
			  启用大小限制: 
			  <input type="checkbox" value="true" {#if $T.question.rules.size = 'true'}checked="checked"{#/if} name="question.rules.size">
			  </div>
			  <div class="col-md-2">大小范围:</div>
			  <div class="col-md-2">
			  	<input type="input" value="0" class="form-control" name="question.rules.minSize" value="${$T.question.rules.minSize}">
			  </div>
			  <div class="col-md-2">
			  	<input type="input" value="0" class="form-control" name="question.rules.maxSize" value="${$T.question.rules.maxSize}">
			  </div>
		  </div>
	  </li>
	  <li class="list-group-item">
		  <div class="row">
			  <div class="col-md-2">
			  启用长度限制: 
			  <input type="checkbox" value="true" name="question.rules.length">
			  </div>
			  <div class="col-md-2">长度范围:</div>
			  <div class="col-md-2">
			  	<input type="input" value="0" class="form-control" name="question.rules.minLength" value="${$T.question.rules.minLength}">
			  </div>
			  <div class="col-md-2">
			 	<input type="input" value="0" class="form-control" name="question.rules.maxLength" value="${$T.question.rules.maxLength}">
			  </div>
		  </div>
	  </li>
	</ul>
</li>
</script>

<script type="text/template" id="optionAddHtml">

<li class="option list-group-item" id="option_{$T.option.id}" >
	<div class="row">
		<div class="col-md-7">
			<div class="input-group col-md-12">
				<span class="input-group-addon">答案</span>
				<input type="input" name="option.name" class="form-control" value="{$T.option.name}" >
				<input type="hidden" name="option.id"  value='{$T.option.id}'>
				<input type="hidden" name="option.question.id"  value='{$T.option.question.id}'>
				<span class="input-group-btn">
					<button class="btn btn-default" type="button" data-type="option_check" title="更新答案标题"><i class="fa fa-hdd-o"></i></button>
					<button class="btn btn-danger" type="button" data-type="option_minus" title="删除问题答案"><i class="fa fa-trash-o"></i></button>
					<button class="btn btn-success" type="button" data-type="option_copy" title="复制答案"><i class="fa fa-files-o"></i></button>
					<button class="btn btn-success" type="button" data-type="option_up" title="答案上移"><i class="fa fa-arrow-up"></i></button>
					<button class="btn btn-success" type="button" data-type="option_down" title="答案下移"><i class="fa fa-arrow-down"></i></button>
				</span>
			</div>
		</div>
		<div class="col-md-5">
			<i class="fa fa-terminal"></i>
			<input type="checkbox" title="此题允许用户填写" value="true" name="option.writed" {#if $T.option.writed = 'true'}checked="checked"{#/if} > 可填空
			<input type="checkbox" title="默认被选择" value="true" name="option.checked" {#if $T.option.checked == 'true'}checked="checked"{#/if}> 默认
			<button class="btn btn-success" type="button" data-type="option_magnet" title="绑定点击可以跳转到任意题"><i class="fa fa-magnet"></i></button>
		</div>
	</div>
</li>
</script>

<script type="text/javascript">

$(function(){
 	$().snipersurvey();
});

</script>