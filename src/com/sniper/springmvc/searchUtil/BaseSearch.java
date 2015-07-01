package com.sniper.springmvc.searchUtil;


import java.util.HashMap;
import java.util.Map;

public class BaseSearch {

	// 常被一些基本的数据

	private final static Map<String, String> statusValues = new HashMap<>();
	{
		statusValues.put("true", "启用");
		statusValues.put("false", "禁用");
	}

	private Boolean status;
	private Integer channel;
	private String name;

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getChannel() {
		return channel;
	}

	public void setChannel(Integer channel) {
		this.channel = channel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static Map<String, String> getStatusvalues() {
		return statusValues;
	}
}
