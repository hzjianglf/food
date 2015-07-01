package com.sniper.springmvc.action.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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
import com.sniper.springmvc.hibernate.service.impl.PptFileService;
import com.sniper.springmvc.model.Channel;
import com.sniper.springmvc.model.Files;
import com.sniper.springmvc.utils.BeanUtils;
import com.sniper.springmvc.utils.HqlUtils;
import com.sniper.springmvc.utils.HttpRequestUtils;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
@RequestMapping("/admin/admin-images")
public class AdminImagesAction extends BaseAction {
	@Resource
	private PptFileService pptFileService;

	@Resource
	private ChannelService channelService;

	@Resource
	private FilesService filesService;

	/**
	 * ztree数据
	 * 
	 * @return
	 */
	public String getTreePostMap() {

		String tree = channelService.getChannelListByTypeForTree(
				new Integer[] { 4 }, true, "sort asc, id asc",
				",open:true,nocheck:true");
		return tree;
	}

	@RequestMapping("/")
	public String index(Map<String, Object> map,
			@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "id", required = false) Integer id) {

		map.put("key", key);
		map.put("sniperUrl", "/admin-images/delete");

		ParamsToHtml toHtml = new ParamsToHtml();

		HqlUtils hqlUtils = new HqlUtils();
		hqlUtils.setEntityName("Files");
		String ean = hqlUtils.getEntityAsName();

		Map<String, Object> params = new HashMap<>();

		if (ValidateUtil.isValid(key)) {
			hqlUtils.startBracket();
			hqlUtils.where(ean + ".oldName like :name");
			hqlUtils.orWhere(ean + ".tags like :name");
			hqlUtils.endBracket();
			params.put("name", "%" + key + "%");
		}

		if (ValidateUtil.isValid(id)) {
			hqlUtils.setWhere("and  " + "channel.id = :cid");
			params.put("cid", id);
		}
		// 图片限制
		hqlUtils.andWhere(ean + ".album  = true");
		hqlUtils.setOrder(ean + ".id desc");

		hqlUtils.setParams(params);

		int count = filesService.pageCountList(hqlUtils);
		PageUtil page = new PageUtil(count, 50);
		page.setRequest(request);
		String pageHtml = page.show();
		List<Files> lists = filesService.pageList(hqlUtils, page.getFristRow(),
				page.getListRow());
		map.put("pageHtml", pageHtml);
		map.put("lists", lists);

		map.put("sniperMenu", toHtml);
		map.put("imageHelpUtil", imageHelpUtil);

		return "admin/admin-images/index.ftl";
	}

	/**
	 * 图片上传
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "upload", method = RequestMethod.GET)
	public String upload(Map<String, Object> map) {
		
		String firefox = "";
		String agent = (String) getRequest().getHeader("User-Agent");
		if (HttpRequestUtils.isFirefox(agent)) {
			firefox = ";jsessionid=" + request.getSession(false).getId();
		}
		map.put("firefox", firefox);
		return "admin/admin-images/save-upload.jsp";
	}

	@RequestMapping("show")
	public String show(Map<String, Object> map) {
		return "admin/admin-images/save-show.jsp";
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

		map.put("files", new Files());
		return "admin/admin-images/save-input.jsp";
	}

	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String insert(@Valid Files files, BindingResult result,
			@RequestParam("filesId") String filesPost, Map<String, Object> map) {

		try {

			if (result.getErrorCount() > 0) {
				map.put("files", files);
				// 设置选中的栏目
				map.put("treePostMap", getTreePostMap());
				return "admin/admin-images/save-input.jsp";
			} else {

				filesService.saveOrUpdateEntiry(files);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "redirect:/admin/admin-images/insert";
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "update", method = RequestMethod.GET)
	public String update(
			@RequestParam(value = "id", required = false) Integer id,
			Map<String, Object> map, Files files) throws IOException {

		if (ValidateUtil.isValid(id)) {
			files = filesService.getEntity(id);
			map.put("treePostMap", getTreePostMap());
		} else {
			return "redirect:/admin/admin-images/upload";
		}

		map.put("files", files);
		return "admin/admin-images/save-input.jsp";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid Files files, BindingResult result,
			@RequestParam("filesId") String filesPost, Map<String, Object> map) {
		if (null == files.getId()) {
			return "reditct:/admin/admin-images/insert";
		}

		try {
			if (result.getErrorCount() > 0) {

				files = filesService.getEntity(files.getId());
				map.put("treePostMap", getTreePostMap());

				return "admin/admin-images/save-input.jsp";
			} else {
				Files post2 = filesService.getEntity(files.getId());
				BeanUtils.copyProperties(files, post2);
				filesService.saveOrUpdateEntiry(post2);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

		return "redirect:/admin/admin-images/";
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
				filesService.deleteEntirys(delid);
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

	/**
	 * 图片频道拆分出来
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("online")
	public String online(Map<String, Object> map) {

		List<String> nodes = new ArrayList<>();
		List<Channel> channels = channelService
				.getChannelListByType(new Integer[] { 4 });

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

	public static void main(String[] args) {
		String agent = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:38.0) Gecko/20100101 /38.0";
		System.out.println(HttpRequestUtils.isFirefox(agent));
	}

}
