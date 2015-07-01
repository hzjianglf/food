package com.sniper.springmvc.datasource;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 分库数据源路由配置 自定义分布式数据库(数据库路由器)
 * 
 * @author laolang
 * 
 */
public class DataSourceRouter extends AbstractRoutingDataSource {

	/**
	 * 检测key 走那个数据源和这个key有关系
	 */
	@Override
	protected Object determineCurrentLookupKey() {

		String hostName = "";
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		InputStream in = getClass().getClassLoader().getResourceAsStream(
				"properties/local.properties");

		Properties properties = new Properties();
		try {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (properties.get("localHostName") != null) {
			String localHostName = (String) properties.get("localHostName");
			if (hostName.equalsIgnoreCase(localHostName)) {
				return DataSourceValue.LOCAL.toString();
			}
		}

		return DataSourceSwitch.getDataSource();
	}

	public static void main(String[] args) throws UnknownHostException {
		String ip = InetAddress.getLocalHost().getHostName();
		System.out.println(ip);
	}

}
