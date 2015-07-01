package com.sniper.springmvc.coder;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;

public class PBECoder {

	private static final String ALGORITHM = "PBEWITHMD5andDES";

	private static final int ITERATION_COUNT = 100;

	/**
	 * 盐初始化 长度8字节
	 * 
	 * @return
	 */
	public static byte[] initSalt() {
		SecureRandom random = new SecureRandom();
		return random.generateSeed(8);
	}

	/**
	 * 转换密钥
	 * 
	 * @param password
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static Key toKey(String password) throws NoSuchAlgorithmException,
			InvalidKeySpecException {
		PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
		SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey key = factory.generateSecret(keySpec);
		return key;
	}

	/**
	 * 加密
	 * 
	 * @param data
	 * @param password
	 * @param salt
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, String password, byte[] salt)
			throws Exception {
		Key key = toKey(password);
		PBEParameterSpec parameterSpec = new PBEParameterSpec(salt,
				ITERATION_COUNT);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
		return cipher.doFinal(data);
	}

	/**
	 * 解密
	 * 
	 * @param data
	 * @param password
	 * @param salt
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, String password, byte[] salt)
			throws Exception {
		Key key = toKey(password);
		PBEParameterSpec parameterSpec = new PBEParameterSpec(salt,
				ITERATION_COUNT);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
		return cipher.doFinal(data);
	}

	public static void main(String[] args) throws Exception {
		String inputStr = "PBE";
		byte[] inputData = inputStr.getBytes();
		String pwd = "1111";
		byte[] salt = PBECoder.initSalt();
		System.out.println("盐\t" + Base64.encodeBase64String(salt));
		// 加密
		byte[] data = PBECoder.encrypt(inputData, pwd, salt);
		System.out.println("加密\t" + Base64.encodeBase64String(data));
		// 解密
		byte[] output = PBECoder.decrypt(data, pwd, salt);
		System.out.println("解密\t" + new String(output));
	}
}
