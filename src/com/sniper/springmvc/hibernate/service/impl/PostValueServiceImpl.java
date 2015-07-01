package com.sniper.springmvc.hibernate.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.hibernate.dao.BaseDao;
import com.sniper.springmvc.model.PostValue;

@Service("postValue")
public class PostValueServiceImpl extends BaseServiceImpl<PostValue> implements PostValueService{

	@Resource(name = "postValueDao")
	public void setDao(BaseDao<PostValue> dao) {
		super.setDao(dao);
	}
	
}
