package com.sniper.springmvc.converter;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.sniper.springmvc.hibernate.service.impl.AdminRightService;
import com.sniper.springmvc.model.AdminRight;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 自定义类型转换器
 * 
 * @author sniper
 * 
 */
@Component
final public class GroupAddRightConverter implements
		Converter<String, Set<AdminRight>> {

	
	public GroupAddRightConverter() {
		
	}
	@Resource
	private AdminRightService adminRightService;

	@Override
	public Set<AdminRight> convert(String source) {

		Set<AdminRight> adminRights = new HashSet<>();

		if (ValidateUtil.isValid(source)) {
			String[] sIDs = source.split(",");
			for (int i = 0; i < sIDs.length; i++) {

				AdminRight adminRight = adminRightService.getEntity(Integer
						.parseInt(sIDs[i]));
				adminRights.add(adminRight);
			}
		}

		return adminRights;
	}

}
