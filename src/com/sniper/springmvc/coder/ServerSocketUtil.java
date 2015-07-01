package com.sniper.springmvc.coder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class ServerSocketUtil {

	/**
	 * 获取 keyStore
	 * 
	 * @param keyStorePath
	 * @param password
	 * @return
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 */
	public static KeyStore getKeyStore(String keyStorePath, String password)
			throws KeyStoreException, NoSuchAlgorithmException,
			CertificateException, IOException {

		FileInputStream in = new FileInputStream(keyStorePath);
		KeyStore keyStore = KeyStore.getInstance("jks");
		keyStore.load(in, password.toCharArray());
		in.close();
		return keyStore;
	}

	/**
	 * 获取 SSLSocketFactory
	 * 
	 * @param password
	 * @param keyStorePath
	 * @param trustKeyStorePath
	 * @return
	 * @throws Exception
	 */
	public static SSLSocketFactory getSSlSocketFactory(String password,
			String keyStorePath, String trustKeyStorePath) throws Exception {

		KeyManagerFactory factory = KeyManagerFactory.getInstance("SunX509");
		KeyStore keyStore = getKeyStore(keyStorePath, password);
		factory.init(keyStore, password.toCharArray());

		TrustManagerFactory trustManagerFactory = TrustManagerFactory
				.getInstance("SunX509");
		KeyStore keyStore2 = getKeyStore(trustKeyStorePath, password);
		trustManagerFactory.init(keyStore2);

		SSLContext ctx = SSLContext.getInstance("SSL");
		ctx.init(factory.getKeyManagers(),
				trustManagerFactory.getTrustManagers(), new SecureRandom());

		SSLSocketFactory sf = ctx.getSocketFactory();
		return sf;
	}
	
	

	public static void main(String[] args) throws Exception {
		URL url = new URL("https://60.216.101.120:8443/survey/");
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setSSLSocketFactory(getSSlSocketFactory("sniper",
				"/approot/www/tomcat.keystore", "/approot/www/tomcat.keystore"));
		InputStream is = conn.getInputStream();
		int length = conn.getContentLength();
		
		System.out.println(length);
		
		is.close();

	}
}
