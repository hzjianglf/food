package com.sniper.springmvc.action.admin;

import java.io.InputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.freemarker.FreeMarkerUtil;
import com.sniper.springmvc.hibernate.service.impl.SurveyPageService;
import com.sniper.springmvc.hibernate.service.impl.SurveyQuestionOptionService;
import com.sniper.springmvc.hibernate.service.impl.SurveyQuestionServie;
import com.sniper.springmvc.hibernate.service.impl.SurveyResultDataService;
import com.sniper.springmvc.hibernate.service.impl.SurveyResultService;
import com.sniper.springmvc.hibernate.service.impl.SurveyService;
import com.sniper.springmvc.model.Survey;
import com.sniper.springmvc.model.SurveyPage;
import com.sniper.springmvc.model.SurveyQuestion;
import com.sniper.springmvc.model.SurveyQuestionOption;
import com.sniper.springmvc.model.SurveyResult;
import com.sniper.springmvc.model.SurveyResultData;
import com.sniper.springmvc.searchUtil.ChannelSearch;
import com.sniper.springmvc.utils.BaseHref;
import com.sniper.springmvc.utils.BeanUtils;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.HqlUtils;
import com.sniper.springmvc.utils.MapUtil;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.PropertiesUtil;
import com.sniper.springmvc.utils.SurveyTemplateUtil;
import com.sniper.springmvc.utils.SurveyUtils;
import com.sniper.springmvc.utils.ValidateUtil;

import freemarker.template.TemplateHashModel;

@Controller
@RequestMapping("/admin/admin-survey")
public class SurveyAction extends BaseAction {

	private static TemplateHashModel surveyModel = FreeMarkerUtil
			.getFreeMarkerStaticModel(SurveyTemplateUtil.class);

	/**
	 * 对问题类型进行排序
	 * 
	 * @author sniper
	 * 
	 */
	public class TypeSort implements Comparator<Map.Entry<String, String>> {

