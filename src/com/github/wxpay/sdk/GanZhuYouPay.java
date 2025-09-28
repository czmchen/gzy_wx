package com.github.wxpay.sdk;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.weixin.entity.WxSetting;
import com.weixin.service.WxSettingService;
import com.weixin.util.DateUtils;
import com.weixin.util.SpringUtil;
import com.weixin.util.Utility;
import com.yq.vo.OrderVO;

public class GanZhuYouPay {
	private static Logger logger = Logger.getLogger(GanZhuYouPay.class);

	public void getPayPackage(final OrderVO objOrderVO, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		// 发起微信支付
		WXPay wxpay = null;
		Map<String, String> result = new HashMap<>();
		Map<String, String> data = new HashMap<String, String>();
		try {
			wxpay = new WXPay(MyConfig.getInstance());
			
			String openid = (String)session.getAttribute("oppen_id");
			
			WxSettingService wxSettingService =(WxSettingService)SpringUtil.getBean("wxSettingService") ;
			WxSetting wxSetting  =  wxSettingService.selectByPrimaryKey(1);
			String notify_url = wxSetting.getLink()+"page/payCallback.html";
			
			data.put("body", "甘竹花生油支付业务");
			data.put("out_trade_no", objOrderVO.getOrder_id()+"|_|"+DateUtils.nowDate("HHmmss"));
			data.put("device_info", "");
			data.put("fee_type", "CNY");
			data.put("total_fee", new DecimalFormat("0").format(objOrderVO.getGoods_total()*100));// 订单金额, 单位分 String.valueOf((int)(objOrderVO.getGoods_total()*100))
			data.put("spbill_create_ip", "127.0.0.1");
			data.put("notify_url", notify_url);
			data.put("trade_type", "JSAPI"); // 此处指定为扫码支付
			data.put("openid", openid);
			
			logger.error("GanZhuYouPay.getPayPackage发起微信支付下单接口, request={}"+data);
			Map<String, String> resp = wxpay.unifiedOrder(data);
			logger.error("GanZhuYouPay.getPayPackage微信支付下单成功, 返回值 response={}"+resp);
			
			String returnCode = resp.get("return_code");
			if (!"SUCCESS".equals(returnCode)) {
				return ;
			}
			String prepay_id = resp.get("prepay_id");
			if (prepay_id == null) {
				return ;
			}

			// ******************************************
			//
			// 前端调起微信支付必要参数
			//
			// ******************************************
			String packages = "prepay_id=" + prepay_id;
			Map<String, String> wxPayMap = new HashMap<String, String>();
			wxPayMap.put("appId", MyConfig.getInstance().getAppID());
			wxPayMap.put("timeStamp", String.valueOf(Utility.getCurrentTimeStamp()));
			wxPayMap.put("nonceStr", Utility.generateUUID());
			wxPayMap.put("package", packages);
			wxPayMap.put("signType", "MD5");
			// 加密串中包括 appId timeStamp nonceStr package signType 5个参数, 通过sdk
			// WXPayUtil类加密, 注意, 此处使用 MD5加密 方式
			String sign = WXPayUtil.generateSignature(wxPayMap, MyConfig.getInstance().getKey());

			// ******************************************
			//
			// 返回给前端调起微信支付的必要参数
			//
			// ******************************************
			result.put("prepay_id", prepay_id);
			result.put("paySign", sign);
			result.putAll(wxPayMap);
			
			request.setAttribute("payDataAttr", result);
		} catch (Exception e) {
			logger.error("发起微信支付下单接口, request={}"+data+"||"+e);
		}
	}
}
