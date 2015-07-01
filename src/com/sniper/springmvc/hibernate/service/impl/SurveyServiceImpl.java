package com.sniper.springmvc.hibernate.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.hibernate.dao.BaseDao;
import com.sniper.springmvc.model.Survey;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.ValidateUtil;

@Service("surveyService")
public class SurveyServiceImpl extends BaseServiceImpl<Survey> implements
		SurveyService {

	@Resource(name = "surveyDao")
	@Override
	public void setDao(BaseDao<Survey> dao) {
		super.setDao(dao);
	}

	@Override
	public Survey getSurvey(Integer id) {
		Survey survey = super.getEntity(id);
		if (!ValidateUtil.isValid(survey)) {
			return null;
		}
		survey.getSurveyPages().size();
		return survey;
	}

	@Override
	public Survey getCSurvey(Integer id) {
		return this.getSurvey(id);
	}

	@Override
	public List<Survey> surveyCopy(Integer[] ids) {
		List<Survey> lists = new ArrayList<>();
		List<Survey> surveys = getEntitys(ids);
		for (Survey survey : surveys) {
			survey.getSurveyPages().size();
			// 创建新的问卷
			Survey surveyNew = (Survey) DataUtil.deeplyCopy(survey);
			lists.add(surveyNew);
		}
		return lists;
	}

}
