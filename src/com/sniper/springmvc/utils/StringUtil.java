package com.sniper.springmvc.utils;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {
	/**
	 * 判断字符是否存在数组之中
	 * 
	 * @param values
	 * @param value
	 * @return
	 */
	public static boolean contains(String[] values, String value) {
		if (ValidateUtil.isValid(values)) {
			for (String s : values) {
				if (s.equals(value)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 数组连接
	 * 
	 * @param col
	 * @return
	 */
	public static String arr2Str(Object[] col) {
		String str = "";

		if (ValidateUtil.isValid(col)) {
			for (Object s : col) {
				str = str + s + ",";
			}
			return str.substring(0, str.length() - 1);
		}
		return str;

	}

	/**
	 * 字符串截取
	 * 
	 * @param str
	 * @return
	 */
	public static String getDescString(String str) {

		if (str != null && str.trim().length() > 30) {
			return str.substring(0, 30);
		}
		return str;
	}

	public static String join(List<Integer> ids) {
		return StringUtils.join(ids, ",");

	}

	public static Object getObject(Object key, Map<Object, Object> map) {

		System.out.println(map);
		System.out.println(key);
		if (map.containsKey(key)) {
			return map.get(key);
		}

		return "";
	}

	public static void main(String[] args) {
		String string = "2011香港山东周签约项目明细（东营）";
		System.out.println(string.indexOf("（"));
		System.out.println(string.indexOf("a"));
		if (string.indexOf("（") != -1) {
			String string2 = string.substring(string.indexOf("（") + 1,
					string.lastIndexOf("）"));
			System.out.println(string2);
		}
	}

}
