package com.sniper.springmvc.hibernate.service.impl;

import java.util.List;

import com.sniper.springmvc.hibernate.service.BaseService;
import com.sniper.springmvc.model.SurveyQuestion;

public interface SurveyQuestionServie extends BaseService<SurveyQuestion> {

	public List<SurveyQuestion> executeSort(SurveyQuestion question, String sort);

	public SurveyQuestion executeCopy(SurveyQuestion question);
}
