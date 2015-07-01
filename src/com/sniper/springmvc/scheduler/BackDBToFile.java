package com.sniper.springmvc.scheduler;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 数据库备份到文件
 * 
 * @author sniper
 * 
 */
public class BackDBToFile implements Job {

	// mysqldump -hhostname -uusername -ppassword databasename | gzip >
	// backupfile.sql.gz
	/**
	 * 数据源
	 */
	private static ComboPooledDataSource dataSource;
	/**
	 * 备份路径
	 */
	private static String backToFilePath;
	/**
	 * 备份文件限制数量
	 */
	private static int fileCount = 10;

	public void setBackToFilePath(String backToFilePath) {
		if (!backToFilePath.endsWith("/")) {
			backToFilePath = backToFilePath + "/";
		}
		BackDBToFile.backToFilePath = backToFilePath;
	}

	public String getBackToFilePath() {
		return backToFilePath;
	}

	public void setDataSource(ComboPooledDataSource dataSource) {
		BackDBToFile.dataSource = dataSource;
	}

	public void setFileCount(int fileCount) {
		BackDBToFile.fileCount = fileCount;
	}

	public int getFileCount() {
		return fileCount;
	}

	public ComboPooledDataSource getDataSource() {
		return dataSource;
	}

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		String url = dataSource.getJdbcUrl();
		String host = url
				.substring(url.indexOf("//") + 2, url.lastIndexOf("/"));
		String ip = host.substring(0, host.indexOf(":"));
		String db = url.substring(url.lastIndexOf("/") + 1, url.indexOf("?"));
		String port = host.substring(host.indexOf(":") + 1);
		String user = dataSource.getUser();
		String pwd = dataSource.getPassword();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();

		StringBuffer buffer = new StringBuffer();
		buffer.append("/usr/bin/mysqldump -h");
		buffer.append(ip);
		buffer.append(" -p");
		buffer.append(port);
		buffer.append(" -u");
		buffer.append(user);
		buffer.append(" -p");
		buffer.append(pwd);
		buffer.append(" ");
		buffer.append(db);
		buffer.append(" | gzip > ");
		buffer.append(getBackToFilePath());
		buffer.append(dateFormat.format(date));
		buffer.append(".sql.gz");
		try {
			Runtime.getRuntime().exec(
					new String[] { "sh", "-c", buffer.toString() });
		} catch (IOException e) {
			e.printStackTrace();
		}

		File file = new File(getBackToFilePath());
		if (file.isDirectory()) {
			if (file.listFiles().length > getFileCount()) {
				// 删除多余的文件
				// 根据文件名称排序,
				File[] files = file.listFiles();
				int fileLength = files.length;
				int fileDiff = fileLength - getFileCount();
				// 正序删除,删除排名前几个的
				for (int i = 0; i < fileLength; i++) {
					if (i <= fileDiff) {
						files[i].delete();
					}
				}

			}
		}

		// JobKey key = context.getJobDetail().getKey();
		// JobDataMap dataMap = context.getJobDetail().getJobDataMap();

	}

}
