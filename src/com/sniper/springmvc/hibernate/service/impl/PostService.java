package com.sniper.springmvc.hibernate.service.impl;

import java.util.List;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.hibernate.service.BaseService;
import com.sniper.springmvc.model.Post;
import com.sniper.springmvc.utils.HqlUtils;

public interface PostService extends BaseService<Post> {

	@DataSource(DataSourceValue.LOCAL)
	public Post getAllEntity(Integer id);

	@DataSource(DataSourceValue.LOCAL)
	public Post getPostFiles(Integer id);

	@DataSource(DataSourceValue.LOCAL)
	public List<Post> getCListByChannelID(Integer[] channelId, Integer limit);

	@DataSource(DataSourceValue.LOCAL)
	public List<Post> getListsWithPostValue(HqlUtils hqlUtils, int maxResult,
			Object... Object);

	@DataSource(DataSourceValue.LOCAL)
	public List<Post> getListsWithFiles(HqlUtils hqlUtils, int maxResult,
			Object... Object);
}
