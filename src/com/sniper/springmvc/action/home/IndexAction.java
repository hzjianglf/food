package com.sniper.springmvc.action.home;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.hibernate.service.impl.PptFileService;
import com.sniper.springmvc.model.AdminUser;
import com.sniper.springmvc.model.Channel;
import com.sniper.springmvc.model.PptFile;
import com.sniper.springmvc.security.SpringContextUtil;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
@RequestMapping("/")
public class IndexAction extends HomeBaseAction {

	

	@Resource
	PptFileService pptFileService;

	@RequestMapping("")
	public String index(Map<String, Object> map) {

		List<PptFile> pptFiles = pptFileService.lastLists(12, null);
		map.put("pptFiles", pptFiles);

		map.put("imageHelpUtil", imageHelpUtil);
		return "home/index/index.ftl";
	}
	
	@RequestMapping("images")
	public String image(Map<String, Object> map) {

		return "home/index/image.ftl";
	}
	

	@RequestMapping("types")
	public String types(Map<String, Object> map,
			@RequestParam(value = "id", required = false) Integer id) {
		// 获取ppt第一级平道列表
		List<Channel> channelsTop = channelService.getChannelsByFid(4, true,
				"sort asc, id asc");
		// 获取二级lanm
		Map<String, List<Channel>> channels = new HashMap<>();

		for (Channel channel : channelsTop) {
			List<Channel> channelsChild = channelService.getChannelsByFid(
					channel.getId(), true, "sort asc, id asc");
			channels.put(String.valueOf(channel.getId()), channelsChild);
		}

		map.put("channelsTop", channelsTop);
		map.put("channels", channels);
		return "home/index/types.ftl";
	}

	@ResponseBody
	@RequestMapping("noLogin")
	public String noLogin(@RequestParam("uname") String uname) {

		if (ValidateUtil.isValid(uname)) {
			AdminUser result = adminUserService.noLogin(uname);
			// 用户不存在
			if (!ValidateUtil.isValid(result.getId())) {
				adminUserService.saveOrUpdateEntiry(result);
			}

			UserDetailsService detail = (UserDetailsService) SpringContextUtil
					.getBean("myUserDetail");
			UserDetails details = null;
			try {

				details = detail.loadUserByUsername(result.getName());
			} catch (Exception e) {
				return "用户未找到";
			}

			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					details, null, details.getAuthorities());

			SecurityContextHolder.getContext().setAuthentication(
					authenticationToken);

			HttpSession session = getRequest().getSession(true); //
			session.setAttribute("SPRING_SECURITY_CONTEXT",
					SecurityContextHolder.getContext());

		}
		return "redirect:/user";
	}

	@RequestMapping("logout")
	public String logout(
			@RequestParam(value = "service", required = false, defaultValue = "") String service) {
		
		try {
			getRequest().getSession(false).invalidate();
		} catch (Exception e) {
			getRequest().getSession().invalidate();
		}
		
		SecurityContextHolder.getContext().setAuthentication(null);
		SecurityContextHolder.clearContext();
		String casLogout = request.getServletContext().getInitParameter(
				"casLogout");
		return "redirect:" + casLogout + "?service=" + service;

	}

}
