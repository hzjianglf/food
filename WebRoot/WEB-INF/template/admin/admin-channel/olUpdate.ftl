<link rel="stylesheet" href="myfiles/Plugin/zTree_v3/css/metroStyle/metroSniperStyle.css" type="text/css">
<script type="text/javascript" src="myfiles/Plugin/zTree_v3/js/jquery.ztree.exedit-3.5.js"></script>
    <SCRIPT type="text/javascript">
        <!--
        var setting = {
            view: {
                addHoverDom: addHoverDom,
                removeHoverDom: removeHoverDom,
                selectedMulti: false
            },
            check: {
                enable: true
            },
            edit: {
				enable: true,
				editNameSelectAll: true,
				showRemoveBtn: showRemoveBtn,
				showRenameBtn: showRenameBtn
			},
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
				beforeDrag: beforeDrag,
				beforeEditName: beforeEditName,
				beforeRemove: beforeRemove,
				beforeRename: beforeRename,
				onRemove: onRemove,
				onRename: onRename,
				onClick: onClick
			}
        };

        var zNodes =[
           ${treeMap}
        ];
      
      	//删除
		function onClick(e, treeId, treeNode) {
			console.log(treeNode.url);
			if(treeNode.url != ''){
				//if(treeNode.target == '')
				//window.location.href='main.html'
				window.open(treeNode.url);
			}			
		}
		
        var log, className = "dark";
		function beforeDrag(treeId, treeNodes) {
			return false;
		}
		
		function beforeEditName(treeId, treeNode) {
			className = (className === "dark" ? "":"dark");
			//showLog("[ "+getTime()+" beforeEditName ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			zTree.selectNode(treeNode);
			//return confirm("进入节点 -- " + treeNode.name + " 的编辑状态吗？");
			return true;
		}
		function beforeRemove(treeId, treeNode) {
			className = (className === "dark" ? "":"dark");
			//showLog("[ "+getTime()+" beforeRemove ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			zTree.selectNode(treeNode);
			return confirm("确认删除 频道 -- " + treeNode.name + " 吗？");
		}
		
		//删除
		function onRemove(e, treeId, treeNode) {
			
			showLog("[ "+getTime()+" 删除频道 ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
			var data = {};
			data['id'] = treeNode.id;
			$.post("admin/admin-channel/olDelete?${_csrf.parameterName}=${_csrf.token}",data,function(text, status){
				
				if(text.code != 200){
					var titledialog  = dialog();
					titledialog.content(data.msg);
            		titledialog.show();
				}
			});
		}
		
		function beforeRename(treeId, treeNode, newName, isCancel) {
			className = (className === "dark" ? "":"dark");
			//showLog((isCancel ? "<span style='color:red'>":"") + "[ "+getTime()+" beforeRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name + (isCancel ? "</span>":""));
						
			if (newName.length == 0) {
				alert("频道名称不能为空.");
				var zTree = $.fn.zTree.getZTreeObj("treeDemo");
				setTimeout(function(){zTree.editName(treeNode)}, 10);
				return false;
			}
			return true;
		}
		
		
		// 重命名
		function onRename(e, treeId, treeNode) {
			var data = {};
			data['id'] = treeNode.id;
			data['name'] = treeNode.name;
			$.post("admin/admin-channel/olUpdate?${_csrf.parameterName}=${_csrf.token}",data,function(text, status){
				if(text.code != 200){
					var titledialog  = dialog();
					titledialog.content(data.msg);
            		titledialog.show();
				}
			});
			
			showLog("[ "+getTime()+" 重命名 ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
		}
		function showRemoveBtn(treeId, treeNode) {
			return !treeNode.isParent;
			//return !treeNode.isFirstNode;
		}
		function showRenameBtn(treeId, treeNode) {
			//return !treeNode.isLastNode;
			return true;
		}
		function showLog(str) {
			if (!log) log = $("#log");
			log.append("<li class='"+className+"'>"+str+"</li>");
			if(log.children("li").length > 8) {
				log.get(0).removeChild(log.children("li")[0]);
			}
		}
		function getTime() {
			var now= new Date(),
			h=now.getHours(),
			m=now.getMinutes(),
			s=now.getSeconds(),
			ms=now.getMilliseconds();
			return (h+":"+m+":"+s+ " " +ms);
		}

		var newCount = 1;
		//添加
		function addHoverDom(treeId, treeNode) {
			var sObj = $("#" + treeNode.tId + "_span");
			if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
			//var addStr = "<span class='button add' id='addBtn_" + treeNode.tId + "' title='add node' onfocus='this.blur();'></span>";
			
			var addStr = "<span class='button add' id='addBtn_" + treeNode.tId + "' title='add node'></span>";
				
			sObj.after(addStr);
			var btn = $("#addBtn_"+treeNode.tId);
			if (btn) btn.bind("click", function(){
				var zTree = $.fn.zTree.getZTreeObj("treeDemo");
				var inputText	= '<input type="text" id="newinsert" style="width:300px" class="form-control" name="name" >';
				var d = dialog({
            	    title: "添加频道",
            	    content: inputText,
            	    quickClose: false,
            	    ok: function () {
            	    	input = $('#newinsert').val();	    	
            	    	
            	    	if(input == ''){
            	    		return false;
            	    	}
            	    	var data = {};
						data['name'] = input;
						data['fid'] = treeNode.id;
						data['showType'] = treeNode.type;
						$.post("admin/admin-channel/olAdd?${_csrf.parameterName}=${_csrf.token}",data,function(text, status){
							if(text.code == 200){
								showLog("[ "+getTime()+" 添加频道 ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name + " ]" );
								zTree.addNodes(treeNode, {id:text.channel.id, pId:treeNode.id, name:text.channel.name});
							}
						});
            	    },
            	    cancel: function () {}
            	});
            	d.show();
            	
				return false;
			});
		};
		function removeHoverDom(treeId, treeNode) {
			$("#addBtn_"+treeNode.tId).unbind().remove();
		};
		function selectAll() {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			zTree.setting.edit.editNameSelectAll =  $("#selectAll").attr("checked");
		}
		
		$(document).ready(function(){
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
			$("#selectAll").bind("click", selectAll);
		});
        //-->
    </SCRIPT>
	  	<h3>TREE栏目管理</h3>
	 	<div class="zTreeDemoBackground left">
        	<ul id="treeDemo" class="ztreeSniper"></ul>
    	</div>
	    
    <ul id="log" class="log"></ul>
