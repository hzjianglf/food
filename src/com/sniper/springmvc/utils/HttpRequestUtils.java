package com.sniper.springmvc.utils;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequestUtils {

	public static String getAccept(String accept) {
		return accept;

	}

	public static String getLocale(Locale locale) {

		if (Locale.SIMPLIFIED_CHINESE.equals(locale)) {
			return "简体中文";
		} else if (Locale.TRADITIONAL_CHINESE.equals(locale)) {
			return "繁体中文";

		} else if (Locale.ENGLISH.equals(locale)) {
			return "英文";
		} else if (Locale.JAPANESE.equals(locale)) {
			return "日本";
		}
		return locale.getLanguage();

	}

	/**
	 * 获取浏览器及其版本
	 * 
	 * @param userAgent
	 * @return
	 */
	public static String getNavigator(String userAgent) {
		if (userAgent.toLowerCase().indexOf("msie") > 0) {
			return getIE(userAgent);
		} else if (userAgent.toLowerCase().indexOf("chrome") > 0) {
			return getChrome(userAgent);
		} else if (userAgent.toLowerCase().indexOf("maxthon") > 0) {
			return getMaxthon(userAgent);
		} else if (userAgent.toLowerCase().indexOf("firefox") > 0) {
			return getFirefox(userAgent);
		} else {
			return "其他浏览器";
		}
	}

	public static String getIE(String userAgent) {
		Pattern pattern = Pattern.compile("MSIE [0-9]*\\.[0-9]*",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(userAgent);
		String group = "";
		while (matcher.find()) {
			group = matcher.group();
		}
		return group;
	}

	public static boolean isFirefox(String userAgent) {
		Pattern pattern = Pattern.compile("Firefox/[0-9]*\\.[0-9]*",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(userAgent);
		while (matcher.find()) {
			return true;
		}
		return false;
	}

	public static String getFirefox(String userAgent) {
		Pattern pattern = Pattern.compile("Firefox/[0-9]*\\.[0-9]*",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(userAgent);
		String group = "";
		while (matcher.find()) {
			group = matcher.group();
		}
		return group;
	}

	public static String getChrome(String userAgent) {
		Pattern pattern = Pattern.compile("Chrome/[0-9]*\\.[0-9]*",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(userAgent);
		String group = "";
		while (matcher.find()) {
			group = matcher.group();
		}
		return group;
	}

	public static String getMaxthon(String userAgent) {
		Pattern pattern = Pattern.compile("Maxthon/.* ",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(userAgent);
		String group = "";
		while (matcher.find()) {
			group = matcher.group();
		}
		return group;
	}

	public static String getOS(String userAgent) {

		if (userAgent.indexOf("MSIE") > 0) {
			return getWindow(userAgent);
		} else if (userAgent.indexOf("Linux") > 0) {
			return "Linux";
		} else if (userAgent.indexOf("Mac") > 0) {
			return getMac(userAgent);
		} else {
			return getMobile(userAgent);
		}
	}

	public static String getWindow(String userAgent) {
		Pattern pattern = Pattern.compile("Windows [a-zA-Z]* [0-9]*\\.[0-9]*",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(userAgent);
		String group = "";
		while (matcher.find()) {
			group = matcher.group().trim();
		}
		return group;
	}

	public static String getMac(String userAgent) {
		Pattern pattern = Pattern.compile("Chrome/[0-9]*\\.[0-9]*",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(userAgent);
		String group = "";
		while (matcher.find()) {
			group = matcher.group();
		}
		return group;
	}

	public static String getMobile(String userAgent) {
		String[] mobileAgents = { "iphone", "android", "phone", "mobile",
				"wap", "netfront", "java", "opera mobi", "opera mini", "ucweb",
				"windows ce", "symbian", "series", "webos", "sony",
				"blackberry", "dopod", "nokia", "samsung", "palmsource", "xda",
				"pieplus", "meizu", "midp", "cldc", "motorola", "foma",
				"docomo", "up.browser", "up.link", "blazer", "helio", "hosin",
				"huawei", "novarra", "coolpad", "webos", "techfaith",
				"palmsource", "alcatel", "amoi", "ktouch", "nexian",
				"ericsson", "philips", "sagem", "wellcom", "bunjalloo", "maui",
				"smartphone", "iemobile", "spice", "bird", "zte-", "longcos",
				"pantech", "gionee", "portalmmm", "jig browser", "hiptop",
				"benq", "haier", "^lct", "320x320", "240x320", "176x220",
				"w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq",
				"bird", "blac", "blaz", "brew", "cell", "cldc", "cmd-", "dang",
				"doco", "eric", "hipt", "inno", "ipaq", "java", "jigs", "kddi",
				"keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo",
				"midp", "mits", "mmef", "mobi", "mot-", "moto", "mwbp", "nec-",
				"newt", "noki", "oper", "palm", "pana", "pant", "phil", "play",
				"port", "prox", "qwap", "sage", "sams", "sany", "sch-", "sec-",
				"send", "seri", "sgh-", "shar", "sie-", "siem", "smal", "smar",
				"sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-",
				"upg1", "upsi", "vk-v", "voda", "wap-", "wapa", "wapi", "wapp",
				"wapr", "webc", "winw", "winw", "xda", "xda-",
				"Googlebot-Mobile" };

		for (String mobileAgent : mobileAgents) {
			if (userAgent.toLowerCase().indexOf(mobileAgent) >= 0) {
				return mobileAgent;
			}
		}
		return "其他";
	}
}
