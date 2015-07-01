package com.sniper.springmvc.hibernate.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.id.UUIDHexGenerator;
import org.springframework.stereotype.Service;

import com.sniper.springmvc.hibernate.dao.BaseDao;
import com.sniper.springmvc.model.Log;
import com.sniper.springmvc.utils.LogUtil;

@Service("logService")
public class LogServiceImpl extends BaseServiceImpl<Log> implements LogService {

	@Resource(name = "logDao")
	public void setDao(BaseDao<Log> dao) {
		super.setDao(dao);
	}

	@Override
	public void createLogTable(String tableName) {
		String sql = "CREATE TABLE IF NOT EXISTS  " + tableName + " like mc_log";
		this.executeSQL(null, sql);
	}

	/**
	 * 重写该方法动态插入数据表
	 */
	@Override
	public void saveEntiry(Log t) {
		String sql = "insert into " + LogUtil.generateLogTableNameByYear(0)
				+ " (id,user,name,params,result,resultMsg,time) "
				+ "values(?,?,?,?,?,?,?)";
		UUIDHexGenerator uuid = new UUIDHexGenerator();
		String id = (String) uuid.generate(null, null);
		this.executeSQL(Log.class, sql, id, t.getUser(), t.getName(),
				t.getParams(), t.getResult(), t.getResultMsg(), t.getTime());
	}

	public void test() {
		String sql = "select table_name from tables where table_schema = 'taishan' "
				+ " and table_name like 'mc_logs_%' and table_name < '2014_8'"
				+ " order table_name";
	}

	public List<Log> findNearesLogs(int n) {
		String tableName = LogUtil.generateLogTableNameByYear(0);

		// 查询最近的日志表名称
		String sql = "select table_name from information_schema.tables "
				+ " where table_schema = 'taishan' "
				+ " and table_name like 'mc_logs_%' " + " and table_name <= '"
				+ tableName + "'" + " order by table_name desc limit 0," + n;
		List<String> lists = this.findEntityBySQLQuery(sql).list();
		String logSql = "";
		String logName = "";
		for (int i = 0; i < lists.size(); i++) {
			logName = lists.get(i);
			if (i == (lists.size() - 1)) {
				logSql = logSql + "select * from " + logName;
			} else {
				logSql = logSql + "select * from " + logName + "union";
			}
		}

		return this.findEntityBySQLQuery(logSql).list();
	}
}
