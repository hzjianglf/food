package com.sniper.springmvc.hibernate.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import com.sniper.springmvc.hibernate.dao.BaseDao;
import com.sniper.springmvc.model.Members;
import com.sniper.springmvc.utils.ValidateUtil;

@Service("membersService")
public class MembersServiceImpl extends BaseServiceImpl<Members> implements
		MembersService {

	@Resource(name = "membersDao")
	@Override
	public void setDao(BaseDao<Members> dao) {
		super.setDao(dao);
	}

	/**
	 * 
	 */
	@Override
	public Members validateByName(String name) {

		String hql = "from Members where name = :name";
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		return (Members) this.uniqueResult(hql, params);
	}

	@Override
	public Members validateByNickName(String name) {
		String hql = "from Members where nickName = :name";
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		return (Members) this.uniqueResult(hql, params);
	}

	@Override
	public Members validateByEmail(String email) {
		String hql = "from Members where email = :email";
		Map<String, String> params = new HashMap<>();
		params.put("email", email);
		return (Members) this.uniqueResult(hql, params);
	}

	@Override
	public List<Members> findListsByEmail(String email) {

		String hql = "from Members where email like :email order by id desc limit 10";
		Map<String, String> params = new HashMap<>();
		params.put("email", email);
		return this.findEntityByHQL(hql, params);
	}

	@Override
	public boolean validateUser(String name, String password) {
		password = DigestUtils.sha1Hex(password);
		String hql = "select count(a) from Members a where a.name=:name and a.password = :password";
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		params.put("password", password);
		long l = (long) this.uniqueResult(hql, params);
		if (l == 1) {
			return true;
		}
		return false;
	}

	/**
	 * 用户修改添加删除 更改用户密码必须使用这个
	 */
	@Override
	public void saveOrUpdateEntiry(Members t) {

		if (ValidateUtil.isValid(t.getPassword())
				&& t.getPassword().length() != 40) {

			String password = t.getPassword();
			password = DigestUtils.sha1Hex(password);
			t.setPassword(password);
		}

		super.saveOrUpdateEntiry(t);

	}

	/**
	 * 查询用户一些公共信息
	 */
	@Override
	public Members getEntity(Integer id) {
		Members Members = super.getEntity(id);
		return Members;
	}

	/**
	 * 查找用户同级别的其他用户
	 */
	@Override
	public List<Members> getUserByLevel(int gid) {
		// 首先查处级别
		String hql = "SELECT a FROM Members a join a.adminGroup ag WHERE ag.id = "
				+ gid;
		List<Members> adminUsers = this.findCEntityByHQL(hql);
		return adminUsers;
	}

}
