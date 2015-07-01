package com.sniper.springmvc.action.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.fabric.xmlrpc.base.Member;
import com.sniper.springmvc.hibernate.service.impl.MembersService;
import com.sniper.springmvc.model.Members;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.HqlUtils;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 前台用户管理
 * 
 * @author sniper
 * 
 */
@Controller
@RequestMapping("/admin/admin-members")
public class AdminMembersAction extends BaseAction {

	@Resource
	private MembersService membersService;

	@RequestMapping("/")
	public String index(Map<String, Object> map) {

		map.put("sniperUrl", "/admin-members/delete");

		ParamsToHtml toHtml = new ParamsToHtml();

		Map<String, String> menu = new HashMap<>();
		menu.put("true", "是");
		menu.put("false", "否");

		toHtml.addMapValue("enabled", menu);

		Map<String, String> ispublic = new HashMap<>();
		ispublic.put("true", "是");
		ispublic.put("false", "否");

		toHtml.addMapValue("locked", ispublic);

		Map<String, String> keys = new HashMap<>();
		keys.put("enabled", "启用");
		keys.put("locked", "锁定");

		toHtml.setKeys(keys);

		HqlUtils hqlUtils = new HqlUtils();
		hqlUtils.setEntityName("Members");
		hqlUtils.setOrder("id desc");
		int count = membersService.pageCountList(hqlUtils);
		PageUtil page = new PageUtil(count, 50);
		page.setRequest(request);
		String pageHtml = page.show();
		List<Members> lists = membersService.pageList(hqlUtils,
				page.getFristRow(), page.getListRow());

		map.put("pageHtml", pageHtml);
		map.put("lists", lists);
		map.put("sniperMenu", toHtml);
		return "admin/admin-members/index.jsp";
	}

	@RequestMapping(value = "insert", method = RequestMethod.GET)
	public String insert(Map<String, Object> map) {

		map.put("members", new Members());
		return "admin/admin-members/save-input.jsp";

	}

	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String insert(
			Map<String, Object> map,
			@Valid Members Members,
			BindingResult result,
			@RequestParam(value = "password_c", required = false) String password_c)
			throws Exception {

		if (result.getErrorCount() > 0) {
			return "admin/admin-members/save-input.jsp";
		} else {

			if (ValidateUtil.isValid(password_c)) {
				Members.setPassword(password_c);
			}

			try {
				membersService.saveOrUpdateEntiry(Members);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		}

		return "redirect:/admin/admin-members/insert";

	}

	@RequestMapping(value = "update", method = RequestMethod.GET)
	public String update(
			@RequestParam(value = "id", required = false) Integer id,
			Map<String, Object> map, Members Members) {

		if (ValidateUtil.isValid(id)) {
			Members = membersService.getEntity(id);
		} else {
			return "redirect:/admin/admin-members/insert";
		}

		map.put("members", Members);

		return "admin/admin-members/save-input.jsp";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(
			@Valid Members Members,
			BindingResult result,
			@RequestParam(value = "password_c", required = false) String password_c)
			throws Exception {

		if (result.getErrorCount() > 0) {
			return "admin/admin-members/save-input.jsp";
		} else {

			// 数据拷贝
			Members adminUser2 = membersService.getEntity(Members.getMyid());
			// 因为password提交过来的可能为空,所以必须赋值
			Members.setPassword(adminUser2.getPassword());
			BeanUtils.copyProperties(Members, adminUser2);
			// 处理提交过来的用户密码
			if (ValidateUtil.isValid(password_c)) {
				adminUser2.setPassword(password_c);
			}

			membersService.saveOrUpdateEntiry(adminUser2);
		}

		return "redirect:/admin/admin-members/update?id=" + Members.getMyid();

	}

	@RequestMapping(value = "changepassword", method = RequestMethod.GET)
	public String changepassword(Map<String, Object> map) {
		map.put("members", new Member());
		return "admin/admin-members/change-password.jsp";
	}

	/**
	 * 这里使用时不应该放在此地
	 * 
	 * @param map
	 * @param Members
	 * @param password_old
	 * @param password_c
	 * @return
	 */
	@RequestMapping(value = "changepassword", method = RequestMethod.POST)
	public String changepassword(
			Map<String, Object> map,
			Members Members,
			@RequestParam(value = "password_old", required = true) String password_old,
			@RequestParam(value = "password_c", required = true) String password_c) {

		List<String> errors = new ArrayList<>();

		if (!ValidateUtil.isValid(password_old)) {
			errors.add("旧密码不得为空");
		}

		if (!ValidateUtil.isValid(Members.getPassword())
				|| Members.getPassword().length() < 6) {
			errors.add("新密码必须6位以上");
		}

		if (!ValidateUtil.isValid(password_c)
				|| !password_c.equals(Members.getPassword())) {
			errors.add("两次密码输入不一致");
		}

		// 检查密码
		String pwd = DigestUtils.sha1Hex(password_old);
		if (pwd.equals(getAdminUser().getPassword())) {
			Members.setPassword(password_c);
		} else {
			errors.add("旧密码校验不对");
		}
		map.put("members", new Member());
		if (errors.size() == 0) {
			// 数据拷贝
			try {
				Members adminUser2 = membersService
						.getEntity(Members.getMyid());
				// 因为password提交过来的可能为空,所以必须赋值
				BeanUtils.copyProperties(Members, adminUser2);
				membersService.saveOrUpdateEntiry(adminUser2);
				map.put("Members", adminUser2);
				errors.add("密码修改成功");
			} catch (Exception e) {
				errors.add(e.getMessage());
			}
		}

		map.put("errors", errors);

		return "admin/admin-members/change-password.jsp";
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
				membersService.deleteBatchEntityById(delid);
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", "删除失败");
			}

			break;
		case "enabled":

			try {
				membersService.batchFiledChange("enables",
						DataUtil.stringToBoolean(menuValue), delid);
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", e.getMessage());
			}

			break;
		case "locked":
			try {
				membersService.batchFiledChange("locked",
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

	@ResponseBody
	@RequestMapping("ajaxUserList")
	public List<Map<String, String>> ajaxUserList(
			@RequestParam("term") String term) {

		List<Map<String, String>> ajaxMaps = new ArrayList<>();

		if (ValidateUtil.isValid(term)) {
			List<Members> lists = membersService.findListsByEmail("%" + term
					+ "%");
			for (Members u : lists) {
				Map<String, String> map = new HashMap<>();
				map.put("id", String.valueOf(u.getMyid()));
				map.put("label", u.getUsername());
				map.put("value", u.getEmail());
				ajaxMaps.add(map);
			}
		}

		return ajaxMaps;
	}

}
