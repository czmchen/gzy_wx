package com.wechat.pay.jsapi;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.CloseOrderRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.wechat.pay.java.service.payments.jsapi.model.QueryOrderByIdRequest;
import com.wechat.pay.java.service.payments.jsapi.model.QueryOrderByOutTradeNoRequest;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.weixin.util.PropertiesUtils;
import com.yq.constants.CommonConstants;

public class WXPayJSAPIUtil {

//	private JsapiService service = null;
	private static JsapiServiceExtension jsapiServiceExtension = null;
	private static WXPayJSAPIUtil instance;
	private static NotificationParser notificationParser = null;

	private WXPayJSAPIUtil() {

	}

	public static WXPayJSAPIUtil getInstance() {
		if (instance == null) {
			instance = new WXPayJSAPIUtil();
		}
		return instance;
	}
	
	public JsapiServiceExtension getJsapiServiceExtension() {
		if (jsapiServiceExtension == null) {
			/** 商户号 */
			String merchantId = PropertiesUtils.read("wx.pay.merchantId");
			/** 商户API私钥路径 */
			String privateKeyPath = PropertiesUtils.read("wx.pay.privateKeyPath");
			/** 商户证书序列号 */
			String merchantSerialNumber = PropertiesUtils.read("wx.pay.merchantSerialNumber");
			/** 商户APIV3密钥 */
			String apiV3Key = PropertiesUtils.read("wx.pay.apiV3Key");

			Config config = new RSAAutoCertificateConfig.Builder().merchantId(merchantId)
					.privateKeyFromPath(privateKeyPath).merchantSerialNumber(merchantSerialNumber).apiV3Key(apiV3Key)
					.build();
			jsapiServiceExtension = new JsapiServiceExtension.Builder().config(config).build();
		}
		return jsapiServiceExtension;
	}
	
	/** JSAPI支付下单并完成签名 */
	public static PrepayWithRequestPaymentResponse prepayWithRequestPayment(PrepayRequest request) throws Exception {
		return WXPayJSAPIUtil.getInstance().getJsapiServiceExtension().prepayWithRequestPayment(request);
	}

	/** 关闭订单 */
	public static void closeOrder(CloseOrderRequest request) throws Exception {
		WXPayJSAPIUtil.getInstance().getJsapiServiceExtension().closeOrder(request);
	}


	/** 微信支付订单号查询订单 */
	public static Transaction queryOrderById(QueryOrderByIdRequest request) throws Exception {
		return WXPayJSAPIUtil.getInstance().getJsapiServiceExtension().queryOrderById(request);
	}

	/** 商户订单号查询订单 */
	public static Transaction queryOrderByOutTradeNo(QueryOrderByOutTradeNoRequest request) throws Exception {
		return WXPayJSAPIUtil.getInstance().getJsapiServiceExtension().queryOrderByOutTradeNo(request);
	}
	
	public NotificationParser getNotificationParser() {
		if (notificationParser == null) {// 如果已经初始化了 RSAAutoCertificateConfig，可直接使用,没有的话，则构造一个
			NotificationConfig config = new RSAAutoCertificateConfig.Builder().merchantId(CommonConstants.MERCHANTID)
					.privateKeyFromPath(CommonConstants.PRIVATEKEYPATH)
					.merchantSerialNumber(CommonConstants.MERCHANTSERIALNUMBER).apiV3Key(CommonConstants.APIV3KEY)
					.build();
			notificationParser = new NotificationParser(config);// 初始化 NotificationParser
		}
		return notificationParser;
	}
	
//	public JsapiService getService() {
//		if (service == null) {
//			/** 商户号 */
//			String merchantId = PropertiesUtils.read("wx.pay.merchantId");
//			/** 商户API私钥路径 */
//			String privateKeyPath = PropertiesUtils.read("wx.pay.privateKeyPath");
//			/** 商户证书序列号 */
//			String merchantSerialNumber = PropertiesUtils.read("wx.pay.merchantSerialNumber");
//			/** 商户APIV3密钥 */
//			String apiV3Key = PropertiesUtils.read("wx.pay.apiV3Key");
//
//			Config config = new RSAAutoCertificateConfig.Builder().merchantId(merchantId)
//					.privateKeyFromPath(privateKeyPath).merchantSerialNumber(merchantSerialNumber).apiV3Key(apiV3Key)
//					.build();
//			service = new JsapiService.Builder().config(config).build();
//		}
//		return service;
//	}
	
	/** JSAPI支付下单 */
//	public static PrepayResponse prepay(PrepayRequest request) throws Exception {
//		return WXPayJSAPIUtil.getInstance().getService().prepay(request);
//	}
}
