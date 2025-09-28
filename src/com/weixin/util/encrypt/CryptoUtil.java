/*
 * 文 件 名  :  CryptoUtil.java
 * 版    权    :  Ltd. Copyright (c) 2013 深圳市商巢互联网软件有限公司,All rights reserved
 * 描    述    :  <描述>
 * 创建人    :  彭彩云
 * 创建时间:  下午3:09:11
 */
package com.weixin.util.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.weixin.util.StringUtil;

/**
 * 加密码工具类
 * 
 * @author 彭彩云
 * @version [版本号, 2013-7-1]
 */
public class CryptoUtil {
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	private final static String ENCRYPTKEY = "HBdataCollection~}2017";
	/***
	 * 
	* @Title: encode 
	* @Description: TODO 编译加密
	* @param @param key 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String encode(String key){
		if(StringUtil.isRealEmpty(key)){
			return "";
		}
		return aesEncrypt(key, ENCRYPTKEY);
	}
	
	/***
	 * 
	* @Title: encode 
	* @Description: TODO 编译加密
	* @param @param key 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String encode(Long key){
		if(key == null){
			return "";
		}
		return aesEncrypt(key.toString(), ENCRYPTKEY);
	}
	
	/***
	 * 
	* @Title: decode 
	* @Description: TODO 解密
	* @param @param encryptCode
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String decode(String encryptCode){
		if(StringUtil.isRealEmpty(encryptCode)){
			return "";
		}
		return aesDecrypt(encryptCode, ENCRYPTKEY);
	}
	
	/**
	 * AES加密码
	 * 
	 * @author 彭彩云
	 * @param origin
	 * @param key
	 * @return [参数说明]
	 * @return String [返回类型说明]
	 */
	private static String aesEncrypt(String origin, String key) {
		try {
			SecretKey secretKey = getSecretKey(key);
			SecretKeySpec keySpec = getSecretKeySpec(secretKey);

			// 创建密码器
			Cipher cipher = Cipher.getInstance("AES");

			// 初始化
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);

			// 加密
			byte[] result = cipher.doFinal(origin.getBytes("UTF-8"));

			// 将密文转换成十六进制的字符串
			return byteArrayToHexString(result);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * AES解密
	 * 
	 * @author 彭彩云
	 * @param origin
	 * @param key
	 * @return [参数说明]
	 * @return String [返回类型说明]
	 */
	private static String aesDecrypt(String origin, String key) {
		try {
			SecretKey secretKey = getSecretKey(key);
			SecretKeySpec keySpec = getSecretKeySpec(secretKey);

			// 创建密码器
			Cipher cipher = Cipher.getInstance("AES");

			// 初始化
			cipher.init(Cipher.DECRYPT_MODE, keySpec);

			// 将十六进制转成二进制数组，并解密
			byte[] result = cipher.doFinal(parseHexStr2Byte(origin));
			// 生成字符串
			return new String(result, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 2012-9-22 上午08:37:40
	 * 
	 * @author
	 * @param secretKey
	 * @return
	 */
	private static SecretKeySpec getSecretKeySpec(SecretKey secretKey) {
		byte[] keyEncoded = secretKey.getEncoded();
		SecretKeySpec keySpec = new SecretKeySpec(keyEncoded, "AES");
		return keySpec;
	}

	/**
	 * 2012-9-22 上午08:37:15
	 * 
	 * @author
	 * @param key
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private static SecretKey getSecretKey(String key) throws NoSuchAlgorithmException {
		KeyGenerator kGen = KeyGenerator.getInstance("AES");
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(key.getBytes());
		kGen.init(128, secureRandom);
		SecretKey secretKey = kGen.generateKey();
		return secretKey;
	}

	/**
	 * 将二进制转成16进制的字符串，所有字母大写
	 * 
	 * @author 彭彩云
	 * @param b
	 * @return [参数说明]
	 * @return String [返回类型说明]
	 */
	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString().toUpperCase();
	}

	/**
	 * 将字节转换成16进制
	 * 
	 * <br>
	 * 2012-9-21 下午08:56:33
	 * 
	 * @author
	 * @param b
	 * @return
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * 将十六进制的字符串转成二进制数组 <br>
	 * 2012-9-22 上午08:29:43
	 * 
	 * @author 彭彩云
	 * @param hexStr
	 *            16进制字符串
	 * @return 二进制数组
	 */
	private static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	public static void main(String[] args) {//jdbc:mysql://121.17.120.12:3306/hb_data?characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false
		System.out.println(CryptoUtil.encode("jdbc:sqlserver://202.104.28.102:3355;DatabaseName=DefWeixin"));
		System.out.println(CryptoUtil.decode("2DA4083B806FF23988B2343067CFA8A5665E9B904A602FEAF9BBEBD4682FFC593661110291CF51E690D170D59648D233008357DD8A17787F843C3AC1714FBD99"));
	}
}
