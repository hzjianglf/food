package com.sniper.springmvc.action.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.sniper.springmvc.action.RootAction;
import com.sniper.springmvc.hibernate.service.impl.AdminRightService;
import com.sniper.springmvc.model.AdminRight;
import com.sniper.springmvc.utils.BaseHref;
import com.sniper.springmvc.utils.HqlUtils;
import com.sniper.springmvc.utils.TreeZTreeUtil;
import com.sniper.springmvc.utils.UserDetailsUtils;

@Controller
public class BaseAction extends RootAction {

	@Resource
	protected AdminRightService adminRightService;

	/**
	 * 系统左侧菜单使用 组装ztree必备
	 */
	private static List<AdminRight> zTreeMenuData = new ArrayList<>();

	public List<AdminRight> getzTreeMenuData() {
		if (zTreeMenuData.size() == 0) {
			HqlUtils hqlUtils = new HqlUtils();
			hqlUtils.setEntityName("AdminRight");
			hqlUtils.setWhere("theMenu = true");
			hqlUtils.setOrder("sort asc");
			String hql = hqlUtils.getHql();
			zTreeMenuData = adminRightService.findEntityByHQL(hql);
		}
		return zTreeMenuData;
	}

	public BaseAction() {
		super();

	}

	protected boolean isXMLHttpRequest() {
		String header = getRequest().getHeader("X-Requested-With");
		if (header != null && "XMLHttpRequest".equals(header))
			return true;
		else
			return false;
	}

	@Override
	@ModelAttribute
	public void init(Map<String, Object> map, BaseHref baseHref) {

		String url = this.request.getRequestURI().replaceFirst(
				this.request.getContextPath(), "");

		if (url.indexOf(";jsessionid") != -1) {
			url = url.substring(0, url.indexOf(";jsessionid"));
		}
		// 组装左边菜单
		TreeZTreeUtil treeUtil = new TreeZTreeUtil();
		treeUtil.setUrlPath(url);
		treeUtil.setAdminRights(getzTreeMenuData());
		treeUtil.setContextPath(this.request.getContextPath());

		UserDetailsUtils detailsUtils = new UserDetailsUtils();
		treeUtil.setAuthorities(detailsUtils.getAuthorities());
		treeUtil.init();
		String zTreeMenu = treeUtil.getTreeNodes();

		baseHref.setzTreeMenu(zTreeMenu);

		// 获取当前url的实体
		AdminRight adminRight = treeUtil.getRightByUrl();
		if (null != adminRight) {
			baseHref.setAdminRight(adminRight);
			String title = adminRight.getName();
			baseHref.setPageTitle(title);
		}
		super.init(map, baseHref);

	}

}
