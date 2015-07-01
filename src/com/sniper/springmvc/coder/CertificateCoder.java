package com.sniper.springmvc.coder;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.crypto.Cipher;

/**
 * 证书组建
 * 
 * @author sniper
 * 
 */
public class CertificateCoder {

	/**
	 * 证书类型
	 */
	private static final String CERT_TYPE = "X.509";

	/**
	 * 获取私钥根据 keystore
	 * 
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKeyByStore(String keyStorePath,
			String alias, String password) throws Exception {
		KeyStore ks = getKeyStore(keyStorePath, password);
		PrivateKey privateKey = (PrivateKey) ks.getKey(alias,
				password.toCharArray());
		return privateKey;
	}

	/**
	 * 获取 keystore
	 * 
	 * @param keyStorePath
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private static KeyStore getKeyStore(String keyStorePath, String password)
			throws Exception {
		// 默认是jks
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		FileInputStream is = new FileInputStream(keyStorePath);
		ks.load(is, password.toCharArray());
		is.close();
		return ks;
	}

	/**
	 * 根据证书获取公钥
	 * 
	 * @param certificatePath
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPublicKeyByCertificate(String certificatePath)
			throws Exception {
		Certificate certificate = getCertificate(certificatePath);
		return certificate.getPublicKey();
	}

	/**
	 * 根据证书路径获取证书
	 * 
	 * @param certificatePath
	 * @return
	 * @throws Exception
	 */
	private static Certificate getCertificate(String certificatePath)
			throws Exception {
		CertificateFactory certificateFactory = CertificateFactory
				.getInstance(CERT_TYPE);
		FileInputStream is = new FileInputStream(certificatePath);
		Certificate certificate = certificateFactory.generateCertificate(is);
		is.close();
		return certificate;
	}

	/**
	 * 获取证书
	 * 
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private static Certificate getCertificate(String keyStorePath,
			String alias, String password) throws Exception {
		KeyStore ks = getKeyStore(keyStorePath, password);
		return ks.getCertificate(alias);
	}

	/**
	 * 私钥加密
	 * 
	 * @param data
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String keyStorePath,
			String alias, String password) throws Exception {

		PrivateKey privateKey = getPrivateKeyByStore(keyStorePath, alias,
				password);
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	/**
	 * 私钥解密
	 * 
	 * @param data
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String keyStorePath,
			String alias, String password) throws Exception {

		PrivateKey privateKey = getPrivateKeyByStore(keyStorePath, alias,
				password);

		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	/**
	 * 公钥加密
	 * 
	 * @param data
	 * @param certificatePath
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, String certificatePath)
			throws Exception {
		PublicKey publicKey = getPublicKeyByCertificate(certificatePath);
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	/**
	 * 公钥解密
	 * 
	 * @param data
	 * @param certificatePath
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] data, String certificatePath)
			throws Exception {
		PublicKey publicKey = getPublicKeyByCertificate(certificatePath);
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	/**
	 * 签名
	 * 
	 * @param sign
	 * @param keyStorePath
	 * @param alias
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static byte[] sign(byte[] sign, String keyStorePath, String alias,
			String password) throws Exception {
		X509Certificate certificate = (X509Certificate) getCertificate(
				keyStorePath, alias, password);
		Signature signature = Signature
				.getInstance(certificate.getSigAlgName());
		PrivateKey privateKey = getPrivateKeyByStore(keyStorePath, alias,
				password);
		signature.initSign(privateKey);
		signature.update(sign);
		return signature.sign();
	}

	/**
	 * 验证签名
	 * 
	 * @param data
	 * @param sign
	 * @param certificatePath
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(byte[] data, byte[] sign,
			String certificatePath) throws Exception {
		X509Certificate x509Certificate = (X509Certificate) getCertificate(certificatePath);
		Signature signature = Signature.getInstance(x509Certificate
				.getSigAlgName());
		signature.initVerify(x509Certificate);
		signature.update(data);
		return signature.verify(sign);
	}

	public static void main(String[] args) throws Exception {

		String password = "sniper";
		String alias = "laolang";
		String aliasClient = "laolang.client";
		String aliasServer = "laolang.server";
		String aliasRoot = "laolang.root";

		String certificatePath = "/etc/ssl/certs/laolang.server.cer";
		String keystorePath = "/etc/ssl/laolang.server.keystore";

		String inputStr = "数字证书";
		byte[] data = inputStr.getBytes();

		// 公钥加密
		byte[] decrypt = encryptByPublicKey(data, certificatePath);
		// 私钥解密
		byte[] encrypt = decryptByPrivateKey(decrypt, keystorePath, alias,
				password);
		String outStr = new String(encrypt);
		System.out.println("加密前:\t" + inputStr);
		System.out.println("加密后:\t" + outStr);
	}

}
