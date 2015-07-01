/**
 * js基本配置文件
 */
var sniperConfig = new Array();
sniperConfig['webUrl'] = '/';
sniperConfig['baseAdminPath'] = 'admin';
sniperConfig['baseAdminConfirm'] = '确定这么做？';
sniperConfig['baseAdminMessage'] = '<img src="/myfiles/images/admin/loading.gif"/>';

function isExitsFunction(funcName) {
	try {
		if (typeof (eval(funcName)) == "function") {
			return true;
			sni
		}
	} catch (e) {
	}
	return false;
}
// 是否存在指定变量
function isExitsVariable(variableName) {
	try {
		if (typeof (variableName) == "undefined") {
			// alert("value is undefined");
			return false;
		} else {
			// alert("value is true");
			return true;
		}
	} catch (e) {
	}
	return false;
}
function loading() {
	var h = $(document).height();
	$(".overlay").css({
		"height" : h
	});

	$(".action").click(function() {

		$(".overlay").css({
			'display' : 'block',
			'opacity' : '0.8'
		});

		$(".showbox").stop(true).animate({
			'margin-top' : '300px',
			'opacity' : '1'
		}, 200);

		setTimeout(function() {

			$(".showbox").stop(true).animate({
				'margin-top' : '250px',
				'opacity' : '0'
			}, 400);

			$(".overlay").css({
				'display' : 'none',
				'opacity' : '0'
			});

		}, 800);

	});

}

function addCookie(objName, objValue, objHours) {// 添加cookie
	var str = objName + "=" + escape(objValue);
	if (objHours > 0) {// 为0时不设定过期时间，浏览器关闭时cookie自动消失
		var date = new Date();
		var ms = objHours * 3600 * 1000;
		date.setTime(date.getTime() + ms);
		str += "; expires=" + date.toGMTString();
	}
	document.cookie = str;
}

function getCookie(objName) {// 获取指定名称的cookie的值
	var arrStr = document.cookie.split("; ");
	for (var i = 0; i < arrStr.length; i++) {
		var temp = arrStr[i].split("=");
		if (temp[0] == objName)
			return unescape(temp[1]);
	}
}

function delCookie(name) {// 为了删除指定名称的cookie，可以将其过期时间设定为一个过去的时间
	var date = new Date();
	date.setTime(date.getTime() - 10000);
	document.cookie = name + "=a; expires=" + date.toGMTString();
}

function allCookie() {// 读取所有保存的cookie字符串
	var str = document.cookie;
	if (str == "") {
		str = "没有保存任何cookie";
	}
	alert(str);
}

