package com.sniper.springmvc.hibernate.service.impl;

import java.util.List;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.hibernate.service.BaseService;
import com.sniper.springmvc.model.AdminRight;

/**
 * 权限right
 * 
 * @author sniper
 * 
 */

public interface AdminRightService extends BaseService<AdminRight> {

	@DataSource
	public void addRgiht();

	@DataSource
	public void saveOrUpdate(AdminRight r);

	@DataSource
	public void appendRightByURL(String url);

	@DataSource(DataSourceValue.LOCAL)
	public int getMaxRightPos();

	@DataSource(DataSourceValue.LOCAL)
	public List<AdminRight> springRight();

	@DataSource(DataSourceValue.LOCAL)
	public AdminRight getCRightByUrl(String url);

}
