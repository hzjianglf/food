package com.sniper.springmvc.hibernate.service.impl;

import java.util.Map;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.hibernate.service.BaseService;
import com.sniper.springmvc.model.SystemConfig;

public interface SystemConfigService extends BaseService<SystemConfig> {

	@DataSource(DataSourceValue.SLAVE)
	public Map<String, String> getAdminConfig(boolean autoload);
}
