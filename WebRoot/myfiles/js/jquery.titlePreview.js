
this.titlePreview = function(name) {
	xOffset = 10;
	yOffset = 30;
	$("." + name).hover(
			function(e) {
				this.t = this.title;
				this.title = "";
				var c = (this.t != "") ? "<br/>" + this.t : "";
				
				$("body").append("<p id='" + name + "'><img src='" + $(this).attr('rel') + "' alt='url preview' />" + c + "</p>");
				$("#" + name)
				.css({
					position : "absolute",
					border : "1px solid #ccc",
					background : "#333",
					padding : "5px",
					display : "none",
					color : "#fff"
				})
				.css("top", (e.pageY - xOffset) + "px")
				.css("left",(e.pageX + yOffset) + "px")
				.fadeIn("fast");
			}, function() {
				this.title = this.t;
				$("#" + name).remove();
			});
	$("." + name).mousemove(
			function(e) {		
				$("#" + name)
				.css("top", (e.pageY - xOffset) + "px")
				.css("left", (e.pageX + yOffset) + "px");
			}
		);
};
// starting the script on page load
$(document).ready(function() {
	titlePreview('screenshot');
});