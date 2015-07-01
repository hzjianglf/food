package com.sniper.springmvc.hibernate.service.impl;

import java.util.List;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.hibernate.service.BaseService;
import com.sniper.springmvc.model.AdminUser;

public interface AdminUserService extends BaseService<AdminUser> {

	@DataSource(DataSourceValue.LOCAL)
	public boolean isRegisted(String name);

	@DataSource(DataSourceValue.LOCAL)
	public AdminUser validateByName(String username);

	@DataSource(DataSourceValue.LOCAL)
	public AdminUser validateByNickName(String username);

	@DataSource(DataSourceValue.LOCAL)
	public AdminUser validateByEmail(String email);

	@DataSource(DataSourceValue.LOCAL)
	public List<AdminUser> findListsByEmail(String email);

	@DataSource(DataSourceValue.LOCAL)
	public boolean validateUser(String name, String password);

	@DataSource(DataSourceValue.LOCAL)
	public List<AdminUser> getUserByGroup(int gid);

	@DataSource(DataSourceValue.LOCAL)
	public List<AdminUser> getCUserByGroup(int gid);

	@DataSource(DataSourceValue.SLAVE)
	public AdminUser noLogin(String uname);

}
