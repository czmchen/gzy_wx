package com.weixin.threads;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.weixin.util.C3P0DBManager;
import com.weixin.util.DateUtils;
import com.weixin.util.StringUtil;
import com.weixin.util.WXCustomerManagerUtil;
import com.weixin.util.WxUtil;
import com.weixin.vo.RMWxSendInfo;
import com.weixin.vo.TemplateMessage;
import com.weixin.vo.TemplateValue;
import com.yq.service.OrderService;

public class TaskRMWxSendInfoThread implements Runnable {
	@Autowired
	private OrderService orderService;

	private final static Logger logger = Logger.getLogger(TaskRMWxSendInfoThread.class);
	public static final String TEMPLATE_ID_ORDER_CONFIG_KEY = "LvEHA5UuQKLnwPgi9fGVdcWhs4bKLimAAM-PdDbUnZI";// 订单下达通知

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(10000);
				logger.debug(new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").format(new Date()) + " TaskRMWxSendInfoThread任务进行中。。。");
				List<RMWxSendInfo> lstRMWxSendInfo = getRMWxSendInfo();
				if (lstRMWxSendInfo.size() > 0) {
					updateRMOrderInfoWXreaddate(lstRMWxSendInfo);// 先更新读取的时间
					sendOrderInfoNote(lstRMWxSendInfo);// 发送微信通知
					updateRMOrderInfoWXSendData(lstRMWxSendInfo);// 回写成功标志并更新回写时间
					updateOrderStatus(lstRMWxSendInfo);// 更新订单表的状态为待付款状态
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}

	private void updateOrderStatus(List<RMWxSendInfo> lstRMWxSendInfo) {
		for (RMWxSendInfo objRMWxSendInfo : lstRMWxSendInfo) {
			if (objRMWxSendInfo.getBz().indexOf("成功") != -1 && !StringUtil.isRealEmpty(objRMWxSendInfo.getWxOrderNO()) && StringUtil.isRealEmpty(objRMWxSendInfo.getWxinfo())) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("status", "0");
				map.put("order_id", objRMWxSendInfo.getWxOrderNO());
				map.put("oppen_id", objRMWxSendInfo.getCustWXnum());
				orderService.upstatusByMap(map);
			}
		}
	}

	private void sendOrderInfoNote(List<RMWxSendInfo> lstRMWxSendInfo) {
		for (RMWxSendInfo objRMWxSendInfo : lstRMWxSendInfo) {// 发送微信的通知
			String reContent = null;
			try {
				TemplateMessage templateMessage = new TemplateMessage();
				templateMessage.setTemplate_id(TEMPLATE_ID_ORDER_CONFIG_KEY);
				if (!StringUtil.isRealEmpty(objRMWxSendInfo.getWxinfo())) {
					JSONObject text = new JSONObject();
					text.put("content", objRMWxSendInfo.getWxinfo());
					JSONObject json = new JSONObject();
					json.put("touser", objRMWxSendInfo.getCustWXnum());
					json.put("text", text);
					json.put("msgtype", "text");
					reContent = WXCustomerManagerUtil.customMsgSend(json);
				} else {
					templateMessage.setTouser(objRMWxSendInfo.getCustWXnum());
					templateMessage.setUrl(StringUtil.getSetting().getLink() + "/page/orderDetail.html?order_id=" + (!StringUtil.isRealEmpty(objRMWxSendInfo.getWxOrderNO()) ? objRMWxSendInfo.getWxOrderNO() : objRMWxSendInfo.getOrderNo())
							+ (!StringUtil.isRealEmpty(objRMWxSendInfo.getWxOrderNO()) ? "微信" : ""));
					templateMessage.getData().setFirst(new TemplateValue("您好,甘竹花生油提醒您,您有新的订单", "#000000"));
					templateMessage.getData().setKeyword1(new TemplateValue(!StringUtil.isRealEmpty(objRMWxSendInfo.getWxOrderNO()) ? objRMWxSendInfo.getWxOrderNO() : objRMWxSendInfo.getOrderNo(), "#000000"));
					templateMessage.getData().setKeyword2(new TemplateValue(objRMWxSendInfo.getOrderDate(), "#000000"));
					templateMessage.getData().setKeyword3(new TemplateValue(objRMWxSendInfo.getOrderJE().toString(), "#000000"));
					templateMessage.getData().setKeyword4(new TemplateValue("请留意订单支付通知!", "#000000"));
					templateMessage.getData().setKeyword5(new TemplateValue(objRMWxSendInfo.getOrderInfo(), "#000000"));
					templateMessage.getData().setRemark(new TemplateValue("微信订单号:" + objRMWxSendInfo.getWxOrderNO()+"\r\n内部订单号:" + objRMWxSendInfo.getOrderNo()+"\r\n物流单号:" + objRMWxSendInfo.getOrderWLDH(), "#000000"));
					reContent = WxUtil.sendNote(templateMessage);
				}

				if (reContent.indexOf("\"errcode\":0,") != -1) {
					logger.info(reContent);
					objRMWxSendInfo.setBz("发送成功");
				} else {
					objRMWxSendInfo.setBz("发送失败");
					throw new RuntimeException(reContent);
				}
			} catch (Exception e) {
				logger.info(e.toString());
			}
		}
	}

