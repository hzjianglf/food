package com.sniper.springmvc.action.admin;

import java.util.HashMap;
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

import com.sniper.springmvc.hibernate.service.impl.AdminGroupService;
import com.sniper.springmvc.model.AdminGroup;
import com.sniper.springmvc.model.AdminRight;
import com.sniper.springmvc.utils.BaseHref;
import com.sniper.springmvc.utils.BeanUtils;
import com.sniper.springmvc.utils.HqlUtils;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.TreeZTreeUtil;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
@RequestMapping("/admin/admin-group")
public class AdminGroupAction extends BaseAction {

	@Resource
	private AdminGroupService adminGroupService;

	public String getTreeRightMap() {
		TreeZTreeUtil treeUtil = new TreeZTreeUtil();
		HqlUtils hqlUtils = new HqlUtils();
		hqlUtils.setEntityName("AdminRight");
		hqlUtils.setOrder("sort asc");
		String hql = hqlUtils.getHql();
		treeUtil.setAdminRights(adminRightService.findEntityByHQL(hql));

		treeUtil.initAll();
		return treeUtil.getTreeNodesAll();
	}

	@ModelAttribute
	public void init(Map<String, Object> map, BaseHref baseHref) {
		super.init(map, baseHref);
		ParamsToHtml html = new ParamsToHtml();
		html.setKey(getTreeRightMap());
		map.put("html", html);
	}

	@RequestMapping("/")
	public String index(Map<String, Object> map) {

		HqlUtils hqlUtils = new HqlUtils();
		hqlUtils.setEntityName("AdminGroup");
		hqlUtils.setOrder("id desc");
		int count = adminGroupService.pageCountList(hqlUtils);
		PageUtil page = new PageUtil(count, 50);
		page.setRequest(request);
		String pageHtml = page.show();
		List<AdminGroup> lists = adminGroupService.pageList(hqlUtils,
				page.getFristRow(), page.getListRow());

		map.put("pageHtml", pageHtml);
		map.put("lists", lists);
		map.put("sniperUrl", "/admin-group/delete");
		return "admin/admin-group/index.ftl";
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

		map.put("adminGroup", new AdminGroup());
		return "admin/admin-group/save-input.jsp";
	}

	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String insert(@Valid AdminGroup adminGroup, BindingResult result,
			@RequestParam("fromRight") Set<AdminRight> fromRight) {

		try {
			if (result.getErrorCount() > 0) {
				return "admin/admin-group/save-input.jsp";
			} else {
				adminGroup.setAdminRight(fromRight);
				adminGroupService.saveOrUpdateEntiry(adminGroup);
			}
		} catch (Exception e) {

		}

		return "redirect:/admin/admin-group/insert";
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
			Map<String, Object> map, AdminGroup adminGroup) {

		if (ValidateUtil.isValid(id)) {
			adminGroup = adminGroupService.getEntity(id);
		} else {
			return "redirect:/admin/admin-group/insert";
		}

		map.put("adminGroup", adminGroup);

		return "admin/admin-group/save-input.jsp";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid AdminGroup adminGroup, BindingResult result,
			@RequestParam("fromRight") Set<AdminRight> fromRight) {
		if (null == adminGroup.getId()) {
			return "reditct:/admin/admin-group/insert";
		}

		try {
			if (result.getErrorCount() > 0) {
				return "admin/admin-group/save-input.jsp";
			} else {
				adminGroup.setAdminRight(fromRight);
				AdminGroup adminGroup2 = adminGroupService.getEntity(adminGroup
						.getId());
				BeanUtils.copyProperties(adminGroup, adminGroup2);
				adminGroupService.saveOrUpdateEntiry(adminGroup2);
			}
		} catch (Exception e) {

		}
		return "redirect:/admin/admin-group/update?id=" + adminGroup.getId();
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
				adminGroupService.deleteEntirys(delid);
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
