package com.sniper.springmvc.hibernate.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.hibernate.dao.BaseDao;
import com.sniper.springmvc.model.Survey;
import com.sniper.springmvc.model.SurveyPage;
import com.sniper.springmvc.model.SurveyQuestion;
import com.sniper.springmvc.model.SurveyQuestionOption;
import com.sniper.springmvc.utils.SurveyInsertUtil;

@Service("surveyPageService")
public class SurveyPageServiceImpl extends BaseServiceImpl<SurveyPage>
		implements SurveyPageService {

	@Resource(name = "surveyPageDao")
	@Override
	public void setDao(BaseDao<SurveyPage> dao) {
		super.setDao(dao);
	}

	/**
	 * 返回json可以显示的数据结构 下面可以产生一个hibernte非可保存实体类的一个警告,但是这个只是作为json返回所以没必要
	 */
	public SurveyPage getJsonPage(int id) {

		SurveyPage page = super.getEntity(id);

		page.setSurvey(new Survey());
		for (SurveyQuestion question : page.getSq()) {
			question.setPage(new SurveyPage());
			for (SurveyQuestionOption option : question.getOptions()) {
				option.setQuestion(new SurveyQuestion());
			}
		}
		return page;
	}

	@Override
	public List<SurveyPage> executeSort(SurveyPage page, String sort) {
		List<SurveyPage> questionsBack = new ArrayList<>();

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

		SurveyPage question2 = this.getEntity(page.getId());

		String hql = "FROM SurveyPage o where o.sort " + sort
				+ " :sort and o.survey.id = :pid order by sort " + order;

		Map<String, Integer> params = new HashMap<>();
		params.put("sort", question2.getSort());
		params.put("pid", question2.getSurvey().getId());

		List<SurveyPage> questions = this
				.findEntityByHQLPage(hql, 0, 2, params);

		if (questions.size() == 2) {

			int sort0 = questions.get(0).getSort();
			int sort1 = questions.get(1).getSort();

			SurveyPage option4 = questions.get(1);
			option4.setSort(sort0);
			this.saveOrUpdateEntiry(option4);
			option4.setSurvey(page.getSurvey());
			option4.setSq(new ArrayList<SurveyQuestion>());
			questionsBack.add(option4);

			SurveyPage option3 = questions.get(0);
			option3.setSort(sort1);
			this.saveOrUpdateEntiry(option3);
			option3.setSurvey(page.getSurvey());
			option3.setSq(new ArrayList<SurveyQuestion>());
			questionsBack.add(option3);

		}

		return questionsBack;
	}

	/**
	 * 设置顺序从里向外
	 */
	@Override
	public SurveyPage getJsonPage(SurveyInsertUtil util) {
		SurveyPage page = super.getEntity(util.getPage().getId());

		for (SurveyQuestion question : page.getSq()) {
			SurveyQuestion question2 = new SurveyQuestion();
			question2.setId(question.getId());
			for (SurveyQuestionOption option : question.getOptions()) {
				option.setQuestion(question2);
			}
			question.setPage(util.getPage());
		}
		page.setSurvey(util.getSurvey());
		return page;
	}

}
