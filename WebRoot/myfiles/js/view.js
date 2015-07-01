lastScrollY = 0;
function heartBeat() {
	var diffY;
	if (document.documentElement && document.documentElement.scrollTop)
		diffY = document.documentElement.scrollTop;
	else if (document.body)
		diffY = document.body.scrollTop
	else {
		/* Netscape stuff */
	}
	// alert(diffY);
	percent =.1 * (diffY - lastScrollY);
	if (percent > 0)
		percent = Math.ceil(percent);
	else
		percent = Math.floor(percent);

	document.getElementById("fudong").style.top = parseInt(document.getElementById("fudong").style.top)+ percent + "px";
	lastScrollY = lastScrollY + percent;
	
}
// 下面这段删除后，对联将不跟随屏幕而移动。
window.setInterval("heartBeat()", 1000);
// -->
// 关闭按钮
function close() {
	fudong.style.visibility = 'hidden';
}
