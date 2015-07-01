package com.sniper.springmvc.hibernate.service.impl;

import java.util.List;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.hibernate.service.BaseService;
import com.sniper.springmvc.model.AdminGroup;

public interface AdminGroupService extends BaseService<AdminGroup> {

	@DataSource(DataSourceValue.LOCAL)
	public List<AdminGroup> getGroupSelectList(String where);

	@DataSource(DataSourceValue.LOCAL)
	public List<AdminGroup> getGroupList(Integer[] id);
}
