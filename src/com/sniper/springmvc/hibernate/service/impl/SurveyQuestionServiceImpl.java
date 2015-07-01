package com.sniper.springmvc.hibernate.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.hibernate.dao.BaseDao;
import com.sniper.springmvc.model.SurveyQuestion;
import com.sniper.springmvc.model.SurveyQuestionOption;
import com.sniper.springmvc.utils.DataUtil;

@Service("surveyQuestionServie")
public class SurveyQuestionServiceImpl extends BaseServiceImpl<SurveyQuestion>
		implements SurveyQuestionServie {

	@Resource
	SurveyQuestionRulesService rulesService;

	@Resource
	SurveyQuestionOptionService optionService;

	@Resource(name = "surveyQuestionDao")
	@Override
	public void setDao(BaseDao<SurveyQuestion> dao) {
		super.setDao(dao);
	}

	@Override
	public List<SurveyQuestion> executeSort(SurveyQuestion question, String sort) {

		String order = "asc";
		switch (sort) {
		case "<=":
			order = "desc";

			break;
		case ">=":
			order = "asc";
			break;

		default:
			break;
		}

		SurveyQuestion question2 = this.getEntity(question.getId());

		String hql = "FROM SurveyQuestion o where o.sort " + sort
				+ " :sort and o.page.id = :qid order by sort " + order;

		Map<String, Integer> params = new HashMap<>();
		params.put("sort", question2.getSort());
		params.put("qid", question.getPage().getId());

		List<SurveyQuestion> optionsBack = new ArrayList<>();
		List<SurveyQuestion> options = this.findEntityByHQLPage(hql, 0, 2,
				params);
		if (options.size() == 2) {

			int sort0 = options.get(0).getSort();
			int sort1 = options.get(1).getSort();

			SurveyQuestion option4 = options.get(1);

			option4.setSort(sort0);
			super.saveOrUpdateEntiry(option4);
			option4.setPage(question.getPage());
			option4.setOptions(new ArrayList<SurveyQuestionOption>());
			optionsBack.add(option4);

			SurveyQuestion option3 = options.get(0);
			option3.setSort(sort1);
			this.saveOrUpdateEntiry(option3);

			option3.setPage(question.getPage());
			option3.setOptions(new ArrayList<SurveyQuestionOption>());
			optionsBack.add(option3);

		}

		return optionsBack;
	}

	@Override
	public SurveyQuestion executeCopy(SurveyQuestion question) {

		SurveyQuestion questionCopy = this.getEntity(question.getId());
		// 增加排序
		SurveyQuestion optionCopy2 = (SurveyQuestion) DataUtil
				.deeplyCopy(questionCopy);
		optionCopy2.setSort(questionCopy.getSort() + 1);
		return optionCopy2;
	}

}
