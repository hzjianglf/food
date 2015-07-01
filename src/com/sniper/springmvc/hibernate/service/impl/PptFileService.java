package com.sniper.springmvc.hibernate.service.impl;

import java.sql.Date;
import java.util.List;

import com.sniper.springmvc.hibernate.service.BaseService;
import com.sniper.springmvc.model.PptFile;

public interface PptFileService extends BaseService<PptFile> {

	public List<PptFile> lastLists(int limit, Date date);
}
