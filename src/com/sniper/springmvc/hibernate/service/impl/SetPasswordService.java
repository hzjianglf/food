package com.sniper.springmvc.hibernate.service.impl;

import com.sniper.springmvc.hibernate.service.BaseService;
import com.sniper.springmvc.model.SetPassword;

public interface SetPasswordService extends BaseService<SetPassword> {

	public SetPassword validBySign(String sign) ;
}
