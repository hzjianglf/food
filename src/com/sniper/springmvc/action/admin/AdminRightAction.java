package com.sniper.springmvc.action.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.model.AdminRight;
import com.sniper.springmvc.searchUtil.AdminGroupSearch;
import com.sniper.springmvc.utils.BeanUtils;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.HqlUtils;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.TreeZTreeUtil;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
@RequestMapping("/admin/admin-right")
public class AdminRightAction extends BaseAction {

	/**
	 * 存放整个系统的菜单
	 */
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

	@RequestMapping("/")
	public String index(Map<String, Object> map, AdminGroupSearch groupSearch) {

		map.put("groupSearch", groupSearch);
		map.put("sniperUrl", "/admin-right/delete");

		ParamsToHtml pth = new ParamsToHtml();
		Map<String, String> menu = new HashMap<>();

		menu.put("true", "是");
		menu.put("false", "否");

		pth.addMapValue("theMenu", menu);

		Map<String, String> ispublic = new HashMap<>();

		ispublic.put("true", "是");
		ispublic.put("false", "否");
		pth.addMapValue("thePublic", ispublic);

		Map<String, String> show = new HashMap<>();

		show.put("true", "是");
		show.put("false", "否");
		pth.addMapValue("theShow", show);

		Map<String, String> keys = new HashMap<>();
		keys.put("theMenu", "是否是菜单");
		keys.put("thePublic", "是否是公共");
		keys.put("theShow", "是否是显示");

		pth.setKeys(keys);
		//
		map.put("sniperMenu", pth);

		HqlUtils hqlUtils = new HqlUtils();

		if (ValidateUtil.isValid(groupSearch.getIsShow())) {
			hqlUtils.setWhere("and theShow = " + groupSearch.getIsShow());
		}

		if (ValidateUtil.isValid(groupSearch.getIsMenu())) {
			hqlUtils.setWhere("and theMenu = " + groupSearch.getIsMenu());
		}
		if (ValidateUtil.isValid(groupSearch.getGroupName())) {
			hqlUtils.setWhere("and name like '%" + groupSearch.getGroupName()
					+ "%'");
		}

		if (ValidateUtil.isValid(groupSearch.getUrl())) {
			hqlUtils.setWhere("and url like '%" + groupSearch.getUrl() + "%'");
		}

		hqlUtils.setEntityName("AdminRight");
		hqlUtils.setOrder("id desc");
		int count = adminRightService.pageCountList(hqlUtils);
		PageUtil page = new PageUtil(count, 50);
		page.setRequest(request);
		String pageHtml = page.show();
		List<AdminRight> lists = adminRightService.pageList(hqlUtils,
				page.getFristRow(), page.getListRow());

		map.put("pageHtml", pageHtml);
		map.put("lists", lists);
		return "admin/admin-right/index.jsp";
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
		// 这是id未0,当id未0时,就是保存操作
		map.put("adminRight", new AdminRight());
		ArrayList<String> targets = new ArrayList<>();
		targets.add("_self");
		targets.add("_blank");

		map.put("targets", targets);
		map.put("treeRightMap", getTreeRightMap());

		return "admin/admin-right/save-input.jsp";
	}

	/**
	 * 数据插入 数据校验
	 * 
	 * @Valid 必须和BindingResult靠着
	 * @param adminRight
	 * @param result
	 * @return
	 */
	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String insert(@Valid AdminRight adminRight, BindingResult result) {

		if (result.getErrorCount() > 0) {
			return "admin/admin-right/save-input.jsp";
		} else {
			adminRightService.saveOrUpdate(adminRight);
		}

		// return "forward:/hello" => 转发到能够匹配 /hello 的 controller 上
		// return "hello" => 实际上还是转发，只不过是框架会找到该逻辑视图名对应的 View 并渲染
		// return "/hello" => 同 return "hello"

		return "redirect:/admin/admin-right/insert";
		// return "admin/admin-right/save-input.jsp";
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
			Map<String, Object> map, AdminRight adminRight) {

		if (ValidateUtil.isValid(id)) {
			adminRight = adminRightService.getEntity(id);
		} else {
			return "redirect:/admin/admin-right/insert";
		}

		map.put("adminRight", adminRight);
		ArrayList<String> targets = new ArrayList<>();
		targets.add("_self");
		targets.add("_blank");

		map.put("targets", targets);
		map.put("treeRightMap", getTreeRightMap());

		return "admin/admin-right/save-input.jsp";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid AdminRight adminRight, BindingResult result) {
		if (null == adminRight.getId()) {
			return "reditct:/admin/admin-right/insert";
		}

		try {
			if (result.getErrorCount() > 0) {
				return "admin/admin-right/save-input.jsp";
			} else {

				AdminRight adminRight2 = adminRightService.getEntity(adminRight
						.getId());
				BeanUtils.copyProperties(adminRight, adminRight2);
				adminRightService.saveOrUpdate(adminRight2);
			}
		} catch (Exception e) {

		}

		return "redirect:/admin/admin-right/update?id=" + adminRight.getId();
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
				adminRightService.deleteEntirys(delid);
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", "删除失败");
			}

			break;
		case "theShow":

			try {
				adminRightService.batchFiledChange("theShow",
						DataUtil.stringToBoolean(menuValue), delid);
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", e.getMessage());
			}

			break;
		case "thePublic":

			try {
				adminRightService.batchFiledChange("thePublic",
						DataUtil.stringToBoolean(menuValue), delid);
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", e.getMessage());
			}

			break;
		case "theMenu":
			try {
				adminRightService.batchFiledChange("theMenu",
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
