package com.wechat.pay.jsapi;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.weixin.util.DateUtils;
import com.weixin.util.SpringUtil;
import com.yq.constants.CommonConstants;
import com.yq.service.OrderService;
import com.yq.service.WXPayResultService;
import com.yq.util.JsonUtil;
import com.yq.vo.OrderVO;
import com.yq.vo.wxpay.ReturnTXServerVo;

public class GanZhuJsPayLogic {

	private static final String notify_url = CommonConstants.ROOT_URL + "/page/payNotify.html";
	private static final String description = "甘竹花生油预支付服务";
	private static final String currency = "CNY";
	private static final Logger logger = Logger.getLogger(GanZhuJsPayLogic.class);

	public void prepay(final String openId, final OrderVO objOrderVO, ModelAndView mav) throws Exception {
		String outTradeNo = objOrderVO.getOrder_id() + "|_|" + DateUtils.nowDate("HHmmss");
		PrepayRequest request = new PrepayRequest();
		request.setMchid(CommonConstants.MERCHANTID);
		request.setAppid(CommonConstants.APPID);
		request.setDescription(description);
		request.setNotifyUrl(notify_url);
		Amount amount = new Amount();
		amount.setCurrency(currency);
		amount.setTotal((int) (objOrderVO.getGoods_total() * 100));
		request.setAmount(amount);
		Payer payer = new Payer();
		payer.setOpenid(openId);
		request.setPayer(payer);
		request.setOutTradeNo(outTradeNo);
		PrepayWithRequestPaymentResponse objPrepayWithRequestPaymentResponse = WXPayJSAPIUtil.prepayWithRequestPayment(request);
		mav.addObject("payDataAttr", objPrepayWithRequestPaymentResponse);
	}

	public void callBack(final String wechatPaySerial,final String wechatSignature,final String wechatTimestamp,final String wechatpayNonce,final String requestBody,HttpServletResponse response) {
		logger.error("wechatSignature:"+wechatSignature+"||wechatPaySerial："+wechatPaySerial+"||wechatTimestamp："+wechatTimestamp+"||wechatpayNonce："+wechatpayNonce+"||requestBody："+requestBody);
		// 构造 RequestParam
		RequestParam requestParam = new RequestParam.Builder().serialNumber(wechatPaySerial).nonce(wechatpayNonce).signature(wechatSignature).timestamp(wechatTimestamp).body(requestBody).build();
		try {// 以支付通知回调为例，验签、解密并转换成 Transaction
			Transaction transaction = WXPayJSAPIUtil.getInstance().getNotificationParser().parse(requestParam,Transaction.class);
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("transaction_id",transaction.getTransactionId());
			map.put("bank_type",transaction.getBankType());
			if(transaction.getPayer()!=null) {
				map.put("openid",transaction.getPayer().getOpenid());
			}
			map.put("sign","");
			map.put("fee_type",transaction.getAmount().getCurrency());
			map.put("mch_id",transaction.getMchid());
			map.put("cash_fee",transaction.getAmount().getPayerTotal().toString());
			map.put("out_trade_no",transaction.getOutTradeNo());
			map.put("appid",transaction.getAppid());
			map.put("total_fee",transaction.getAmount().getTotal().toString());
			map.put("trade_type",transaction.getTradeType().toString());
			map.put("result_code",transaction.getTradeState().toString());
			map.put("time_end",transaction.getSuccessTime());
			map.put("is_subscribe","");
			map.put("return_code",transaction.getTradeState().toString());
			((WXPayResultService)SpringUtil.getBean("WXPayResultService")).insert(map);//插入订单记录表
			
			String orderId = transaction.getOutTradeNo();//更新订单
			((OrderService)SpringUtil.getBean("orderService")).updateZFStatus(orderId.split("\\|\\_\\|")[0], transaction.getPayer().getOpenid(),
					transaction.getTransactionId(),new DecimalFormat("0.00").format(Double.valueOf(transaction.getAmount().getTotal()) / 100));// 更改订单状态
			logger.error(JsonUtil.obj2JSON(transaction));
		} catch (Exception e) {// 签名验证失败，返回 401 UNAUTHORIZED 状态码
			logger.error("wx callBack", e);
			ReturnTXServerVo objReturnTXServerVo = new ReturnTXServerVo();
			objReturnTXServerVo.setCode(CommonConstants.FAIL);
			objReturnTXServerVo.setMessage(e.getMessage());
			notifyWX(response, JsonUtil.obj2JSON(objReturnTXServerVo), 1);
		}
		notifyWX(response, CommonConstants.HTTPSTATUS_OK, 0);// 处理成功，返回 200 OK 状态码
	}
	
	public static final String CONTENTTYPE_JSON = "application/json; charset=UTF-8";
	public static final String CONTENTTYPE_HTML = "text/html; charset=UTF-8";

	/***
	 * 
	 * @param response
	 * @param content
	 * @param type 0:文本 其他:json
	 */
	public void notifyWX(HttpServletResponse response,String content,int type) {
		PrintWriter printWriter = null;
		try {
			String contentType = (type==0?CONTENTTYPE_HTML:CONTENTTYPE_JSON);
			response.setContentType(contentType);
			printWriter = response.getWriter();
			printWriter.write(content);
			printWriter.flush();
		} catch (IOException e) {
			logger.error("returnJson is error!", e);
		}finally {
			printWriter.close();
		}
	}
}
