package com.sniper.springmvc.hibernate.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.hibernate.dao.BaseDao;
import com.sniper.springmvc.model.SetPassword;

@Service("setPasswordService")
public class SetPasswordServiceImpl extends BaseServiceImpl<SetPassword>
		implements SetPasswordService {

	@Resource(name = "setPasswordDao")
	@Override
	public void setDao(BaseDao<SetPassword> dao) {
		super.setDao(dao);
	}

	@Override
	public SetPassword validBySign(String sign) {
		String hql = "from SetPassword where sign = :sign and signaTrue = false order by id desc";
		Map<String, String> params = new HashMap<>();
		params.put("sign", sign);

		List<SetPassword> list = this.findEntityByHQLPage(hql, 0, 1, params);

		if (list.size() == 1) {
			return list.get(0);
		}

		return null;
	}
}
