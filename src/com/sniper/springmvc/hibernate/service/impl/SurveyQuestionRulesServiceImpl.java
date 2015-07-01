package com.sniper.springmvc.hibernate.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.hibernate.dao.BaseDao;
import com.sniper.springmvc.model.SurveyQuestionRules;

@Service("surveyQuestionRulesService")
public class SurveyQuestionRulesServiceImpl extends
		BaseServiceImpl<SurveyQuestionRules> implements
		SurveyQuestionRulesService {

	@Override
	@Resource(name = "surveyQuestionRulesDao")
	public void setDao(BaseDao<SurveyQuestionRules> dao) {
		super.setDao(dao);
	}

}
