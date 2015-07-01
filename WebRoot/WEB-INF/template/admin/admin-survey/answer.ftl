<style type="text/css">
.p_title{ text-align: center;padding:10px 0; margin: 10px 0; font-weight: bold; line-height: 36px;border-bottom: 1px solid #e1e1e1;}
.q_title{line-height: 20px;padding:5px 0; margin: 5px 0;  color: #444444;font-size: 15px;font-weight: bold;}
.q_body{ text-indent: 1em;}
.q_ul{list-style: outside none ${listStyle!'decimal'};}
</style>

<div class="jumbotron">
  <h1>${model.title}</h1>
  <p>${model.note}</p>
</div>
<#if resultErrors?size gt 0>
<div class="alert alert-warning alert-dismissible" role="alert">
  <button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
  <strong>Warning!</strong> 
  <ul>
  	<#list resultErrors?keys as r>
  	<#assign rs = resultErrors.get(r)>
  	<#list rs as e>
  	<li>${e}</li>
  	</#list>
  	</#list>
  </ul>
</div>
</#if>

<form class="form-horizontal" id="mcform" action="" role="form" method="post">
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
<#if model.surveyPages??>
<#assign pages=model.surveyPages>
	<#list pages as p>
		<#if p.title??>
		<div class="p_title">${p.title}</div>
		</#if>
		<#if p.sq??>
			<ul class="q_ul">
			<#assign sq=p.sq>
			<#list sq as q>
			<#if q??>
			<li>${surveyModel.answer(q)}</li>
			</#if>
			</#list>
			</ul>
		</#if>
	</#list>
</#if>
<div class="form-group">
	<div class="col-sm-10 col-md-offset-2">
		<button type="submit" class="btn btn-danger">${model.submitName!'完成问卷'}</button>
	</div>
</div>
</form>

<link href="${basePath }myfiles/Plugin/jQuery-Validation-Engine/css/validationEngine.jquery.css" media="screen" rel="stylesheet" type="text/css">

<script type="text/javascript" src="${basePath }myfiles/js/jquery.from.js"></script>
<script type="text/javascript" src="${basePath }myfiles/Plugin/jQuery-Validation-Engine/js/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${basePath }myfiles/Plugin/jQuery-Validation-Engine/js/languages/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${basePath }myfiles/js/jquery.sniper.answer.js"></script>
<script type="text/javascript">

var loadi;
$().ready(function(){
	$().sniperanswer();
	
	var options = { 
        //target:        '#output',   // target element(s) to be updated with server response 
        beforeSubmit:  showRequest,  // pre-submit callback 
        success:       showResponse,  // post-submit callback 
        // other available options: 
        //url:       url         // override for form's 'action' attribute 
        type:      'post',       // 'get' or 'post', override for form's 'method' attribute 
        dataType:  'json',        // 'xml', 'script', or 'json' (expected server response type)       
        clearForm: false,        // clear all form fields after successful submit       
        //resetForm: true        // reset the form after successful submit 
        timeout:   5000 
    }; 
    
    //验证程序
	/*$("#mcform").validationEngine('attach',{
		 promptPosition: "topLeft"
	});*/
	
    $('#mcform').submit(function() {
       	// $(this).ajaxSubmit(options); 
        // return false; 
    }); 
    
})


// pre-submit callback 
function showRequest(formData, jqForm, options) { 
	
	//if($("#mcform").validationEngine('validate')){
		if(confirm("你只有一次的机会,确定?")){
			loadi = layer.load('稍等...');
			return true;
		}
	//}
	//check.preventDefault();//此处阻止提交表单  ,此处无效
    return false;
} 
// post-submit callback 
function showResponse(responseText, statusText, xhr, $form)  
{ 
	//关闭所有窗口
	layer.close(loadi);
	if(statusText=='success'){
		for(var i in responseText){
			alert(responseText[i]);
		}
		
		$(document).scrollTop(0);
		setTimeout("window.location.reload()",1000);
		
	}else{
		layer.msg('Network blocked', 2, -1);
	}
		
} 


</script>
