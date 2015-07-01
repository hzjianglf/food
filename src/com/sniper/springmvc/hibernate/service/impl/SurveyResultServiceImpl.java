package com.sniper.springmvc.hibernate.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.hibernate.dao.BaseDao;
import com.sniper.springmvc.model.SurveyResult;

@Service("surveyResultService")
public class SurveyResultServiceImpl extends BaseServiceImpl<SurveyResult>
		implements SurveyResultService {

	@Resource(name = "surveyResultDao")
	@Override
	public void setDao(BaseDao<SurveyResult> dao) {
		super.setDao(dao);
	}

}
