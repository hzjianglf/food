/*
* jQuery.Resize by wuxinxi007
* Date: 2011-5-14
* blog : http://wuxinxi007.cnblogs.com/
*/
$(function ()
{
    // 绑定需要拖拽改变大小的元素对象
    bindResize($('#adminmenushadow'));
    //关闭左侧
    $('#pma_navigation_collapser').click(function(){
    	width	= $('#adminmenuwrap').width();
    	if(width>0){
    		$('#adminmenuwrap').width(0);
    		$('#adminmenuback').width(0);
    		$('.bodyRight').css('margin-left',0);
    		$(this).html('→');
    		$(this).attr('title','打开面板');
    	}else if(width==0){
    		$('#adminmenuwrap').width(219);
    		$('#adminmenuback').width(219);
    		$('.bodyRight').css('margin-left','220px');
    		$(this).html('←');
    		$(this).attr('title','关闭面板');
    		
    	}	
    });
});
/**
 * 
 * @param el
 * @param move
 */
function bindResize(el,move)
{
	//需要操作的div #adminmenuwrap .bodyRight
    // 初始化参数
	var adminmenuwrap	= $('#adminmenuwrap');
	var adminmenuback	= $('#adminmenuback');
	var bodyRight		= $('.bodyRight');
    var els = el.style,
    // 鼠标的 X 和 Y 轴坐标
    x = y = 0;
    // 邪恶的食指
    $(el).mousedown(function (e){
    	//alert(adminmenuwrap.width());
        // 按下元素后，计算当前鼠标与对象计算后的坐标
        x = e.clientX - adminmenuwrap.width(),
       // alert(x);
        //x2 = e.clientX - bodyRight.css('margin-left'),
        
        y = e.clientY - el.offsetHeight;
        // 在支持 setCapture 做些东东
        el.setCapture ? (
        // 捕捉焦点
            el.setCapture(),
        // 设置事件
            el.onmousemove = function (ev)
            {
                mouseMove(ev || event);
            },
            el.onmouseup = mouseUp
        ) : (
            // 绑定事件
            $(document).bind("mousemove", mouseMove).bind("mouseup", mouseUp)
        );
        // 防止默认事件发生
        e.preventDefault();
    });
    // 移动事件
    function mouseMove(e)
    {
    	
        // 宇宙超级无敌运算中...
        //els.width = e.clientX - x + 'px',
       // els.height = e.clientY - y + 'px';
    	//alert(e.clientX);
    	adminmenuwrap.width(e.clientX - x);
    	adminmenuback.width(e.clientX - x);
    	//adminmenuwrap.css('',e.clientX - x + 'px'),
    	bodyRight.css('margin-left',e.clientX - x + 'px');
    }
    // 停止事件
    function mouseUp()
    {
        // 在支持 releaseCapture 做些东东
        el.releaseCapture ? (
        // 释放焦点
            el.releaseCapture(),
        // 移除事件
            el.onmousemove = el.onmouseup = null
        ) : (
            // 卸载事件
            $(document).unbind("mousemove", mouseMove).unbind("mouseup", mouseUp)
        );
    }
}