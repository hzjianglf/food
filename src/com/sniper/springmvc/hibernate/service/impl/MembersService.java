package com.sniper.springmvc.hibernate.service.impl;

import java.util.List;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.hibernate.service.BaseService;
import com.sniper.springmvc.model.Members;

public interface MembersService extends BaseService<Members> {

	@DataSource(DataSourceValue.LOCAL)
	public Members validateByName(String username);

	@DataSource(DataSourceValue.LOCAL)
	public Members validateByNickName(String username);

	@DataSource(DataSourceValue.LOCAL)
	public Members validateByEmail(String email);

	@DataSource(DataSourceValue.LOCAL)
	public List<Members> findListsByEmail(String email);

	@DataSource(DataSourceValue.LOCAL)
	public boolean validateUser(String name, String password);

	@DataSource(DataSourceValue.LOCAL)
	public List<Members> getUserByLevel(int gid);

}
