package com.sniper.springmvc.hibernate.service.impl;

import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.hibernate.dao.BaseDao;
import com.sniper.springmvc.model.Files;
import com.sniper.springmvc.model.PptFile;
import com.sniper.springmvc.utils.FilesUtil;

@Service("pptFileService")
public class PptFileServiceImpl extends BaseServiceImpl<PptFile> implements
		PptFileService {

	@Resource
	FilesService filesService;

	@Resource(name = "pptFileDao")
	@Override
	public void setDao(BaseDao<PptFile> dao) {
		super.setDao(dao);
	}

	@Override
	public List<PptFile> lastLists(int limit, Date date) {

		Map<String, Object> params = new HashMap<>();
		String where = "";

		if (date != null) {
			where = " and createDate >=:data";
			params.put("data", date);
		}
		String hql = "from PptFile where enabled = true " + where
				+ " order by sort desc, id desc";

		List<PptFile> files = this.findEntityByHQLPage(hql, 0, limit, params);
		return files;
	}

	@Override
	public void deleteEntiry(PptFile t) {

		try {
			Files files = filesService.getEntity(t.getPpt().getId());
			FilesUtil.delFileByPath(files.getNewPath());

			Iterator<Files> its = t.getFiles().iterator();
			while (its.hasNext()) {
				Files files2 = filesService.getEntity(its.next().getId());
				FilesUtil.delFileByPath(files2.getNewPath());
			}

			FilesUtil.delFileByPath(t.getFlash());
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.deleteEntiry(t);
	}

}
