package com.sniper.springmvc.hibernate.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.hibernate.dao.BaseDao;
import com.sniper.springmvc.model.AdminRight;

@Service("adminRightService")
public class AdminRightServiceImpl extends BaseServiceImpl<AdminRight>
		implements AdminRightService {

	@Resource(name = "adminRightDao")
	public void setDao(BaseDao<AdminRight> dao) {
		super.setDao(dao);
	}

	@Override
	public void addRgiht() {

	}

	/**
	 * 保存更新操作
	 */
	@Override
	public void saveOrUpdate(AdminRight r) {
		int pos = 0;
		long code = 1L;
		if (r.getId() == null) {

			String hql = "select max(a.pos),max(a.code) from AdminRight a "
					+ " where a.pos = (select max(aa.pos) from AdminRight aa )";

			Object[] arr = (Object[]) this.uniqueResult(hql);
			Integer topPos = (Integer) arr[0];
			Long topCode = (Long) arr[1];
			if (topPos == null) {
				pos = 0;
				code = 1L;
			} else {
				// 权限吗检测
				if (topCode >= (1L << 60)) {
					pos = topPos + 1;
					code = 1L;
				} else {
					pos = topPos;
					code = topCode << 1;
				}
			}

			r.setCode(code);
			r.setPos(pos);
		}
		/*
		 * //数据源操作demo,可以完成住从 RightToken token = new RightToken();
		 * token.setRight(r); // 绑定令牌 RightToken.bindToken(token);
		 */

		this.saveOrUpdateEntiry(r);

	}

	/**
	 * 添加 url
	 */
	@Override
	public void appendRightByURL(String url) {

		String hql = "select count(r) from AdminRight r where r.url = ?";
		Long long1 = (Long) this.uniqueResult(hql, url);
		if (long1 == 0) {
			AdminRight right = new AdminRight();
			right.setUrl(url);
			this.saveOrUpdate(right);
		}

	}

	/**
	 * 获取最大权限位
	 */
	@Override
	public int getMaxRightPos() {
		String hql = "select max(r.pos) from AdminRight r";
		Integer pos = (Integer) this.uniqueResult(hql);
		return pos == null ? 0 : pos;
	}

	/**
	 * 获取spring可用的url 加get可能会生成2个缓存 此方法为spring security专用
	 * 
	 * @return
	 */
	@Override
	public List<AdminRight> springRight() {

		List<AdminRight> adminRights = this.findAllEntitles();
		for (AdminRight right : adminRights) {
			right.getAdminGroup().size();
		}
		return adminRights;

	}

	@Override
	public AdminRight getCRightByUrl(String url) {
		String hql = "select a from AdminRight a where url = ?";
		AdminRight adminRight = (AdminRight) this.uniqueResult(hql, url);
		return adminRight;
	}

}
