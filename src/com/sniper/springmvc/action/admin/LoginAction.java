package com.sniper.springmvc.action.admin;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sniper.springmvc.coder.RSACoder;
import com.sniper.springmvc.hibernate.service.impl.AdminUserService;
import com.sniper.springmvc.hibernate.service.impl.MailService;
import com.sniper.springmvc.security.SpringContextUtil;
import com.sniper.springmvc.utils.BaseHref;
import com.sniper.springmvc.utils.ValidateUtil;

@RequestMapping(value = "/admin")
@Controller
public class LoginAction extends BaseAction {

	@Resource
	private MailService mailService;

	@Resource
	private JavaMailSenderImpl sender;

	@Resource
	private AdminUserService adminUserService;

	@ModelAttribute
	public void init(Map<String, Object> map, BaseHref baseHref) {
		super.init(map, baseHref);
	}

	@RequestMapping(value = "login")
	public String login(
			Map<String, Object> map,
			@RequestParam(value = "error", required = false, defaultValue = "false") Boolean error) {

		// 解密
		Cookie[] cookie = getRequest().getCookies();
		if (ValidateUtil.isValid(cookie)) {
			for (Cookie cookie2 : cookie) {

				switch (cookie2.getName()) {
				case "SPRING_SECURITY_LAST_USERNAME_KEY":
					String publicKey = getRequest().getServletContext()
							.getInitParameter("publicKey");
					// 产生签名
					try {
						if (ValidateUtil.isValid(cookie2.getValue())) {
							byte[] decodeData1 = RSACoder.decryptByPublicKey(
									Base64.decodeBase64(cookie2.getValue()),
									Base64.decodeBase64(publicKey));
							String usernameKey = new String(decodeData1);
							map.put("lastLoginName", usernameKey);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case "SPRING_SECURITY_LOGIN_ERROR_NUM":
					String loginNum = cookie2.getValue();
					map.put("loginNum", loginNum);
					break;

				default:
					break;
				}

			}
		}

		// 获取session里面的错误
		HttpSession session = request.getSession();
		if (error
				&& session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION") != null) {

			Exception exception = (Exception) session
					.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
			map.put("loginError", exception.getMessage());

		}

		return "admin/login/index.ftl";
	}

	@RequestMapping(value = "nologin", method = RequestMethod.GET)
	public String apiLogin(@RequestParam("username") String username,
			Map<String, Object> map) {

		map.put("msg", "密码丢失");
		if (ValidateUtil.isValid(username)) {

			UserDetailsService detail = (UserDetailsService) SpringContextUtil
					.getBean("myUserDetail");
			UserDetails details = null;
			try {
				details = detail.loadUserByUsername(username);
			} catch (Exception e) {
				map.put("msg", "用户未找到");
				return "admin/login/nologin.ftl";
			}

			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					details, null, details.getAuthorities());

			SecurityContextHolder.getContext().setAuthentication(
					authenticationToken);

			HttpSession session = getRequest().getSession(true); //
			// 在session中存放security context,方便同一个session中控制用户的其他操作
			session.setAttribute("SPRING_SECURITY_CONTEXT",
					SecurityContextHolder.getContext());

			return "redirect:/admin/";
		}
		return "admin/login/nologin.ftl";
	}

	@RequestMapping("logout")
	public String logout(
			@RequestParam(value = "service", required = false, defaultValue = "") String service) {

		getRequest().getSession(false).invalidate();
		SecurityContextHolder.getContext().setAuthentication(null);
		SecurityContextHolder.clearContext();
		String casLogout = request.getServletContext().getInitParameter(
				"casLogout");
		return "redirect:" + casLogout + "?service=" + service;

	}
}