package com.yq.constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.weixin.entity.WxMsg;
import com.weixin.util.PropertiesUtils;
import com.yq.entity.User;
import com.yq.vo.UploadVo;

public class CommonConstants {
	public static List<WxMsg> WX_MSG_ALL_DATA;
	public static List<User> USER_ALL_DATA;
	public static final String QR_CODE_GANZHU_IMGPATH = PropertiesUtils.read("customer.file.location") + "/qrCode/ganzhu_logon.jpg";// 嵌入二维码的图片路径
	
	public static final String REDIS_CUSTOMER_SERVER_UUID_DATA_CONSTANTS = "REDIS_CUSTOMER_SERVER_UUID_DATA_CONSTANTS";
	public static final String REDIS_GOODS_REPORT_UUID_DATA_CONSTANTS = "REDIS_GOODS_REPORT_UUID_DATA_CONSTANTS";		
	public static final String REDIS_COMMON_DATA_CONSTANTS = "REDIS_COMMON_DATA_CONSTANTS";
	public static final String REDIS_COMMON_TICKET_DATA_KEY = "REDIS_COMMON_TICKET_DATA_KEY";
	public static final String REDIS_PAY_DATA_KEY = "REDIS_PAY_DATA_KEY";
	
	public static final String NAVIGATION_KEY = "navigation.key";

	public static final Map<String,User> CUSTOMER_LOGIN_QRCODE_DATA = new HashMap<String,User>();
	
	public static final Map<String,UploadVo> UPLOAD_FILE_STATUS_DATA = new HashMap<String,UploadVo>();
	
	public static final String MERCHANTID = PropertiesUtils.read("wx.pay.merchantId");
	public static final String APPID = PropertiesUtils.read("wx.pay.appid");
	public static final String APIV3KEY = PropertiesUtils.read("wx.pay.apiV3Key");
	public static final String PRIVATEKEYPATH = PropertiesUtils.read("wx.pay.privateKeyPath");
	public static final String MERCHANTSERIALNUMBER = PropertiesUtils.read("wx.pay.merchantSerialNumber");
	public static final String ROOT_URL = PropertiesUtils.read("web.root.url");
	
	public static final String FAIL = "FAIL";
	public static final String SUCCESS = "SUCCESS";
	
	public static final String HTTPSTATUS_OK = "200";
}
