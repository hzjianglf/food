package com.sniper.springmvc.converter;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.sniper.springmvc.hibernate.service.impl.ChannelService;
import com.sniper.springmvc.model.Channel;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 自定义类型转换器
 * 
 * @author sniper
 * 
 */
@Component
public class PostAddChannelsConverter implements
		Converter<String, Set<Channel>> {

	@Resource
	private ChannelService channelService;
	
	public PostAddChannelsConverter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Set<Channel> convert(String source) {

		Set<Channel> channels = new HashSet<>();

		if (ValidateUtil.isValid(source)) {
			String[] sIDs = source.split(",");
			for (int i = 0; i < sIDs.length; i++) {

				Channel channel = channelService.getEntity(Integer
						.valueOf(sIDs[i]));

				channels.add(channel);
			}

		}

		return channels;
	}

}
