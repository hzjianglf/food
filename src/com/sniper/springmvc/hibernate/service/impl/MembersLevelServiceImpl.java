package com.sniper.springmvc.hibernate.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.hibernate.dao.BaseDao;
import com.sniper.springmvc.model.MembersLevel;

@Service("membersLevelService")
public class MembersLevelServiceImpl extends BaseServiceImpl<MembersLevel>
		implements MembersLevelService {

	@Resource(name = "membersLevelDao")
	@Override
	public void setDao(BaseDao<MembersLevel> dao) {
		super.setDao(dao);
	}
}
