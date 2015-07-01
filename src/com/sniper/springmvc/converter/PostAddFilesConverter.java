package com.sniper.springmvc.converter;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.sniper.springmvc.hibernate.service.impl.FilesService;
import com.sniper.springmvc.model.Files;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 自定义类型转换器
 * 
 * @author sniper
 * 
 */
@Component
public class PostAddFilesConverter implements Converter<String, Set<Files>> {

	@Resource
	private FilesService filesService;
	
	public PostAddFilesConverter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Set<Files> convert(String source) {

		Set<Files> adminRights = new HashSet<>();

		if (ValidateUtil.isValid(source)) {
			String[] sIDs = source.split(",");
			for (int i = 0; i < sIDs.length; i++) {

				Files adminRight = filesService.getEntity(Integer
						.parseInt(sIDs[i]));
				adminRights.add(adminRight);
			}
		}

		return adminRights;
	}

}
