package com.sniper.springmvc.coder;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public abstract class ECDSACoder {

	/**
	 * 数字签名 密钥算法
	 */
	public static final String KEY_ALGORITHM = "DSA";
	/**
	 * 数字签名 签名/验证算法
	 */
	public static final String SIGNATURE_ALGORITHM = "SHA1withDSA";

	private static final String PUBLIC_KEY = "DSAPublicKey";
	private static final String PRIVATE_KEY = "DSAPrivateKey";
	/**
	 * rsa密钥长度默认是1024 迷药必须是64的倍数 范围 512 - 65536
	 */
	private static final int KEY_SIZE = 1024;

	

	/**
	 * 签名
	 * 
	 * @param date
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] sign(byte[] date, byte[] privateKey) throws Exception {
		// 转换成私钥材料
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
				privateKey);
		// 实例化迷药工厂
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 获取私钥对象
		PrivateKey privateKey2 = keyFactory
				.generatePrivate(pkcs8EncodedKeySpec);
		// 实例化密钥签名类 signature
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(privateKey2);
		signature.update(date);
		return signature.sign();
	}

	public static boolean verify(byte[] data, byte[] publicKey, byte[] sign)
			throws NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidKeyException, SignatureException {
		// 转换成公钥材料
		X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(publicKey);
		// 实例化密钥工厂
		KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 生成公钥
		PublicKey key = factory.generatePublic(encodedKeySpec);
		// 实例化签名认真 signature
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		// 初始化signature
		signature.initVerify(key);
		// 更新
		signature.update(data);
		// 验证
		return signature.verify(sign);
	}

	/**
	 * 获取私钥
	 * 
	 * @param keyMap
	 * @return
	 */
	public static byte[] getPrivateKey(Map<String, Object> keyMap) {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return key.getEncoded();
	}

	/**
	 * 获取公钥
	 * 
	 * @param keyMap
	 * @return
	 */
	public static byte[] getPublicKey(Map<String, Object> keyMap) {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return key.getEncoded();
	}

	/**
	 * 初始化密钥
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static Map<String, Object> initKey() throws NoSuchAlgorithmException {
		// 实例化密钥生成器
		KeyPairGenerator generator = KeyPairGenerator
				.getInstance(KEY_ALGORITHM);
		// 初始化迷药生成器
		SecureRandom random = new SecureRandom();

		generator.initialize(KEY_SIZE, random);
		// 生成密钥对
		KeyPair keyPair = generator.generateKeyPair();
		RSAPublicKey puKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey prKry = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(PRIVATE_KEY, prKry);
		keyMap.put(PUBLIC_KEY, puKey);
		return keyMap;

	}
	
	/**
	 * 获取密钥
	 * @param key
	 * @return
	 * @throws DecoderException 
	 */
	public static byte[] getKey(String key) throws DecoderException {
		return Hex.decodeHex(key.toCharArray());
	}

	public static void main(String[] args) throws Exception {
		long s = System.currentTimeMillis();
		Map<String, Object> ketMap = initKey();
		byte[] priKey = getPrivateKey(ketMap);
		byte[] pubKey = getPublicKey(ketMap);
		System.out.println("\n----私钥加密---公钥揭秘---------");
		byte[] data = "rsa加密算法".getBytes();
		
		
		
	}

}
