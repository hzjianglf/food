package com.sniper.springmvc.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * 
 * @author sniper
 * 
 */
public class PageUtil {

	private HttpServletRequest request;
	/**
	 * 存放所有request的参数
	 */
	private Map<String, String> params = new HashMap<>();

	private String url;

	/**
	 * 每栏个数
	 */
	private int rollPage = 5;
	/**
	 * 分栏数
	 */
	private int coolPages;
	/**
	 * 当前分栏数
	 */
	private int nowCoolPage;
	/**
	 * 每页个数
	 */
	private int listRow = 20;
	/**
	 * 返回其实行
	 */
	private int fristRow = 0;
	/**
	 * 总页数
	 */
	private int totalPages = 0;
	/**
	 * 总记录书
	 */

	private int totalRows = 0;
	/**
	 * 当前页数
	 */
	private int nowPage = 1;

	private String varPage = "pageNo";

	private Map<String, String> theme = new HashMap<>();
	{

		theme.put("header", "条记录");
		theme.put("prev", "上一页");
		theme.put("next", "下一页");
		theme.put("first", "第一页");
		theme.put("last", "最后一页");
		theme.put(
				"theme",
				"<span class=\"disabled\">%totalRow% %header% %nowPage%/%totalPage% 页</span> %upPage% %downPage% %first%  %prePage%  %linkPage%  %nextPage% %end%");

	}

	public void setRequest(HttpServletRequest r) {
		this.request = r;
	}

	/**
	 * 获取request
	 * 
	 * @return
	 */
	public HttpServletRequest getRequest() {
		if (null == request) {
			throw new RuntimeException("HttpServletRequest不能为空");
		}
		return request;
	}

	/**
	 * 获取每栏的页数
	 * 
	 * @return
	 */
	public int getRollPage() {
		return rollPage;
	}

	public void setRollPage(int rollPage) {
		this.rollPage = rollPage;
	}

	/**
	 * 获取当前 页数除栏数= 必须条件先获取当前页数
	 * 
	 * @return
	 */
	public void setNowCoolPage() {
		this.nowCoolPage = (int) Math.ceil((double) this.nowPage
				/ getRollPage());
	}

	/**
	 * 获取每页的记录数
	 * 
	 * @return
	 */
	public int getListRow() {
		return listRow;
	}

	public void setListRow(int listRow) {
		this.listRow = listRow;
	}

	/**
	 * 获取数据库每页的起始行 必须条件获取当前页数
	 * 
	 * @return
	 */
	public int getFristRow() {
		this.fristRow = getListRow() * (this.nowPage - 1);
		return fristRow;
	}

