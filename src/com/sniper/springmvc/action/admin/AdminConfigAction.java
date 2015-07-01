package com.sniper.springmvc.action.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;

import com.sniper.springmvc.hibernate.service.impl.SystemConfigService;
import com.sniper.springmvc.model.SystemConfig;
import com.sniper.springmvc.searchUtil.AdminGroupSearch;
import com.sniper.springmvc.security.MySecurityMetadataSource;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.HqlUtils;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.RedisUtil;
import com.sniper.springmvc.utils.TreeZTreeUtil;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 
 * @author sniper
 * 
 */
@Controller
@RequestMapping(value = "/admin/admin-config")
public class AdminConfigAction extends BaseAction {

	@Resource
	private SystemConfigService configService;

	@RequestMapping("/")
	public String index(Map<String, Object> map, AdminGroupSearch groupSearch) {

		map.put("sniperUrl", "/admin-config/delete");
		map.put("groupSearch", groupSearch);

		ParamsToHtml toHtml = new ParamsToHtml();

		Map<String, String> menu = new HashMap<>();
		menu.put("true", "是");
		menu.put("false", "否");

		toHtml.addMapValue("autoload", menu);
		Map<String, String> keys = new HashMap<>();
		keys.put("autoload", "自动加载");

		toHtml.setKeys(keys);

		HqlUtils hqlUtils = new HqlUtils();
		hqlUtils.setEntityName("SystemConfig");
		if (ValidateUtil.isValid(groupSearch.getAutoload())) {
			hqlUtils.setWhere(" and autoload = " + groupSearch.getAutoload());
		}
		hqlUtils.setOrder("id desc");
		int count = configService.pageCountList(hqlUtils);
		PageUtil page = new PageUtil(count, 50);
		page.setRequest(request);
		String pageHtml = page.show();
		List<SystemConfig> lists = configService.pageList(hqlUtils,
				page.getFristRow(), page.getListRow());
		map.put("pageHtml", pageHtml);
		map.put("lists", lists);
		map.put("sniperMenu", toHtml);
		return "admin/admin-config/index.jsp";
	}

	@RequestMapping(value = "insert", method = RequestMethod.GET)
	public String insert(Map<String, Object> map) {

		map.put("config", new SystemConfig());
		return "admin/admin-config/save-input.jsp";

	}

	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String insert(@Valid @ModelAttribute("config") SystemConfig config,
			BindingResult result) {

		if (result.getErrorCount() > 0) {
			return "admin/admin-config/save-input.jsp";
		} else {
			configService.saveOrUpdateEntiry(config);
		}

		return "redirect:/admin/admin-config/insert";

	}

	@RequestMapping(value = "update", method = RequestMethod.GET)
	public String update(
			@RequestParam(value = "id", required = false) Integer id,
			Map<String, Object> map,
			@ModelAttribute("config") SystemConfig config) {

		if (ValidateUtil.isValid(id)) {
			config = configService.getEntity(id);
		} else {
			return "redirect:/admin/admin-config/insert";
		}

		map.put("config", config);

		return "admin/admin-config/save-input.jsp";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("config") SystemConfig config,
			BindingResult result) {

		if (result.getErrorCount() > 0) {
			return "admin/admin-config/save-input.jsp";
		} else {
			configService.saveOrUpdateEntiry(config);
		}
		return "admin/admin-config/save-input.jsp";
	}

	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public Map<String, Object> delete(@RequestParam("delid") Integer[] delid,
			@RequestParam("menuType") String menuType,
			@RequestParam("menuValue") String menuValue) {
		Map<String, Object> ajaxResult = new HashMap<>();
		switch (menuType) {
		case "delete":
			if (configService.deleteBatchEntityById(delid)) {
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} else {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", "删除失败");
			}
			break;
		case "autoload":
			try {
				configService.batchFiledChange("autoload",
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

	public final class KeyMap {
		public final Map<Integer, String> keyMap = new HashMap<>();
		{
			keyMap.put(1, "Redis:当你修改权限管理下面的数据时,一定要清空在这里");
			keyMap.put(2, "系统配置,当你修改系统管理,网站配置的时候请清空此缓存");
			keyMap.put(3, "Redis:删除当前选择数据库中的所有key(默认0)");
			keyMap.put(4, "Redis:删除所有数据库中的所有key");
		}

		public Map<Integer, String> getKeyMap() {
			return keyMap;
		}

		public String getValue(Integer key) {
			if (keyMap.containsKey(key)) {
				return keyMap.get(key);
			}

			return "";
		}
	}

	@RequestMapping(value = "cache", method = RequestMethod.GET)
	public String cache(Map<String, Object> map) {
		map.put("keyMap", new KeyMap());
		return "admin/admin-config/cache.ftl";
	}

	/**
	 * 
	 * @param map
	 * @param clearType
	 * @return
	 */
	@RequestMapping(value = "cache", method = RequestMethod.POST)
	public String cache(Map<String, Object> map,
			@RequestParam("clearType") Integer[] clearType) {
		// 清空tree菜单数据
		// 数据排序
		List<String> result = new ArrayList<>();
		Arrays.sort(clearType);
		// 数组查询位置,返回其位置索引
		Jedis jedis = new RedisUtil().getJedis();

		if (Arrays.binarySearch(clearType, 1) > -1) {
			try {
				// 清空菜单数据
				// getzTreeMenuData().clear();
				// 清空redis存放的已经生成的数据,清空所有用户组的列表
				Set<String> set = jedis.keys(RedisUtil.getKeyName(
						TreeZTreeUtil.class, "treeNodes*"));
				for (String s : set) {
					jedis.del(s);
				}

				// 清空rightMap,spring security
				Map<String, Collection<ConfigAttribute>> rightMap = new HashMap<>();
				MySecurityMetadataSource.setRightMap(rightMap);
				getzTreeMenuData().clear();
				result.add("左侧菜单:" + new KeyMap().keyMap.get(1) + ":操作成功");
			} catch (Exception e) {
				result.add(e.getMessage());
			}
		}

		if (Arrays.binarySearch(clearType, 2) > -1) {
			getSystemConfig().clear();
			result.add("系统配置:" + new KeyMap().keyMap.get(2) + ":操作成功");
		}

		if (Arrays.binarySearch(clearType, 3) > -1) {
			jedis.flushDB();
			result.add("Redis:" + new KeyMap().keyMap.get(3) + ":操作成功");
		}

		if (Arrays.binarySearch(clearType, 4) > -1) {
			jedis.flushDB();
			result.add("Redis:" + new KeyMap().keyMap.get(4) + ":操作成功");
		}
		map.put("keyMap", new KeyMap());
		map.put("result", result);
		return "admin/admin-config/cache.ftl";
	}
}
