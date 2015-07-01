package com.sniper.springmvc.hibernate.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.sniper.springmvc.hibernate.dao.BaseDao;
import com.sniper.springmvc.model.SurveyQuestion;
import com.sniper.springmvc.model.SurveyQuestionOption;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.SurveyInsertUtil;

@Service("surveyQuestionOptionService")
public class SurveyQuestionOptionServiceImpl extends
		BaseServiceImpl<SurveyQuestionOption> implements
		SurveyQuestionOptionService {

	@Override
	@Resource(name = "surveyQuestionOptionDao")
	public void setDao(BaseDao<SurveyQuestionOption> dao) {
		super.setDao(dao);
	}

	/**
	 * 倒序排列的华 上面是小的 正序上面是大的
	 * 
	 */
	@Override
	public List<SurveyQuestionOption> executeSort(SurveyQuestionOption option,
			String sort) {

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

		SurveyQuestionOption option2 = this.getEntity(option.getId());

		String hql = "FROM SurveyQuestionOption o where o.sort " + sort
				+ " :sort and o.question.id = :qid order by sort " + order;

		Map<String, Integer> params = new HashMap<>();
		params.put("sort", option2.getSort());
		params.put("qid", option.getQuestion().getId());

		List<SurveyQuestionOption> optionsBack = new ArrayList<>();
		List<SurveyQuestionOption> options = this.findEntityByHQLPage(hql, 0,
				2, params);
		if (options.size() == 2) {

			int sort0 = options.get(0).getSort();
			int sort1 = options.get(1).getSort();

			SurveyQuestionOption option4 = options.get(1);
			option4.setSort(sort0);
			this.saveOrUpdateEntiry(option4);
			option4.setQuestion(option.getQuestion());
			optionsBack.add(option4);

			SurveyQuestionOption option3 = options.get(0);
			option3.setSort(sort1);
			this.saveOrUpdateEntiry(option3);
			option3.setQuestion(option.getQuestion());
			optionsBack.add(option3);

		}

		return optionsBack;
	}

	@Override
	public SurveyQuestionOption executeCopy(SurveyQuestionOption option) {

		SurveyQuestionOption optionCopy = this.getEntity(option.getId());
		// 增加排序
		SurveyQuestionOption optionCopy2 = (SurveyQuestionOption) DataUtil
				.deeplyCopy(optionCopy);
		optionCopy2.setSort(optionCopy.getSort() + 2);
		return optionCopy2;
	}

	@Override
	public SurveyQuestionOption executeCheck(SurveyQuestionOption option) {

		SurveyQuestionOption option2 = this.getEntity(option.getId());
		option2.setChecked(option.isChecked());
		option2.setWrited(option.isWrited());
		option2.setName(option.getName());
		this.saveOrUpdateEntiry(option2);
		return option;
	}

	@Override
	public List<SurveyQuestionOption> executePlus(SurveyInsertUtil util,
			SurveyQuestion question) {
		List<SurveyQuestionOption> options = new ArrayList<>();
		try {

			String input = util.getInput();
			// 换行符
			String[] inputs = input.split(System.getProperty("line.separator"));
			// 获取当前问题下面有多少答案

			int optionSize = 1;
			if (question.getOptions().size() != 0) {
				optionSize += question.getOptions().size();
			}
			// 创建
			for (int i = 0; i < inputs.length; i++) {
				String string = inputs[i];
				SurveyQuestionOption option = new SurveyQuestionOption();
				BeanUtils.copyProperties(util.getOption(), option);
				option.setName(string);
				option.setSort(optionSize * ((i + 1) * 100));

				try {
					this.saveEntiry(option);
					options.add(option);
				} catch (Exception e) {
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return options;
	}
}
