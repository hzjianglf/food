package com.sniper.springmvc.action;

import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.sniper.springmvc.freemarker.FreeMarkerUtil;
import com.sniper.springmvc.hibernate.service.impl.AdminUserService;
import com.sniper.springmvc.model.AdminUser;
import com.sniper.springmvc.utils.BaseHref;
import com.sniper.springmvc.utils.ImageHelpUtil;
import com.sniper.springmvc.utils.SystemConfigUtil;
import com.sniper.springmvc.utils.UserDetailsUtils;
import com.sniper.springmvc.utils.ValidateUtil;

import freemarker.template.TemplateHashModel;

@Controller
public class RootAction {

	@Autowired
	protected ResourceBundleMessageSource messageSource;

	protected static final Logger LOGGER = LoggerFactory
			.getLogger("com.sniper.springmvc.action");

	protected BaseHref baseHref;

	protected Locale locale = LocaleContextHolder.getLocale();

	// 存放网站配置
	private static Map<String, String> systemConfig = new HashMap<>();

	// 执行原始的request方法
	@Autowired
	protected HttpServletRequest request;

	@Resource
	protected AdminUserService adminUserService;
	// 前台图片显示助手
	protected final TemplateHashModel imageHelpUtil = FreeMarkerUtil
			.getFreeMarkerStaticModel(ImageHelpUtil.class);

	/**
	 * 不可以放在构造器中执行
	 * 
	 * @return
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * 获取网站配置
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public Map<String, String> getSystemConfig() {

		if (systemConfig.size() == 0) {
			systemConfig = SystemConfigUtil.getSystemConfig();
		}

		return systemConfig;
	}

	public RootAction() {

	}

	public String getRemoteIp() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress();
	}

	public String getBasePath() {
		String basePath = getRequest().getScheme() + "://"
				+ getRequest().getServerName();
		if (getRequest().getServerPort() != 80) {
			basePath += ":" + getRequest().getServerPort();
		}
		basePath += getRequest().getContextPath() + "/";
		return basePath;
	}

	@ModelAttribute
	public void init(Map<String, Object> map, BaseHref baseHref) {
		baseHref.setBaseHref(getBasePath());
		map.put("systemConfig", getSystemConfig());
		map.put("adminUser", getAdminUser());
		map.put("baseHref", baseHref);
	}

	/**
	 * 获取用户id用于和其他表关联
	 * 
	 * @return
	 */
	public AdminUser getAdminUser() {

		AdminUser adminUser = null;
		UserDetailsUtils detailsUtils = new UserDetailsUtils();
		UserDetails details = detailsUtils.getDetails();
		if (ValidateUtil.isValid(details)) {
			String username = details.getUsername();
			adminUser = adminUserService.validateByName(username);
		}
		return adminUser;

	}
	/**
	 * 判断是否是火狐浏览器
	 * @return
	 */
	public boolean isFireFox() {
		
		return false;
		
		
	}

}
