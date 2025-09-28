package com.weixin.jobs;

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
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.weixin.util.C3P0DBManager;
import com.weixin.util.DateUtils;
import com.weixin.util.StringUtil;
import com.weixin.util.WxUtil;
import com.weixin.vo.RMWxSendSKInfo;
import com.weixin.vo.TemplateMessage;
import com.weixin.vo.TemplateValue;
import com.yq.service.OrderService;
@DisallowConcurrentExecution
public class RMWxSendSKInfoJob implements Job{

	@Autowired
	private OrderService orderService;

	private final static Logger logger = Logger.getLogger(RMWxSendSKInfoJob.class);
	public static final String TEMPLATE_ID_ORDER_PAY_KEY = "CyKXG5Thhd1isCfUr2z56eAD95KtKKC0bMoJ0Mt3GL8";//订单支付提醒

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			logger.debug(new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").format(new Date()) + " RMWxSendSKInfo任务进行中。。。");
			List<RMWxSendSKInfo> lstRMWxSendSKInfo = getRMWxSendSKInfo();
			if (lstRMWxSendSKInfo.size() > 0) {
				updateRMWxSendSKInfoWXreaddate(lstRMWxSendSKInfo);// 先更新读取的时间
				sendOrderPayInfoNote(lstRMWxSendSKInfo);// 发送微信通知
				updateRMWxSendSKInfoWXSendData(lstRMWxSendSKInfo);// 回写成功标志并更新回写时间
				updateOrderSendNoteStatus(lstRMWxSendSKInfo);// 更新订单表的状态为待付款状态
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	
	}

	private void updateOrderSendNoteStatus(List<RMWxSendSKInfo> lstRMWxSendSKInfo) {
		for (RMWxSendSKInfo objRMWxSendSKInfo : lstRMWxSendSKInfo) {
			if (objRMWxSendSKInfo.getBz().indexOf("成功") != -1 && !StringUtil.isRealEmpty(objRMWxSendSKInfo.getWxOrderNO())) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("status", "0");
				map.put("order_id", objRMWxSendSKInfo.getWxOrderNO());
				map.put("oppen_id", objRMWxSendSKInfo.getCustWXnum());
				orderService.upstatusByMap(map);
			}
		}
	}

	private void sendOrderPayInfoNote(List<RMWxSendSKInfo> lstRMWxSendSKInfo) {
		for (RMWxSendSKInfo objRMWxSendSKInfo : lstRMWxSendSKInfo) {// 发送微信的通知
			try {
				TemplateMessage templateMessage = new TemplateMessage();
				templateMessage.setTemplate_id(TEMPLATE_ID_ORDER_PAY_KEY);
				templateMessage.setTouser(objRMWxSendSKInfo.getCustWXnum());
				templateMessage.setUrl(StringUtil.getSetting().getLink()+"/page/orderDetail.html?order_id="+(!StringUtil.isRealEmpty(objRMWxSendSKInfo.getWxOrderNO()) ? objRMWxSendSKInfo.getWxOrderNO() : objRMWxSendSKInfo.getOrderNo()));
//				templateMessage.getData().setFirst(new TemplateValue("您好,甘竹花生油提醒您,您有新的支付订单", "#000000"));
				templateMessage.getData().setKeyword1(new TemplateValue(objRMWxSendSKInfo.getOrderInfo(), "#000000"));
				templateMessage.getData().setKeyword2(new TemplateValue(objRMWxSendSKInfo.getWxOrderNO() != null && !objRMWxSendSKInfo.getWxOrderNO().equals("") ? objRMWxSendSKInfo.getWxOrderNO() : objRMWxSendSKInfo.getOrderNo(), "#000000"));
				templateMessage.getData().setKeyword3(new TemplateValue(objRMWxSendSKInfo.getOrderJE().toString()+"(请点击支付)", "#000000"));
				templateMessage.getData().setKeyword4(new TemplateValue(objRMWxSendSKInfo.getOrderDate(), "#000000"));
//				templateMessage.getData().setRemark(new TemplateValue("微信订单号:" + objRMWxSendSKInfo.getWxOrderNO()+"\r\n内部订单号:" + objRMWxSendSKInfo.getOrderNo()+"\r\n物流单号:" + objRMWxSendSKInfo.getOrderWLDH()+"\r\n支付方式:"+objRMWxSendSKInfo.getPayway(), "#000000"));
				String reContent = WxUtil.sendNote(templateMessage);
				if (reContent.indexOf("\"errcode\":0,") != -1) {
					logger.info(reContent);
					objRMWxSendSKInfo.setBz("发送成功");
				} else {
					objRMWxSendSKInfo.setBz("发送失败,内容:"+reContent);
					throw new RuntimeException(reContent);
				}
			} catch (Exception e) {
				logger.info(e.toString());
			}
		}
	}

