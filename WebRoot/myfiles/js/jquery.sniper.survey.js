/**
 * 问卷动态添加
 */
(function ($) {
   $.fn.extend({
        "snipersurvey": function (options) {
            //设置默认值
            options = $.extend({
            	baseurl		: sniperConfig['baseAdminPath'],
            	id			: '#sniper_menu',
            	div			: '.survey',
            	page		: '.page',
            	question	: '.question',
            	option		: '.option',
            }, options);
            
            var titledialog  = dialog();
            
            function bind(obj){
            	
            	$(obj).delegate( "button","click", function () {
            		
            		//获取类型
            		var type = $(this).attr('data-type');
            		//获取所有数据
            		typename = type.split('_');
            		
            		var sendData = {};
            		
            		if($(this).attr('data-name') != null){
            			sendData[$(this).attr('data-name')] = $(this).attr('data-id')
            		}
            		
            		//组装要传递的数据
            		$(this).parents("." + typename[0]).find("input[name^='"+typename[0]+"']").each(function(i, obj) {
						
						if($(this).attr("type") == "checkbox"){
							if($(this).prop("checked") == true){
								sendData[$(obj).attr('name')] = true;
							}else{
								sendData[$(obj).attr('name')] = false;
							}
						}else{
							sendData[$(obj).attr('name')] = $(obj).val();
						}
					});
            		
            		
            		//组装要传递的数据
            		$(this).parents("." + typename[0]).find("textarea[name^='"+typename[0]+"']").each(function(i, obj) {
						sendData[$(obj).attr('name')] = $(obj).val();
					});
            		
            		sendData['type'] = type;
            		
            		switch(typename[1]){
            			case "up":
            			case "down":
            				saveorupdate(null, sendData);
            				break;
            			case 'plus':
            				addSurvey(sendData);
            				break;
            			case 'minus':
            				if(confirm('你确定要删除么?')){
            					saveorupdate(null, sendData);	
            					return ;
            				}
            				break;
            			case 'copy':
            			case 'check':
            				saveorupdate(null, sendData);
            				break;
            		}
            	
            		//end
            	});
            	
            }
            //添加新问题
            function addSurvey(result)
            {
            	var inputText	= '<textarea name="newinsert" style="width:500px;" class="form-control" placeholder="" id="newinsert" rows="6"></textarea>';
            	var d = dialog({
            	    title: "添加(一个换行表示一项)",
            	    content: inputText,
            	    quickClose: false, // 点击空白处快速关闭
            	    ok: function () {
            	    	input = $('#newinsert').val();	    	
            	    	if(input!='')	saveorupdate(input,result);
            	    	
            	    },
            	    cancel: function () {}
            	});
            	
            	d.show();
            		
            }
            /**
             * 数据发送和返回
             */
            function saveorupdate(input, result)
            {
            	//2个固定传输数据
            	var url = $('.survey').attr('data-url');
            	if(input){
            		result['input']	= input;
            	}
            	
            	titledialog.content('数据操作中');
            	titledialog.show();
            	
            	$.post(url, result , 
            			function (data, textStatus){
            				if(data.code == 200){
            					//操作			
            					switch(data.type){
            						case 'plus':
            							plusSurvey(data);
            							break;
            						case 'minus':							
            							minusSurvey(data);	
            							break;
            						case 'copy':
            							copySurvey(data);
            							break;
            						case 'up':
            						case 'down':
            							moveSurvey(data);
            							break;
            						case 'check':
            							titledialog.close();
            							break;
            					}				
            				}else{
            					if(data.error){
                					var ed = dialog({ content: data.error});
                					ed.show();
                				}
            				}
            				
            		},'json');
            }
            /**
             * 添加问卷  
             */
            function plusSurvey(data)
            {
            	switch(data.name){
            		case 'page':
						for(var i in data.page){
							if(!data.page.hasOwnProperty(i)){ 
								continue; 
							} 
							var temp = {};
							temp.page = data.page[i];
							$('#tempdata').setTemplate($('#pageAddHtml').html()).processTemplate(temp);
	            			html = $('#tempdata').html();
	            			$('.survey > .panel-footer').before(html);
						}
            			 
            			break;
            		case 'question':
						for(var i in data.question){
							if(!data.question.hasOwnProperty(i)){ 
								continue; 
							} 
							var temp = {};
							temp.question = data.question[i];
							$('#tempdata').setTemplate($('#qustionAddHtml').html()).processTemplate(temp);
	            			html = $('#tempdata').html();
	            			$('#page_n_'+data.question[i].page.id + ' > ul').append(html);
						}
            			
            			break;
            		case 'option':
						for(var i in data.option){
							if(!data.option.hasOwnProperty(i)){ 
								continue; 
							} 
							var temp = {};
							temp.option = data.option[i];
							$('#tempdata').setTemplate($('#optionAddHtml').html()).processTemplate(temp);
	            			html = $('#tempdata').html();
	            			$('#question_n_' + data.option[i].question.id + ' > div > ul').append(html);
						}
            			
            			break;
            	}
            	titledialog.close();
            	return ;
            }
            /**
             * 问卷系列复制
             */
            function copySurvey(data)
            {
            	switch(data.name){
            		case 'page':
            			
						temp = {};
						temp.page = data.page[1];
						$('#tempdata').setTemplate($('#pageAddHtml').html()).processTemplate(temp);
            			html = $('#tempdata').html();
            			$('#page_' + data.page[0].id).append(html);
            			
            			questions = data.page[1].sq;
            			if(questions.length > 0){
            				for(var i in questions){
    							temp1 = {};
    							temp1.question = questions[i];
    							$('#tempdata').setTemplate($('#qustionAddHtml').html()).processTemplate(temp1);
    	            			html = $('#tempdata').html();
    	            			$('#page_n_' + data.page[1].id + ' > ul').append(html);
    	            			
    	            			options = questions[i].options;
    	            			if(options.length > 0){
    	            				for(var j in options){
    	            					
    	    							temp2 = {};
    	    							temp2.option = options[j];
    	    							$('#tempdata').setTemplate($('#optionAddHtml').html()).processTemplate(temp2);
    	    	            			html = $('#tempdata').html();
    	    	            			$('#question_n_' + questions[i].id + ' > div > ul').append(html); 
    	    						}
    	            			}
    						}
            			}
            			 
            			break;
            		case 'question':
            			temp = {};
						temp.question = data.question[1];
						$('#tempdata').setTemplate($('#qustionAddHtml').html()).processTemplate(temp);
            			html = $('#tempdata').html();
            			$('#page_n_' + data.question[0].page.id + ' > ul').append(html);
            			
            			options = data.question[1].options;
            			if(options.length > 0){
            				
            				for(var i in options){
    							temp1 = {};
    							temp1.option = options[i];
    							$('#tempdata').setTemplate($('#optionAddHtml').html()).processTemplate(temp1);
    	            			html = $('#tempdata').html();
    	            			$('#question_n_' + data.question[1].id + ' > div > ul').append(html); 
    						}
            			}
            			
            			break;
            		case 'option':
						temp = {};
						temp.option = data.option[1];
						$('#tempdata').setTemplate($('#optionAddHtml').html()).processTemplate(temp);
            			html = $('#tempdata').html();
            			$(html).insertAfter($('#option_' + data.option[0].id));
            			
            			break;
            	}
            	titledialog.close();
            	return ;
            }
            /**
             * 问卷树形移动
             */
            function moveSurvey(data)
            {
            	
            	switch(data.name){
            		case 'page':
            			if(data.page.length != 2){
                    		return ;
                    	}
            			switch (data.type) {
        				case "up":
        					$('#page_' + data.page[1].id).insertBefore($('#page_' + data.page[0].id));
        					break;
        				case "down":
        					$('#page_' + data.page[0].id).insertBefore($('#page_' + data.page[1].id));
        					break;
        				default:
        					break;
        				}
            			break;
            		case 'question':
            			if(data.question.length != 2){
                    		return ;
                    	}
            			switch (data.type) {
        				case "up":
        					$('#question_' + data.question[1].id).insertBefore($('#question_' + data.question[0].id));
        					break;
        				case "down":
        					$('#question_' + data.question[0].id).insertBefore($('#question_' + data.question[1].id));
        					break;
        				default:
        					break;
        				}
            			
            			break;
            		case 'option':
            			if(data.option.length != 2){
                    		return ;
                    	}
            			switch (data.type) {
        				case "up":
        					$('#option_' + data.option[1].id).insertBefore($('#option_' + data.option[0].id));
        					break;
        				case "down":
        					$('#option_' + data.option[0].id).insertBefore($('#option_' + data.option[1].id));
        					break;
        				default:
        					break;
        				}
            			
            			break;
            	}
            	titledialog.close();
            	return ;
            }
            
            
            /**
             * 删除问卷
             */
            function minusSurvey(data)
            {
            	var div;	
            	switch(data.name){
            		case 'page':
            			div = data.name + '_' + data.page[0].id;
            			break;
            		case 'question':
            			div = data.name + '_' + data.question[0].id;
            			break;
            		case 'option':
            			div = data.name + '_' + data.option[0].id;
            			console.log(div);
            			break;
            	}
            	
            	$('#' + div).remove();
            	titledialog.close();
            	return ;
            }


            $(document).ready(function(){

            	var startindex;
            	var stopindex;
            	
            	$('ul.survey').nestedSortable({
            		
            		forcePlaceholderSize: true,
            		handle: ' > div',
            		helper:	'clone',
            		items: 'li.question',
            		opacity: .6,
            		placeholder: 'placeholder',
            		revert: 250,
            		tabSize: 25,
            		tolerance: 'pointer',
            		toleranceElement: '> div',
            		maxLevels: 2,
            		protectRoot : true,
            		isTree: true,
            		expandOnHover: 700,
            		startCollapsed: true,
            		listType: 'ul',
            		start: function( event, ui ) {
            			startindex = $(ui.item[0]).index();		
            		},
            		
            		stop: function( event, ui ) {
            			var result = $('ul.survey').nestedSortable('toArray', {startDepthCount: 0});
            			//console.log( "stop" );
            			type = $(ui.item[0]).attr('class');
            			id = $(ui.item[0]).attr('id');
            			stopindex	= $(ui.item[0]).index();
            		/* 	console.log(ui);		
            			console.log( 'startindex: ' + startindex );
            			console.log( 'stopindex: ' + stopindex );
            			console.log( 'type: ' + type );
            			console.log( 'id: ' + id ); */
            			console.log(startindex);
            			console.log(stopindex);
            			console.log(id);
            			console.log(type);
            			moveSurveySort(startindex, stopindex, id, type);
            			//console.log( ui );
            		}
            	});
            });
            /**
             * 移动问卷
             */
            function moveSurveySort(startindex, stopindex, id, type){

            	var parent = $('#' + id).parent();
            	startindex = parseInt(startindex);
            	stopindex = parseInt(stopindex);
            	
            	if(type == 'page'){
            		if(startindex > stopindex){			
            			stoporder = $('.survey > li').eq(stopindex + 1).attr('data-order');
            			stoporder = parseFloat(stoporder);
            			
            			$('.survey > li').eq(stopindex).attr('data-order', stoporder - 0.01);
            		}
            		if(startindex < stopindex){
            			stoporder = $('.survey > li').eq(stopindex - 1).attr('data-order');
            			stoporder = parseFloat(stoporder);
            			$('.survey > li').eq(stopindex).attr('data-order', stoporder + 0.01);
            		}
            	}	
            }

            bind(options.div);
            
        }            
    });
})(jQuery);
