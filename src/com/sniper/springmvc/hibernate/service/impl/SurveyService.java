package com.sniper.springmvc.hibernate.service.impl;

import java.util.List;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.hibernate.service.BaseService;
import com.sniper.springmvc.model.Survey;

public interface SurveyService extends BaseService<Survey> {

	@DataSource(DataSourceValue.SLAVE)
	public Survey getSurvey(Integer id);

	@DataSource(DataSourceValue.SLAVE)
	public Survey getCSurvey(Integer id);

	@DataSource(DataSourceValue.SLAVE)
	public List<Survey> surveyCopy(Integer[] ids);
}
