package com.weixin.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonConstants {
	public static final Map<Integer, String> ORDER_STATUS_DATA = new HashMap<Integer, String>();
	public static final Map<String, String> WX_EMOJI2IMG_DATA = new HashMap<String, String>();
	public static final Map<String, String> WX_IMG2EMOJI_DATA = new HashMap<String, String>();
	public static final List<String> WX_SERVER_TYPE = new ArrayList<String>();

	public static final String CUSTOMER_EMOJI2IMG_DOMAIN_URL = "customer.emoji2img.domain.url";
	public static final String CUSTOMER_DOMAIN_URL_FIRST = "customer.img2emoji.domain.url.first";
	public static final String CUSTOMER_DOMAIN_URL_SECOND = "customer.img2emoji.domain.url.second";
	public static final String CUSTOMER_DOMAIN_URL_THIRD = "customer.img2emoji.domain.url.third";
	public static final String WEB_ROOT_URL = "web.root.url";

	public final static String IMG_TYPE_PNG = "PNG";// 微信支持的图片文件格式
	public final static String IMG_TYPE_JPG = "JPG";
	public final static String IMG_TYPE_JPEG = "JPEG";
	public final static String IMG_TYPE_GIF = "GIF";

	public final static String VIDEO_TYPE_MP4 = "MP4";
	
	public final static String DB_MYSQL = "MYSQL";
	public final static String DB_SQL_SERVER = "SQL_SERVER";
	

}
