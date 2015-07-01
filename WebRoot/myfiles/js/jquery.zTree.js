/**
 * 
 */

(function($){
	$.fn.extend({
        "zTreeMenu": function (options) {
            // 设置默认值
            options = $.extend({
            	//菜单数组
            	zNodesRight : [],
            	//tree主div
            	treeMainID	: 'menuContent',
            	//tree 存放ul的id rightMap
            	treeBodyID : 'rightMap',
            	//用户出发tree的id
            	clickID		: '#fidName',
            	// 赋值方式
            	valueType	: 'id',
            	//存放name值的id
            	nameValueId		: '#fidName',
            	//存放id值的id
            	idValueId		: '#fid',
            	//这里存放的值就是之前idValueId存放的值
            	valueId			: 0
            }, options);
            
            var settingRight = {
            		view: {
            			showLine: true,
            			showIcon: true,
            			dblClickExpand: false
            		},
            		data: {
            			simpleData: {
            				enable: true
            			}
            		},
            		callback: {
            			// beforeClick: beforeClickRight,
            			onClick: onClickRight
            		}
            };
            
            $(options.clickID).bind("click",function(){
            	showMenu();
            })
            
            function beforeClickRight(treeId, treeNode) {
        		
        		var check = (treeNode && !treeNode.isParent);
        		if (!check) alert("只能选择城市...");
        		return check;
        	}
            
            function setNameByID(id){
            	
            	var zTree = $.fn.zTree.getZTreeObj(options.treeBodyID);
            	var selected = zTree.getNodeByParam("id", id, null);
            	if(selected){
            		switch(options.valueType){
            		case "id":
            			$(options.idValueId).val(id);
            			break;
            		case "name":
            			$(options.nameValueId).val(selected.name);
            			break;
            		case "all":
            			$(options.idValueId).val(id);
            			$(options.nameValueId).val(selected.name);
            			break;
            		}
            	}
        		return true;
            }
        	
        	function onClickRight(e, treeId, treeNode) {
        		var zTree = $.fn.zTree.getZTreeObj(options.treeBodyID),
        		nodes = zTree.getSelectedNodes(),
        		v = "";
        		n = "";
        		nodes.sort(function compare(a,b){return a.id-b.id;});
        		for (var i=0, l=nodes.length; i<l; i++) {
        			// 可以多选
        			// v += nodes[i].id + ",";
        			// 单选
        			v = nodes[i].id ;
        			n = nodes[i].name ;
        		}
        		// 多选字符串截取
        		// if (v.length > 0 ) v = v.substring(0, v.length-1);
        		switch(options.valueType){
        		case "id":
        			$(options.idValueId).val(v);
        			break;
        		case "name":
        			$(options.nameValueId).val(n);
        			break;
        		case "all":
        			$(options.idValueId).val(v);
        			$(options.nameValueId).val(n);
        			break;
        		}
        		return true;
        	}
        	
        	function showMenu() {
        		var cityObj = $(options.clickID);
        		var cityOffset = $(options.clickID).offset();
        		$("#" + options.treeMainID).slideDown("fast");
        		$("body").bind("mousedown", onBodyDown);
        	}
        	function hideMenu() {
        		$("#" + options.treeMainID).fadeOut("fast");
        		$("body").unbind("mousedown", onBodyDown);
        	}
        	
        	function onBodyDown(event) {
        		if (!(event.target.id == options.treeMainID || $(event.target).parents("#" + options.treeMainID).length>0)) 
        		{
        			hideMenu();
        		}
        	}

        	function run(){
        		// console.log(options.zNodesRight);
        		// console.log(options.treeMainID);
        		// console.log(options.treeBodyID);
        		// console.log(options.clickID);
        		// console.log(options.valueType);
        		
        		$.fn.zTree.init($("#" + options.treeBodyID), settingRight, options.zNodesRight);
        		if(options.valueId > 0){
        			setNameByID(options.valueId);
        		}
        	}
	
			run();
        	
        }
	})
})(jQuery);