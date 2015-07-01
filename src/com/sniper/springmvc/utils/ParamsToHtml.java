package com.sniper.springmvc.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 通过封装把参数传递给试图
 * 
 * @author sniper
 * 
 */
public class ParamsToHtml {

	private String key;
	private Map<String, String> keys = new HashMap<>();
	private Map<String, Map<String, String>> params = new HashMap<>();

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public Map<String, String> getKeys() {
		return keys;
	}

	public void setKeys(Map<String, String> keys) {
		this.keys = keys;
	}

	public String getKeyValue(String key) {
		if (keys.containsKey(key)) {
			return keys.get(key);
		}
		return key;
	}

	public Map<String, Map<String, String>> getParams() {
		return params;
	}

	public void setParams(Map<String, Map<String, String>> params) {
		this.params = params;
	}

	public Map<String, String> getMapValue(String key) {

		return params.get(key);
	}

	public String getMapValueString(String key, String nkey) {

		return params.get(key).get(nkey);

	}

	public void addMapValue(String key, Map<String, String> value) {
		params.put(key, value);
	}

	@Override
	public String toString() {
		return "ParamsToHtml [keys=" + keys + ", params=" + params + "]";
	}

}