	private void updateRMWxSendSKInfoWXreaddate(List<RMWxSendSKInfo> lstRMWxSendSKInfo) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			StringBuffer sql = new StringBuffer("update WxSendSKInfo set wxreaddate=getdate(),IsRead = 1 where id in(");
			for (int i = 0; i < lstRMWxSendSKInfo.size(); i++) {
				RMWxSendSKInfo objRMWxSendSKInfo = lstRMWxSendSKInfo.get(i);
				if (i != 0) {
					sql.append(",");
				}
				sql.append(objRMWxSendSKInfo.getId());
			}
			sql.append(")");
			logger.debug(sql.toString());
			conn = C3P0DBManager.getConnection();
			ps = conn.prepareStatement(sql.toString());
			ps.executeUpdate();
		}  catch (SQLException e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, null);
		}
	}

	private int updateRMWxSendSKInfoWXSendData(List<RMWxSendSKInfo> lstRMWxSendSKInfo) {
		int succFlag = 0;
		Connection conn = null;
		Statement st = null;
		try {
			conn = C3P0DBManager.getConnection();
			st = conn.createStatement();
			for (RMWxSendSKInfo objRMWxSendSKInfo : lstRMWxSendSKInfo) {
				StringBuffer sql = new StringBuffer("update WxSendSKInfo set wxsenddate=getdate(),IsSend=1,");
				sql.append("bz=(ISNULL(bz,'')+'发送时间:").append(DateUtils.nowDate(DateUtils.DATETIME_FORMATE)).append(",发送状态:").append(objRMWxSendSKInfo.getBz()).append("'+CHAR(10))");
				sql.append(" where id = ").append(objRMWxSendSKInfo.getId());
				st.addBatch(sql.toString());
			}
			st.executeBatch();
			st.clearBatch();
			succFlag = 1;
		} catch (SQLException e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, st, null);
		}
		return succFlag;
	}

	private List<RMWxSendSKInfo> getRMWxSendSKInfo() throws RuntimeException {
		List<RMWxSendSKInfo> lstRMWxSendInfo = new ArrayList<RMWxSendSKInfo>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = C3P0DBManager.getConnection();
			ps = conn.prepareStatement("select t.ID,t.CustID,t.CustMC,t.CustWXnum,t.payway,t.OrderNo,t.OrderDate,t.Ordernum,t.OrderJE,t.OrderWLDH,t.OrderInfo,t.wxOrderNO,t.creater,t.creatdate,t.wxreaddate,t.wxsenddate,t.IsRead,t.IsSend,t.bz,t.ISZF from WxSendSKInfo t left join WxUserCust f on t.CustWXnum = f.openid where ISNULL(t.CustWXnum,'') <> '' and IsSend<>1 and  f.isWXFocus <>-1");
			rs = ps.executeQuery();
			while (rs.next()) {
				RMWxSendSKInfo objRMWxSendSKInfo = new RMWxSendSKInfo();
				objRMWxSendSKInfo.setId(rs.getLong("ID"));
				objRMWxSendSKInfo.setCreatdate(rs.getString("creatdate"));
				objRMWxSendSKInfo.setCreater(rs.getString("creater"));
				objRMWxSendSKInfo.setCustID(rs.getString("CustID"));
				objRMWxSendSKInfo.setCustMC(rs.getString("CustMC"));
				objRMWxSendSKInfo.setCustWXnum(rs.getString("CustWXnum"));
				objRMWxSendSKInfo.setIsRead(rs.getInt("IsRead"));
				objRMWxSendSKInfo.setIsSend(rs.getInt("IsSend"));
				objRMWxSendSKInfo.setOrderDate(rs.getString("OrderDate"));
				objRMWxSendSKInfo.setOrderInfo(rs.getString("OrderInfo"));
				objRMWxSendSKInfo.setOrderJE(rs.getDouble("OrderJE"));
				objRMWxSendSKInfo.setOrderNo(rs.getString("OrderNo"));
				objRMWxSendSKInfo.setOrdernum(rs.getDouble("Ordernum"));
				objRMWxSendSKInfo.setOrderWLDH(rs.getString("OrderWLDH"));
				objRMWxSendSKInfo.setPayway(rs.getString("payway"));
				objRMWxSendSKInfo.setWxOrderNO(rs.getString("wxOrderNO"));
				objRMWxSendSKInfo.setWxreaddate(rs.getString("wxreaddate"));
				objRMWxSendSKInfo.setWxsenddate(rs.getString("wxsenddate"));
				objRMWxSendSKInfo.setBz(rs.getString("bz"));
				objRMWxSendSKInfo.setIszf(rs.getString("ISZF"));
				lstRMWxSendInfo.add(objRMWxSendSKInfo);
			}
		} catch (SQLException e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, rs);
		}
		return lstRMWxSendInfo;
	}



}
