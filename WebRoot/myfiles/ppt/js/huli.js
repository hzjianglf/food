// JavaScript Document
	$(document).ready(function(){
		var windows_height=$(window).height();
		var windows_width=$(window).width();
		function nav_height(){
			$(".nav").height(windows_height+17);
			$(".subnav").height(windows_height+17);
		}
		nav_height();
		$(".subnav").hide();
		$(".nav ul li").hover(function(){
			$(this).children(".subnav").show();
		  },function(){
			$(this).children(".subnav").hide();
		  }
		);
		$(".main").width(windows_width-100);
		$(window).resize(function(){
			nav_height();
			$(".main").width($(window).width()-100);
		});
		if($(window).width()<1300){
			$(".arc_attribute").width(500);
		}
		$(".list").css("marginLeft",($(".main").width()-$(".list").width())/2)
		$(".list li").each(function(i){
			var maxWidth = $(this).width();
			var maxheight = $(this).height()-50;
			var width = $(this).children("img").width();
			var height = $(this).children("img").height();
			if(width > height){
				$(this).find("img").css("width",maxWidth);
				$(this).find("img").css("height",maxWidth/width*height);
			}else{
				$(this).find("img").css("height",maxheight);
				$(this).find("img").css("width",maxheight/height*width);
			}
		});
	});