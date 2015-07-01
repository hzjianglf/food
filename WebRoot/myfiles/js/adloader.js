/**
 * adLoader
 * iwebshop的广告加载器
 *  
 * 先引入这个js，再load广告，1是广告位的id
 * <code>
 * 	<script language="javascript" src="/iwebshop/javascript/adloader/"></script>
 * 	<div id="adDiv">广告......</div>
 * 	<script language="javascript">(new adLoader()).load(1,"adDiv");</script>
 * </code>
 * @author walu
 */

function adLoader()
{
	var _self=this;
	var _id=0;
	var positionData={};
	var adData={};
	
	var urlPS=function(url)
	{
		url="/"+url+"/";
		return url.replace(/[\/\\]+/g,"/");
	}
	
	//获取广告位的信息，id广告位id
	this.getAdPositionInfo=function(id,async)
	{	
		var url="/album/public/getadList";
		var rand = Math.random();
		var args = {'key':id,'rand':rand};
		$.getJSON(url,args,function(c){
			if(c==false)
				return;
				
			
			adData=c;
			
			//设置position的高和宽
			var width,height;
			width	= parseInt(adData.ma_width);
			height	= parseInt(adData.ma_height);
			var element = $("#"+_self.elementID);
			element.css("overflow",'hidden');
			if(width>0){
				element.width(width);
			}
			if(height>0){
				element.height(height);
			}
			//稍作修改，传递广告位新id
			//_self.getAdList(positionData.ma_id);

			_self.next();
		});
	}
	
	//获取某个广告位的广告列表，id:广告位id
	/*this.getAdList=function(id,async)
	{
		var url="/public/get.medias/";
		var args = {'id':id,rand:Math.random()}
		$.getJSON(url,args,function(c)
		{
			if(c['flag']==false)
				return;
				
			adData=c['data'];
			_self.next();
		});
	}*/
	
	//加载广告
	//id:广告位id
	//async:异步加载机制，如果为true则直接输出，否则等页面加载完成后再输出
	this.load=function(id,elementID,async){
		_self.id=id;
		_self.elementID=elementID;
		_self.async=async || false;

		_self.getAdPositionInfo( id,async );
	}
	
	//广告位置div
	this.display=function(adDataKey)
	{
		_self.adDataKey=adDataKey;
		var ad	=adData[adDataKey];
		
		var width,height,code,title;
		title	= (ad.ad_name.length>0) ?" title='"+ad.ad_name+"' ":" ";
		
		width	= (ad.ad_width!=0) ?" width='"+ad.ad_width+"' ":" ";
		height	= (ad.ad_height!=0) ?" height='"+ad.ad_height+"' ":" ";
		switch( parseInt(ad['ad_type']) )
		{
			case 1:
				var code="<img "+width+height+title+" src='{URL}' />";
				code=code.replace(/{URL}/g,ad.ad_path);
				break;
			case 2:
				var code='<object classid="clsid:D27CDB6E-AE6D-11CF-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,40,0" '+width+height+' hspace="0" vspace="0" border="0" align="left" id="flashname"><param name="movie" value="{URL}"><param name="quality" value="high"><param name="wmode" value="transparent"><param name="scale" value="noborder"><embed src="{URL}" quality="high" wmode="transparent" scale="noborder" '+width+height+' hspace="0" vspace="0" border="0" align="left" name="flashname" type="application/x-shockwave-flash" luginspage="http://www.macromedia.com/go/getflashplayer"></embed></object>';
				code=code.replace(/{URL}/g,ad.ad_path);
				break;
			case 3:
				var code=ad.ad_path;
				break;
			case 4:
				var code=ad.ad_path;
				break;
			default:
				break;
		}
		//设置div宽度
		//$('#'+_self.elementID).css('cursor','pointer');
		//跳转事宜
		if(ad.ad_url.length>2){
			$('#'+_self.elementID).css('cursor','pointer');
			$('#'+_self.elementID).click(function(){window.open(ad.ad_url, "_blank");});
		}
		
		//异步与同步加载
		if(_self.async==true){
			$(document).ready(function(){
				$('#'+_self.elementID).html(code);
				if(adData.length>1)
					setTimeout(function(){_self.next();},5000);         
			});
		}else{
			//alert($('#'+_self.elementID).html());
			$('#'+_self.elementID).html(code);		
			if(adData.length>1)
				setTimeout(function(){_self.next();},5000);
		}
	}
	
	this.next=function()
	{
		var adDataKey=_self.adDataKey || 0;
		var len=adData.length;
		
		if(positionData.fashion==1){
			newKey=(adDataKey>=(len-1))?0:(adDataKey+1);
		}else{
			rand = parseInt(Math.random()*1000);
			newKey = rand%len;
		}
		_self.display(newKey);
	}
}