		@Override
		public int compare(Entry<String, String> arg0,
				Entry<String, String> arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	private static Map<String, String> surveyType = new LinkedHashMap<>();
	static {
		//
		InputStream in = SurveyAction.class.getClassLoader()
				.getResourceAsStream("properties/surveytypes.properties");
		PropertiesUtil util = new PropertiesUtil(in);
		surveyType = MapUtil.sortTreeMapByKey(util.getValues());

	}

	private static Map<String, String> listStyles = new HashMap<>();

	static {
		listStyles.put("none", "空");
		listStyles.put("decimal", "阿拉伯数字");
		listStyles.put("upper-alpha", "大写字母");
		listStyles.put("upper-roman", "大写罗马");
	}

	private Map<String, String[]> parameters = new HashMap<>();
	private Map<Integer, List<String>> resultErrors = new HashMap<>();

	public Map<Integer, List<String>> getResultErrors() {
		return resultErrors;
	}

	@Resource
	private SurveyService surveyService;

	@Resource
	private SurveyPageService pageService;

	@Resource
	private SurveyQuestionServie questionServie;

	@Resource
	private SurveyResultDataService resultDataService;

	@Resource
	private SurveyResultService resultService;

	@Resource
	private SurveyQuestionOptionService optionService;

	@ModelAttribute
	@Override
	public void init(Map<String, Object> map, BaseHref baseHref) {
		super.init(map, baseHref);
	}

	@RequestMapping("/")
	public String index(Map<String, Object> map, ChannelSearch search) {

		map.put("sniperUrl", "/admin-survey/delete");
		ParamsToHtml toHtml = new ParamsToHtml();

		Map<String, String> locked = new HashMap<>();
		locked.put("true", "是");
		locked.put("false", "否");

		toHtml.addMapValue("locked", locked);

		Map<String, String> copy = new HashMap<>();
		copy.put("copy", "复制");
		toHtml.addMapValue("copy", copy);

		Map<String, String> keys = new HashMap<>();
		keys.put("locked", "锁定");
		keys.put("copy", "复制");

		toHtml.setKeys(keys);

		HqlUtils hqlUtils = new HqlUtils();
		hqlUtils.setEntityName("Survey");
		hqlUtils.setOrder("id desc");

		Map<String, Object> params = new LinkedHashMap<>();

		if (ValidateUtil.isValid(search.getStatus())) {
			hqlUtils.setWhere("and locked = :locked");
			params.put("locked", search.getStatus());
		}

		if (ValidateUtil.isValid(search.getName())) {
			hqlUtils.setWhere("and name like :name");
			params.put("name", "%" + search.getName() + "%");
		}

		int count = surveyService.pageCountList(hqlUtils);
		PageUtil page = new PageUtil(count, 10);
		page.setRequest(getRequest());
		String pageHtml = page.show();

		List<Survey> lists = surveyService.pageList(hqlUtils,
				page.getFristRow(), page.getListRow());
		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		map.put("sniperMenu", toHtml);
		return "admin/admin-survey/index.jsp";
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "insert", method = RequestMethod.GET)
	public String insert(Map<String, Object> map) {

		map.put("survey", new Survey());
		map.put("listStyles", listStyles);
		return "admin/admin-survey/save-input.jsp";
	}

	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String insert(@Valid Survey survey, BindingResult result,
			Map<String, Object> map) {

		try {
			if (result.getErrorCount() > 0) {
				map.put("listStyles", listStyles);
				return "admin/admin-survey/save-input.jsp";
			} else {
				survey.setAdminUser(getAdminUser());
				surveyService.saveOrUpdateEntiry(survey);
			}
		} catch (Exception e) {

		}

		return "redirect:/admin/admin-survey/insert";
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.GET)
	public String update(
			@RequestParam(value = "id", required = false) Integer id,
			Survey survey, Map<String, Object> map) {

		if (ValidateUtil.isValid(id)) {
			survey = surveyService.getEntity(id);
		} else {
			return "redirect:/admin/admin-survey/insert";
		}
		map.put("listStyles", listStyles);
		map.put("survey", survey);
		return "admin/admin-survey/save-input.jsp";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid Survey survey, BindingResult result,
			Map<String, Object> map) {
		if (null == survey.getId()) {
			return "redirect:/admin/admin-survey/insert";
		}

		try {
			if (result.getErrorCount() > 0) {
				map.put("listStyles", listStyles);
				return "admin/admin-survey/save-input.jsp";
			} else {
				Survey channel2 = surveyService.getEntity(survey.getId());
				BeanUtils.copyProperties(survey, channel2);
				surveyService.saveOrUpdateEntiry(channel2);
			}
		} catch (Exception e) {

		}

		return "redirect:/admin/admin-survey/update?id=" + survey.getId();
	}

	@RequestMapping("survey")
	public String survey(@RequestParam("id") Integer id, Map<String, Object> map) {

		Survey survey = surveyService.getSurvey(id);
		if (survey == null) {
			return "redirect:/admin/admin-survey/";
		}
		map.put("survey", survey);

		ParamsToHtml toHtml = new ParamsToHtml();
		toHtml.setKeys(surveyType);
		map.put("toHtml", toHtml);
		return "admin/admin-survey/survey-input.ftl";
	}

	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public Map<String, Object> delete(@RequestParam("delid") Integer[] delid,
			@RequestParam("menuType") String menuType,
			@RequestParam("menuValue") String menuValue) {
		Map<String, Object> ajaxResult = new HashMap<>();
		switch (menuType) {
		case "delete":
			try {
				for (int i = 0; i < delid.length; i++) {
					Survey survey = surveyService.getSurvey(delid[i]);
					surveyService.deleteEntiry(survey);
				}
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");

			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", "删除失败");
			}
			break;
		case "locked":
			try {
				surveyService.batchFiledChange("locked",
						DataUtil.stringToBoolean(menuValue), delid);
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", e.getMessage());
			}
			break;
		case "copy":
			try {
				List<Survey> lists = surveyService.surveyCopy(delid);
				for (Survey survey : lists) {
					surveyService.saveEntiry(survey);
					for (SurveyPage page : survey.getSurveyPages()) {
						pageService.saveEntiry(page);
						for (SurveyQuestion question : page.getSq()) {
							questionServie.saveEntiry(question);
							for (SurveyQuestionOption option : question
									.getOptions()) {
								optionService.saveEntiry(option);
							}
						}
					}
				}
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", e.getMessage());
			}
			break;

		default:
			break;
		}

		return ajaxResult;
	}

	@RequestMapping(value = "answer", method = RequestMethod.GET)
	public String show(Map<String, Object> map,
			@RequestParam(value = "id") Integer id) {
		// 所有问卷的内容

		Survey model = surveyService.getSurvey(id);
		map.put("survey", model);
		return "admin/admin-survey/answer.ftl";
	}

	/**
	 * 提交问卷
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "answer", method = RequestMethod.POST)
	public String show(@RequestParam(value = "id") Integer id) {
		// 所有问卷的内容

		SurveyUtils surveyUtils = new SurveyUtils();
		surveyUtils.setParameters(parameters);
		surveyUtils.setRequest(getRequest());
		surveyUtils.setSurveyId(id);
		surveyUtils.setSurveyService(surveyService);
		surveyUtils.run();

		System.out.println(surveyUtils.getResults());
		System.out.println(surveyUtils.getResultData());
		System.out.println(surveyUtils.getErrors());

		resultErrors = surveyUtils.getErrors();
		// 数据保存
		SurveyResultData resultData = surveyUtils.getResultData();
		List<SurveyResult> results = surveyUtils.getResults();

		if (resultErrors.size() == 0) {
			resultDataService.saveEntiry(resultData);
			for (SurveyResult surveyResult : results) {
				resultService.saveEntiry(surveyResult);
			}
		}

		// Survey model = surveyService.getSurvey(id);
		// map.put("survey", model);
		return "admin/admin-survey/answer.ftl";
	}

}
