package com.weixin.jobs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.weixin.util.WxUtil;
import com.weixin.vo.TemplateMessage;
import com.weixin.vo.TemplateValue;
@DisallowConcurrentExecution
public class RMWxOrderSignJob implements Job{
	
	private final static Logger logger = Logger.getLogger(RMWxOrderSignJob.class);
	public static final String TEMPLATE_ID_ORDER_SIGN_KEY = "-PP9StZyjoGWRVV0DFZAFix3jgcX6FNf91VNnN_pjQg";//订单签收提醒

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			logger.debug(new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").format(new Date()) + " TaskRMWxOrderSignThread任务进行中。。。");
			this.getWxOrder();
			if (!DATA.isEmpty()&&DATA.size()>0) {
				sendOrderPayInfoNote();// 发送微信通知，通知状态改变
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	
	private static Map<String,Map<String,String>> DATA = new HashMap<String,Map<String,String>>();
	private void getWxOrder() throws RuntimeException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			DATA.clear();
			conn = C3P0DBManager.getConnection();
			ps = conn.prepareStatement("SELECT ISNULL( t.wxOrderNO, t.OrderNO ) orderId,t.CustWXnum,t.fid FROM  WxOrder t where t.signSendFlag=1 and t.signFlag=2");
			rs = ps.executeQuery();
			while (rs.next()) {
				Map<String,String> sendData = new HashMap<String,String>();
				sendData.put("orderId", rs.getString("orderId"));
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
	
	private void sendOrderPayInfoNote() throws Exception {
		Map<String,List<String>> execSqlData = new HashMap<String,List<String>>(); 
		List<String> batExecSQL = new ArrayList<String>();
		List<String> batExecLocalSQL = new ArrayList<String>();
		for(Entry<String, Map<String, String>> iterData : DATA.entrySet()){

			try {
				String sendDate = DateUtils.nowDate(DateUtils.DATETIME_FORMATE);
				StringBuffer updateSql = new StringBuffer();
				StringBuffer updateLocalSql = new StringBuffer();
				TemplateMessage templateMessage = new TemplateMessage();
				templateMessage.setTemplate_id(TEMPLATE_ID_ORDER_SIGN_KEY);
				templateMessage.setTouser(DATA.get(iterData.getKey()).get("CustWXnum"));
//				templateMessage.setUrl(StringUtil.getSetting().getLink()+"/page/orderList.html?go2Tab=sign");
				templateMessage.getData().setKeyword1(new TemplateValue("甘竹花生油已送达成功","#000000"));
				templateMessage.getData().setKeyword2(new TemplateValue(sendDate,"#000000"));
				String reContent = WxUtil.sendNote(templateMessage);
				if (reContent.indexOf("\"errcode\":0,") != -1) {
					updateSql.append("update WxOrder set signSendFlag = 2,signDatetime=GETDATE(),signSendMsg=(ISNULL(signSendMsg,'')+'甘竹花生油已送达成功").append(",发送时间:").append(sendDate).append(",发送状态:成功").append("'+CHAR(10)+'/r/n') where fid ='").append(iterData.getKey()).append("'");
					updateLocalSql.append("update WxOrder set signSendFlag = 2,signDatetime=now(),signSendMsg=CONCAT(ifnull(signSendMsg,''),'甘竹花生油已送达成功").append(",发送时间:").append(sendDate).append(",发送状态:成功").append("\r\n') where fid ='").append(iterData.getKey()).append("'");
					logger.info(reContent);
				} else {
					updateSql.append("update WxOrder set signSendFlag = -1,signSendMsg=(ISNULL(signSendMsg,'')+'甘竹花生油已送达成功").append(",发送时间:").append(sendDate).append(",发送状态:失败,详细内容:").append(reContent).append("'+CHAR(10)+'/r/n') where fid ='").append(iterData.getKey()).append("'");
					updateLocalSql.append("update WxOrder set signSendFlag = -1,signSendMsg=CONCAT(ifnull(signSendMsg,''),'甘竹花生油已送达成功").append(",发送时间:").append(sendDate).append(",发送状态:失败,详细内容:").append(reContent).append("\r\n') where fid ='").append(iterData.getKey()).append("'");
				}
				batExecSQL.add(updateSql.toString());
				batExecLocalSQL.add(updateLocalSql.toString());
				Thread.sleep(500);
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
	
}
