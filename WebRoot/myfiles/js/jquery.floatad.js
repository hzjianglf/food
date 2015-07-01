(function($){
	$.fn.floatAd = function(options){
		var defaults = {
			imgSrc : "", //漂浮图片路径
			imgJumpToUrl : "", //图片点击跳转页
			openStyle : 1, //跳转页打开方式 1为新页面打开  0为当前页打开
			speed : 10,	//漂浮速度 单位毫秒
			position : "fixed"	//fixed可是区域|absolute。整网页
		};
		var options = $.extend(defaults,options);
		var _target = options.openStyle == 1 ?  "target='_blank'" : '' ;
		
		var html = "<div id='float_ad' style='position:" + options.position + ";left:0px;top:0px;z-index:1000000;cleat:both;'>";
            html += "  <a href='" + options.imgJumpToUrl + "' " + _target + "><img src='" + options.imgSrc + "' border='0' class='float_ad_img'/></a> <a href='javascript:;' id='close_float_ad' style=''>x</a>";
            html += "</div>";

        $('body').append(html);

		function init(){
			var x = 0,y = 0 
			var xin = true, yin = true 
			var step = 1 
			var delay = 10 
			var obj=$("#float_ad") 
			obj.find('img.float_ad_img').load(function(){
				var float = function(){
				    var L = T = 0;
					var OW = obj.width();//当前广告的宽
					var OH = obj.height();//高
					if(options.position=='absolute')
					{
						var DW = $(document).width(); //浏览器窗口的宽
						var DH = $(document).height(); 
					}else if(options.position=='fixed'){
						//可视页面
						var DH = $(window).height();
						var DW = $(window).width();
					}
					//var DST = $(document).scrollTop();//滚动条高度，没有用过到

 				    x = x + step *( xin ? 1 : -1 ); 
					if (x < L) { 
						xin = true; x = L
					} 
					if (x > DW-OW-1){//-1为了ie
						xin = false; x = DW-OW-1
					} 

					y = y + step * ( yin ? 1 : -1 ); 
					if (y > DH-OH-1) { 

						yin = false; y = DH-OH-1 ;
					}
					if (y < T) {
						yin = true; y = T
					} 

					var left = x ; 
					var top = y; 

					obj.css({'top':top,'left':left});
				}
				var itl = setInterval(float,options.speed);
				$('#float_ad').mouseover(function(){clearInterval(itl)}); 
				$('#float_ad').mouseout(function(){itl=setInterval(float, options.speed)} )
			});
			// 点击关闭
			$('#close_float_ad').live('click',function(){
			    $('#float_ad').hide();
			});
		}

	   init();

	}; //floatAd

})(jQuery);


/*
<script type="text/javascript" src="floatAd.js"></script>
<script type="text/javascript">
	$(function(){
		//调用漂浮插件
		$("body").floatAd({
			imgSrc : 'http://wenwen.soso.com/p/20100204/20100204193850-483274619.jpg',
			url:'http://yanue.net'
		});
	})
</script>*/