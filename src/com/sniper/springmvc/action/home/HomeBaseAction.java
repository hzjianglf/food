package com.sniper.springmvc.action.home;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.sniper.springmvc.action.RootAction;
import com.sniper.springmvc.hibernate.service.impl.ChannelService;
import com.sniper.springmvc.hibernate.service.impl.PptFileService;
import com.sniper.springmvc.model.Channel;
import com.sniper.springmvc.utils.BaseHref;

@Controller
public class HomeBaseAction extends RootAction {

	@Resource
	protected ChannelService channelService;

	@Resource
	protected PptFileService pptFileService;

	public HomeBaseAction() {

	}

	@Override
	public void init(Map<String, Object> map, BaseHref baseHref) {

		// 获取ppt第一级平道列表
		List<Channel> channelsTop = channelService.getChannelsByFid(4, true,
				true, "sort asc");
		// 获取二级lanm
		Map<String, List<Channel>> channels = new HashMap<>();

		for (Channel channel : channelsTop) {
			List<Channel> channelsChild = channelService.getChannelsByFid(
					channel.getId(), true, "sort asc");
			channels.put(String.valueOf(channel.getId()), channelsChild);
		}

		map.put("channelsLeftTop", channelsTop);
		map.put("channelsLeft", channels);
		super.init(map, baseHref);
	}

	/**
	 * ztree数据
	 * 
	 * @return
	 */
	public String getTreePostMap() {

		String tree = channelService.getChannelListByTypeForTree(
				new Integer[] { 2 }, true, "sort asc, id asc", ",nocheck:true");
		return tree;
	}

	/**
	 * 读取PPT二级第一层栏目
	 * 
	 * @return
	 */
	public Map<String, String> getSelectChannelsForFid() {
		List<Channel> channels = channelService.getChannelsByFid(4, true,
				"sort asc");
		Map<String, String> map = new LinkedHashMap<>();
		for (Channel channel : channels) {
			map.put(String.valueOf(channel.getId()), channel.getName());
		}
		return map;
	}

	/**
	 * 获取 二层栏目
	 * 
	 * @return
	 */
	public Map<String, List<Channel>> getSelectChannelsForChild(
			Map<String, String> fids) {
		Map<String, List<Channel>> selects = new HashMap<>();

		for (Map.Entry<String, String> entry : fids.entrySet()) {
			selects.put(
					entry.getKey(),
					channelService.getChannelsByFid(
							Integer.valueOf(entry.getKey()), true, "sort asc"));
		}

		return selects;
	}

}
