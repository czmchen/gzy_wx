package com.weixin.interceptor;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.weixin.constants.CommonConstants;
import com.weixin.util.C3P0DBManager;
import com.weixin.vo.WxEmoji;

public class PriorityStrartUp extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected final static Logger LOGGER = Logger.getLogger(PriorityStrartUp.class);

	@Override
	public void init() throws ServletException {
		super.init();
		try {
//			String keyString = "WX_NEW_USER:olSMfty-Izl7sqsQM493M4YjmEsI";//标识为新关注用户
//			RedisTemplate redisTemplate =(RedisTemplate)SpringUtil.getBean("redisTemplate");
//			redisTemplate.opsForHash().put(keyString,"olSMfty-Izl7sqsQM493M4YjmEsI","NEW_TRUE");
//			
//			
			LOGGER.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").format(new Date()) + " PriorityStrartUp任务进行中。。。");
			C3P0DBManager.getInstance().initPools();
			
//			ExecutorService exec = Executors.newFixedThreadPool(20);
//			exec.execute(new TaskRMWXMsgNoteThread());
//			exec.execute(new TaskRMWxSendInfoThread());
//			exec.execute(new TaskRMWxSendSKInfoThread());
//			exec.execute(new TaskRMWxUserCustThread());
//			exec.execute(new TaskRMWxOrderThread());
//			exec.execute(new TaskRMWXSendMsgThread());
//			exec.execute(new TaskRMWxOrderAutoCancleThread());
//			exec.execute(new TaskRMWxOrderSignThread());
//			exec.execute(new TaskWXCustromerMsgThread());// 微信客服服务
			
			/*
			 * // exec.execute(new TaskUserSysThread());// 全量从公众号抽取所有关注的客户数据信息进行数据更新
			 */			
			initWXServerType();//微信服务支持的业务类型
			initOrderStatus();
			WxEmoji.initWXEmojiData();
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}
	
	private void initWXServerType() {
		CommonConstants.WX_SERVER_TYPE.add(CommonConstants.IMG_TYPE_GIF);
		CommonConstants.WX_SERVER_TYPE.add(CommonConstants.IMG_TYPE_JPEG);
		CommonConstants.WX_SERVER_TYPE.add(CommonConstants.IMG_TYPE_JPG);
		CommonConstants.WX_SERVER_TYPE.add(CommonConstants.IMG_TYPE_PNG);
	}

	private void initOrderStatus() {
		CommonConstants.ORDER_STATUS_DATA.put(-10, "已取消");
		CommonConstants.ORDER_STATUS_DATA.put(-6, "已关闭");
		CommonConstants.ORDER_STATUS_DATA.put(-5, "退款中");
		CommonConstants.ORDER_STATUS_DATA.put(-4, "已审待发货");
		CommonConstants.ORDER_STATUS_DATA.put(-3, "已审待付款");
		CommonConstants.ORDER_STATUS_DATA.put(-2, "弃审(无效单据)");
		CommonConstants.ORDER_STATUS_DATA.put(-1, "待审核");
		CommonConstants.ORDER_STATUS_DATA.put(0, "待付款");
		CommonConstants.ORDER_STATUS_DATA.put(1, "待发货");
		CommonConstants.ORDER_STATUS_DATA.put(2, "已发货");
		CommonConstants.ORDER_STATUS_DATA.put(3, "已收货未付款");
		CommonConstants.ORDER_STATUS_DATA.put(4, "已发货待付款");
		CommonConstants.ORDER_STATUS_DATA.put(5, "签收未付款");
		CommonConstants.ORDER_STATUS_DATA.put(6, "已收款");
		CommonConstants.ORDER_STATUS_DATA.put(10, "已付款已收货");

		CommonConstants.ORDER_STATUS_DATA.put(11, "未付款未受理");
		CommonConstants.ORDER_STATUS_DATA.put(12, "未付款已受理");
//		CommonConstants.ORDER_STATUS_DATA.put(13, "未付款正在备货");
//		CommonConstants.ORDER_STATUS_DATA.put(14, "未付款已发货");
		CommonConstants.ORDER_STATUS_DATA.put(15, "未付款已收货");
		CommonConstants.ORDER_STATUS_DATA.put(16, "已受理");//modifiy by 20230225，银联支付触发
		CommonConstants.ORDER_STATUS_DATA.put(21, "已付款未受理");
		CommonConstants.ORDER_STATUS_DATA.put(22, "已付款已受理");
//		CommonConstants.ORDER_STATUS_DATA.put(23, "已付款正在备货");
//		CommonConstants.ORDER_STATUS_DATA.put(24, "已付款已发货");
		


		/*
		 * CommonConstants.ORDER_STATUS_DATA.put(10, "已完成");
		 * CommonConstants.ORDER_STATUS_DATA.put(11, "未付款待发货");
		 * CommonConstants.ORDER_STATUS_DATA.put(12, "未付款已受理");
		 * CommonConstants.ORDER_STATUS_DATA.put(13, "未付款正在备货");
		 * CommonConstants.ORDER_STATUS_DATA.put(14, "未付款已发货");
		 * CommonConstants.ORDER_STATUS_DATA.put(15, "未付款已签收");
		 * CommonConstants.ORDER_STATUS_DATA.put(21, "已付款待发货");
		 * CommonConstants.ORDER_STATUS_DATA.put(22, "已付款已受理");
		 * CommonConstants.ORDER_STATUS_DATA.put(23, "已付款正在备货");
		 * CommonConstants.ORDER_STATUS_DATA.put(24, "已付款已发货");
		 */
	}
	
	
}
