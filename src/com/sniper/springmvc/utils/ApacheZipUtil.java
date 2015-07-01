package com.sniper.springmvc.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

public class ApacheZipUtil {

	public String zip() throws IOException {
		File f = new File("中文测试.txt");
		ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(
				new FileOutputStream("zipTest.zip"), 1024));
		zos.putNextEntry(new ZipEntry("中国人.txt"));
		
		DataInputStream dis = new DataInputStream(new BufferedInputStream(
				new FileInputStream(f)));
		zos.putNextEntry(new ZipEntry(f.getName()));
		int c;
		while ((c = dis.read()) != -1) {
			zos.write(c);
		}
		dis.close();
		zos.setEncoding("gbk");
		zos.setComment("中文测试");
		
		zos.closeEntry();
		zos.close();
		return null;
	}
}
