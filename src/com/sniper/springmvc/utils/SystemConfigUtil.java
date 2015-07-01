package com.sniper.springmvc.utils;

import java.util.HashMap;
import java.util.Map;

import com.sniper.springmvc.hibernate.service.impl.SystemConfigService;
import com.sniper.springmvc.security.SpringContextUtil;

/**
 * 负责保存系统基本数据,
 * 
 * @author sniper
 * 
 */
public class SystemConfigUtil {
	
	private static SystemConfigService configService;

	public static SystemConfigService getConfigService() {
		if (configService == null) {
			configService = (SystemConfigService) SpringContextUtil
					.getBean(SystemConfigService.class);
		}
		return configService;
	}

	private static Map<String, String> systemConfig = new HashMap<>();

	public static Map<String, String> getSystemConfig() {
		if (systemConfig.size() == 0) {
			systemConfig = getConfigService().getAdminConfig(true);
		}
		return systemConfig;
	}

	public static void setSystemConfig(Map<String, String> systemConfig) {
		SystemConfigUtil.systemConfig = systemConfig;
	}
}
