/**
 * 不断的获取用户聊天信息
 */
function getNotUploadUser() {
	setTimeout("getNotUploadUser()", 100000);
	// 获取信息
	var timenow = new Date().getTime();
	$.get('admin/admin-notice/checkNotUploadProject?time=' + timenow, function(
			data, textStatus) {
		var html = "";
		var j = 0;
		for ( var i in data) {
			html += "<li><a href=\"admin/admin-user/update?id=" + i + "\">"
					+ data[i] + "</li>";
			j++;
		}
		$("#notUploadUsersNum").html(j);
		$("#notUploadUsers").html(html);

	}, 'json');
}
getNotUploadUser();
