package com.sniper.springmvc.hibernate.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.hibernate.dao.BaseDao;
import com.sniper.springmvc.model.MembersConnect;

@Service("membersConnectService")
public class MembersConnectServiceImpl extends BaseServiceImpl<MembersConnect>
		implements MembersConnectService {

	@Resource(name = "membersConnectDao")
	@Override
	public void setDao(BaseDao<MembersConnect> dao) {
		super.setDao(dao);
	}
}
