package com.sniper.springmvc.hibernate.service.impl;

import java.util.List;

import com.sniper.springmvc.hibernate.service.BaseService;
import com.sniper.springmvc.model.SurveyQuestion;
import com.sniper.springmvc.model.SurveyQuestionOption;
import com.sniper.springmvc.utils.SurveyInsertUtil;

public interface SurveyQuestionOptionService extends
		BaseService<SurveyQuestionOption> {

	public List<SurveyQuestionOption> executeSort(SurveyQuestionOption option,
			String sort);

	public SurveyQuestionOption executeCopy(SurveyQuestionOption option);

	public SurveyQuestionOption executeCheck(SurveyQuestionOption option);

	public List<SurveyQuestionOption> executePlus(SurveyInsertUtil utils,
			SurveyQuestion question);
}
