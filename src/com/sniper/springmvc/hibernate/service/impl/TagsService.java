package com.sniper.springmvc.hibernate.service.impl;

import java.util.List;

import com.sniper.springmvc.hibernate.service.BaseService;
import com.sniper.springmvc.model.Tags;

public interface TagsService extends BaseService<Tags> {

	
	public List<Tags> getTagsByName(String name) ;
}
