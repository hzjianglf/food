package com.sniper.springmvc.hibernate.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.hibernate.dao.BaseDao;
import com.sniper.springmvc.model.Files;
import com.sniper.springmvc.utils.FilesUtil;

//15264112711
@Service("filesService")
public class FilesServiceImpl extends BaseServiceImpl<Files> implements
		FilesService {

	@Override
	@Resource(name = "filesDao")
	public void setDao(BaseDao<Files> dao) {
		super.setDao(dao);
	}

	@Override
	public Files getFileByUrl(String url) {
		String hql = "from Files f where f.filePath = :path";

		Map<String, Object> params1 = new HashMap<>();
		params1.put("path", url);
		return (Files) this.uniqueResult(hql, params1);
	}

	@Override
	public void deleteEntiry(Files t) {

		FilesUtil.delFileByPath(t.getNewPath());

		super.deleteEntiry(t);
	}

}