	/**
	 * 获取总的页数 构造器初始化
	 * 
	 * @return
	 */
	public int getTotalPages() {
		return totalPages;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	/**
	 * 必须先执行 getParams获取所有的参数
	 */
	public void setNowPage() {

		String pageNoString = this.params.get(getVarPage());
		if (null == pageNoString) {
			this.nowPage = 1;
		} else {
			this.nowPage = Integer.parseInt(pageNoString);
			if (this.nowPage < 1) {
				this.nowPage = 1;
			} else if (this.nowPage > getTotalPages()) {
				this.nowPage = getTotalPages();
			}
		}

	}

	/**
	 * 获取所有的参数 没有必须条件
	 * 
	 * @return
	 */
	public Map<String, String> getParams() {
		if (params.isEmpty()) {
			// 获取所有的提交的参数
			Enumeration<String> names = getRequest().getParameterNames();
			while (names.hasMoreElements()) {
				String string = (String) names.nextElement();
				String value = getRequest().getParameter(string);
				if (null != value) {
					params.put(string, value);
				}
			}
		}

		return params;
	}

	/**
	 * 添加自定义或者修改参数
	 */
	public void setParams(String name, String value) {
		this.params.put(name, value);
	}

	/**
	 * 获取当前页数必须在次方法之前执行 没有必须条件可以随地执行获取
	 * 
	 * @return
	 */
	public String getUrl() {

		url = getRequest().getScheme() + "://" + getRequest().getServerName();
		if (getRequest().getServerPort() != 80) {
			url += ":" + getRequest().getServerPort();
		}
		url += getRequest().getContextPath();
		url += getRequest().getServletPath();
		return url;
	}

	public String getVarPage() {
		return varPage;
	}

	public void setVarPage(String varPage) {
		this.varPage = varPage;
	}

	/**
	 * 默认构造器完成
	 * 
	 * @return
	 */
	public Map<String, String> getTheme() {

		return theme;
	}

	/**
	 * 设置主题
	 * 
	 * @param name
	 * @param value
	 */
	public void setTheme(String name, String value) {
		this.theme.put(name, value);
	}

	public PageUtil() {
		super();
	}

	public PageUtil(int totalRows, int listRow) {
		super();
		setTotalRows(totalRows);
		setListRow(listRow);
		this.totalPages = (int) Math.ceil((double) getTotalRows()
				/ getListRow());
		this.coolPages = (int) Math.ceil((double) getTotalPages()
				/ getRollPage());
		// 设置主题
		getTheme();
	}

	public String show() {
		// 上下翻页

		// showPost();
		// showGet();
		return showGet();
	}

	/**
	 * post形式分页
	 * 
	 * @return
	 */
	private String showPost() {
		return varPage;

	}

	/**
	 * get形式分页
	 * 
	 * @return
	 */
	private String showGet() {

		if (0 == getTotalRows())
			return "";

		String upPage = "";
		String downPage = "";
		String theFrist = "";
		String prePage = "";

		String nextPage = "";
		String theEnd = "";

		int preRow;
		int nextRow;
		int theEndRow;
		// 方法执行顺序不能错,
		// 先获取所有的参数
		getParams();
		// 获取当前页数
		setNowPage();
		// 获取当前url
		getUrl();
		// 必须在获取当前页之后
		setNowCoolPage();
		// 站位
		this.params.put(getVarPage(), "_PAGE_");
		String urlParam = "";
		for (Entry<String, String> entry : params.entrySet()) {
			String name = entry.getKey();
			if (!"".equals(name)) {
				urlParam += "&" + entry.getKey() + "=" + entry.getValue();
			}
		}
		if (urlParam.isEmpty()) {

		}
		url += "?" + (!urlParam.isEmpty() ? urlParam.substring(1) : "");

		// 处理上一页，下一页
		int upRow = this.nowPage - 1;

		if (upRow > 0) {
			upPage = "<a href='" + url.replace("_PAGE_", String.valueOf(upRow))
					+ "'>" + getTheme().get("prev") + "</a>";
		} else {
			upPage = "";
		}

		int downRow = this.nowPage + 1;

		if (downRow <= this.totalPages) {
			downPage = "<a href='"
					+ url.replace("_PAGE_", String.valueOf(downRow)) + "'>"
					+ getTheme().get("next") + "</a>";
		} else {
			downPage = "";
		}

		// << < > >>

		if (this.nowCoolPage == 1) {
			theFrist = "";
			prePage = "";
		} else {
			preRow = this.nowPage - getRollPage();

			prePage = "<a href='"
					+ url.replace("_PAGE_", String.valueOf(preRow)) + "'>"
					+ "上" + String.valueOf(getRollPage()) + "页</a>";
			theFrist = "<a href='" + url.replace("_PAGE_", "1") + "'>"
					+ getTheme().get("first") + "</a>";
		}

		if (this.nowCoolPage == this.coolPages) {
			nextPage = "";
			theEnd = "";
		} else {
			nextRow = this.nowPage + getRollPage();
			theEndRow = getTotalPages();
			nextPage = "<a href='"
					+ url.replace("_PAGE_", String.valueOf(nextRow)) + "'>"
					+ "下" + String.valueOf(getRollPage()) + "页</a>";
			theEnd = "<a href='"
					+ url.replace("_PAGE_", String.valueOf(theEndRow)) + "'>"
					+ getTheme().get("last") + "</a>";
		}

		// 1 2,3,4

		String linkPage = "";
		int page;

		for (int i = 1; i <= getRollPage(); i++) {

			page = (this.nowCoolPage - 1) * getRollPage() + i;

			if (page != this.nowPage) {
				if (page <= getTotalPages()) {
					linkPage += "<a href='"
							+ url.replace("_PAGE_", String.valueOf(page))
							+ "'>" + String.valueOf(page) + "</a>";
				} else {
					break;
				}
			} else {
				if (getTotalPages() != 1) {
					linkPage += "<span class='current'>" + String.valueOf(page)
							+ "</span>";
				}
			}
		}

		String html = this.theme.get("theme");
		html = html.replace("%header%", this.theme.get("header"));
		html = html.replace("%nowPage%", String.valueOf(this.nowPage));
		html = html.replace("%totalRow%", String.valueOf(getTotalRows()));
		html = html.replace("%totalPage%", String.valueOf(getTotalPages()));
		html = html.replace("%upPage%", upPage);
		html = html.replace("%downPage%", downPage);
		html = html.replace("%first%", theFrist);
		html = html.replace("%prePage%", prePage);
		html = html.replace("%linkPage%", linkPage);
		html = html.replace("%nextPage%", nextPage);
		html = html.replace("%end%", theEnd);
		return html;

	}
}
