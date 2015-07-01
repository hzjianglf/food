/**
 * .class文字家红
 */
(function ($) {
   $.fn.extend({
        "setHeightKeyWord": function (options) {
            //设置默认值
            options = $.extend({
            	classname	: '.shkw',
            	keyword		: '',
            	color		: 'Red',
            	bold		: true
            }, options);
            
           
            function setHeightKeyWords() {
            	if (options.keyword == "")	return;
            	var idlength	= $(options.classname).length;
            	
            	for(i=0;i<idlength;i++){
            		setHeightKeyWord($(options.classname).eq(i));
            	}
            }
            //个体
            function setHeightKeyWord(id)
            {
            	var tempHTML 	= id.html();
            	
            	var htmlReg 	= new RegExp("\<.*?\>", "i");
            	var arrA 		= new Array();
            	for ( var i = 0; true; i++) {
            		var m = htmlReg.exec(tempHTML);
            		if (m) {
            			arrA[i] = m;
            		} else {
            			break;
            		}
            		tempHTML = tempHTML.replace(m, "[[[[" + i + "]]]]");
            	}
            	var replaceText
            	if (options.bold)
            		replaceText = "<b style='color:" + options.color + ";'>$1</b>";
            	else
            		replaceText = "<font style='color:" + options.color + ";'>$1</font>";
            	var arrayWord = options.keyword.split(',');
            	for ( var w = 0; w < arrayWord.length; w++) {
            		var r = new RegExp("("
            				+ arrayWord[w].replace(/[(){}.+*?^$|\\\[\]]/g, "\\$&") + ")",
            				"ig");
            		tempHTML = tempHTML.replace(r, replaceText);
            	}
            	$(id).html(tempHTML);
            }
            setHeightKeyWords();
            //end
        }
   	 });
})(jQuery);