package com.sniper.springmvc.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

public class DataUtil {

	public static String md5(File file) {
		InputStream is;
		try {
			is = new FileInputStream(file);
			return DigestUtils.md5Hex(is);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	public static String md5(String md5) {
		String result = "";
		try {
			StringBuffer buffer = new StringBuffer();
			char[] chars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
					'A', 'B', 'C', 'D', 'E', 'F' };
			byte[] bytes = md5.getBytes();
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] bs = digest.digest(bytes);
			for (byte b : bs) {
				buffer.append(chars[(b >> 4) & 0x0F]);
				buffer.append(chars[b & 0x0F]);
			}
			result = buffer.toString().toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 从str中随机读取 num 个字符串
	 * 
	 * @param str
	 * @param num
	 * @return
	 */
	public static String getStringRomton(String str, int num) {

		if (!ValidateUtil.isValid(str)) {
			return "";
		}
		Random random = new Random();
		String cs = "";
		int strLength = str.length();
		int a = 0;
		for (int i = 0; i < num; i++) {
			a = random.nextInt(strLength - 1);
			cs += str.substring(a, a + 1);
			try {
			} catch (Exception e) {
			}
		}

		return cs;

	}

	/**
	 * 首字母转小写
	 * 
	 * @param s
	 * @return
	 */
	public static String toLowerCaseFirstOne(String s) {
		if (Character.isLowerCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder())
					.append(Character.toLowerCase(s.charAt(0)))
					.append(s.substring(1)).toString();
	}

	/**
	 * 首字母转大写
	 * 
	 * @param s
	 * @return
	 */
	public static String toUpperCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder())
					.append(Character.toUpperCase(s.charAt(0)))
					.append(s.substring(1)).toString();
	}

	/**
	 * 深度复制序列化
	 * 
	 * @param src
	 * @return
	 */
	public static Serializable deeplyCopy(Serializable src) {

		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(outputStream);
			oos.writeObject(src);
			oos.close();
			outputStream.close();

			byte[] bytes = outputStream.toByteArray();
			ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(inputStream);
			Serializable copy = (Serializable) ois.readObject();
			inputStream.close();
			ois.close();
			return copy;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return src;

	}

	/**
	 * 1.用了3秒
	 * 
	 * @param str
	 *            <a href="http://my.oschina.net/u/556800" class="referer"
	 *            target="_blank">@return</a>
	 */
	public static String firstLetterToUpper(String str) {
		char[] array = str.toCharArray();
		array[0] -= 32;
		return String.valueOf(array);
	}

	/**
	 * 2.用了5秒
	 * 
	 * @param str
	 *            <a href="http://my.oschina.net/u/556800" class="referer"
	 *            target="_blank">@return</a>
	 */
	public static String lcy_firstLetterToUpper(String str) {
		return String.valueOf(str.charAt(0)).concat(str.substring(1));
	}

	/**
	 * 3.用了5秒
	 * 
	 * @param str
	 *            <a href="http://my.oschina.net/u/556800" class="referer"
	 *            target="_blank">@return</a>
	 */
	public static String letterToUpper(String str) {
		Character c = Character.toUpperCase(str.charAt(0));
		return c.toString().concat(str.substring(1));
	}

	/**
	 * 将Unicode字符串转换成bool型数组
	 * 
	 * @param input
	 * @return
	 */
	public boolean[] StrToBool(String input) {
		boolean[] output = Binstr16ToBool(BinstrToBinstr16(StrToBinstr(input)));
		return output;
	}

	/**
	 * 将bool型数组转换成Unicode字符串
	 * 
	 * @param input
	 * @return
	 */
	public String BoolToStr(boolean[] input) {
		String output = BinstrToStr(Binstr16ToBinstr(BoolToBinstr16(input)));
		return output;
	}

	/**
	 * 将字符串转换成二进制字符串，以空格相隔
	 * 
	 * @param str
	 * @return
	 */
	private String StrToBinstr(String str) {
		char[] strChar = str.toCharArray();
		String result = "";
		for (int i = 0; i < strChar.length; i++) {
			result += Integer.toBinaryString(strChar[i]) + " ";
		}
		return result;
	}

	/**
	 * 将二进制字符串转换成Unicode字符串
	 * 
	 * @param binStr
	 * @return
	 */
	private String BinstrToStr(String binStr) {
		String[] tempStr = StrToStrArray(binStr);
		char[] tempChar = new char[tempStr.length];
		for (int i = 0; i < tempStr.length; i++) {
			tempChar[i] = BinstrToChar(tempStr[i]);
		}
		return String.valueOf(tempChar);
	}

	/**
	 * 将二进制字符串格式化成全16位带空格的Binstr
	 * 
	 * @param input
	 * @return
	 */
	private String BinstrToBinstr16(String input) {
		StringBuffer output = new StringBuffer();
		String[] tempStr = StrToStrArray(input);
		for (int i = 0; i < tempStr.length; i++) {
			for (int j = 16 - tempStr[i].length(); j > 0; j--)
				output.append('0');
			output.append(tempStr[i] + " ");
		}
		return output.toString();
	}

	/**
	 * 将全16位带空格的Binstr转化成去0前缀的带空格Binstr
	 * 
	 * @param input
	 * @return
	 */
	private String Binstr16ToBinstr(String input) {
		StringBuffer output = new StringBuffer();
		String[] tempStr = StrToStrArray(input);
		for (int i = 0; i < tempStr.length; i++) {
			for (int j = 0; j < 16; j++) {
				if (tempStr[i].charAt(j) == '1') {
					output.append(tempStr[i].substring(j) + " ");
					break;
				}
				if (j == 15 && tempStr[i].charAt(j) == '0')
					output.append("0" + " ");
			}
		}
		return output.toString();
	}

