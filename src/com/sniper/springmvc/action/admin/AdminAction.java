package com.sniper.springmvc.action.admin;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
public class AdminAction extends BaseAction {

	/**
	 * 
	 * @return
	 */
	@RequestMapping("/")
	public String index(Map<String, Object> map) {

		
		map.put("admin", "======================");
		return "admin/admin/index.ftl";

	}

}
