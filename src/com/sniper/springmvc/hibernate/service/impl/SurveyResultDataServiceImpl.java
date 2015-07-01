package com.sniper.springmvc.hibernate.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.hibernate.dao.BaseDao;
import com.sniper.springmvc.model.SurveyResultData;

@Service("surveyResultDataService")
public class SurveyResultDataServiceImpl extends
		BaseServiceImpl<SurveyResultData> implements SurveyResultDataService {

	@Resource(name = "surveyResultDataDao")
	@Override
	public void setDao(BaseDao<SurveyResultData> dao) {
		super.setDao(dao);
	}
}
