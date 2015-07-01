<div id="layer-photos" class="layer-photos-demo">
  <img layer-src="http://static.oschina.net/uploads/space/2014/0516/012728_nAh8_1168184.jpg" layer-pid="" src="http://static.oschina.net/uploads/space/2014/0516/012728_nAh8_1168184.jpg" alt="layer宣传图">
  <img layer-src="http://sentsin.qiniudn.com/sentsinmy5.jpg" layer-pid="" src="http://sentsin.qiniudn.com/sentsinmy5.jpg" alt="我入互联网这五年">
  <img layer-src="" layer-pid="" src="http://sentsin.qiniudn.com/sentsin_39101a660cf4671b7ec297a74cc652c74152104f.jpg" alt="微摄影">
  <img layer-src="http://sentsin.qiniudn.com/sentsinsan01.jpg" layer-pid="" src="http://sentsin.qiniudn.com/sentsinsan01.jpg" alt="三清山">
  <img layer-src="http://ww2.sinaimg.cn/mw1024/5db11ff4jw1ehcyirr6quj20q00ex42w.jpg" layer-pid="" src="http://ww2.sinaimg.cn/mw1024/5db11ff4jw1ehcyirr6quj20q00ex42w.jpg" alt="国足">
  <img layer-src="http://sentsin.qiniudn.com/sentsin_7dee4a4134214dd2d8ff390fbeb11333.jpg" layer-pid="" src="http://sentsin.qiniudn.com/sentsin_7dee4a4134214dd2d8ff390fbeb11333.jpg" alt="劲草">
</div>


<script src="myfiles/Plugin/layer-v1.9.3/layer/layer.js"></script>
<script>
;!function(){

//加载扩展模块
layer.config({
    extend: 'extend/layer.ext.js'
});

//页面一打开就执行，放入ready是为了layer所需配件（css、扩展模块）加载完毕
layer.ready(function(){ 
    
    //使用相册
    layer.photos({
        photos: '#layer-photos'
    });
});


}();
</script>