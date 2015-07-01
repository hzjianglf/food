function enablePngImages() 
{ var imgArr = document.getElementsByTagName("IMG"); 
 for(i=0; i<imgArr.length; i++){    
 if(imgArr[i].src.toLowerCase().lastIndexOf(".png") != -1)
 {     
   imgArr[i].style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + imgArr[i].src + "', sizingMethod='auto')";     
   imgArr[i].src = "spacer.gif";    
 }       
   if(imgArr[i].currentStyle.backgroundImage.lastIndexOf(".png") != -1)
   {     
	 var img = imgArr[i].currentStyle.backgroundImage.substring(5,imgArr[i].currentStyle.backgroundImage.length-2);
	 imgArr[i].style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+img+"', sizingMethod='crop')";     
	 imgArr[i].style.backgroundImage = "url(spacer.gif)";    
   } 
 } 
} 
function enableBgPngImages(bgElements)
{ 
for(i=0; i<bgElements.length; i++)
 {    
 if(bgElements[i].currentStyle.backgroundImage.lastIndexOf(".png") != -1)
 {     //alert(bgElements[i]);     
 var img = bgElements[i].currentStyle.backgroundImage.substring(5,bgElements[i].currentStyle.backgroundImage.length-2);     
 bgElements[i].style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+img+"', sizingMethod='crop')";     
 bgElements[i].style.backgroundImage = "url(spacer.gif)";    
} 
} 
}