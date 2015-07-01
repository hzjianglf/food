package survey;

import org.junit.Test;
import org.springframework.web.util.HtmlUtils;

public class HtmlUtilsTest {

	@Test
	public void test1() {
		String s = "<p>111</p>";
		System.out.println(HtmlUtils.htmlEscape(s));
		System.out.println(HtmlUtils.htmlEscapeDecimal(s));
		System.out.println(HtmlUtils.htmlEscapeHex(s));
		System.out.println(HtmlUtils.htmlUnescape(s));
	}
}
