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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sniper.springmvc.hibernate.service.impl.ChannelService;
import com.sniper.springmvc.hibernate.service.impl.FilesService;
import com.sniper.springmvc.hibernate.service.impl.PptFileService;
import com.sniper.springmvc.model.Channel;
import com.sniper.springmvc.model.Files;
import com.sniper.springmvc.model.PptFile;
import com.sniper.springmvc.searchUtil.ChannelSearch;
import com.sniper.springmvc.utils.BeanUtils;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.HqlUtils;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
@RequestMapping("/admin/admin-ppt")
public class AdminPptAction extends BaseAction {

	@Resource
	private PptFileService pptFileService;

	@Resource
	private ChannelService channelService;

	@Resource
	private FilesService filesService;

	private Map<String, String> channels() {
		Map<String, String> channels = channelService.getMapChannels(
				new Integer[] { 2 }, "sort asc");

		return channels;

	}

	/**
	 * ztree数据
	 * 
	 * @return
	 */
	public String getTreePostMap() {

		String tree = channelService.getChannelListByTypeForTree(
				new Integer[] { 2 }, true, "sort asc", ",nocheck:true");
		return tree;
	}

	@RequestMapping("/")
	public String index(Map<String, Object> map, ChannelSearch search) {

		map.put("search", search);
		map.put("sniperUrl", "/admin-ppt/delete");

		ParamsToHtml toHtml = new ParamsToHtml();

		toHtml.addMapValue("enabled", ChannelSearch.getStatusvalues());
		toHtml.addMapValue("channel", channels());

		Map<String, String> keys = new HashMap<>();
		keys.put("enabled", "审核");
		keys.put("channel", "更改频道");

		toHtml.setKeys(keys);

		HqlUtils hqlUtils = new HqlUtils();
		hqlUtils.setEntityName("PptFile");
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
		hqlUtils.setDistinct(true);
		hqlUtils.setOrder(ean + ".id desc");

		int count = pptFileService.pageCountList(hqlUtils);
		PageUtil page = new PageUtil(count, 20);
		page.setRequest(request);
		String pageHtml = page.show();
		List<PptFile> lists = pptFileService.pageList(hqlUtils,
				page.getFristRow(), page.getListRow());
		map.put("pageHtml", pageHtml);
		map.put("lists", lists);

		map.put("sniperMenu", toHtml);
		return "admin/admin-ppt/index.jsp";
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
		List<Channel> channels = channelService.getChannelListByType(
				new Integer[] { 2 }, "sort asc");

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

		PptFile pptFile = new PptFile();
		pptFile.setEnabled(true);
		map.put("pptFile", pptFile);
		map.put("treePostMap", getTreePostMap());
		map.put("jsessionid", request.getSession().getId());
		return "admin/admin-ppt/save-input.jsp";
	}

	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String insert(@Valid PptFile pptFile, BindingResult result,
			@RequestParam("filesId") String filesPost, Map<String, Object> map) {

		try {

			if (result.getErrorCount() > 0) {
				map.put("pptFile", pptFile);
				// 设置选中的栏目
				map.put("treePostMap", getTreePostMap());
				map.put("jsessionid", request.getSession().getId());
				return "admin/admin-ppt/save-input.jsp";
			} else {

				pptFile.setFiles(convert(filesPost));
				pptFile.setAdminUser(getAdminUser());
				pptFileService.getCurrentSession().clear();
				pptFileService.saveOrUpdateEntiry(pptFile);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "redirect:/admin/admin-ppt/insert";
	}

	private Set<Files> convert(String source) {

		Set<Files> adminRights = new HashSet<>();

		if (ValidateUtil.isValid(source)) {
			String[] sIDs = source.split(",");
			for (int i = 0; i < sIDs.length; i++) {

				Files adminRight = filesService.getEntity(Integer
						.parseInt(sIDs[i]));
				adminRights.add(adminRight);
			}
		}

		return adminRights;
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
			Map<String, Object> map, PptFile pptFile) throws IOException {

		if (ValidateUtil.isValid(id)) {
			pptFile = pptFileService.getEntity(id);
			map.put("treePostMap", getTreePostMap());
			SetPptFiles(map, pptFile);
			map.put("jsessionid", request.getSession().getId());

		} else {
			return "redirect:/admin/admin-ppt/insert";
		}

		map.put("pptFile", pptFile);
		return "admin/admin-ppt/save-input.jsp";
	}

	/**
	 * 设置文件
	 * 
	 * @param map
	 * @param post
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	private void SetPptFiles(Map<String, Object> map, PptFile post)
			throws JsonProcessingException, IOException {
		Set<Files> files = post.getFiles();

		List<Map<String, Object>> fileValues = new ArrayList<>();
		for (Files files2 : files) {
			Map<String, Object> fileValue = new HashMap<>();
			fileValue.put("url", files2.getNewPath());
			fileValue.put("filePath", files2.getNewPath());
			fileValue.put("fileType", files2.getFileType());
			fileValue.put("id", files2.getId());
			fileValue.put("oldName", files2.getOldName());
			fileValues.add(fileValue);
		}

		ObjectMapper json = new ObjectMapper();
		String tempfileValue = json.writeValueAsString(fileValues);
		JsonNode jsonNode = json.readTree(tempfileValue);
		map.put("tempfileValue", jsonNode);

		Files ppt = post.getPpt();

		Map<String, Object> fileValuePpt = new HashMap<>();
		fileValuePpt.put("url", ppt.getNewPath());
		fileValuePpt.put("filePath", ppt.getNewPath());
		fileValuePpt.put("fileType", ppt.getFileType());
		fileValuePpt.put("id", ppt.getId());
		fileValuePpt.put("oldName", ppt.getOldName());

		String tempfileValuePpt = json.writeValueAsString(fileValuePpt);
		JsonNode jsonNodePpt = json.readTree(tempfileValuePpt);
		map.put("tempfileValuePpt", jsonNodePpt);

	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid PptFile pptFile, BindingResult result,
			@RequestParam("filesId") String filesPost, Map<String, Object> map) {
		if (null == pptFile.getId()) {
			return "reditct:/admin/admin-ppt/insert";
		}

		try {
			if (result.getErrorCount() > 0) {

				pptFile = pptFileService.getEntity(pptFile.getId());
				pptFile.setFiles(convert(filesPost));
				map.put("treePostMap", getTreePostMap());
				SetPptFiles(map, pptFile);
				map.put("jsessionid", request.getSession().getId());

				return "admin/admin-ppt/save-input.jsp";
			} else {
				pptFile.setFiles(convert(filesPost));
				PptFile post2 = pptFileService.getEntity(pptFile.getId());
				BeanUtils.copyProperties(pptFile, post2);
				pptFileService.saveOrUpdateEntiry(post2);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

		return "redirect:/admin/admin-ppt/";
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
				pptFileService.deleteEntirys(delid);
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", "删除失败");
			}

			break;

		case "enabled":
			try {
				int result = pptFileService.batchFiledChange("enabled",
						DataUtil.stringToBoolean(menuValue), delid);
				if (result > 0) {
					ajaxResult.put("code", 1);
					ajaxResult.put("msg", messageSource.getMessage(
							"action.success", null, locale));
				} else {
					ajaxResult.put("code", 1);
					ajaxResult.put("msg", "Failed");
				}

			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", e.getMessage());
			}
			break;

		case "channel":
			try {
				Channel channel = new Channel();
				channel.setId(Integer.valueOf(menuValue));
				for (int i = 0; i < delid.length; i++) {
					PptFile file = pptFileService.getEntity(delid[i]);
					file.setChannel(channel);
					pptFileService.saveOrUpdateEntiry(file);
				}

				ajaxResult.put("code", 1);
				ajaxResult.put("msg", messageSource.getMessage(
						"action.success", null, locale));

			} catch (Exception e) {
				e.printStackTrace();
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
