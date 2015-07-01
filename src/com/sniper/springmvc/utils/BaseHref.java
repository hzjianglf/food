package com.sniper.springmvc.utils;

import com.sniper.springmvc.model.AdminRight;

public class BaseHref {

	private String pageTitle;
	private String baseHref;
	private String zTreeMenu;
	private AdminRight adminRight;

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public String getBaseHref() {
		return baseHref;
	}

	public void setBaseHref(String baseHref) {
		this.baseHref = baseHref;
	}

	public BaseHref(String baseHref) {
		super();
		this.baseHref = baseHref;
	}

	public void setzTreeMenu(String zTreeMenu) {
		this.zTreeMenu = zTreeMenu;
	}

	public String getzTreeMenu() {
		return zTreeMenu;
	}

	public void setAdminRight(AdminRight adminRight) {
		this.adminRight = adminRight;
	}

	public AdminRight getAdminRight() {
		return adminRight;
	}

	public BaseHref() {
		// TODO Auto-generated constructor stub
	}

}
