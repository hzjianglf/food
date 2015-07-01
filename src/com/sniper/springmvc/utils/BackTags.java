package com.sniper.springmvc.utils;

/**
 * 标签返回数据
 * @author sniper
 *
 */
public class BackTags {
	String id;
	String label;
	String value;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public BackTags(String id, String label, String value) {
		super();
		this.id = id;
		this.label = label;
		this.value = value;
	}
}
