package com.sniper.springmvc.utils;

import com.sniper.springmvc.model.Survey;
import com.sniper.springmvc.model.SurveyPage;
import com.sniper.springmvc.model.SurveyQuestion;
import com.sniper.springmvc.model.SurveyQuestionOption;
import com.sniper.springmvc.model.SurveyQuestionRules;

/**
 * 添加问卷问题
 * 
 * @author sniper
 * 
 */
public class SurveyInsertUtil {

	private Survey survey = new Survey();
	private SurveyPage page = new SurveyPage();
	private SurveyQuestion question = new SurveyQuestion();
	private SurveyQuestionOption option = new SurveyQuestionOption();
	private SurveyQuestionRules rules = new SurveyQuestionRules();

	private String type;

	// 默认输入
	private String input;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public SurveyPage getPage() {
		return page;
	}

	public void setPage(SurveyPage page) {
		this.page = page;
	}

	public SurveyQuestion getQuestion() {
		return question;
	}

	public void setQuestion(SurveyQuestion question) {
		this.question = question;
	}

	public SurveyQuestionOption getOption() {
		return option;
	}

	public void setOption(SurveyQuestionOption option) {
		this.option = option;
	}

	public SurveyQuestionRules getRules() {
		return rules;
	}

	public void setRules(SurveyQuestionRules rules) {
		this.rules = rules;
	}

}
