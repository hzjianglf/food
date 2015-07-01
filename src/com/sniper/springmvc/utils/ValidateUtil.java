package com.sniper.springmvc.utils;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验工具类
 * 
 * @author sniper
 * 
 */
public class ValidateUtil {

	public static boolean isValid(Object o) {
		if (o == null) {
			return false;
		}
		return true;
	}

	/**
	 * 验证字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isValid(String str) {

		if (str == null || "".equals(str.trim())) {
			return false;
		}
		return true;
	}

	/**
	 * 验证整数是否为正整数
	 * 
	 * @param i
	 * @return
	 */
	public static boolean isValid(Integer i) {
		if (null == i) {
			return false;
		}
		return true;
	}

	public static boolean isValid(int i) {

		return i <= 0 ? false : true;
	}

	/**
	 * 集合验证
	 * 
	 * @param cel
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isValid(Collection cel) {
		if (cel == null || cel.isEmpty()) {
			return false;
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isValid(Class[] paramType) {
		if (paramType != null && paramType.length > 0) {
			return true;
		}
		return false;
	}

	public static boolean isValid(Object[] paramType) {
		if (paramType != null && paramType.length > 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmail(String email) {

		if (!ValidateUtil.isValid(email)) {
			return false;
		}
		if (email.matches("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$")) {
			return true;
		}
		return false;

	}

	public static boolean isUrl(String url) {

		Pattern pattern = Pattern
				.compile(
						"^(http|https|www|ftp|)?(://)?(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)$",
						Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(url);
		if (matcher.matches()) {
			return true;
		}
		return false;

	}

	public static boolean isInt(String s) {

		Pattern pattern = Pattern.compile("^-?\\d+$");
		Matcher matcher = pattern.matcher(s);
		if (matcher.matches()) {
			return true;
		}
		return false;

	}

}
