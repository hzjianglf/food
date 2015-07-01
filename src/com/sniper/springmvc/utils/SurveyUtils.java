package com.sniper.springmvc.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.HtmlUtils;

import com.sniper.springmvc.hibernate.service.impl.SurveyService;
import com.sniper.springmvc.model.Survey;
import com.sniper.springmvc.model.SurveyPage;
import com.sniper.springmvc.model.SurveyQuestion;
import com.sniper.springmvc.model.SurveyQuestionRules;
import com.sniper.springmvc.model.SurveyResult;
import com.sniper.springmvc.model.SurveyResultData;

/**
 * 问卷答案处理 这里不负责保存数据只是返回处理之后的数据
 * 
 * @author sniper
 * 
 */
public class SurveyUtils {

	private SurveyService surveyService;
	/**
	 * 保存的url
	 */
	private HttpServletRequest request;
	/**
	 * 
	 */
	private int surveyId;
	private String prefix = "answers";
	private String split = "_";
	/**
	 * 要保存的答案结果
	 */
	private List<SurveyResult> results = new ArrayList<>();
	/**
	 * 答案整合
	 */
	private SurveyResultData resultData = new SurveyResultData();
	/**
	 * 格式化之后的答案
	 */

	private Map<Integer, String[]> formatAnswers = new HashMap<>();
	/**
	 * 答案
	 * 
	 * @param surveyService
	 */
	private Map<String, String[]> parameters = new HashMap<>();

