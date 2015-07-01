package com.sniper.springmvc.action.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.coder.RSACoder;
import com.sniper.springmvc.hibernate.service.impl.AdminGroupService;
import com.sniper.springmvc.model.AdminGroup;
import com.sniper.springmvc.model.AdminUser;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.HqlUtils;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.ValidateUtil;

@RequestMapping("/admin/admin-user")
@Controller
public class AdminUserAction extends BaseAction {

	@Resource
	private AdminGroupService adminGroupService;

	// 用户组列表
	public List<AdminGroup> getAdminGroups() {
		return adminGroupService.getGroupSelectList(null);
	}

	@RequestMapping("/")
	public String index(Map<String, Object> map) {

		map.put("sniperUrl", "/admin-user/delete");

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
		hqlUtils.setEntityName("AdminUser");
		hqlUtils.setOrder("id desc");
		int count = adminUserService.pageCountList(hqlUtils);
		PageUtil page = new PageUtil(count, 50);
		page.setRequest(request);
		String pageHtml = page.show();
		List<AdminUser> lists = adminUserService.pageList(hqlUtils,
				page.getFristRow(), page.getListRow());

		map.put("pageHtml", pageHtml);
		map.put("lists", lists);
		map.put("sniperMenu", toHtml);
		return "admin/admin-user/index.jsp";
	}

