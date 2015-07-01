package com.sniper.springmvc.action.admin;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.hibernate.service.impl.TagsService;
import com.sniper.springmvc.model.Tags;
import com.sniper.springmvc.searchUtil.ChannelSearch;
import com.sniper.springmvc.utils.BaseHref;
import com.sniper.springmvc.utils.BeanUtils;
import com.sniper.springmvc.utils.HqlUtils;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
@RequestMapping("/admin/admin-tags")
public class AdminTagAction extends BaseAction {

	@Resource
	private TagsService tagsService;

	@ModelAttribute
	public void init(Map<String, Object> map, BaseHref baseHref) {
		super.init(map, baseHref);
		ParamsToHtml html = new ParamsToHtml();

		map.put("html", html);
	}

	@RequestMapping("/")
	public String index(Map<String, Object> map, ChannelSearch channelSearch)
			throws FileNotFoundException {

		map.put("sniperUrl", "/admin-tags/delete");

		ParamsToHtml toHtml = new ParamsToHtml();

		Map<String, Object> params = new LinkedHashMap<>();

		HqlUtils hqlUtils = new HqlUtils();
		hqlUtils.setEntityName("Tags");
		hqlUtils.setOrder("id desc");

		if (ValidateUtil.isValid(channelSearch.getName())) {
			hqlUtils.setWhere("and name like :name");
			params.put("name", "%" + channelSearch.getName() + "%");
		}

		hqlUtils.setParams(params);
		int count = tagsService.pageCountList(hqlUtils);
		PageUtil page = new PageUtil(count, 50);
		page.setRequest(request);
		String pageHtml = page.show();

		List<Tags> lists = tagsService.pageList(hqlUtils, page.getFristRow(),
				page.getListRow());
		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		map.put("toHtml", toHtml);
		return "admin/admin-tags/index.jsp";
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

		map.put("tags", new Tags());
		return "admin/admin-tags/save-input.jsp";
	}

	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String insert(@Valid Tags tags, BindingResult result) {

		try {
			if (result.getErrorCount() > 0) {
				return "admin/admin-tags/save-input.jsp";
			} else {

				tagsService.saveOrUpdateEntiry(tags);
			}
		} catch (Exception e) {

		}

		return "redirect:/admin/admin-tags/insert";
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
			Map<String, Object> map) {

		Tags tags = null;
		if (ValidateUtil.isValid(id)) {
			tags = tagsService.getEntity(id);
		} else {
			return "redirect:/admin/admin-tags/insert";
		}

		map.put("tags", tags);
		return "admin/admin-tags/save-input.jsp";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid Tags tags, BindingResult result) {
		if (null == tags.getId()) {
			return "reditct:/admin/admin-tags/insert";
		}

		try {
			if (result.getErrorCount() > 0) {
				return "admin/admin-tags/save-input.jsp";
			} else {

				Tags tags2 = tagsService.getEntity(tags.getId());
				BeanUtils.copyProperties(tags, tags2);
				tagsService.saveOrUpdateEntiry(tags2);
			}
		} catch (Exception e) {

		}

		return "redirect:/admin/admin-tags/update?id=" + tags.getId();
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
				tagsService.deleteEntirys(delid);
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", "删除失败");
			}

			break;

		default:
			break;
		}

		return ajaxResult;
	}

}
