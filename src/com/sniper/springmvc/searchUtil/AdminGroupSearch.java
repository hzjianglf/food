package com.sniper.springmvc.searchUtil;

public class AdminGroupSearch {

	private Boolean isShow;
	private Boolean isMenu;
	private String groupName;
	private String url;
	private String autoload;

	public Boolean getIsShow() {
		return isShow;
	}

	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}

	public Boolean getIsMenu() {
		return isMenu;
	}

	public void setIsMenu(Boolean isMenu) {
		this.isMenu = isMenu;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAutoload() {
		return autoload;
	}

	public void setAutoload(String autoload) {
		this.autoload = autoload;
	}

	@Override
	public String toString() {
		return "AdminGroupSearch [isShow=" + isShow + ", isMenu=" + isMenu
				+ ", groupName=" + groupName + ", url=" + url + "]";
	}

}
