package com.sniper.springmvc.action.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sniper.springmvc.action.home.HomeBaseAction;
import com.sniper.springmvc.hibernate.service.impl.FilesService;
import com.sniper.springmvc.model.Files;
import com.sniper.springmvc.model.PptFile;
import com.sniper.springmvc.utils.BaseHref;
import com.sniper.springmvc.utils.BeanUtils;
import com.sniper.springmvc.utils.HqlUtils;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
@RequestMapping("/user/ppts")
public class UserPptAction extends HomeBaseAction {

	@Resource
	private FilesService filesService;

	@ModelAttribute
	@Override
	public void init(Map<String, Object> map, BaseHref baseHref) {
		super.init(map, baseHref);
	}

	@RequestMapping("")
	public String ppts(Map<String, Object> map,
			@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "id", required = false) Integer id) {
		HqlUtils hqlUtils = new HqlUtils();
		hqlUtils.setEntityName("PptFile");
		String ean = hqlUtils.getEntityAsName();

		Map<String, Object> params = new HashMap<>();

		hqlUtils.andWhere(ean + ".adminUser.id  = :uid ");
		params.put("uid", getAdminUser().getId());

		if (ValidateUtil.isValid(key)) {
			hqlUtils.andWhere(ean + ".name  like :name");
			hqlUtils.orWhere(ean + ".tags  like :name");
			params.put("name", "%" + key + "%");
		}

		if (ValidateUtil.isValid(id)) {
			hqlUtils.setWhere("and  " + "channel.id = :cid");
			params.put("cid", id);
		}

		hqlUtils.setDistinct(true);
		hqlUtils.setOrder(ean + ".id desc");
		hqlUtils.setParams(params);

		int count = pptFileService.pageCountList(hqlUtils);
		PageUtil page = new PageUtil(count, 20);
		page.setRequest(request);
		String pageHtml = page.show();
		List<PptFile> lists = pptFileService.pageList(hqlUtils,
				page.getFristRow(), page.getListRow());
		map.put("pageHtml", pageHtml);
		map.put("lists", lists);
		map.put("key", key);
		return "/user/ppts/index.ftl";
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

		map.put("pptFile", new PptFile());
		Map<String, String> fids = getSelectChannelsForFid();
		map.put("selectChannelFids", fids);
		map.put("selectChannelsChilds", getSelectChannelsForChild(fids));
		return "user/ppts/save-input.jsp";
	}

	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String insert(@Valid PptFile pptFile, BindingResult result,
			@RequestParam("filesId") String filesPost, Map<String, Object> map) {

		try {

			if (result.getErrorCount() > 0) {
				map.put("pptFile", pptFile);
				// 设置选中的栏目
				Map<String, String> fids = getSelectChannelsForFid();
				map.put("selectChannelFids", fids);
				map.put("selectChannelsChilds", getSelectChannelsForChild(fids));
				return "user/ppts/save-input.jsp";
			} else {
				pptFile.setEnabled(true);
				pptFile.setFiles(convert(filesPost));
				pptFile.setAdminUser(getAdminUser());
				pptFileService.saveOrUpdateEntiry(pptFile);
			}
		} catch (Exception e) {
		}
		return "redirect:/user/ppts";
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
			Map<String, String> fids = getSelectChannelsForFid();
			map.put("selectChannelFids", fids);
			map.put("selectChannelsChilds", getSelectChannelsForChild(fids));
			SetPptFiles(map, pptFile);

		} else {
			return "redirect:/user/ppts/insert";
		}

		map.put("pptFile", pptFile);
		return "user/ppts/save-input.jsp";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid PptFile pptFile, BindingResult result,
			@RequestParam("filesId") String filesPost, Map<String, Object> map) {
		if (null == pptFile.getId()) {
			return "reditct:/user/ppts/insert";
		}

		try {
			if (result.getErrorCount() > 0) {

				pptFile = pptFileService.getEntity(pptFile.getId());
				pptFile.setFiles(convert(filesPost));
				Map<String, String> fids = getSelectChannelsForFid();
				map.put("selectChannelFids", fids);
				map.put("selectChannelsChilds", getSelectChannelsForChild(fids));
				SetPptFiles(map, pptFile);

				return "user/ppts/save-input.jsp";
			} else {
				pptFile.setFiles(convert(filesPost));
				PptFile post2 = pptFileService.getEntity(pptFile.getId());
				BeanUtils.copyProperties(pptFile, post2);
				pptFileService.saveOrUpdateEntiry(post2);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

		return "redirect:/user/ppts/update?id=" + pptFile.getId();
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

	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public Map<String, Object> delete(@RequestParam("delid") Integer[] delid,
			@RequestParam("menuType") String menuType,
			@RequestParam("menuValue") String menuValue) {
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

		default:
			break;
		}

		return ajaxResult;

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
}
