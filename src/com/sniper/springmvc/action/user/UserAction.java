package com.sniper.springmvc.action.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sniper.springmvc.action.home.HomeBaseAction;
import com.sniper.springmvc.model.PptFile;
import com.sniper.springmvc.searchUtil.BaseSearch;
import com.sniper.springmvc.utils.HqlUtils;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 用户管理用心
 * 
 * @author sniper
 * 
 */
@Controller
@RequestMapping("/user")
public class UserAction extends HomeBaseAction {

	@RequestMapping("")
	public String index(Map<String, Object> map, BaseSearch search) {
		HqlUtils hqlUtils = new HqlUtils();
		hqlUtils.setEntityName("PptFile");
		String ean = hqlUtils.getEntityAsName();

		Map<String, Object> params = new HashMap<>();

		hqlUtils.andWhere(ean + ".adminUser.id  = :uid ");
		params.put("uid", getAdminUser().getId());

		if (ValidateUtil.isValid(search.getName())) {
			hqlUtils.andWhere(ean + ".name  like :name");
			params.put("name", "'%" + search.getName() + "%'");
		}

		if (ValidateUtil.isValid(search.getChannel())) {
			hqlUtils.setWhere("and  " + "channel.id = :cid");
			params.put("cid", search.getChannel());
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
		return "user/index/index.ftl";
	}
}
