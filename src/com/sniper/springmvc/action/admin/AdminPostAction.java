package com.sniper.springmvc.action.admin;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.hibernate.service.impl.ChannelService;
import com.sniper.springmvc.hibernate.service.impl.FilesService;
import com.sniper.springmvc.hibernate.service.impl.PostService;
import com.sniper.springmvc.model.Channel;
import com.sniper.springmvc.model.Files;
import com.sniper.springmvc.model.Post;
import com.sniper.springmvc.searchUtil.ChannelSearch;
import com.sniper.springmvc.utils.BeanUtils;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.HqlUtils;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.PropertiesUtil;
import com.sniper.springmvc.utils.ValidateUtil;

@RequestMapping("/admin/admin-post")
@Controller
public class AdminPostAction extends BaseAction {

	@Resource
	private PostService postService;

	@Resource
	private ChannelService channelService;

	@Resource
	private FilesService filesService;

	/**
	 * 文章栏目选择
	 */
	private Map<String, String> postChannels = new HashMap<>();
	// 状态列表
	private Map<String, String> statusList = new HashMap<>();

	// 栏目提交之后得到的结果
	// 文件关系

	public Map<String, String> getPostChannels() {

		if (postChannels.isEmpty()) {
			List<Channel> channels = channelService.getChannelListByType(
					new Integer[] { 0 }, true);
			for (Channel c : channels) {
				postChannels.put(String.valueOf(c.getId()), c.getName());
			}
		}

		return postChannels;
	}

	/**
	 * ztree数据
	 * 
	 * @return
	 */
	public String getTreePostMap() {

		String tree = channelService.getChannelListByTypeForTree(
				new Integer[] { 0 }, true, "sort desc, id desc",
				",open:true,nocheck:true");
		return tree;
	}

	public Map<String, String> getStatusList() {
		if (statusList.isEmpty()) {
			InputStream in = AdminPostAction.class.getClassLoader()
					.getResourceAsStream("properties/postStatus.properties");
			PropertiesUtil propertiesUtil = new PropertiesUtil(in);
			statusList = propertiesUtil.getValues();
		}
		return statusList;
	}

	@RequestMapping("/")
	public String index(Map<String, Object> map, ChannelSearch search) {

		map.put("search", search);
		map.put("sniperUrl", "/admin-post/delete");

		ParamsToHtml toHtml = new ParamsToHtml();
		toHtml.addMapValue("status", getStatusList());

		Map<String, String> keys = new HashMap<>();
		keys.put("status", "审核");

		toHtml.setKeys(keys);

		HqlUtils hqlUtils = new HqlUtils();
		hqlUtils.setEntityName("Post");
		String ean = hqlUtils.getEntityAsName();

		if (ValidateUtil.isValid(search.getName())) {
			hqlUtils.setWhere("and  " + ean + ".name  like '%"
					+ search.getName() + "%'");
		}
		if (ValidateUtil.isValid(search.getStatus())) {
			hqlUtils.setWhere("and  " + ean + ".status  =" + search.getStatus());
		}
		if (ValidateUtil.isValid(search.getType())) {
			hqlUtils.setWhere("and  " + "c.id =" + search.getType());
		}
		hqlUtils.setJoin("LEFT JOIN " + ean + ".channels as c");
		hqlUtils.setDistinct(true);
		hqlUtils.setOrder(ean + ".id desc");

		int count = postService.pageCountList(hqlUtils);
		PageUtil page = new PageUtil(count, 20);
		page.setRequest(request);
		String pageHtml = page.show();
		List<Post> lists = postService.pageList(hqlUtils, page.getFristRow(),
				page.getListRow());
		map.put("pageHtml", pageHtml);
		map.put("lists", lists);

		map.put("sniperMenu", toHtml);
		return "admin/admin-post/index.jsp";
	}

