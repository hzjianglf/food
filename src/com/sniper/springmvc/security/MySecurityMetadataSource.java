package com.sniper.springmvc.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.sniper.springmvc.hibernate.service.impl.AdminRightService;
import com.sniper.springmvc.model.AdminGroup;
import com.sniper.springmvc.model.AdminRight;

/**
 * 该过滤器的主要作用就是通过spring著名的IoC生成securityMetadataSource。
 * securityMetadataSource相当于本包中自定义的MyInvocationSecurityMetadataSourceService。
 * 该MyInvocationSecurityMetadataSourceService的作用提从数据库提取权限和资源，装配到HashMap中，
 * 供Spring Security使用，用于权限校验。
 * 
 * @author sparta 11/3/29
 */
public class MySecurityMetadataSource implements
		FilterInvocationSecurityMetadataSource {

	@Resource
	private AdminRightService adminRightService;

	private static Map<String, Collection<ConfigAttribute>> rightMap = new HashMap<>();

	public static Map<String, Collection<ConfigAttribute>> getRightMap() {
		return rightMap;
	}

	public static void setRightMap(
			Map<String, Collection<ConfigAttribute>> rightMap) {
		MySecurityMetadataSource.rightMap = rightMap;
	}

	public void setAdminRightService(AdminRightService adminRightService) {
		this.adminRightService = adminRightService;
	}

	/**
	 * 加载所有资源与权限的关系
	 */
	private void loadResourceDefine() {

		List<AdminRight> adminRights = this.adminRightService.springRight();
		for (AdminRight right : adminRights) {
			Collection<ConfigAttribute> configAttributes = new ArrayList<>();
			for (AdminGroup adminGroup : right.getAdminGroup()) {
				ConfigAttribute configAttribute = new SecurityConfig(
						adminGroup.getValue());
				configAttributes.add(configAttribute);
			}

			rightMap.put(right.getUrl(), configAttributes);
		}
	}

	@SuppressWarnings("unused")
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return new ArrayList<ConfigAttribute>();
	}

	/**
	 * 返回所请求资源所需要的权限
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {

		FilterInvocation filterInvocation = (FilterInvocation) object;
		HttpServletRequest request = filterInvocation.getHttpRequest();
		String requestUrl = filterInvocation.getRequestUrl();

		if (rightMap.isEmpty()) {
			loadResourceDefine();
		}
		if (requestUrl.indexOf("?") != -1) {
			requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf("?"));
		}
		RequestMatcher requestMatcher = new AntPathRequestMatcher(requestUrl);

		if (requestMatcher.matches(request)) {
			if (rightMap.get(requestUrl) != null) {
				return rightMap.get(requestUrl);
			}
		}
		// 如果加上,整个网站都无法访问
		// throw new AccessDeniedException("errorNotRight");
		return null;
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

}
