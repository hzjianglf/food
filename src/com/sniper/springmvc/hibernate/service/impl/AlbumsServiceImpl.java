package com.sniper.springmvc.hibernate.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.hibernate.dao.BaseDao;
import com.sniper.springmvc.model.Albums;

@Service("albumsService")
public class AlbumsServiceImpl extends BaseServiceImpl<Albums> implements
		AlbumsService {

	@Resource(name = "albumsDao")
	@Override
	public void setDao(BaseDao<Albums> dao) {
		super.setDao(dao);
	}
}
