package com.sniper.springmvc.coder;

import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.DHParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class ELGamalCoder {

	private static final String KEY_ALGORITHM = "ELGamal";

	private static final int KEY_SIZE = 256;
	private static final String PUBLIC_KEY = "ELGamalPublicKey";
	private static final String PRIVATE_KEY = "ELGamalPrivateKey";

	/**
	 * 私钥解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] data, byte[] key)
			throws Exception {
		// 加入算法支持
		Security.addProvider(new BouncyCastleProvider());
		// 实例化工厂密钥
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
		KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = factory.generatePrivate(keySpec);
		//
		Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	/**
	 * 公钥加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrptByPublicKey(byte[] data, byte[] key)
			throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
		KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicKey = factory.generatePublic(keySpec);
		Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE	, publicKey);
		return cipher.doFinal(data);

	}

	/**
	 * 生成密钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> initKey() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		// 实例化算法参数生成器
		AlgorithmParameterGenerator apg = AlgorithmParameterGenerator
				.getInstance(KEY_ALGORITHM);
		// 初始化算法生成器
		apg.init(KEY_SIZE);
		// 生成算法参数
		AlgorithmParameters parameters = apg.generateParameters();
		// 构建参数材料
		DHParameterSpec parameterSpec = parameters
				.getParameterSpec(DHParameterSpec.class);
		// 实例化密钥队生成器
		KeyPairGenerator generator = KeyPairGenerator
				.getInstance(KEY_ALGORITHM);
		// 初始化密钥对生成器
		generator.initialize(parameterSpec, new SecureRandom());
		// 生成密钥对
		KeyPair keyPair = generator.generateKeyPair();
		PublicKey publicKey = (PublicKey) keyPair.getPublic();
		PrivateKey privateKey = (PrivateKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(PRIVATE_KEY, privateKey);
		keyMap.put(PUBLIC_KEY, publicKey);
		return keyMap;
	}

	public static byte[] getPublicKey(Map<String, Object> keyMap) {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return key.getEncoded();
	}

	public static byte[] getPrivateKey(Map<String, Object> keyMap) {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return key.getEncoded();

	}

	public static void main(String[] args) throws Exception {
		long s = System.currentTimeMillis();
		Map<String, Object> keyMap = initKey();
		System.out.println(System.currentTimeMillis() - s);
		byte[] priKey = getPrivateKey(keyMap);
		byte[] pubKey = getPublicKey(keyMap);
		String str = "eleamel加密";
		//加密
		byte[] encodeData = encrptByPublicKey(str.getBytes(), pubKey);
		System.out.println("加密:\t" + Base64.encodeBase64String(encodeData));
		//解密
		byte[] decodeData = decryptByPrivateKey(encodeData, priKey);
		System.out.println("解密:\t" + new String(decodeData));
		System.out.println(System.currentTimeMillis() - s);
	}
}
