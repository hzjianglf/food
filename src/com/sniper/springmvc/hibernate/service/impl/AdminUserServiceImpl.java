package com.sniper.springmvc.hibernate.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import com.sniper.springmvc.hibernate.dao.BaseDao;
import com.sniper.springmvc.model.AdminGroup;
import com.sniper.springmvc.model.AdminUser;
import com.sniper.springmvc.utils.ValidateUtil;

@Service("adminUserService")
public class AdminUserServiceImpl extends BaseServiceImpl<AdminUser> implements
		AdminUserService {

	@Resource(name = "adminUserDao")
	public void setDao(BaseDao<AdminUser> dao) {
		super.setDao(dao);
	}

	/**
	 * 判断用户名是否注册
	 * 
	 * @param name
	 * @return
	 */
	public boolean isRegisted(String name) {
		String hql = "FROM AdminUser where name = :name";
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		List<AdminUser> adminUsers = this.findEntityByHQL(hql, params);
		return ValidateUtil.isValid(adminUsers) ? false : true;
	}

	/**
	 * 
	 */
	@Override
	public AdminUser validateByName(String name) {

		String hql = "from AdminUser where name = :name";
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		return (AdminUser) this.uniqueResult(hql, params);
	}

	@Override
	public AdminUser validateByNickName(String name) {
		String hql = "from AdminUser where nickName = :name";
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		return (AdminUser) this.uniqueResult(hql, params);
	}

	@Override
	public AdminUser validateByEmail(String email) {
		String hql = "from AdminUser where email = :email";
		Map<String, String> params = new HashMap<>();
		params.put("email", email);
		return (AdminUser) this.uniqueResult(hql, params);
	}

	@Override
	public List<AdminUser> findListsByEmail(String email) {

		String hql = "from AdminUser where email like :email order by id desc limit 10";
		Map<String, String> params = new HashMap<>();
		params.put("email", email);
		return this.findEntityByHQL(hql, params);
	}

	@Override
	public boolean validateUser(String name, String password) {
		password = DigestUtils.sha1Hex(password);
		String hql = "select count(a) from AdminUser a where a.name=:name and a.password = :password";
		Map<String, String> params = new LinkedHashMap<>();
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
	public void saveOrUpdateEntiry(AdminUser t) {

		if (ValidateUtil.isValid(t.getPassword())
				&& t.getPassword().length() != 40) {

			String password = t.getPassword();
			password = DigestUtils.sha1Hex(password);
			t.setPassword(password);
		}

		super.saveOrUpdateEntiry(t);

	}

	@Override
	public AdminUser getEntity(Integer id) {
		AdminUser adminUser = super.getEntity(id);
		adminUser.getAdminGroup().size();
		return adminUser;
	}

	@Override
	public List<AdminUser> getUserByGroup(int gid) {
		String hql = "SELECT a FROM AdminUser a join a.adminGroup ag WHERE ag.id = "
				+ gid;
		List<AdminUser> adminUsers = this.findCEntityByHQL(hql);
		return adminUsers;
	}

	@Override
	public List<AdminUser> getCUserByGroup(int gid) {
		return getUserByGroup(gid);
	}

	@Override
	public AdminUser noLogin(String uname) {

		// 检查用户

		AdminUser adminUser = this.validateByName(uname);

		if (adminUser == null) {
			adminUser = new AdminUser();
			adminUser.setName(uname);
			adminUser.setPassword("123456");
			Set<AdminGroup> adminGroups = new HashSet<>();
			AdminGroup adminGroup = new AdminGroup(3);
			adminGroups.add(adminGroup);
			adminUser.setAdminGroup(adminGroups);
			adminUser.setEnabled(true);

		}
		return adminUser;
	}

}
