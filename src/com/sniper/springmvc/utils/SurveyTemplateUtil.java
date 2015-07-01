package com.sniper.springmvc.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.sniper.springmvc.model.SurveyQuestion;
import com.sniper.springmvc.model.SurveyQuestionOption;
import com.sniper.springmvc.model.SurveyQuestionRules;

public class SurveyTemplateUtil {

	private static String baseName = "answers_";

	public static String getBaseName() {
		return baseName;
	}

	public static void setBaseName(String baseName) {
		SurveyTemplateUtil.baseName = baseName;
	}

	public static String answer(SurveyQuestion question) {

		StringBuffer buffer = new StringBuffer();
		buffer.append("<div class=\"q_title\">");
		buffer.append(question.getTitle());
		buffer.append("</div>");
		buffer.append("<div class=\"q_body\">");
		SurveyQuestionRules rules = question.getRules();
		List<SurveyQuestionOption> options = question.getOptions();

		switch (question.getType()) {
		case 1:
			for (SurveyQuestionOption option : options) {
				HorizontalRadio(question, option, buffer);
			}
			SetOther(question, buffer, rules);
			break;
		case 2:
			for (SurveyQuestionOption option : options) {
				VerticalRadio(question, option, buffer);
			}
			SetOther(question, buffer, rules);
			break;
		case 3:
			for (SurveyQuestionOption option : options) {
				HorizontalMulti(question, option, buffer);
			}
			SetOther(question, buffer, rules);
			break;
		case 4:
			for (SurveyQuestionOption option : options) {
				VerticalMulti(question, option, buffer);
			}
			SetOther(question, buffer, rules);
			break;
		case 5:
			buffer.append("<select ");
			SetID(question, null, buffer);
			SetName(question, buffer);
			SetClass(question, buffer);
			buffer.append(">");
			for (SurveyQuestionOption option : options) {
				DownMenu(option, buffer);
			}
			buffer.append("</select>");
			SetOther(question, buffer, rules);
			break;
		case 6:
			SingleInput(question, buffer);
			break;
		case 7:
			MulitInput(question, buffer);

		default:
			break;
		}
		buffer.append("</div>");
		return buffer.toString();
	}

	private static void SetID(SurveyQuestion question,
			SurveyQuestionOption option, StringBuffer buffer) {
		buffer.append("id=\"o_");
		buffer.append(question.getId());
		if (option != null) {
			buffer.append("_");
			buffer.append(option.getId());
		}
		buffer.append("\" ");
	}

	private static void SetName(SurveyQuestion question, StringBuffer buffer) {

		buffer.append("name=\"");
		buffer.append(getBaseName());
		buffer.append(question.getId());
		buffer.append("\" ");
	}

	private static void SetName(SurveyQuestion question, StringBuffer buffer,
			String suffix) {

		buffer.append("name=\"");
		buffer.append(getBaseName());
		buffer.append(suffix);
		buffer.append(question.getId());
		buffer.append("\" ");
	}

	private static void SetValue(SurveyQuestionOption option,
			StringBuffer buffer) {
		buffer.append("value=\"");
		buffer.append(option.getTitle());
		buffer.append("\" ");
	}

	/**
	 * 获取组装的class
	 * 
	 * @param question
	 * @param buffer
	 */
	private static void SetClass(SurveyQuestion question, StringBuffer buffer) {

		buffer.append("class=\"");
		if (question.getType() == 6 || question.getType() == 7) {
			buffer.append("form-control ");
		}
		SurveyQuestionRules rules = question.getRules();
		if (rules == null) {
			buffer.append("\" ");
			return;
		}

		buffer.append("validate[");

		List<String> notCustoms = new ArrayList<>();

		if (rules.isRequired()) {
			notCustoms.add("required");
		}
		if (rules.isSize()) {
			notCustoms.add("min[" + rules.getMin() + "]");
			notCustoms.add("max[" + rules.getMax() + "]");
		}
		if (rules.isLength()) {
			notCustoms.add("minSize[" + rules.getMinLength() + "]");
			notCustoms.add("maxSize[" + rules.getMaxLength() + "]");
		}

		if (notCustoms.size() > 0) {
			buffer.append(StringUtils.join(notCustoms, ","));
		}

		List<String> customs = new ArrayList<>();
		if (rules.isEmail()) {
			customs.add("email");
		}
		if (rules.isNumber()) {
			customs.add("integer");
		}
		if (rules.isUrl()) {
			customs.add("url");
		}
		if (customs.size() > 0) {
			if (notCustoms.size() > 0) {
				buffer.append(",");
			}
			buffer.append("custom[");
			buffer.append(StringUtils.join(customs, ","));
			buffer.append("]");
		}

		buffer.append("]\" ");
	}

