package com.sniper.springmvc.hibernate.service.impl;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.hibernate.service.BaseService;
import com.sniper.springmvc.model.Files;

public interface FilesService extends BaseService<Files> {

	@DataSource(DataSourceValue.LOCAL)
	public Files getFileByUrl(String url);


}
