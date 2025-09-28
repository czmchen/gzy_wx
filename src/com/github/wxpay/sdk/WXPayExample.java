package com.github.wxpay.sdk;

import com.github.wxpay.sdk.WXPay;

import java.util.HashMap;
import java.util.Map;

public class WXPayExample {

	public static void main(String[] args) throws Exception {

		WXPay wxpay = new WXPay(MyConfig.getInstance());

		Map<String, String> data = new HashMap<String, String>();
		data.put("body", "腾讯充值中心-QQ会员充值");
		data.put("out_trade_no", "2016090910595900000012");
		data.put("device_info", "");
		data.put("fee_type", "CNY");
		data.put("total_fee", "1");
		data.put("spbill_create_ip", "221.4.163.98");
		data.put("notify_url", "http://www.example.com/wxpay/notify");
		data.put("trade_type", "NATIVE"); // 此处指定为扫码支付
		data.put("product_id", "12");

		try {
			Map<String, String> resp = wxpay.unifiedOrder(data);
			System.out.println(resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}