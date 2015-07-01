package com.sniper.springmvc.converter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.sniper.springmvc.hibernate.service.impl.AdminGroupService;
import com.sniper.springmvc.model.AdminGroup;

/**
 * 用户添加是用户组的转换
 * 
 * @author sniper
 * 
 */
@Component
public class GroupAddUserConverter implements
		Converter<String[], Set<AdminGroup>> {

	public GroupAddUserConverter() {
		// TODO Auto-generated constructor stub
	}
	@Resource
	AdminGroupService adminGroupService;

	@Override
	public Set<AdminGroup> convert(String[] f) {
		System.out.println(Arrays.asList(f));
		Set<AdminGroup> adminGroups = new HashSet<>();
		if (f.length > 0) {

			for (int i = 0; i < f.length; i++) {
				AdminGroup adminGroup = adminGroupService.getEntity(f[i]);
				adminGroups.add(adminGroup);
			}

		}
		return adminGroups;
	}

}
