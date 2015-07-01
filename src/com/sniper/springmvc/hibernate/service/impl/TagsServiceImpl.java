package com.sniper.springmvc.hibernate.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.hibernate.dao.BaseDao;
import com.sniper.springmvc.model.Tags;

@Service("tagsService")
public class TagsServiceImpl extends BaseServiceImpl<Tags> implements
		TagsService {

	@Resource(name = "tagsDao")
	public void setDao(BaseDao<Tags> dao) {
		super.setDao(dao);
	}

	@Override
	public List<Tags> getTagsByName(String name) {

		String hql = "from Tags  where name like :name";
		Map<String, String> params = new HashMap<>();
		params.put("name", "%" + name + "%");
		List<Tags> tags = super.findEntityByHQL(hql, params);

		return tags;
	}
}
