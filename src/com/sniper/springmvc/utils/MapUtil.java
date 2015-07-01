package com.sniper.springmvc.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapUtil {
	

	

	public static Map<String, String> sortTreeMapByKey(
			Map<String, String> oriMap) {
		if (oriMap == null || oriMap.isEmpty()) {
			return null;
		}
		Map<String, String> sortedMap = new TreeMap<>(new Comparator<String>() {
			public int compare(String key1, String key2) {
				int intKey1 = 0, intKey2 = 0;
				try {
					intKey1 = getInt(key1);
					intKey2 = getInt(key2);
				} catch (Exception e) {
					intKey1 = 0;
					intKey2 = 0;
				}
				return intKey1 - intKey2;
			}
		});
		sortedMap.putAll(oriMap);
		return sortedMap;
	}

	private static int getInt(String str) {
		int i = 0;
		try {
			Pattern p = Pattern.compile("^\\d+");
			Matcher m = p.matcher(str);
			if (m.find()) {
				i = Integer.valueOf(m.group());
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return i;
	}

	public static Map<String, String> sortLinkedHashMapByValue(
			Map<String, String> oriMap) {
		Map<String, String> sortedMap = new LinkedHashMap<>();
		if (oriMap != null && !oriMap.isEmpty()) {
			List<Map.Entry<String, String>> entryList = new ArrayList<>(
					oriMap.entrySet());
			Collections.sort(entryList,
					new Comparator<Map.Entry<String, String>>() {
						public int compare(Entry<String, String> entry1,
								Entry<String, String> entry2) {
							int value1 = 0, value2 = 0;
							try {
								value1 = getInt(entry1.getValue());
								value2 = getInt(entry2.getValue());
							} catch (NumberFormatException e) {
								value1 = 0;
								value2 = 0;
							}
							return value2 - value1;
						}
					});
			Iterator<Map.Entry<String, String>> iter = entryList.iterator();
			Map.Entry<String, String> tmpEntry = null;
			while (iter.hasNext()) {
				tmpEntry = iter.next();
				sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
			}
		}
		return sortedMap;
	}
}