	private static void SetOther(SurveyQuestion question, StringBuffer buffer,
			SurveyQuestionRules rules) {
		if (rules != null && false) {
			buffer.append("<div class=\"input-group col-xs-12 radio-inline\">");
			buffer.append("<div class=\"input-group-addon\">其他</div>");
			buffer.append("<input type=\"text\" ");
			SetID(question, null, buffer);
			SetName(question, buffer, "other_");
			buffer.append("class=\"form-control\" ");
			buffer.append(">");
			buffer.append("</div>");
		}
	}

	/**
	 * 横向单选
	 * 
	 * @param question
	 * @param rules
	 * @return
	 */
	private static void HorizontalRadio(SurveyQuestion question,
			SurveyQuestionOption option, StringBuffer buffer) {

		buffer.append("<label class=\"radio-inline\">");
		buffer.append("<input type=\"radio\" ");
		SetID(question, option, buffer);
		SetName(question, buffer);
		SetValue(option, buffer);
		SetClass(question, buffer);
		buffer.append("> ");
		buffer.append(option.getTitle());
		buffer.append("<input type=\"text\" ");
		SetID(question, null, buffer);
		SetName(question, buffer, "other_");
		buffer.append("class=\"form-control\" ");
		buffer.append(">");
		buffer.append("</label>");
	}

	/**
	 * 竖向单选
	 * 
	 * @param question
	 * @param option
	 * @return
	 */
	private static void VerticalRadio(SurveyQuestion question,
			SurveyQuestionOption option, StringBuffer buffer) {

		buffer.append("<div class=\"radio\">");
		buffer.append("<label>");
		buffer.append("<input type=\"radio\" ");
		SetID(question, option, buffer);
		SetName(question, buffer);
		SetValue(option, buffer);
		SetClass(question, buffer);
		buffer.append(">");
		buffer.append(option.getTitle());
		buffer.append("<input type=\"text\" ");
		SetID(question, null, buffer);
		SetName(question, buffer, "other_");
		buffer.append("class=\"form-control\" ");
		buffer.append(">");
		buffer.append("</label>");
		buffer.append("</div>");
	}

	/**
	 * 横向多选
	 * 
	 * @param question
	 * @param option
	 * @return
	 */
	private static void HorizontalMulti(SurveyQuestion question,
			SurveyQuestionOption option, StringBuffer buffer) {

		buffer.append("<label class=\"checkbox-inline\">");
		buffer.append("<input type=\"checkbox\" ");
		SetID(question, option, buffer);
		SetClass(question, buffer);
		SetValue(option, buffer);
		SetName(question, buffer);
		buffer.append(">");
		buffer.append(option.getTitle());
		buffer.append("<input type=\"text\" ");
		SetID(question, null, buffer);
		SetName(question, buffer, "other_");
		buffer.append("class=\"form-control\" ");
		buffer.append(">");
		buffer.append("</label>");
	}

	/**
	 * 竖向
	 * 
	 * @param question
	 * @param option
	 * @param buffer
	 */
	private static void VerticalMulti(SurveyQuestion question,
			SurveyQuestionOption option, StringBuffer buffer) {

		buffer.append("<div class=\"checkbox\">");
		buffer.append("<label>");
		buffer.append("<input type=\"checkbox\" ");
		SetID(question, option, buffer);
		SetClass(question, buffer);
		SetValue(option, buffer);
		SetName(question, buffer);
		buffer.append(">");
		buffer.append(option.getTitle());
		buffer.append("</label>");
		buffer.append("<input type=\"text\" ");
		SetID(question, null, buffer);
		SetName(question, buffer, "other_");
		buffer.append("class=\"form-control\" ");
		buffer.append(">");
		buffer.append("</div>");
	}

	private static void DownMenu(SurveyQuestionOption option,
			StringBuffer buffer) {

		buffer.append("<option value=\"");
		buffer.append(option.getTitle());
		buffer.append("\">");
		buffer.append(option.getTitle());
		buffer.append("</option>");

	}

	/**
	 * 单行文本输入
	 * 
	 * @param option
	 * @return
	 */
	private static void SingleInput(SurveyQuestion question, StringBuffer buffer) {

		buffer.append("<input type=\"text\" ");
		SetID(question, null, buffer);
		SetName(question, buffer);
		SetClass(question, buffer);
		buffer.append(">");

	}

	/**
	 * 多行行文本输入
	 * 
	 * @param option
	 * @return
	 */
	private static void MulitInput(SurveyQuestion question, StringBuffer buffer) {

		buffer.append("<textarea rows=\"3\" ");
		SetID(question, null, buffer);
		SetName(question, buffer);
		SetClass(question, buffer);
		buffer.append(">");
		buffer.append("</textarea>");
	}
}