	@RequestMapping(value = "insert", method = RequestMethod.GET)
	public String insert(Map<String, Object> map) {

		map.put("sniperAdminUser", new AdminUser());
		map.put("adminGroups", getAdminGroups());
		return "admin/admin-user/save-input.jsp";

	}

	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String insert(
			Map<String, Object> map,
			@Valid AdminUser sniperAdminUser,
			BindingResult result,
			@RequestParam(value = "password_c", required = false) String password_c,
			@RequestParam(value = "sign", required = false) Boolean sign,
			@RequestParam(value = "adminGroups", required = false) String[] adminGroups)
			throws Exception {

		if (result.getErrorCount() > 0) {
			map.put("adminGroups", getAdminGroups());
			map.put("sniperAdminUser", sniperAdminUser);
			return "admin/admin-user/save-input.jsp";
		} else {

			setAdminGroups(adminGroups, sniperAdminUser);

			if (ValidateUtil.isValid(password_c)) {
				sniperAdminUser.setPassword(password_c);
			}

			if (ValidateUtil.isValid(sign) && sign) {
				// 生成加密吗
				String privateKey = request.getServletContext()
						.getInitParameter("privateKey");
				byte[] data = sniperAdminUser.getName().getBytes();
				// 产生签名
				byte[] signByte = RSACoder.sign(data,
						Base64.decodeBase64(privateKey));
				// 保存相关信息
				sniperAdminUser.setSignCode(Hex.encodeHexString(signByte));
			}

			try {
				adminUserService.getCurrentSession().clear();
				adminUserService.saveOrUpdateEntiry(sniperAdminUser);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		}

		return "redirect:/admin/admin-user/insert";

	}

	/**
	 * 去除提交过程中的空值
	 * 
	 * @param adminUser
	 * @return
	 */
	private void setAdminGroups(String[] adminGroups, AdminUser adminUser) {
		Set<AdminGroup> adminGroups2 = new HashSet<>();
		if (adminGroups.length > 0) {
			for (int i = 0; i < adminGroups.length; i++) {
				AdminGroup adminGroup = adminGroupService.getEntity(Integer
						.valueOf(adminGroups[i]));
				adminGroups2.add(adminGroup);
			}
		}
		adminUser.setAdminGroup(adminGroups2);
	}

	@RequestMapping(value = "update", method = RequestMethod.GET)
	public String update(
			@RequestParam(value = "id", required = false) Integer id,
			Map<String, Object> map) {

		if (ValidateUtil.isValid(id)) {
			AdminUser sniperAdminUser = adminUserService.getEntity(id);
			map.put("adminGroups", getAdminGroups());
			map.put("sniperAdminUser", sniperAdminUser);
		} else {
			return "redirect:/admin/admin-user/insert";
		}

		return "admin/admin-user/save-input.jsp";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(
			Map<String, Object> map,
			@Valid AdminUser sniperAdminUser,
			BindingResult result,
			@RequestParam(value = "password_c", required = false) String password_c,
			@RequestParam(value = "sign", required = false) Boolean sign,
			@RequestParam(value = "adminGroups", required = false) String[] adminGroups)
			throws Exception {
		if (result.getErrorCount() > 0) {
			map.put("adminGroups", getAdminGroups());
			return "admin/admin-user/save-input.jsp";
		} else {

			setAdminGroups(adminGroups, sniperAdminUser);

			if (ValidateUtil.isValid(sign) && sign) {
				// 生成加密吗
				String privateKey = request.getServletContext()
						.getInitParameter("privateKey");
				byte[] data = sniperAdminUser.getName().getBytes();
				// 产生签名
				byte[] signByte = RSACoder.sign(data,
						Base64.decodeBase64(privateKey));
				// 保存相关信息
				sniperAdminUser.setSignCode(Hex.encodeHexString(signByte));
			}

			// 数据拷贝
			AdminUser adminUser2 = adminUserService.getEntity(sniperAdminUser
					.getId());
			// 因为password提交过来的可能为空,所以必须赋值
			sniperAdminUser.setPassword(adminUser2.getPassword());
			BeanUtils.copyProperties(sniperAdminUser, adminUser2);
			// 处理提交过来的用户密码
			if (ValidateUtil.isValid(password_c)) {
				adminUser2.setPassword(password_c);
			}

			adminUserService.getCurrentSession().clear();
			adminUserService.saveOrUpdateEntiry(adminUser2);
		}

		return "redirect:/admin/admin-user/update?id="
				+ sniperAdminUser.getId();

	}

	@RequestMapping(value = "changepassword", method = RequestMethod.GET)
	public String changepassword(Map<String, Object> map) {
		map.put("adminUser", getAdminUser());
		return "admin/admin-user/change-password.jsp";
	}

	@RequestMapping(value = "changepassword", method = RequestMethod.POST)
	public String changepassword(
			Map<String, Object> map,
			AdminUser adminUser,
			@RequestParam(value = "password_old", required = true) String password_old,
			@RequestParam(value = "password_c", required = true) String password_c) {

		List<String> errors = new ArrayList<>();

		if (!ValidateUtil.isValid(password_old)) {
			errors.add("旧密码不得为空");
		}

		if (!ValidateUtil.isValid(adminUser.getPassword())
				|| adminUser.getPassword().length() < 6) {
			errors.add("新密码必须6位以上");
		}

		if (!ValidateUtil.isValid(password_c)
				|| !password_c.equals(adminUser.getPassword())) {
			errors.add("两次密码输入不一致");
		}

		// 检查密码
		String pwd = DigestUtils.sha1Hex(password_old);
		System.out.println("旧密码:" + getAdminUser().getPassword());
		System.out.println("新密码:" + pwd);
		if (pwd.equals(getAdminUser().getPassword())) {
			adminUser.setPassword(password_c);
		} else {
			errors.add("旧密码校验不对");
		}
		map.put("adminUser", getAdminUser());
		if (errors.size() == 0) {
			// 数据拷贝
			try {
				AdminUser adminUser2 = adminUserService.getEntity(adminUser
						.getId());
				// 因为password提交过来的可能为空,所以必须赋值
				BeanUtils.copyProperties(adminUser, adminUser2);
				System.out.println(adminUser2.getPassword());
				adminUser.setAdminGroup(adminUser2.getAdminGroup());
				adminUserService.saveOrUpdateEntiry(adminUser2);
				map.put("adminUser", adminUser2);
				errors.add("密码修改成功");
			} catch (Exception e) {
				errors.add(e.getMessage());
			}
		}

		map.put("errors", errors);

		return "admin/admin-user/change-password.jsp";
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
				adminUserService.deleteEntirys(delid);
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", "删除失败");
			}

			break;
		case "enabled":

			try {
				adminUserService.batchFiledChange("enables",
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
				adminUserService.batchFiledChange("locked",
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
			List<AdminUser> lists = adminUserService.findListsByEmail("%"
					+ term + "%");
			for (AdminUser u : lists) {
				Map<String, String> map = new HashMap<>();
				map.put("id", String.valueOf(u.getId()));
				map.put("label", u.getName());
				map.put("value", u.getEmail());
				ajaxMaps.add(map);
			}
		}

		return ajaxMaps;
	}

	public static void main(String[] args) {
		System.out.println("d033e22ae348aeb5660fc2140aec35850c4da997".length());
	}
}
