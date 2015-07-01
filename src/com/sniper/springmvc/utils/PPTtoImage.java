package com.sniper.springmvc.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.RichTextRun;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

public class PPTtoImage {

	public static void main(String[] args) {
		// 读入PPT文件
		File file = new File("/home/sniper/图片/3.ppt");
		// doPPTtoImage(file, null);

		File file1 = new File("/home/sniper/图片/1.pptx");
		doPPTXtoImage(file1, null, 100);
	}

	public static boolean doPPTtoImage(File file, String path) {
		boolean isppt = checkFile(file);
		if (!isppt) {
			return false;
		}
		try {

			FileInputStream is = new FileInputStream(file);
			SlideShow ppt = new SlideShow(is);
			is.close();
			Dimension pgsize = ppt.getPageSize();
			Slide[] slide = ppt.getSlides();
			for (int i = 0; i < slide.length; i++) {
				TextRun[] truns = slide[i].getTextRuns();
				for (int k = 0; k < truns.length; k++) {
					RichTextRun[] rtruns = truns[k].getRichTextRuns();
					for (int l = 0; l < rtruns.length; l++) {
						int index = rtruns[l].getFontIndex();
						String name = rtruns[l].getFontName();
						rtruns[l].setFontIndex(1);
						rtruns[l].setFontName("宋体");
					}
				}
				BufferedImage img = new BufferedImage(pgsize.width,
						pgsize.height, BufferedImage.TYPE_INT_RGB);

				Graphics2D graphics = img.createGraphics();
				graphics.setPaint(Color.white);
				graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width,
						pgsize.height));
				slide[i].draw(graphics);

				// 这里设置图片的存放路径和图片的格式(jpeg,png,bmp等等),注意生成文件路径
				FileOutputStream out = new FileOutputStream(
						"/home/sniper/图片/ppt4357_" + i + ".jpeg");
				javax.imageio.ImageIO.write(img, "jpeg", out);
				out.close();

			}
			return true;
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
		}
		return false;
	}

	public static boolean doPPTXtoImage(File file, String path, int limit) {
		boolean isppt = checkFile(file);
		if (!isppt) {
			return false;
		}
		try {

			FileInputStream is = new FileInputStream(file);
			XMLSlideShow ppt = new XMLSlideShow(is);
			is.close();
			Dimension pgsize = ppt.getPageSize();
			XSLFSlide[] slide = ppt.getSlides();
			for (int i = 0; i < slide.length; i++) {

				BufferedImage img = new BufferedImage(pgsize.width,
						pgsize.height, BufferedImage.TYPE_INT_RGB);

				Graphics2D graphics = img.createGraphics();
				graphics.setPaint(Color.white);
				graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width,
						pgsize.height));
				slide[i].draw(graphics);

				// 这里设置图片的存放路径和图片的格式(jpeg,png,bmp等等),注意生成文件路径
				FileOutputStream out = new FileOutputStream(
						"/home/sniper/图片/ppt4357_" + i + ".jpeg");
				javax.imageio.ImageIO.write(img, "jpeg", out);
				out.close();

				if (i >= limit) {
					break;
				}

			}
			return true;
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
		}
		return false;
	}

	// function 检查文件是否为PPT
	public static boolean checkFile(File file) {

		boolean isppt = false;
		String filename = file.getName();
		String suffixname = null;

		if (filename != null && filename.indexOf(".") != -1) {
			suffixname = filename.substring(filename.indexOf("."));
			if (suffixname.equals(".ppt") || suffixname.equals(".pptx")) {
				isppt = true;
			}
			return isppt;
		} else {
			return isppt;
		}
	}
}
