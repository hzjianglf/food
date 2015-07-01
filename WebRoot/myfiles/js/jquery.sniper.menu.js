/**
 * 用户列表也的所有操作
 */
jQuery.ajaxSettings.traditional = true;
(function ($) {
   $.fn.extend({
        "snipermenu": function (options) {
            //设置默认值
            options = $.extend({
            	baseurl		: sniperConfig['baseAdminPath'],
            	//滚动菜单 object
            	id			: '#sniper_menu',
            	//被操作区域的object
            	div			: '.table',
            	//没条记录的值
            	valuename	: 'list.id',
            	//记录区分前缀
            	prefix		: '#sl_',
            	delid		: 'delid',
            	//审核css
            	//audit		: 'success',
            	//为审css
            	//naudit		: 'warning',
                //数据处理url
                url			: '',
                //数据导出url
                exporturl	: ''
            }, options);
            
            var chk_value	= [];
            var menuValue	= '';
            var menuType	= '';
            
            //组装url
            var url			= options.baseurl + options.url;
            var exporturl	= options.baseurl + options.exporturl;
            //新叶打开
            var targettype	= ["exportword", "exportexecl", "print","download"];
           //区分记录状态
           
        	//$(options.div + " tbody>tr[data-status='1']").addClass(options.audit);
            //$(options.div + " tbody>tr[data-status!='1']").addClass(options.naudit);
            
            //获取checkbox的值
            var getcheckboxdata = function(){
            	chk_value	= [];
            	$(options.div).find('input[name="' + options.valuename + '"]:checked').each(function(){chk_value.push($(this).val());}); 
            }
            
            //文件导出喊出，方式是新页打开
            var exportword = function (chk_value)
            {
            	//?a[]=1&a[]=3
            	paramid	= chk_value.join('&delid=');
            	//alert(id);
            	//alert(exporturl+'?id[]='+paramid);
            	//一般会带着csrf
            	window.open(url+'&delid='+paramid+'&type='+menuType);
            }
           
            //数据发送，及获取
            var send = function()
            {
            	
                console.log(url);
            	
            	var d ;
            	getcheckboxdata();
            	if(chk_value.length==0) {
            		d = dialog({
            		    title: '提示',
            		    content: '没有选择记录！'
            		});
            		d.show();
            		return false; 
            	}
            	
            	if(!confirm('你确定继续？')){		
        			return false;
        		}    	
            	
            	//新页打开检测
            	if(targettype.toString().indexOf(menuValue) > -1) {
            		exportword(chk_value);
            		return false;
            	}
            	d = dialog({content: '加载中...'});
            	
            	var postData = {};
            	postData['menuValue'] =  menuValue;
            	postData['menuType'] =  menuType;
            	postData[options.delid] =  chk_value;
            	
            	$.post(url, postData, 
            			function (data, textStatus){ 
	            		//关闭所有窗口
		    			d.close();
		        		//信息反馈
			    		if(textStatus=='success'){
			    			d = dialog({  title: '提示', content: data.msg});
			    		}
			    		else{
			    			d = dialog({  title: '提示', content: '网络链接不通'});
			    		}
			    		d.show();
			    		//d.close().remove();
        	    		if(data.code > 0){
        	    			
        	    			switch(menuType){    				
    	    					case 'delete':
	    	    					for(var i in chk_value){
	    	    						
	        	    					if($(options.prefix + chk_value[i]).length!=0){
	        	    						$(options.prefix + chk_value[i]).remove();
	        	    					}
	        	       				}
	    	    					//window.location.reload();
	    	    					break;
    	    					default:
    	    						
    	    						//更改值
    	    						for(var j in chk_value){
	        	    					if($(options.prefix + chk_value[j]).length!=0){
	        	    						
	        	    						$(options.prefix + chk_value[j])
	        	    							.find("td[data-type='"+menuType+"']")
	        	    							.html(menuValue);
	        	    					}
	        	       				}
    	    						
    	    						
    	    						if(data.html!=false){
    	    							for(var m in data.html){
    	    								if($(options.prefix + m).length!=0){
                	    						$(options.prefix + m).replaceWith(data.html[m]);
                	    					}
    	    							}  	    							
    	        	    			}  		
        	    			}       	    			      	    			
        			  	}         	    		     
        	    	}, "json");   	
            };
            
            var click = function(obj){
            	menuValue	= $(obj).attr('data-value'); 
        		menuType	= $(obj).attr('data-type');
        		menuUrl 	= $(obj).attr('data-url');
        		if(menuUrl){
        			url = menuUrl;
        		}
        		
            	
        		//执行选择操作
        		if(menuType == 'select' ){
        			
        			switch(menuValue){
        				case '1':
        					$(options.div + ' :checkbox').prop("checked",true);
        					break;
        				case '2':
        					$(options.div + ' :checkbox').prop("checked",false);
        					break;
        				case '3':
        					$(options.div+' :checkbox').each(
        						function (){$(this).prop("checked", !$(this).prop("checked"));}
        					); 
        					break;
        			}
        			
        		}else{
        			send();
        		}
        		
            }
            
            
            //ul li ul li span点击操作
           var run =  function(){
            	//sniper_menu
            	$(options.id + ' a').click(function(event){
            		click(this);
        		});
            	
            	$(options.id + ' button[data-click="on"]').click(function(event){
            		click(this);
        		});
            	
            	
            }	
           
            run();
            //end
        }            
    });
})(jQuery);
