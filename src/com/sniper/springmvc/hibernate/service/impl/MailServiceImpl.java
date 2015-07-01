package com.sniper.springmvc.hibernate.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.hibernate.dao.BaseDao;
import com.sniper.springmvc.model.Mail;

@Service("mailService")
public class MailServiceImpl extends BaseServiceImpl<Mail> implements
		MailService {

	@Override
	@Resource(name = "mailDao")
	public void setDao(BaseDao<Mail> dao) {
		super.setDao(dao);
	}

	@Override
	public Mail getEntity(String id) {

		Mail mail = super.getEntity(id);
		return mail;
	}
}
