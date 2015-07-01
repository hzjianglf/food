package com.sniper.springmvc.hibernate.service.impl;

import java.util.List;

import com.sniper.springmvc.hibernate.service.BaseService;
import com.sniper.springmvc.model.Log;

public interface LogService extends BaseService<Log> {

	void createLogTable(String tableName);

	public List<Log> findNearesLogs(int n);

}
