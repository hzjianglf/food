package com.sniper.springmvc.action.admin;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.sniper.springmvc.hibernate.service.impl.ChannelService;
import com.sniper.springmvc.hibernate.service.impl.LinksService;
import com.sniper.springmvc.model.Channel;
import com.sniper.springmvc.model.Links;
import com.sniper.springmvc.searchUtil.ChannelSearch;
import com.sniper.springmvc.utils.BaseHref;
import com.sniper.springmvc.utils.BeanUtils;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.HqlUtils;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
@RequestMapping("/admin/admin-links")
public class AdminLinksAction extends BaseAction {

	@Resource
	ChannelService channelService;

	@Resource
	LinksService linksService;

	/**
	 * 获取友情链接组
	 * 
	 * @return
	 */
	public List<Channel> getChannels() {
		List<Channel> channels = new ArrayList<>();
		if (channels.size() == 0) {
			channels = channelService.getChannelListByType(new Integer[] { 1 },
					true);
		}
		return channels;
	}

	private Map<String, String> channels() {
		Map<String, String> channels = channelService.getMapChannels(
				new Integer[] { 1 }, "sort asc");

		return channels;

	}

	@ModelAttribute
	@Override
	public void init(Map<String, Object> map, BaseHref baseHref) {
		super.init(map, baseHref);
		map.put("channels", getChannels());
	}

	@RequestMapping("/")
	public String index(Map<String, Object> map, ChannelSearch search) {

		map.put("search", search);
		map.put("sniperUrl", "/admin-links/delete");

		ParamsToHtml toHtml = new ParamsToHtml();

		toHtml.addMapValue("enabled", ChannelSearch.getStatusvalues());
		toHtml.addMapValue("channel", channels());

		Map<String, String> keys = new HashMap<>();
		keys.put("enabled", "审核");
		keys.put("channel", "更改频道");
		toHtml.setKeys(keys);

		HqlUtils hqlUtils = new HqlUtils("Links");
		String ean = hqlUtils.getEntityAsName();

		if (ValidateUtil.isValid(search.getName())) {
			hqlUtils.setWhere("and  " + ean + ".name  like '%"
					+ search.getName() + "%'");
		}
		if (ValidateUtil.isValid(search.getStatus())) {
			hqlUtils.setWhere("and  " + ean + ".enabled  ="
					+ search.getStatus());
		}
		if (ValidateUtil.isValid(search.getType())) {
			hqlUtils.setWhere("and  " + "channel.id =" + search.getType());
		}

		hqlUtils.setOrder("id desc");
		int count = linksService.pageCountList(hqlUtils);
		PageUtil page = new PageUtil(count, 20);
		page.setRequest(request);
		String pageHtml = page.show();
		List<Links> lists = linksService.pageList(hqlUtils, page.getFristRow(),
				page.getListRow());

		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		map.put("sniperMenu", toHtml);

		return "admin/admin-links/index.jsp";
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

		map.put("links", new Links());
		return "admin/admin-links/save-input.jsp";
	}

	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String insert(@Valid Links links, BindingResult result) {

		try {
			if (result.getErrorCount() > 0) {
				return "admin/admin-links/save-input.jsp";
			} else {
				linksService.saveOrUpdateEntiry(links);
			}
		} catch (Exception e) {

		}

		return "redirect:/admin/admin-links/insert";
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
			Links links, Map<String, Object> map) {

		if (ValidateUtil.isValid(id)) {
			links = linksService.getEntity(id);
		} else {
			return "redirect:/admin/admin-links/insert";
		}

		map.put("links", links);
		return "admin/admin-links/save-input.jsp";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid Links links, BindingResult result) {
		if (null == links.getId()) {
			return "reditct:/admin/admin-links/insert";
		}

		try {
			if (result.getErrorCount() > 0) {
				return "admin/admin-links/save-input.jsp";
			} else {

				Links links2 = linksService.getEntity(links.getId());
				BeanUtils.copyProperties(links, links2);
				linksService.saveOrUpdateEntiry(links2);
			}
		} catch (Exception e) {

		}

		return "redirect:/admin/admin-links/update?id=" + links.getId();
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public Map<String, Object> delete(@RequestParam("delid") Integer[] delid,
			@RequestParam("menuType") String menuType,
			@RequestParam("menuValue") String menuValue) {

		// code 小于1表示有错误,大于0表示ok,==0表示未操作
		Map<String, Object> ajaxResult = new HashMap<>();
		switch (menuType) {
		case "delete":

			try {
				linksService.deleteEntirys(delid);
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", "删除失败");
			}

			break;
		case "enabled":

			try {
				linksService.batchFiledChange("enabled",
						DataUtil.stringToBoolean(menuValue), delid);
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

}