	private void setMap(Map<String, Object> map) {
		Map<String, String> targets = new HashMap<>();
		targets.put("_self", "当前页");
		targets.put("_blank", "新页");
		map.put("targets", targets);
		map.put("statusList", getStatusList());
		map.put("treePostMap", getTreePostMap());
	}

	/**
	 * 设置文章的栏目
	 * 
	 * @param map
	 * @param post
	 */
	private void setPostChannel(Map<String, Object> map, Post post) {
		List<String> nodes = new ArrayList<>();
		for (Channel c : post.getChannels()) {
			nodes.add(String.valueOf(c.getId()));
		}
		String channelPost = StringUtils.join(nodes, ",");
		map.put("postChannel", channelPost);
	}

	private void setPostChannel(Map<String, Object> map, Set<Channel> channels) {
		List<String> nodes = new ArrayList<>();
		for (Channel c : channels) {
			nodes.add(String.valueOf(c.getId()));
		}
		String channelPost = StringUtils.join(nodes, ",");

		map.put("postChannel", channelPost);
	}

	/**
	 * 设置已经有的文件
	 * 
	 * @param map
	 * @param post
	 */
	private void setPostFiles(Map<String, Object> map, Post post) {
		List<String> nodes = new ArrayList<>();
		for (Files c : post.getFiles()) {
			nodes.add(String.valueOf(c.getId()));
		}
		String channelPost = StringUtils.join(nodes, ",");
		map.put("postFiles", channelPost);
	}

	private void setPostFiles(Map<String, Object> map, Set<Files> files) {
		List<String> nodes = new ArrayList<>();
		for (Files c : files) {
			nodes.add(String.valueOf(c.getId()));
		}
		String channelPost = StringUtils.join(nodes, ",");
		map.put("postFiles", channelPost);
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

		setMap(map);
		map.put("post", new Post());
		return "admin/admin-post/save-input.jsp";
	}

	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String insert(@Valid Post post, BindingResult result,
			@RequestParam("postFiles") Set<Files> filesPost,
			@RequestParam("postChannel") Set<Channel> channels,
			Map<String, Object> map) {

		try {
			if (result.getErrorCount() > 0) {
				setMap(map);
				// 设置选中的栏目
				setPostChannel(map, channels);
				setPostFiles(map, filesPost);
				return "admin/admin-post/save-input.jsp";
			} else {

				post.setFiles(filesPost);
				post.setChannels(channels);
				post.setAdminUser(getAdminUser());
				postService.saveOrUpdateEntiry(post);
			}
		} catch (Exception e) {
			return "admin/admin-post/save-input.jsp";
		}

		return "redirect:/admin/admin-post/insert";
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
			Map<String, Object> map, Post post) {

		if (ValidateUtil.isValid(id)) {
			post = postService.getAllEntity(id);
		} else {
			return "redirect:/admin/admin-post/insert";
		}

		setMap(map);
		map.put("post", post);

		// 设置选中的栏目
		setPostChannel(map, post);
		setPostFiles(map, post);

		return "admin/admin-post/save-input.jsp";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid Post post, BindingResult result,
			@RequestParam("postFiles") Set<Files> filesPost,
			@RequestParam("postChannel") Set<Channel> channels,
			Map<String, Object> map) {
		if (null == post.getId()) {
			return "reditct:/admin/admin-right/insert";
		}

		try {
			if (result.getErrorCount() > 0) {
				setMap(map);
				setPostChannel(map, post);
				setPostFiles(map, post);
				map.put("post", post);
				return "admin/admin-right/save-input.jsp";
			} else {

				post.setChannels(channels);
				post.setFiles(filesPost);
				Post post2 = postService.getEntity(post.getId());
				BeanUtils.copyProperties(post, post2);
				postService.saveOrUpdateEntiry(post2);
			}
		} catch (Exception e) {

		}

		return "redirect:/admin/admin-post/update?id=" + post.getId();
	}

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
				postService.deleteEntirys(delid);
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", "删除失败");
			}

			break;

		case "status":
			try {
				postService.batchFiledChange("status",
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

}