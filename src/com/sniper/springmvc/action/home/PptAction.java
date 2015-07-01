package com.sniper.springmvc.action.home;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sniper.springmvc.hibernate.service.impl.ChannelService;
import com.sniper.springmvc.hibernate.service.impl.PptFileService;
import com.sniper.springmvc.model.Channel;
import com.sniper.springmvc.model.PptFile;
import com.sniper.springmvc.utils.HqlUtils;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ValidateUtil;

@RequestMapping("/ppt")
@Controller
public class PptAction extends HomeBaseAction {

	@Resource
	private ChannelService channelService;

	@Resource
	PptFileService pptFileService;

	@RequestMapping("")
	public String index(Map<String, Object> map,
			@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "id", required = false) Integer id) {

		// 获取当前栏目的信息和伏击的信息
		HqlUtils hqlUtils = new HqlUtils();
		hqlUtils.setEntityName("PptFile");
		String ean = hqlUtils.getEntityAsName();

		Map<String, Object> params = new HashMap<>();
		if (ValidateUtil.isValid(key)) {
			hqlUtils.andWhere(ean + ".name  like :name");
			hqlUtils.orWhere(ean + ".tags  like :name");
			params.put("name", "%" + key + "%");
			
		}
		hqlUtils.andWhere(ean + ".enabled  = :enabled ");
		params.put("enabled", true);

		if (ValidateUtil.isValid(id)) {
			hqlUtils.setWhere("and  " + "channel.id = :cid");
			params.put("cid", id);
		}

		hqlUtils.setDistinct(true);
		hqlUtils.setOrder(ean + ".id desc");
		hqlUtils.setParams(params);

		int count = pptFileService.pageCountList(hqlUtils);
		PageUtil page = new PageUtil(count, 20);
		page.setRequest(request);
		String pageHtml = page.show();
		List<PptFile> lists = pptFileService.pageList(hqlUtils,
				page.getFristRow(), page.getListRow());
		map.put("pageHtml", pageHtml);
		map.put("lists", lists);
		map.put("key", key);

		if (ValidateUtil.isValid(id)) {
			// 获取当前栏目的信息和父级的信息
			Channel channel = channelService.getCEntity(id);
			if (ValidateUtil.isValid(channel)) {
				Channel channelTop = channelService
						.getCEntity(channel.getFid());
				map.put("channel", channel);
				map.put("channelTop", channelTop);
			}
		}
		return "home/ppt/list.ftl";
	}

	@RequestMapping("show/{id}")
	public String show(Map<String, Object> map, @PathVariable Integer id) {

		PptFile pptFile = pptFileService.getCEntity(id);

		if (ValidateUtil.isValid(pptFile) && ValidateUtil.isValid(id)
				&& pptFile.getChannel() != null) {
			// 获取当前栏目的信息和伏击的信息
			Channel channel = channelService.getCEntity(pptFile.getChannel()
					.getId());
			if (ValidateUtil.isValid(channel) && channel.getFid() != 4) {
				Channel channelTop = channelService
						.getCEntity(channel.getFid());

				map.put("channelTop", channelTop);
			}

			map.put("channel", channel);

		} else {
			pptFile = new PptFile();
		}

		map.put("pptFile", pptFile);

		return "home/ppt/show.ftl";

	}

	@RequestMapping("download/{id}")
	public String download(@PathVariable("id") Integer id,
			Map<String, Object> map) {
		
		if(!ValidateUtil.isValid(getAdminUser())){
			return "redirect:/";
		}
		
		try {
			PptFile file = pptFileService.getEntity(id);

			if (ValidateUtil.isValid(file)) {
				map.put("id", file.getPpt().getId());

				file.setDownload(file.getDownload() + 1);
				pptFileService.saveOrUpdateEntiry(file);

				return "redirect:/file-info/download";
			}

		} catch (Exception e) {
		}

		return "redirect:/";
	}
}