	/**
	 * 二进制字串转化为boolean型数组 输入16位有空格的Binstr
	 * 
	 * @param input
	 * @return
	 */
	private boolean[] Binstr16ToBool(String input) {
		String[] tempStr = StrToStrArray(input);
		boolean[] output = new boolean[tempStr.length * 16];
		for (int i = 0, j = 0; i < input.length(); i++, j++)
			if (input.charAt(i) == '1')
				output[j] = true;
			else if (input.charAt(i) == '0')
				output[j] = false;
			else
				j--;
		return output;
	}

	/**
	 * boolean型数组转化为二进制字串 返回带0前缀16位有空格的Binstr
	 * 
	 * @param input
	 * @return
	 */
	private String BoolToBinstr16(boolean[] input) {
		StringBuffer output = new StringBuffer();
		for (int i = 0; i < input.length; i++) {
			if (input[i])
				output.append('1');
			else
				output.append('0');
			if ((i + 1) % 16 == 0)
				output.append(' ');
		}
		output.append(' ');
		return output.toString();
	}

	/**
	 * 将二进制字符串转换为char
	 * 
	 * @param binStr
	 * @return
	 */
	private char BinstrToChar(String binStr) {
		int[] temp = BinstrToIntArray(binStr);
		int sum = 0;
		for (int i = 0; i < temp.length; i++) {
			sum += temp[temp.length - 1 - i] << i;
		}
		return (char) sum;
	}

	/**
	 * 将初始二进制字符串转换成字符串数组，以空格相隔
	 * 
	 * @param str
	 * @return
	 */
	private String[] StrToStrArray(String str) {
		return str.split(" ");
	}

	/**
	 * 将二进制字符串转换成int数组
	 * 
	 * @param binStr
	 * @return
	 */
	private int[] BinstrToIntArray(String binStr) {
		char[] temp = binStr.toCharArray();
		int[] result = new int[temp.length];
		for (int i = 0; i < temp.length; i++) {
			result[i] = temp[i] - 48;
		}
		return result;
	}

	/**
	 * 字符串转二进制
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] hex2byte(String str) {
		if (str == null) {
			return null;
		}

		str = str.trim();
		int len = str.length();

		if (len == 0 || len % 2 == 1) {
			return null;
		}

		byte[] b = new byte[len / 2];
		try {
			for (int i = 0; i < str.length(); i += 2) {
				b[i / 2] = (byte) Integer
						.decode("0X" + str.substring(i, i + 2)).intValue();
			}
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 二进制转字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) {
		StringBuffer sb = new StringBuffer();
		String tmp = "";
		for (int i = 0; i < b.length; i++) {
			tmp = Integer.toHexString(b[i] & 0XFF);
			if (tmp.length() == 1) {
				sb.append("0" + tmp);
			} else {
				sb.append(tmp);
			}

		}
		return sb.toString();
	}

	/**
	 * 把一个字符串专程boolean
	 * 
	 * @param id
	 * @return
	 */
	public static boolean stringToBoolean(String string) {

		try {
			return Boolean.parseBoolean(string);

		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("非法参数");
		}

	}

	/**
	 * 把一个字符串专程整型
	 * 
	 * @param string
	 * @return
	 * @throws Exception
	 */
	public static Integer stringToInteger(String string) throws Exception {
		try {
			return Integer.parseInt(string);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("非法字符");
		}
	}

	public static String[] StringToStringArray(String str) {
		if (ValidateUtil.isValid(str)) {
			str = str.replace(";", " ").replace(",", " ");
			return str.split(" ");
		}
		return new String[] {};
	}

	/**
	 * HTML标签转义方法 —— java代码库
	 * 
	 * @param content
	 * @return
	 */
	public static String html(String content) {
		if (content == null)
			return "";
		String html = content;
		html = StringUtils.replace(html, "'", "&apos;");
		html = StringUtils.replace(html, "\"", "&quot;");
		html = StringUtils.replace(html, "\t", "&nbsp;&nbsp;");// 替换跳格
		// html = StringUtils.replace(html, " ", "&nbsp;");// 替换空格
		html = StringUtils.replace(html, "<", "&lt;");
		html = StringUtils.replace(html, ">", "&gt;");
		return html;
	}

	public static String[] getBaiDuMapPoint(String str) {
		String[] points = new String[2];
		// String strs = str.substring(str.indexOf("center"));
		return points;
	}

	/**
	 * 整数查找
	 * 
	 * @param arr
	 * @param target
	 * @return
	 */
	public static int find(int[] arr, int target) {
		int mid = arr.length / 2;
		int start = 0;
		int end = arr.length - 1;

		while (start < mid && mid < end) {
			if (arr[mid] > target) {
				end = mid;
			} else if (arr[mid] < target) {
				start = mid;
			} else {
				return mid;
			}
			mid = (start + end) / 2;

		}
		return -1;

		// int[] arr = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		// System.out.println(find(arr, 2));
	}

	

	public static void main(String[] args) {
		//String filePath = "/home/sniper/图片/54fbb2fb43166d22ef46b670442309f79052d225.jpg";
		//System.out.println(md5(new File(filePath)));
		//System.out.println(md5(new File(filePath)).length());
		
		String a = "http://www.tuicool.com/articles/ziIjiu";
		
		System.out.println(a.substring(0, a.length() - 1));
	}
}
