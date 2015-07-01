package com.sniper.springmvc.datasource;

/**
 * 数据源切换 在数据操作中切换数据源代码
 * DataSourceSwitch.setDataSource(DataSourceSwitch.DATA_SOURCE_MASTER);
 * 
 * @author sniper
 * 
 */
public class DataSourceSwitch {

	public static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

	public static void setDataSource(String dataSourceType) {
		THREAD_LOCAL.set(dataSourceType);
	}

	public static String getDataSource() {
		return THREAD_LOCAL.get();
	}

	public static void clearDataSource() {
		THREAD_LOCAL.remove();
	}

	public static void main(String[] args) {
		System.out.println(DataSourceValue.MASTER);
	}
}
