package com.sniper.springmvc.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.hibernate.service.impl.TagsService;
import com.sniper.springmvc.model.Tags;
import com.sniper.springmvc.utils.BackTags;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
@RequestMapping("/public")
public class PublicAction extends RootAction {

	@Resource
	private TagsService tagsService;

	@ResponseBody
	@RequestMapping("sendAjaxTags")
	public List<BackTags> sendAjaxTags(@RequestParam("term") String name) {

		List<BackTags> backTagss = new ArrayList<>();

		if (ValidateUtil.isValid(name)) {
			List<Tags> lists = tagsService.getTagsByName(name);

			if (lists.size() == 0) {
				// 插入关键词
			}

			for (Tags tags2 : lists) {
				BackTags backTags = new BackTags(tags2.getName(),
						tags2.getName(), tags2.getName());
				backTagss.add(backTags);
			}
		}

		return backTagss;

	}

	@ResponseBody
	@RequestMapping("addAjaxTags")
	public List<BackTags> addAjaxTags(@RequestParam("term") String name) {

		List<BackTags> backTagss = new ArrayList<>();

		if (ValidateUtil.isValid(name)) {
			List<Tags> lists = tagsService.getTagsByName(name);
			if (lists.size() == 0) {
				// 插入关键词
				Tags tags = new Tags();
				tags.setAdminUser(getAdminUser());
				tags.setName(name);
				tagsService.saveOrUpdateEntiry(tags);
			}

		}

		return backTagss;

	}

}