	private void updateRMOrderInfoWXreaddate(List<RMWxSendInfo> lstRMWxSendInfo) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			StringBuffer sql = new StringBuffer("update WxSendInfo set wxreaddate=getdate(),IsRead = 1 where id in(");
			for (int i = 0; i < lstRMWxSendInfo.size(); i++) {
				RMWxSendInfo objRMWxSendInfo = lstRMWxSendInfo.get(i);
				if (i != 0) {
					sql.append(",");
				}
				sql.append(objRMWxSendInfo.getId());
			}
			sql.append(")");

			conn = C3P0DBManager.getConnection();
			ps = conn.prepareStatement(sql.toString());
			ps.executeUpdate();
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, null);
		}
	}

	private int updateRMOrderInfoWXSendData(List<RMWxSendInfo> lstRMWxSendInfo) {
		int succFlag = 0;
		Connection conn = null;
		Statement st = null;
		try {
			conn = C3P0DBManager.getConnection();
			st = conn.createStatement();
			for (RMWxSendInfo objRMWxSendInfo : lstRMWxSendInfo) {
				StringBuffer sql = new StringBuffer("update WxSendInfo set wxsenddate=getdate(),IsSend=1,");
				sql.append("bz=(ISNULL(bz,'')+'发送时间:").append(DateUtils.nowDate(DateUtils.DATETIME_FORMATE)).append(",发送状态:").append(objRMWxSendInfo.getBz()).append("'+CHAR(10))");
				sql.append(" where id = ").append(objRMWxSendInfo.getId());
				st.addBatch(sql.toString());
			}
			st.executeBatch();
			succFlag = 1;
		} catch (SQLException e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, st, null);
		}
		return succFlag;
	}

	private List<RMWxSendInfo> getRMWxSendInfo() throws RuntimeException {
		List<RMWxSendInfo> lstRMWxSendInfo = new ArrayList<RMWxSendInfo>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = C3P0DBManager.getConnection();
			ps = conn.prepareStatement("select t.ID,t.CustID,t.CustMC,t.CustWXnum,t.wxinfo,t.OrderNo,t.OrderDate,t.Ordernum,t.OrderJE,t.OrderWLDH,t.OrderInfo,t.wxOrderNO,t.creater,t.creatdate,t.wxreaddate,t.wxsenddate,t.IsRead,t.IsSend,t.bz from WxSendInfo t  left join WxUserCust f on t.CustWXnum = f.openid where ISNULL(t.CustWXnum,'') <> '' and IsSend<>1 and  f.isWXFocus <>-1");
			rs = ps.executeQuery();
			while (rs.next()) {
				RMWxSendInfo objRMWxSendInfo = new RMWxSendInfo();
				objRMWxSendInfo.setId(rs.getLong("ID"));
				objRMWxSendInfo.setCreatdate(rs.getString("creatdate"));
				objRMWxSendInfo.setCreater(rs.getString("creater"));
				objRMWxSendInfo.setCustID(rs.getString("CustID"));
				objRMWxSendInfo.setCustMC(rs.getString("CustMC"));
				objRMWxSendInfo.setCustWXnum(rs.getString("CustWXnum"));
				objRMWxSendInfo.setIsRead(rs.getInt("IsRead"));
				objRMWxSendInfo.setIsSend(rs.getInt("IsSend"));
				objRMWxSendInfo.setOrderDate(rs.getString("OrderDate"));
				objRMWxSendInfo.setOrderInfo(rs.getString("OrderInfo"));
				objRMWxSendInfo.setOrderJE(rs.getDouble("OrderJE"));
				objRMWxSendInfo.setOrderNo(rs.getString("OrderNo"));
				objRMWxSendInfo.setOrdernum(rs.getDouble("Ordernum"));
				objRMWxSendInfo.setOrderWLDH(rs.getString("OrderWLDH"));
				objRMWxSendInfo.setWxinfo(rs.getString("wxinfo"));
				objRMWxSendInfo.setWxOrderNO(rs.getString("wxOrderNO"));
				objRMWxSendInfo.setWxreaddate(rs.getString("wxreaddate"));
				objRMWxSendInfo.setWxsenddate(rs.getString("wxsenddate"));
				objRMWxSendInfo.setBz(rs.getString("bz"));
				lstRMWxSendInfo.add(objRMWxSendInfo);
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, rs);
		}
		return lstRMWxSendInfo;
	}

}
