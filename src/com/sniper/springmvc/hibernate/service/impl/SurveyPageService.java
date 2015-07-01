package com.sniper.springmvc.hibernate.service.impl;

import java.util.List;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.hibernate.service.BaseService;
import com.sniper.springmvc.model.SurveyPage;
import com.sniper.springmvc.utils.SurveyInsertUtil;

public interface SurveyPageService extends BaseService<SurveyPage> {

	@DataSource(DataSourceValue.LOCAL)
	public SurveyPage getJsonPage(SurveyInsertUtil util);

	@DataSource
	public List<SurveyPage> executeSort(SurveyPage page, String desc);
}
