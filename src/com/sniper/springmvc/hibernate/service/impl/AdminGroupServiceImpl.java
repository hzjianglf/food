package com.sniper.springmvc.hibernate.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.sniper.springmvc.hibernate.dao.BaseDao;
import com.sniper.springmvc.model.AdminGroup;

@Service("adminGroupService")
public class AdminGroupServiceImpl extends BaseServiceImpl<AdminGroup>
		implements AdminGroupService {

	@Resource(name = "adminGroupDao")
	public void setDao(BaseDao<AdminGroup> dao) {

		super.setDao(dao);
	}

	@Override
	public List<AdminGroup> getGroupSelectList(String where) {
		String hql = "SELECT new AdminGroup(g.id, g.name) FROM AdminGroup g ";

		if (null != where || "".equals(where)) {
			hql += where;
		}
		List<AdminGroup> adminGroups = this.findEntityByHQL(hql);
		return adminGroups;
	}

	@Override
	public List<AdminGroup> getGroupList(Integer[] id) {

		String hql = " from AdminGroup where id in("
				+ StringUtils.join(id, ",") + ")";

		return this.findEntityByHQL(hql);
	}

	@Override
	public AdminGroup getEntity(Integer id) {
		AdminGroup adminGroup = super.getEntity(id);
		adminGroup.getAdminRight().size();
		return adminGroup;
	}

}
