package com.hq.test;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 * @author lijie
 * @date 2023年6月20日 下午2:47:38
 * @Description
 */
public class CouponUtils {
	protected final static Logger logger = Logger.getLogger(CouponUtils.class);

	private static String LOGIN = "/SQ-User/Login";

	private static String COUPONS_ADD = "/SQ-Coupons/Add";

	private static String COUPONS_LIST = "/SQ-Coupons/List";
	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };
	private final static String HMAC_SHA1 = "HmacSHA1";

	private final static String ENCODING = "UTF-8";

	public final static String COUPON_CONFIG = "COUPON_CONFIG";

	/**
	 * 获取字符串 MD5 值
	 * 
	 * @param rawStr 原始字符串
	 * @return
	 */
	private static String getStringMD5(byte[] rawStr) {
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(rawStr);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}

			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取字符串 SHA 值
	 * 
	 * @param rawStr    原始字符串
	 * @param secretKey 密钥
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	private static String getStringSHA(byte[] rawStr, byte[] secretKey)
			throws NoSuchAlgorithmException, InvalidKeyException {
		SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, HMAC_SHA1);
		Mac mac = Mac.getInstance(HMAC_SHA1);
		mac.init(secretKeySpec);
		byte[] rawHmac = mac.doFinal(rawStr);

		return getStringMD5(rawHmac);
	}
	
	
	/** 
	  * 获取加密字符串  
	  * @param rawStr 原始字符串 
	  * @param secretKey 密钥 
	  * @return 
	  * @throws InvalidKeyException 
	  * @throws UnsupportedEncodingException 
	  * @throws NoSuchAlgorithmException 
	  */ 
	 public static String getEncryptedString(String rawStr, String secretKey) 
	   throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException { 
	  if ((null == rawStr || "".equals(rawStr)) || (null == secretKey || "".equals(secretKey))) return null; 
	   
	  return new String(getStringSHA(rawStr.getBytes(ENCODING), secretKey.getBytes(ENCODING)) 
	      .getBytes(ENCODING)); 
	 } 

		/**
		 * 获取数字签名
		 * 
		 * @param host      请求主机
		 * @param path      请求路径
		 * @param params    请求参数(不包含 signature 参数)
		 * @param secretKey 密钥
		 * @return
		 * @throws Exception
		 */
		public static String getSignatureString(String host, String path, Map<String, String> params,
				String secretKey) throws Exception {
			if ((null == host || "".equals(host)) || (null == path || "".equals(path))
					|| (null == params || params.size() == 0) || (null == secretKey || "".equals(secretKey)))
				return null;

			// [1]对参数排序(将请求参数按参数名做字典序升序排序)
			List<String> list = new ArrayList<String>();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				list.add(entry.getKey());
			}
			Collections.sort(list);
			// [2]拼接请求字符串(将上一步排序好的请求参数,格式化成 key=value,然后用"&"拼接在一起。value 为原始值而非 url 编码后的值)
			StringBuffer strBuffer = new StringBuffer();
			for (int i = 0; i < list.size(); i++) {
				strBuffer.append(
						list.get(i) + "=" + (null == params.get(list.get(i)) ? "" : params.get(list.get(i))) + "&");
			}
			String paramsStr = strBuffer.toString().substring(0, strBuffer.length() - 1);
			// [3]拼接签名原文字符串
			String url = host + path + "?" + paramsStr;
			// [4]生成数字签名
			// a.使用 HMAC-SHA1 算法对步骤[3]拼接的签名原文字符串进行加密
			String encryptedStr = "";
			try {
				encryptedStr = CouponUtils.getEncryptedString(url, secretKey);
			} catch (Exception e) {
				throw new Exception(e);
			}
			// b.将生成的数字签名使用 Base64 进行编码,获得最终的数字签名。
			String signatureStr = new String(Base64.encodeBase64(encryptedStr.getBytes("UTF-8")), "UTF-8");

			return signatureStr;
		}


		public static String mapToQueryString(Map<String, String> params) {
			StringBuilder result = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				if (result.length() > 0) {
					result.append("&");
				}
				result.append(key).append("=").append(value);
			}
			return result.toString();
		}

		/**
		 * 
		 * @author lijie
		 * @date 2023年6月20日 下午2:58:53
		 * @description
		 * @param args
		 * @throws Exception
		 */
		public static void main(String args[]) throws Exception {
			Map<String, String> params = new HashMap<String, String>();
			params.put("userName", "fsYTYY");
			params.put("passWord", "34bHlueWjPmOWvWo0dzOx1AAL");
			long currentTimeMillis = System.currentTimeMillis();
			System.out.println(currentTimeMillis);
			params.put("timeStamp", String.valueOf(currentTimeMillis));
			String signatureString = getSignatureString("http://oepn.ajbcloud.com", "/SQ-User/Login", params,
					"MTkyZTdhYzhmZGE0YjM4MTc1NDc1ZmQwMDVmNmRiYjA=");
			System.out.println(signatureString);


			/**
			// 商户查询
			Map<String, String> queryParams2 = new HashMap<String, String>();
			queryParams2.put("secretId", "361cdbda79c2447f85da7a86b879393e");
			queryParams2.put("parkCode", "75701040001");
			queryParams2.put("pageSize", "1000");
			queryParams2.put("timeStamp", String.valueOf(currentTimeMillis));
			String signatureString3 = getSignatureString("http://oepn.ajbcloud.com", "/SQ-Tenants/List", queryParams2,
					"MTkyZTdhYzhmZGE0YjM4MTc1NDc1ZmQwMDVmNmRiYjA=");

			System.out.println(signatureString3);

			
			// 派发查询
			Map<String, String> queryParams3 = new HashMap<String, String>();
			queryParams3.put("secretId", "361cdbda79c2447f85da7a86b879393e");
			queryParams3.put("merchantId", "6c542be6-8a17-490d-828b-e3779f3efa40");
			queryParams3.put("parkCode", "75701040001");
			queryParams3.put("startTime", "2024-11-01 00:00:00");
			queryParams3.put("endTime", "2024-11-7 23:00:00");
			queryParams3.put("pageSize", "100");

			queryParams3.put("timeStamp", String.valueOf(currentTimeMillis));
			String signatureString4 = getSignatureString("http://oepn.ajbcloud.com", "/SQ-Coupons/List", queryParams3,
					"MTkyZTdhYzhmZGE0YjM4MTc1NDc1ZmQwMDVmNmRiYjA=");

			System.out.println("派发查询" + signatureString4);



			// 电子优惠查询
			Map<String, String> queryParams11 = new HashMap<String, String>();
			queryParams11.put("secretId", "361cdbda79c2447f85da7a86b879393e");
			queryParams11.put("parkCode", "75701040001");
			queryParams11.put("carNo", "粤EGD189");
			queryParams11.put("startTime", "2024-11-01 00:00:00");
			queryParams11.put("endTime", "2024-11-07 23:00:00");
			queryParams11.put("pageSize", "100");
			queryParams11.put("timeStamp", String.valueOf(currentTimeMillis));
			String signatureString11 = getSignatureString("http://oepn.ajbcloud.com", "/SQ-Coupons/List", queryParams11,
					"MTkyZTdhYzhmZGE0YjM4MTc1NDc1ZmQwMDVmNmRiYjA=");

			System.out.println("收费记录查询：" + signatureString11);
			
			**/
			/*
			// 车辆查询
			Map<String, String> queryParams4 = new HashMap<String, String>();
			queryParams4.put("secretId", "361cdbda79c2447f85da7a86b879393e");
			queryParams4.put("parkCode", "75701040001");
			queryParams4.put("carNo", "粤A107PE");
			queryParams4.put("startTime", "2024-03-21 00:00:00");
			queryParams4.put("endTime", "2024-03-26 00:00:00");

			queryParams4.put("timeStamp", String.valueOf(currentTimeMillis));
			String signatureString5 = getSignatureString("http://oepn.ajbcloud.com", "/SQ-ParkingEnterLog/EnterLog",
					queryParams4,
					"MTkyZTdhYzhmZGE0YjM4MTc1NDc1ZmQwMDVmNmRiYjA=");

			System.out.println("车辆查询" + signatureString5);

			// 商户创建
			Map<String, String> queryParams5 = new HashMap<String, String>();
			queryParams5.put("secretId", "361cdbda79c2447f85da7a86b879393e");
			queryParams5.put("parkCode", "75701040001");
			queryParams5.put("tenantsAccount", "china-tcm@china-tcm.com.cn");
			queryParams5.put("tenantsPassWord", "123456ab");
			queryParams5.put("tenantsName", "国药集团");
			queryParams5.put("tenantsStatus", "1");
			queryParams5.put("mobilePhone", "13450251958");
			queryParams5.put("timeStamp", String.valueOf(currentTimeMillis));
			String signatureString6 = getSignatureString("http://oepn.ajbcloud.com", "/SQ-Tenants/Add", queryParams5,
					"MTkyZTdhYzhmZGE0YjM4MTc1NDc1ZmQwMDVmNmRiYjA=");

			System.out.println(signatureString6);

			// 商户创建
			Map<String, String> queryParams6 = new HashMap<String, String>();
			queryParams6.put("secretId", "361cdbda79c2447f85da7a86b879393e");
			queryParams6.put("parkCode", "75701040001");
			queryParams6.put("timeStamp", String.valueOf(currentTimeMillis));
			String signatureString7 = getSignatureString("http://oepn.ajbcloud.com", "/SQ-LeasedCard/TypeList",
					queryParams6,
					"MTkyZTdhYzhmZGE0YjM4MTc1NDc1ZmQwMDVmNmRiYjA=");

			System.out.println(signatureString7);

			// 优惠核销查询
			Map<String, String> queryParams7 = new HashMap<String, String>();
			queryParams7.put("secretId", "361cdbda79c2447f85da7a86b879393e");
			queryParams7.put("parkCode", "75701040001");
			queryParams7.put("carNo", "粤XGG271");
//			queryParams7.put("postStatus", "2");

			queryParams7.put("startTime", "2023-06-25 00:00:00");
			queryParams7.put("endTime", "2023-06-27 00:00:00");
			queryParams7.put("timeStamp", String.valueOf(currentTimeMillis));
			String signatureString8 = getSignatureString("http://oepn.ajbcloud.com", "/SQ-PostLogList/Coupons",
					queryParams7, "MTkyZTdhYzhmZGE0YjM4MTc1NDc1ZmQwMDVmNmRiYjA=");
			System.out.println(signatureString8);



			// 优惠券撤销
			Map<String, String> queryParams8 = new HashMap<String, String>();
			queryParams8.put("secretId", "361cdbda79c2447f85da7a86b879393e");
			queryParams8.put("parkCode", "75701040001");
			queryParams8.put("barCode", "	a64da040-0939-4c36-b8cb-4a1dee7d2932");
			queryParams8.put("timeStamp", String.valueOf(currentTimeMillis));
			String signatureString9 = getSignatureString("http://oepn.ajbcloud.com", "/SQ-Coupons/Cancel",
					queryParams8, "MTkyZTdhYzhmZGE0YjM4MTc1NDc1ZmQwMDVmNmRiYjA=");
			System.out.println("优惠券撤销" + signatureString9);
			System.out.println(UUID.randomUUID().toString().replaceAll("-", ""));*/

			// 优惠券派发
			Map<String, String> queryParams9 = new HashMap<String, String>();
			queryParams9.put("secretId", "361cdbda79c2447f85da7a86b879393e");
			queryParams9.put("parkCode", "75701040001");
			queryParams9.put("carNo", "粤E925SZ");
			queryParams9.put("barCode", "d0f10177-4f42-487f-1f38-24cbc5ff9a0e");
			queryParams9.put("merchantId", "6c542be6-8a17-490d-828b-e3779f3efa40");
			queryParams9.put("type", "6");
			queryParams9.put("startTime", "2025-04-27 07:00:00");
			queryParams9.put("endTime", "2025-04-27 20:00:00");
			queryParams9.put("timeStamp", String.valueOf(currentTimeMillis));
			System.out.println("优惠券派发："
					+ getSignatureString("http://oepn.ajbcloud.com", COUPONS_ADD, queryParams9,
							"MTkyZTdhYzhmZGE0YjM4MTc1NDc1ZmQwMDVmNmRiYjA="));
		}


}
