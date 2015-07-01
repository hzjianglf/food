package com.sniper.springmvc.action.admin;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.hibernate.service.impl.ChannelService;
import com.sniper.springmvc.model.Channel;
import com.sniper.springmvc.searchUtil.ChannelSearch;
import com.sniper.springmvc.utils.BaseHref;
import com.sniper.springmvc.utils.BeanUtils;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.HqlUtils;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.PropertiesUtil;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
@RequestMapping("/admin/admin-channel")
public class AdminChannelAction extends BaseAction {

	@Resource
	private ChannelService channelService;

	public String getChannelMap() {

		List<String> nodes = new ArrayList<>();
		List<Channel> channels = channelService.findAllEntitles();
		for (Channel c : channels) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("{id:");
			buffer.append(c.getId());
			buffer.append(",pId:");
			buffer.append(c.getFid());
			buffer.append(",name:");
			buffer.append("\"");
			buffer.append(c.getName());
			buffer.append("\"");
			buffer.append(",title:\"");
			buffer.append(c.getName().trim());
			buffer.append("\"");
			buffer.append(",target:\"_blank\"");
			buffer.append("}");

			nodes.add(buffer.toString());
		}

		return StringUtils.join(nodes, ",");
	}

	private static Map<String, String> channelType = new HashMap<>();

	public static Map<String, String> getChannelType()
			throws FileNotFoundException {
		if (channelType.isEmpty()) {

			InputStream in = AdminChannelAction.class.getClassLoader()
					.getResourceAsStream("properties/channelType.properties");

			PropertiesUtil propertiesUtil = new PropertiesUtil(in);
			channelType = propertiesUtil.getValues();

		}
		return channelType;
	}

	@ModelAttribute
	public void init(Map<String, Object> map, BaseHref baseHref) {
		super.init(map, baseHref);
		ParamsToHtml html = new ParamsToHtml();
		html.setKey(getChannelMap());
		try {
			html.setKeys(getChannelType());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		map.put("sniperMenu", html);
	}

	@RequestMapping("/")
	public String index(Map<String, Object> map, ChannelSearch channelSearch)
			throws FileNotFoundException {

		map.put("sniperUrl", "/admin-channel/delete");

		ParamsToHtml toHtml = new ParamsToHtml();

		Map<String, String> status = new HashMap<>();
		status.put("true", "启用");
		status.put("false", "禁止");
		toHtml.addMapValue("status", status);
		toHtml.addMapValue("type", getChannelType());

		Map<String, String> keys = new HashMap<>();
		keys.put("status", "变更状态");
		keys.put("type", "更改类型");

		toHtml.setKeys(keys);

		Map<String, Object> params = new LinkedHashMap<>();

		HqlUtils hqlUtils = new HqlUtils();
		hqlUtils.setEntityName("Channel");
		hqlUtils.setOrder("sort desc,id desc");
		if (ValidateUtil.isValid(channelSearch.getType())) {
			hqlUtils.setWhere("and showType = :type");
			params.put("type", channelSearch.getType());
		}

		if (ValidateUtil.isValid(channelSearch.getStatus())) {
			hqlUtils.setWhere("and status = :status");
			params.put("status", channelSearch.getStatus());
		}

		if (ValidateUtil.isValid(channelSearch.getName())) {
			hqlUtils.setWhere("and name like :name");
			params.put("name", "%" + channelSearch.getName() + "%");
		}

		if (ValidateUtil.isValid(channelSearch.getFid())) {
			hqlUtils.setWhere("and fid = :fid");
			params.put("fid", channelSearch.getFid());
		}

		hqlUtils.setParams(params);
		int count = channelService.pageCountList(hqlUtils);
		PageUtil page = new PageUtil(count, 50);
		page.setRequest(request);
		String pageHtml = page.show();

		List<Channel> lists = channelService.pageList(hqlUtils,
				page.getFristRow(), page.getListRow());
		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		map.put("sniperMenu", toHtml);
		return "admin/admin-channel/index.jsp";
	}

	@RequestMapping("online")
	public String online(Map<String, Object> map) {

		List<String> nodes = new ArrayList<>();
		List<Channel> channels = channelService.findAllEntitles();

		Set<Integer> fids = new HashSet<>();
		for (Channel channel : channels) {
			if (channel.getFid() != 0) {
				fids.add(channel.getFid());
			}
		}

		for (Channel c : channels) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("{id:");
			buffer.append(c.getId());
			buffer.append(",pId:");
			buffer.append(c.getFid());
			buffer.append(",name:");
			buffer.append("\"");
			buffer.append(c.getName());
			buffer.append("\"");
			buffer.append(",title:\"");
			buffer.append(c.getName().trim());
			buffer.append("\"");
			buffer.append(",target:\"_blank\"");
			buffer.append(",url:\"admin/admin-channel/update?id=" + c.getId()
					+ "\"");
			buffer.append(",type:\"" + c.getShowType() + "\"");

			Integer[] fidss = fids.toArray(new Integer[fids.size()]);
			if (Arrays.binarySearch(fidss, c.getId()) > -1) {
				buffer.append(",isParent:true");
			}

			buffer.append("}");

			nodes.add(buffer.toString());
		}

		String buffer = StringUtils.join(nodes, ",\r");

		map.put("treeMap", buffer);

		return "admin/admin-channel/olUpdate.ftl";
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

		map.put("channel", new Channel());
		return "admin/admin-channel/save-input.jsp";
	}

	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String insert(@Valid Channel channel, BindingResult result) {

		try {
			if (result.getErrorCount() > 0) {
				return "admin/admin-channel/save-input.jsp";
			} else {
				channel.setUid(getAdminUser().getId());
				channelService.saveOrUpdateEntiry(channel);
			}
		} catch (Exception e) {

		}

		return "redirect:/admin/admin-channel/insert";
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
			Channel channel, Map<String, Object> map) {

		if (ValidateUtil.isValid(id)) {
			channel = channelService.getEntity(id);
		} else {
			return "redirect:/admin/admin-channel/insert";
		}

		map.put("channel", channel);
		return "admin/admin-channel/save-input.jsp";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid Channel channel, BindingResult result) {
		if (null == channel.getId()) {
			return "reditct:/admin/admin-channel/insert";
		}

		try {
			if (result.getErrorCount() > 0) {
				return "admin/admin-channel/save-input.jsp";
			} else {

				Channel channel2 = channelService.getEntity(channel.getId());
				BeanUtils.copyProperties(channel, channel2);
				channelService.saveOrUpdateEntiry(channel2);
			}
		} catch (Exception e) {

		}

		return "redirect:/admin/admin-channel/update?id=" + channel.getId();
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
				channelService.deleteEntirys(delid);
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", "删除失败");
			}

			break;
		case "status":
			try {
				channelService.batchFiledChange("status",
						DataUtil.stringToBoolean(menuValue), delid);
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", e.getMessage());
			}
			break;
		case "type":
			try {
				channelService.batchFiledChange("showType",
						DataUtil.stringToInteger(menuValue), delid);
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

	/**
	 * 在线管理
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("olAdd")
	public Map<String, Object> olAdd(Channel channel) {

		Map<String, Object> result = new HashMap<>();

		try {
			channel.setStatus(true);
			channel.setUid(getAdminUser().getId());
			channelService.saveOrUpdateEntiry(channel);
			// 跟新排序
			channel.setSort(channel.getId() * 10);
			channelService.saveOrUpdateEntiry(channel);
			result.put("code", "200");
			result.put("msg", "添加成功");
			result.put("channel", channel);
		} catch (Exception e) {
			result.put("code", "500");
			result.put("msg", "添加失败");
		}

		return result;

	}

	@ResponseBody
	@RequestMapping("olUpdate")
	public Map<String, String> olUpdate(Channel channel) {

		Map<String, String> result = new HashMap<>();

		try {
			Channel channel2 = channelService.getEntity(channel.getId());
			channel2.setName(channel.getName());
			channelService.saveOrUpdateEntiry(channel2);
			result.put("code", "200");
			result.put("msg", "修改成功");
		} catch (Exception e) {
			result.put("code", "500");
			result.put("msg", "修改失败");
		}

		return result;

	}

	@ResponseBody
	@RequestMapping("olDelete")
	public Map<String, String> olDelete(Channel channel) {

		Map<String, String> result = new HashMap<>();
		try {
			Channel channel2 = channelService.getEntity(channel.getId());
			channelService.deleteEntiry(channel2);
			result.put("code", "200");
			result.put("msg", "删除成功");
		} catch (Exception e) {
			result.put("code", "500");
			result.put("msg", "删除失败");
		}
		return result;

	}

}
