/**
 * 用户列表也的所有操作
 */
jQuery.ajaxSettings.traditional = true;
(function($) {
	$.fn.extend({
		"SniperMain" : function(options) {
			// 设置默认值
			options = $.extend({
				zNodes : '',
				selectID:0,
				activity:0,
				defaultBindUpload : 0,
				defaultBindProject : 0
				
					
			}, options);

			var zTree_Menu = null;

			function getFont(treeId, node) {
				return node.font ? node.font : {};
			}
			var setting = {
				view : {
					showLine : false,
					showIcon : false,
					selectedMulti : false,
					addDiyDom : addDiyDom,
					fontCss : getFont,
					nameIsHTML : false,
					dblClickExpand : true

				},
				data : {
					simpleData : {
						enable : true
					},
					key : {
						title : "title"
					}
				},
				callback : {
					beforeClick : beforeClick
				}
			};


			function addDiyDom(treeId, treeNode) {
				var spaceWidth = 5;
				var switchObj = $("#" + treeNode.tId + "_switch"),
				icoObj = $("#" + treeNode.tId + "_ico");
				switchObj.remove();
				icoObj.before(switchObj);

				if (treeNode.level > 1) {
					var spaceStr = "<span style='display: inline-block;width:" + (spaceWidth * treeNode.level)+ "px'></span>";
					switchObj.before(spaceStr);
				}
			}

			function beforeClick(treeId, treeNode) {
				if (treeNode.level == 0 ) {
					var zTree = $.fn.zTree.getZTreeObj("ztree");
					zTree.expandNode(treeNode);
					return false;
				}
				return true;
			}
			
			function setTitle(zTree_Menu, node) {
				var nodes = node ? [node]:zTree_Menu.transformToArray(zTree.getNodes());
				// console.log(nodes);
				for (var i=0, l=nodes.length; i<l; i++) {
					var n = nodes[i];
					n.title = "[" + n.id + "] isFirstNode = " + n.isFirstNode + ", isLastNode = " + n.isLastNode;
					zTree_Menu.updateNode(n);
				}
			}
			function count() {
				var zTree = $.fn.zTree.getZTreeObj("ztree"),
				hiddenCount = zTree.getNodesByParam("isHidden", true).length;
				console.log(hiddenCount);
			}
			
			function showNodes(zTree_Menu, ids) {
				var hs = new Array();
				if(ids.length > 0 ){
					for(var i in ids){
						hs[i] = zTree_Menu.getNodeByParam("id", ids[i], null);
					}
				}
				zTree_Menu.showNodes(hs);
			}
			
			function hideNodes(zTree_Menu, ids) {
				var hs = new Array();
				if(ids.length > 0 ){
					for(var i in ids){
						hs[i] = zTree_Menu.getNodeByParam("id", ids[i], null);
					}
				}
				zTree_Menu.hideNodes(hs);
			}
			
			
			
			
			var init = function(){
				var treeObj = $("#ztree");
				$.fn.zTree.init(treeObj, setting, options.zNodes);
				zTree_Menu = $.fn.zTree.getZTreeObj("ztree");
				if(options.selectID > 0){
					var selected = zTree_Menu.getNodeByParam("id", options.selectID, null);
					zTree_Menu.selectNode(selected);
				}

				treeObj.hover(function () {
					if (!treeObj.hasClass("showIcon")) {
						treeObj.addClass("showIcon");
					}
				}, function() {
					treeObj.removeClass("showIcon");
				});
				
				
				
				
				
			}
			
			init();

			// end
		}
	});
})(jQuery);
