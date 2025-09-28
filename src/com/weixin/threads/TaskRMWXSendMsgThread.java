package com.weixin.threads;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.weixin.entity.WxMsg;
import com.weixin.service.WxMsgService;
import com.weixin.util.C3P0DBManager;
import com.weixin.util.DateUtils;
import com.weixin.util.SpringUtil;
import com.weixin.util.WXCustomerManagerUtil;

public class TaskRMWXSendMsgThread implements Runnable {

	private final static Logger logger = Logger.getLogger(TaskRMWXSendMsgThread.class);

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(5000);
				logger.debug(new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").format(new Date()) + " TaskWXMsgSysThread任务进行中。。。");
				List<WxMsg> lstWxMsg = getNeed2SendMsg();
				for(WxMsg wxMsg : lstWxMsg){
					sendMsg(wxMsg);
					Thread.sleep(500);
				}
				if(lstBatSql.size()>0){
					updateResult();
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}

	private List<WxMsg> getNeed2SendMsg() throws RuntimeException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<WxMsg> lstWxMsg = new ArrayList<WxMsg>();

		try {
			conn = C3P0DBManager.getConnection();
			ps = conn.prepareStatement("select tx.id,tx.messageType,tx.toUserName,tx.contents from WxPostGetInfo tx left join WxUserCust f on tx.toUserName = f.openid where tx.isMsgNew = 1 and  f.isWXFocus <>-1");
			rs = ps.executeQuery();
			while (rs.next()) {
				WxMsg wxMsg = new WxMsg();
				wxMsg.setId(rs.getInt("id"));
				wxMsg.setMessageType(rs.getString("messageType"));
				wxMsg.setToUserName(rs.getString("toUserName"));
				wxMsg.setContent(rs.getString("contents"));
				wxMsg.setMsgSendOrReceive(1);
				wxMsg.setIsMsgNew(1);
				lstWxMsg.add(wxMsg);
			}
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, rs);
		}
		return lstWxMsg;
	}
	private List<String> lstBatSql = new ArrayList<String>();
	private void sendMsg(WxMsg wxMsg) {
		try {
			wxMsg.setCreateTime(DateUtils.nowDate(DateUtils.DATETIME_FORMATE));

			JSONObject text = new JSONObject();
			text.put("content", wxMsg.getContent());
			JSONObject json = new JSONObject();
			json.put("touser", wxMsg.getToUserName());
			json.put("text", text);
			json.put("msgtype", "text");

			String result = WXCustomerManagerUtil.customMsgSend(json);
			WxMsgService wxMsgService = (WxMsgService)SpringUtil.getBean("wxMsgService");
			wxMsg.setSendResult(result);
			wxMsgService.insert(wxMsg);
			lstBatSql.add("update WxPostGetInfo set sendType=1,sendDateTime=GETDATE(),isMsgNew=0 where id = "+wxMsg.getId());
		} catch (Exception e) {
			lstBatSql.add("update WxPostGetInfo set sendDateTime=GETDATE(),isMsgNew=0 where id = "+wxMsg.getId());
			logger.error(e);
		}
	}
	
	
	private void updateResult() throws RuntimeException {
		Connection conn = null;
		Statement st = null;

		try {
			conn = C3P0DBManager.getConnection();
			st = conn.createStatement();
			for(String sql : lstBatSql){
				st.addBatch(sql);
			}
			st.executeBatch();
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			lstBatSql.clear();
			C3P0DBManager.releaseConnection(conn, st, null);
		}
	}

}
