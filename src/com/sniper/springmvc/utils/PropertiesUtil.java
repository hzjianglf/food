package com.sniper.springmvc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {

	private Properties properties = null;
	private File file;
	private InputStream in;

	public PropertiesUtil() {

	}

	/**
	 * 路过你要修改文件的华必须传递file
	 * 
	 * @param file
	 * @throws FileNotFoundException
	 */
	public PropertiesUtil(File file) {
		this.file = file;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		read();
	}

	/**
	 * 读取专用
	 * 
	 * @param in
	 * @throws FileNotFoundException
	 */
	public PropertiesUtil(InputStream in) {

		this.in = in;
		read();
	}

	private void read() {

		properties = new Properties();
		try {
			properties.load(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getValue(String key) {
		try {
			String value = properties.getProperty(key);
			return value;
		} catch (Exception e) {
			return "";
		}

	}

	public Integer getIntegerValue(String key) {
		try {
			Integer value = Integer.parseInt(properties.getProperty(key));
			return value;
		} catch (Exception e) {
			return 0;
		}
	}

	@SuppressWarnings("rawtypes")
	public Map<String, String> getValues() {
		Map<String, String> list = new HashMap<>();

		Enumeration en = properties.propertyNames();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			String value = properties.getProperty(key);
			list.put(key, value);
		}

		return list;

	}

	@SuppressWarnings("rawtypes")
	public Map<String, Object> getObjectValues() {
		Map<String, Object> list = new HashMap<>();

		Enumeration en = properties.propertyNames();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			String value = properties.getProperty(key);
			list.put(key, value);
		}

		return list;

	}

	@SuppressWarnings("rawtypes")
	public Map<Integer, String> getIntegerValues() {
		Map<Integer, String> list = new HashMap<>();

		Enumeration en = properties.propertyNames();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			String value = properties.getProperty(key);
			list.put(Integer.parseInt(key), value);
		}

		return list;

	}

	/**
	 * 修改值
	 * 
	 * @param key
	 * @param value
	 * @throws IOException
	 */
	public void setValue(String key, String value) throws IOException {

		FileOutputStream out = new FileOutputStream(this.file);
		properties.setProperty(key, value);
		properties.store(out, "update " + key);
		out.close();
	}

}
