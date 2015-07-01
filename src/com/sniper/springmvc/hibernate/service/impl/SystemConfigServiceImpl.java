package com.sniper.springmvc.hibernate.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.hibernate.dao.BaseDao;
import com.sniper.springmvc.model.SystemConfig;

@Service("systemConfigService")
public class SystemConfigServiceImpl extends BaseServiceImpl<SystemConfig>
		implements SystemConfigService {

	@Override
	@Resource(name = "systemConfigDao")
	public void setDao(BaseDao<SystemConfig> dao) {
		super.setDao(dao);
	}

	@Override
	public Map<String, String> getAdminConfig(boolean autoload) {

		String hql = "select new SystemConfig(sc.keyName, sc.keyValue) from SystemConfig sc where sc.autoload = :auto";
		Map<String, Boolean> param = new HashMap<>();
		param.put("auto", autoload);
		List<SystemConfig> configs = this.findEntityByHQL(hql, param);
		
		Map<String, String> map = new HashMap<>();
		for (SystemConfig config : configs) {
			map.put(config.getKeyName(), config.getKeyValue());
		}
		return map;
	}

}
