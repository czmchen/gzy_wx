package com.weixin.jobs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.weixin.constants.CommonConstants;
import com.weixin.service.WxOrderService;
import com.weixin.util.C3P0DBManager;
import com.weixin.util.DateUtils;
import com.weixin.util.SpringUtil;
import com.weixin.util.StringUtil;
import com.weixin.util.WxUtil;
import com.weixin.vo.TemplateMessage;
import com.weixin.vo.TemplateValue;
@DisallowConcurrentExecution
public class RMWxOrderJob implements Job {

	private final static Logger logger = Logger.getLogger(RMWxOrderJob.class);
	public static final String TEMPLATE_ID_ORDER_STATUS_NOTE_KEY = "6Tup3UdojqmU_WLbR9wFla0h6BQ0ITb93nIxJbWkneA";//订单状态提醒

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			logger.debug(new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").format(new Date()) + " TaskRMWxOrderThread任务进行中。。。");
			this.getWxOrder();
			if (!DATA.isEmpty()&&DATA.size()>0) {
				sendOrderPayInfoNote();// 发送微信通知，通知状态改变
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private void sendOrderPayInfoNote() throws Exception {
		StringBuffer sql = new StringBuffer();
		Map<String,List<String>> execSqlData = new HashMap<String,List<String>>(); 
		List<String> batExecSQL = new ArrayList<String>();
		List<String> batExecLocalSQL = new ArrayList<String>();
		int index = 0;
		for(Entry<String, Map<String, String>> iter : DATA.entrySet()){
		    sql.append("'").append(iter.getKey()).append("'");
		    if(index!=(DATA.size()-1)){
		    	sql.append(",");
		    }
		    index++;
		}
		Map<String,List<Map<String,String>>> orderDetail = getWxOrderDetail(sql.toString());
		for(Entry<String, List<Map<String, String>>> iterData : orderDetail.entrySet()){
			StringBuffer msg = new StringBuffer();
//			msg.append("微信订单号:").append(DATA.get(iterData.getKey()).get("wxOrderNO")).append("\r\n");
//			msg.append("内部订单号:").append(DATA.get(iterData.getKey()).get("orderNO")).append("\r\n").append("\r\n");
			msg.append("订单明细:").append("\r\n");
			for(Map<String, String> mapIter : iterData.getValue()){
				msg.append(mapIter.get("InvName")).append("X").append(mapIter.get("Qty")).append("罐").append("(").append(mapIter.get("NQty")).append("件)").append("\r\n");
			}
			msg.append("金额合计:¥").append(DATA.get(iterData.getKey()).get("Amount")).append("\r\n\r\n");
			if (Integer.valueOf(DATA.get(iterData.getKey()).get("orderStatus")) == -3
					|| Integer.valueOf(DATA.get(iterData.getKey()).get("orderStatus")) == 0
					|| Integer.valueOf(DATA.get(iterData.getKey()).get("orderStatus")) == 3
					|| Integer.valueOf(DATA.get(iterData.getKey()).get("orderStatus")) == 4
					|| Integer.valueOf(DATA.get(iterData.getKey()).get("orderStatus")) == 5
					|| Integer.valueOf(DATA.get(iterData.getKey()).get("orderStatus")) == 11
					|| Integer.valueOf(DATA.get(iterData.getKey()).get("orderStatus")) == 12
					|| Integer.valueOf(DATA.get(iterData.getKey()).get("orderStatus")) == 15) {
				msg.append("请点击进行订单支付");
			}
			
			try {
				String sendDate = DateUtils.nowDate(DateUtils.DATETIME_FORMATE);
				String statusVal = CommonConstants.ORDER_STATUS_DATA.get(Integer.valueOf(DATA.get(iterData.getKey()).get("orderStatus")));
				StringBuffer statusSbf = new StringBuffer();
				statusSbf.append(statusVal).append("(详情请点击");
				if (Integer.valueOf(DATA.get(iterData.getKey()).get("orderStatus")) > 10 && Integer.valueOf(DATA.get(iterData.getKey()).get("orderStatus")) < 20) {
					statusSbf.append("支付");
				}
				statusSbf.append(")");
				StringBuffer updateSql = new StringBuffer();
				StringBuffer updateLocalSql = new StringBuffer();
				TemplateMessage templateMessage = new TemplateMessage();
				templateMessage.setTemplate_id(TEMPLATE_ID_ORDER_STATUS_NOTE_KEY);
				templateMessage.setTouser(DATA.get(iterData.getKey()).get("CustWXnum"));
				templateMessage.setUrl(StringUtil.getSetting().getLink()+"/page/orderDetail.html?order_id="+(DATA.get(iterData.getKey()).get("orderId")));
//				templateMessage.getData().setFirst(new TemplateValue("您好,甘竹花生油提醒您,您的订单状态已改变，请留意", "#000000"));
				templateMessage.getData().setKeyword1(new TemplateValue(DATA.get(iterData.getKey()).get("orderId"), "#000000"));
				templateMessage.getData().setKeyword2(new TemplateValue(statusSbf.toString(),"#000000"));
				templateMessage.getData().setKeyword3(new TemplateValue(sendDate, "#000000"));
//				templateMessage.getData().setRemark(new TemplateValue(msg.toString(), "#d81e06"));
				String reContent = WxUtil.sendNote(templateMessage);
				logger.error("RMWxOrderJob.sendOrderPayInfoNote():"+reContent+"||templateMessage.getData().setKeyword1() orderId:"+templateMessage.getData().getKeyword1().getValue()+"||templateMessage.getData().getUrl()"+templateMessage.getUrl()+"||templateMessage.getTouser()"+templateMessage.getTouser());
				if (reContent.indexOf("\"errcode\":0,") != -1) {
					updateSql.append("update WxOrder set issend=0,sendDataText=(ISNULL(sendDataText,'')+'订单状态(").append(DATA.get(iterData.getKey()).get("orderStatus")).append("):").append(statusSbf.toString()).append(",发送时间:").append(sendDate).append(",发送状态:成功").append("'+CHAR(10)) where fid ='").append(iterData.getKey()).append("'");
					updateLocalSql.append("update WxOrder set IsSend=0,sendDataText=CONCAT(ifnull(sendDataText,''),'订单状态(").append(DATA.get(iterData.getKey()).get("orderStatus")).append("):").append(statusSbf.toString()).append(",发送时间:").append(sendDate).append(",发送状态:成功").append("\r\n') where fid ='").append(iterData.getKey()).append("'");
					logger.info(reContent);
				} else {
					updateSql.append("update WxOrder set issend=0,sendDataText=(ISNULL(sendDataText,'')+'订单状态(").append(DATA.get(iterData.getKey()).get("orderStatus")).append("):").append(statusSbf.toString()).append(",发送时间:").append(sendDate).append(",发送状态:失败,详细内容:").append(reContent).append("'+CHAR(10)) where fid ='").append(iterData.getKey()).append("'");
					updateLocalSql.append("update WxOrder set IsSend=0,sendDataText=CONCAT(ifnull(sendDataText,''),'订单状态(").append(DATA.get(iterData.getKey()).get("orderStatus")).append("):").append(statusSbf.toString()).append(",发送时间:").append(sendDate).append(",发送状态:失败,详细内容:").append(reContent).append("\r\n') where fid ='").append(iterData.getKey()).append("'");
				}
				batExecSQL.add(updateSql.toString());
				batExecLocalSQL.add(updateLocalSql.toString());
			} catch (Exception e) {
				logger.info(e.toString());
			}
		}
		
		execSqlData.put(CommonConstants.DB_MYSQL, batExecLocalSQL);
		execSqlData.put(CommonConstants.DB_SQL_SERVER, batExecSQL);
		
		((WxOrderService) SpringUtil.getBean("wxOrderService")).execSQL(execSqlData);// 批量更新状态取消
		
		execSqlData.clear();
		batExecLocalSQL.clear();
		batExecSQL.clear();
	}

	private static Map<String,Map<String,String>> DATA = new HashMap<String,Map<String,String>>();
	private void getWxOrder() throws RuntimeException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			DATA.clear();
			conn = C3P0DBManager.getConnection();
			ps = conn.prepareStatement("SELECT TOP(100) t.wxOrderNO, t.OrderNO orderNO,ISNULL( t.wxOrderNO, t.OrderNO ) orderId, t.CustWXnum, 	t.Amount,  CASE  	t.receiveType   	WHEN 0 THEN  	'物流'   	WHEN 1 THEN  	'自提' ELSE '厂家配送'   	END receive,t.orderStatus orderStatus,t.fid   FROM  WxOrder t left join WxUserCust f on t.CustWXnum = f.openid where t.IsSend=1 and f.isWXFocus <>-1 ");
			rs = ps.executeQuery();
			while (rs.next()) {
				Map<String,String> sendData = new HashMap<String,String>();
				sendData.put("wxOrderNO", rs.getString("wxOrderNO"));
				sendData.put("orderNO", rs.getString("orderNO"));
				sendData.put("orderId", rs.getString("orderId"));
				sendData.put("Amount",	new DecimalFormat("0.00").format(rs.getDouble("Amount")));
				sendData.put("receive", rs.getString("receive"));
				sendData.put("orderStatus",rs.getString("orderStatus"));
				sendData.put("fid", rs.getString("fid"));
				sendData.put("CustWXnum", rs.getString("CustWXnum"));
				DATA.put(rs.getString("fid"), sendData);
			}
		} catch (SQLException e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, rs);
		}
	}
	
	private Map<String,List<Map<String,String>>> getWxOrderDetail(String fidSql) throws RuntimeException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String,List<Map<String,String>>> sendData = new HashMap<String,List<Map<String,String>>>();
		
		try {
			conn = C3P0DBManager.getConnection();
			ps = conn.prepareStatement("select t.InvName,t.Qty,t.NQty,t.Price,t.Amount,t.goodsQty,t.fid from WxOrderDetail t where t.fid in("+fidSql+")");
			rs = ps.executeQuery();
			while (rs.next()) {
				String fid = rs.getString("fid");
				List<Map<String,String>> orderDetail = sendData.get(fid);
				if(orderDetail==null){
					orderDetail = new ArrayList<Map<String,String>>();
				}
				Map<String,String> orderDetailData = new HashMap<String,String>(); 
				orderDetailData.put("InvName", rs.getString("InvName"));
				orderDetailData.put("Qty",	new DecimalFormat("0.00").format(rs.getDouble("Qty")));
				orderDetailData.put("NQty", new DecimalFormat("0.00").format(rs.getDouble("NQty")));
				orderDetailData.put("Price", new DecimalFormat("0.00").format(rs.getDouble("Price")));
				orderDetailData.put("Amount", new DecimalFormat("0.00").format(rs.getDouble("Amount")));
				orderDetailData.put("goodsQty", new DecimalFormat("0.00").format(rs.getDouble("goodsQty")));
				orderDetail.add(orderDetailData);
				sendData.put(fid, orderDetail);
			}
		} catch (SQLException e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, rs);
		}
		return sendData;
	}



}
