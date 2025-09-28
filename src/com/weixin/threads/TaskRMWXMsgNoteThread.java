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

import com.weixin.entity.WxMsgNote;
import com.weixin.util.C3P0DBManager;
import com.weixin.util.DateUtils;
import com.weixin.util.StringUtil;
import com.weixin.util.WxUtil;
import com.weixin.vo.TemplateData;
import com.weixin.vo.TemplateMessage;
import com.weixin.vo.TemplateValue;

public class TaskRMWXMsgNoteThread implements Runnable {

	private final static Logger logger = Logger.getLogger(TaskRMWXMsgNoteThread.class);

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(5000);
				logger.debug(new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").format(new Date()) + " TaskRMWXMsgNoteThread任务进行中。。。");
				List<WxMsgNote> lstWxMsg = getNeed2SendMsg();
				for(WxMsgNote wxMsg : lstWxMsg){
					sendMsg(wxMsg);
					Thread.sleep(500);
				}
				if(updateSql.size()>0){
					updateResult();
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}

	private List<WxMsgNote> getNeed2SendMsg() throws RuntimeException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<WxMsgNote> lstWxMsg = new ArrayList<WxMsgNote>();

		try {
			conn = C3P0DBManager.getConnection();
			ps = conn.prepareStatement("select * from WxMsgNote t where t.sendstatus is null");
			rs = ps.executeQuery();
			while (rs.next()) {
				WxMsgNote wxMsg = new WxMsgNote();
				wxMsg.setId(rs.getLong("id"));
				wxMsg.setTemplid(rs.getString("templid"));
				wxMsg.setFirstdata(rs.getString("firstdata"));
				wxMsg.setKeyword1(rs.getString("keyword1"));
				wxMsg.setKeyword2(rs.getString("keyword2"));
				wxMsg.setKeyword3(rs.getString("keyword3"));
				wxMsg.setKeyword4(rs.getString("keyword4"));
				wxMsg.setKeyword5(rs.getString("keyword5"));
				wxMsg.setKeyword1color(rs.getString("keyword1color"));
				wxMsg.setKeyword2color(rs.getString("keyword2color"));
				wxMsg.setKeyword3color(rs.getString("keyword3color"));
				wxMsg.setKeyword4color(rs.getString("keyword4color"));
				wxMsg.setKeyword5color(rs.getString("keyword5color"));
				wxMsg.setRemarkcolor(rs.getString("remarkcolor"));
				wxMsg.setFirstdatacolor(rs.getString("firstdatacolor"));
				wxMsg.setRemark(rs.getString("remark"));
				wxMsg.setSendstatus(rs.getInt("sendstatus"));
				wxMsg.setSenddate(rs.getString("senddate"));
				wxMsg.setCreatetime(rs.getString("createtime"));
				wxMsg.setSend2openid(rs.getString("send2openid"));
				wxMsg.setSender(rs.getString("sender"));
				wxMsg.setUrl(rs.getString("url"));
				lstWxMsg.add(wxMsg);
			}
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, rs);
		}
		return lstWxMsg;
	}
	private List<String> updateSql = new ArrayList<String>();
	private void sendMsg(WxMsgNote wxMsg) {
		try {
			wxMsg.setCreatetime(DateUtils.nowDate(DateUtils.DATETIME_FORMATE));

			
			TemplateMessage templateMessage = new TemplateMessage();
			TemplateData data = new TemplateData();
			templateMessage.setTemplate_id(wxMsg.getTemplid());
			templateMessage.setTouser(wxMsg.getSend2openid());
			
			data.setFirst(new TemplateValue(wxMsg.getFirstdata(),(StringUtil.isRealEmpty(wxMsg.getFirstdatacolor())?"#000000":wxMsg.getFirstdatacolor())));
			data.setKeyword1(new TemplateValue(wxMsg.getKeyword1(),(StringUtil.isRealEmpty(wxMsg.getKeyword1color())?"#000000":wxMsg.getKeyword1color())));
			data.setKeyword2(new TemplateValue(wxMsg.getKeyword2(),(StringUtil.isRealEmpty(wxMsg.getKeyword2color())?"#000000":wxMsg.getKeyword2color())));
			data.setKeyword3(new TemplateValue(wxMsg.getKeyword3(),(StringUtil.isRealEmpty(wxMsg.getKeyword3color())?"#000000":wxMsg.getKeyword3color())));
			data.setKeyword4(new TemplateValue(wxMsg.getKeyword4(),(StringUtil.isRealEmpty(wxMsg.getKeyword4color())?"#000000":wxMsg.getKeyword4color())));
			data.setKeyword5(new TemplateValue(wxMsg.getKeyword5(),(StringUtil.isRealEmpty(wxMsg.getKeyword5color())?"#000000":wxMsg.getKeyword5color())));
			data.setRemark(new TemplateValue(wxMsg.getRemark(),(StringUtil.isRealEmpty(wxMsg.getRemarkcolor())?"#000000":wxMsg.getRemarkcolor())));
			templateMessage.setUrl(wxMsg.getUrl());
			templateMessage.setData(data);
			String reContent = WxUtil.sendNote(templateMessage);
			if (reContent.indexOf("\"errcode\":0,") != -1) {
				updateSql.add("update WxMsgNote set sendstatus=1,senddate=GETDATE() where id = "+wxMsg.getId());
				logger.info(reContent);
			} else {
				updateSql.add("update WxMsgNote set sendstatus=0,senddate=GETDATE(),sendErrorContent='"+reContent+"' where id = "+wxMsg.getId());
			}
		} catch (Exception e) {
			updateSql.add("update WxMsgNote set sendstatus=0,senddate=GETDATE(),sendErrorContent='"+e.getMessage() +"' where id = "+wxMsg.getId());
			logger.error(e);
		}
	}
	
	
	private void updateResult() throws RuntimeException {
		Connection conn = null;
		Statement st = null;

		try {
			conn = C3P0DBManager.getConnection();
			st = conn.createStatement();
			for(String sql : updateSql){
				st.addBatch(sql);
			}
			st.executeBatch();
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			updateSql.clear();
			C3P0DBManager.releaseConnection(conn, st, null);
		}
	}

}