	private Map<Integer, List<String>> errors = new HashMap<>();

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setSurveyId(int surveyId) {
		this.surveyId = surveyId;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setSplit(String split) {
		this.split = split;
	}

	public void setParameters(Map<String, String[]> parameters) {
		// 把所有的答案json方式存入数据库
		String userAgent = request.getHeader("user-agent");
		resultData.setAgent(userAgent);
		Locale local = request.getLocale();
		// 语言环境
		resultData.setLocale(local.getLanguage());
		// 浏览器
		resultData.setNavigator(HttpRequestUtils.getNavigator(userAgent));
		// 系统
		resultData.setOs(HttpRequestUtils.getOS(userAgent));
		resultData.setcTime(new Date());
		// 可以根据sessionid会显答案
		try {
			resultData.setIp(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			resultData.setIp("");
		}

		this.parameters = parameters;
	}

	public Map<Integer, List<String>> getErrors() {
		return errors;
	}

	public void setErrors(Integer id, String string) {
		if (errors.containsKey(id)) {
			errors.get(id).add(string);
		} else {
			List<String> list = new ArrayList<>();
			list.add(string);
			errors.put(id, list);
		}
	}

	private void formatAnswer() {

		for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
			if (!entry.getKey().startsWith(prefix)) {
				continue;
			}
			String[] qids = entry.getKey().split(this.split);
			String sid = null;
			if (qids.length >= 2) {
				if (qids.length == 2) {
					sid = qids[1];
				} else if (qids.length == 3) {
					sid = qids[2];
				}

				int iid = Integer.valueOf(sid);
				formatAnswers.put(iid, entry.getValue());
			}
		}
	}

	public void run() {
		this.formatAnswer();
		Survey survey = surveyService.getCSurvey(surveyId);
		// 绑定问卷
		resultData.setSurvey(survey);
		List<SurveyPage> pages = survey.getSurveyPages();

		for (SurveyPage p : pages) {
			List<SurveyQuestion> questions = p.getSq();
			for (SurveyQuestion q : questions) {
				// 数据检测
				SurveyQuestionRules rules = q.getRules();
				if (rules != null) {

					if (rules.isRequired()) {
						checkRequire(q);
					}

					if (rules.isEmail()) {
						// 先检查是否存在
						checkEmail(q);
					}

					if (rules.isUrl()) {
						// 先检查是否存在
						checkUrl(q);
					}

					if (rules.isNumber()) {
						// 先检查是否存在
						checkInt(q);
					}

					if (rules.isLength()) {
						checkLength(q, rules);
					}

					if (rules.isSize()) {
						checkSize(q, rules);
					}

					// 上面保存错误信息,但是数据同样会被保存在对象中,但是不会保存在数据库中
					if (formatAnswers.containsKey(q.getId())) {
						for (String sa : formatAnswers.get(q.getId())) {
							SurveyResult result = new SurveyResult();
							// 回答的问题
							result.setQuetion(q.getId());
							// 回答的答案
							result.setAnswer(HtmlUtils.htmlEscape(sa));
							result.setResultData(resultData);
							results.add(result);
						}
					}

				} else {
					// 如果答案存在,没有规则验证

					if (formatAnswers.containsKey(q.getId())) {
						for (String sa : formatAnswers.get(q.getId())) {
							SurveyResult result = new SurveyResult();
							// 回答的问题
							result.setQuetion(q.getId());
							// 回答的答案
							result.setAnswer(HtmlUtils.htmlEscape(sa));
							result.setResultData(resultData);
							results.add(result);
						}
					}
				}
			}
		}
	}

	private void checkRequire(SurveyQuestion q) {
		if (ValidateUtil.isValid(formatAnswers.get(q.getId()))
				&& !ValidateUtil.isValid(formatAnswers.get(q.getId())[0])) {
			setErrors(q.getId(), q.getTitle() + ": 必须");
		}
	}

	private void checkInt(SurveyQuestion q) {

		if (ValidateUtil.isValid(formatAnswers.get(q.getId()))) {
			if (formatAnswers.get(q.getId()).length == 1) {
				if (!ValidateUtil.isInt(formatAnswers.get(q.getId())[0])) {
					setErrors(q.getId(), q.getTitle() + ": 必须是数字");
				}
			}
		}
	}

	private void checkEmail(SurveyQuestion q) {

		if (ValidateUtil.isValid(formatAnswers.get(q.getId()))) {
			if (formatAnswers.get(q.getId()).length == 1) {
				if (!ValidateUtil.isEmail(formatAnswers.get(q.getId())[0])) {
					setErrors(q.getId(), q.getTitle() + ": 必须是Email");
				}
			}
		}
	}

	private boolean checkUrl(SurveyQuestion q) {

		if (ValidateUtil.isValid(formatAnswers.get(q.getId()))) {
			if (formatAnswers.get(q.getId()).length == 1) {
				if (!ValidateUtil.isUrl(formatAnswers.get(q.getId())[0])) {
					setErrors(q.getId(), q.getTitle() + ": 必须是URL");
				}
			}
		}
		return false;
	}

	private void checkLength(SurveyQuestion q, SurveyQuestionRules rules) {

		if (ValidateUtil.isValid(formatAnswers.get(q.getId()))) {
			if (formatAnswers.get(q.getId()).length < rules.getMinLength()
					|| formatAnswers.get(q.getId()).length > rules
							.getMinLength()) {

				setErrors(q.getId(),
						q.getTitle() + ": 答案保持在" + rules.getMinLength() + "项到"
								+ rules.getMaxLength() + "之间");

			}
		}
	}

	private void checkSize(SurveyQuestion q, SurveyQuestionRules rules) {

		if (ValidateUtil.isValid(formatAnswers.get(q.getId()))) {
			if (formatAnswers.get(q.getId()).length == 1) {
				String a = formatAnswers.get(q.getId())[0];
				if (ValidateUtil.isInt(a)) {
					int b = Integer.valueOf(a);
					if (b > rules.getMax() || b < rules.getMin()) {

						setErrors(q.getId(),
								q.getTitle() + ": 答案选项保持在" + rules.getMax()
										+ "项到" + rules.getMin() + "之间");

					}
				}
			}
		}
	}

	public SurveyResultData getResultData() {
		return resultData;
	}

	public List<SurveyResult> getResults() {
		return results;
	}
}
