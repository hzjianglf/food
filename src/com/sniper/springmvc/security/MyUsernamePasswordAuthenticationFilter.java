package com.sniper.springmvc.security;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sniper.springmvc.action.home.VerifyAction;
import com.sniper.springmvc.coder.RSACoder;
import com.sniper.springmvc.hibernate.service.impl.AdminUserService;
import com.sniper.springmvc.model.AdminUser;
import com.sniper.springmvc.utils.SystemConfigUtil;

public class MyUsernamePasswordAuthenticationFilter extends
		UsernamePasswordAuthenticationFilter {

	// 必须要和验证码保存那一段一致 //com.sniper.survey.struts2.action.admin.VerifyAction
	public static final String VALIDATE_CODE = VerifyAction.getValidatecode();
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";

	private AdminUserService adminUserService;

	public AdminUserService getAdminUserService() {
		return adminUserService;
	}

	public void setAdminUserService(AdminUserService adminUserService) {
		this.adminUserService = adminUserService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {

		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		response.setCharacterEncoding("UTF-8");

		Map<String, String> configs = new HashMap<>();
		configs = SystemConfigUtil.getSystemConfig();

		int loginErrorNumCheck = 10;
		if (configs.get("loginErrorNumCheck") != null) {
			loginErrorNumCheck = Integer.parseInt(configs
					.get("loginErrorNumCheck"));
		}
		// 设置session有效时间
		// 用户登录次数验证
		// 首次登录=0
		long loginErrorNum = 0;
		// 登录时间

		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("SPRING_SECURITY_LOGIN_ERROR_NUM")) {
				loginErrorNum = Integer.valueOf(cookie.getValue());
			}
		}

		loginErrorNum++;
		// 第一次修改登录次数
		Cookie loginNumCookie = new Cookie("SPRING_SECURITY_LOGIN_ERROR_NUM",
				String.valueOf(loginErrorNum));
		loginNumCookie.setMaxAge(3600);
		loginNumCookie.setHttpOnly(true);
		response.addCookie(loginNumCookie);

		if (!request.getMethod().equalsIgnoreCase("post")) {
			throw new AuthenticationServiceException("Invalid request");
		}

		if (loginErrorNum > 100) {
			checkValidateCode(request);
		}

		String username = obtainUsername(request);
		String password = obtainPassword(request);

		username = username.trim();
		password = password.trim();

		if (StringUtils.isEmpty(username)) {
			throw new AuthenticationServiceException("用户名不能为空");
		}
		// 加密用户名
		String privateKey = request.getServletContext().getInitParameter(
				"privateKey");
		// 被加密的字符串
		byte[] data = username.getBytes();
		// 产生签名
		// 加密过程
		byte[] encodeData1 = null;
		try {
			encodeData1 = RSACoder.encryptByPrivateKey(data,
					Base64.decodeBase64(privateKey));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String usernameKey = Base64.encodeBase64String(encodeData1);
		// 保存最后提交的用户名
		Cookie cookie = new Cookie("SPRING_SECURITY_LAST_USERNAME_KEY",
				usernameKey);
		cookie.setMaxAge(2592000);
		response.addCookie(cookie);

		if (StringUtils.isEmpty(password)) {
			throw new AuthenticationServiceException("密码不能为空");
		}
		// 获取数据库比对密码
		AdminUser adminUser = adminUserService.validateByName(username);

		if (adminUser == null) {
			throw new AuthenticationServiceException("用户名不存在");
		}

		password = DigestUtils.sha1Hex(password);

		if (adminUser.getPassword() != null
				&& !adminUser.getPassword().equals(password)) {

			/*
			 * httpSession.setAttribute( SPRING_SECURITY_FORM_USERNAME_KEY,
			 * TextEscapeUtils.escapeEntities(username));
			 */
			/*
			 * 在我们配置的simpleUrlAuthenticationFailureHandler处理登录失败的处理类在这么一段
			 * 这样我们可以在登录失败后，向用户提供相应的信息。
			 */
			/*
			 * if (forwardToDestination) {
			 * request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION,
			 * exception); } else { HttpSession session =
			 * request.getSession(false);
			 * 
			 * if (session != null || allowSessionCreation) {
			 * request.getSession(
			 * ).setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION,
			 * exception); } }
			 */
			// 满足条件登录错误次数大于允许范围,作用一定时间内不能多次登录
			if ((loginErrorNum > loginErrorNumCheck)) {
				// 锁定用户
				adminUser.setLocked(true);
				adminUserService.updateEntiry(adminUser);
				throw new AuthenticationServiceException("用户被锁定,请管理员解锁");
			}
			throw new AuthenticationServiceException("用户名密码不匹配！");
		}
		// 进行到这登录错误次数 = 0
		loginErrorNum = 0;
		// 一切ok登录次数清空
		Cookie loginNumCookie1 = new Cookie("SPRING_SECURITY_LOGIN_ERROR_NUM",
				String.valueOf(loginErrorNum));
		loginNumCookie1.setMaxAge(3600);
		loginNumCookie1.setHttpOnly(true);
		response.addCookie(loginNumCookie1);

		// UsernamePasswordAuthenticationToken实现 Authentication
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				username, password);
		// Place the last username attempted into HttpSession for views
		// 允许子类设置详细属性
		setDetails(request, authenticationToken);
		// 运行UserDetailsService的loadUserByUsername 再次封装Authentication
		return this.getAuthenticationManager()
				.authenticate(authenticationToken);
	}

	/**
	 * 验证session验证码是否一致
	 * 
	 * @param request
	 */
	private void checkValidateCode(HttpServletRequest request) {

		HttpSession session = request.getSession();
		String sessionValidatecode = obtailSessionValidateCode(session);
		// 让上一次的验证码失效,这里被调用2次,第二次调用在myUserDetail回调
		// session.setAttribute(VALIDATE_CODE, null);
		String validateCodeParamter = obtainValidateCodeParamter(request);

		if (StringUtils.isEmpty(validateCodeParamter)
				|| !sessionValidatecode.equalsIgnoreCase(validateCodeParamter)) {
			throw new AuthenticationServiceException("验证码不匹配！");
		}
	}

	/**
	 * 获取参数中的验证码
	 * 
	 * @param request
	 * @return
	 */
	private String obtainValidateCodeParamter(HttpServletRequest request) {
		Object obj = request.getParameter(VALIDATE_CODE);
		return null == obj ? "" : obj.toString();
	}

	/**
	 * 获取出存在session中的验证码
	 * 
	 * @param httpSession
	 * @return
	 */
	private String obtailSessionValidateCode(HttpSession httpSession) {

		Object object = httpSession.getAttribute(VALIDATE_CODE);
		return object == null ? "" : object.toString();
	}

	@Override
	protected String obtainPassword(HttpServletRequest request) {
		Object obj = request.getParameter(PASSWORD);
		return null == obj ? "" : obj.toString();
	}

	@Override
	protected String obtainUsername(HttpServletRequest request) {
		Object obj = request.getParameter(USERNAME);
		return null == obj ? "" : obj.toString();
	}

}
