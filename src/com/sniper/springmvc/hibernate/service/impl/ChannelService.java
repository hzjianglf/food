package com.sniper.springmvc.hibernate.service.impl;

import java.util.List;
import java.util.Map;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.hibernate.service.BaseService;
import com.sniper.springmvc.model.Channel;

public interface ChannelService extends BaseService<Channel> {

	@DataSource(DataSourceValue.LOCAL)
	public List<Channel> getChannelListById(Integer[] id);

	@DataSource(DataSourceValue.LOCAL)
	public List<Channel> getChannelListByType(Integer[] id);

	@DataSource(DataSourceValue.LOCAL)
	public List<Channel> getChannelListByType(Integer[] id, String sort);

	@DataSource(DataSourceValue.LOCAL)
	public List<Channel> getChannelListByType(Integer[] id, boolean enabled);

	@DataSource(DataSourceValue.LOCAL)
	public List<Channel> getChannelListByType(Integer[] id, boolean enabled,
			String sort);

	@DataSource(DataSourceValue.LOCAL)
	public Map<String, String> getMapChannels(Integer[] id, boolean enabled,
			String sort);

	@DataSource(DataSourceValue.LOCAL)
	public Map<String, String> getMapChannels(Integer[] id, String sort);

	@DataSource(DataSourceValue.LOCAL)
	public String getChannelListByTypeForTree(Integer[] id, boolean enabled,
			String sort, String treeFidAttr);

	@DataSource(DataSourceValue.LOCAL)
	public Channel getChannelByName(String name);

	@DataSource(DataSourceValue.LOCAL)
	public List<Channel> getChannelsByType(int type, boolean status);

	@DataSource(DataSourceValue.LOCAL)
	public List<Channel> getChannelsByFidAndSelf(int id, boolean status);

	@DataSource(DataSourceValue.LOCAL)
	public List<Channel> getChannelsByFid(int fid, boolean status);

	@DataSource(DataSourceValue.LOCAL)
	public List<Channel> getChannelsByFid(int fid, boolean status, String sort);

	@DataSource(DataSourceValue.LOCAL)
	public List<Channel> getChannelsByFid(int fid, boolean status,
			Boolean showHome, String sort);

	@DataSource(DataSourceValue.LOCAL)
	public List<String> getChannelList(int id, boolean status);

	@DataSource(DataSourceValue.LOCAL)
	public String getTreeMenuByType(int type, boolean status);

	@DataSource(DataSourceValue.LOCAL)
	public String getTreeMenuByFid(int id, boolean status);

}
