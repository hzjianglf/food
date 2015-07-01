package com.sniper.springmvc.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 工具类，生成验证码图片
 * 
 * @version 1.0 2012/08/21
 * @author dongliyang
 * 
 */
public class Verify {

	// 字体名称
	private static String fontName;
	static {
		InputStream in = null;
		try {
			PathUtil p = new PathUtil();
			String webRootPath = p.getWebInfPath();

			in = new FileInputStream(webRootPath + "file/shades.ttf");
		} catch (Exception e) {
			e.printStackTrace();

		}

		RegisterFont gf = new RegisterFont(in);

		try {
			fontName = gf.reginterFont();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取随机类
	 */
	public static Random random = new Random();

	/**
	 * 获取随即颜色 生成155-255的数字
	 * 
	 * @return
	 */
	public static Color getRandomColor() {

		return new Color(random.nextInt(100), random.nextInt(100),
				random.nextInt(100), random.nextInt(100) + 155);
	}

	/**
	 * 获取反色
	 * 
	 * @param c
	 * @return
	 */
	public static Color getReverseColor(Color c) {
		return new Color(255 - c.getRed(), 255 - c.getGreen(),
				255 - c.getBlue());
	}

	/**
	 * 生成验证码图片
	 * 
	 * @param securityCode
	 *            验证码字符
	 * @return BufferedImage 图片
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public static BufferedImage createImage(String VerifyCode) {

		// 验证码长度
		int codeLength = VerifyCode.length();
		// 字体大小
		int fSize = 30;
		// 字体宽度
		int fWidth = 30;
		// 上下左右偏移量
		int padding = 5;
		// 图片宽度
		int width = codeLength * fWidth + padding * 2;

		// 图片高度
		int height = fWidth + padding * 2;
		Color color = getRandomColor();
		// 当前颜色反之
		Color reverse = getReverseColor(color);

		// 创建一个彩色图片
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		// 绘制图像对象
		Graphics g = image.createGraphics();
		g.setFont(new Font(fontName, Font.ITALIC, fSize));
		// 设置背景色
		// g.setColor(color);
		g.setColor(Color.WHITE);
		// 填充背景
		g.fillRect(0, 0, width, height);
		// 设置边框颜色
		// g.setColor(Color.LIGHT_GRAY);
		// 边框字体样式
		// 绘制背景
		g.drawRect(0, 0, width - 1, height - 1);
		// 设置噪点
		g.setColor(Color.LIGHT_GRAY);
		int maxNum = codeLength * 10;
		for (int i = 0; i < maxNum; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			// 绘制1*1大小的矩形
			g.drawRect(x, y, 1, 1);
		}
		// 添加噪点
		// for(int i = 0, n = random.nextInt(200); i < n; i++)
		// {
		// g.drawRect(random.nextInt(width), random.nextInt(height), 1, 1);
		// }
		// 绘制验证码
		int codeY = padding * 2 + fWidth / 2;
		// 设置字体颜色和样式
		g.setColor(color);
		// g.setFont(new Font("宋体", Font.PLAIN, fSize));
		// 定义文字左偏移量
		int j = 0;
		for (int i = 0; i < codeLength; i++) {
			if (i == 0) {
				j = i * fWidth + padding;
			} else {
				j = i * fWidth;
			}
			g.drawString(String.valueOf(VerifyCode.charAt(i)), j, codeY);
		}

		// 关闭资源
		g.dispose();
		return image;
	}

	/**
	 * 返回验证码图片的流格式
	 * 
	 * @param securityCode
	 *            验证码
	 * @return ByteArrayInputStream 图片流
	 * @throws Exception
	 */
	public static ByteArrayInputStream getImageAsInputStream(String securityCode)
			throws Exception {
		BufferedImage image = createImage(securityCode);
		return convertImageToStream(image);
	}

	/**
	 * 将BufferedImage转换成ByteArrayInputStream
	 * 
	 * @param image
	 *            图片
	 * @return ByteArrayInputStream 流
	 */
	private static ByteArrayInputStream convertImageToStream(BufferedImage image) {

		ByteArrayInputStream inputStream = null;
		// 创建输出留
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// 格式转成jpeg
		JPEGImageEncoder jpeg = JPEGCodec.createJPEGEncoder(bos);
		try {
			jpeg.encode(image);
			byte[] bts = bos.toByteArray();
			inputStream = new ByteArrayInputStream(bts);
		} catch (ImageFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 输出字节流
		return inputStream;
	}

}