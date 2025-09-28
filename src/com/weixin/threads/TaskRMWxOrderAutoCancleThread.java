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

import com.weixin.util.C3P0DBManager;

public class TaskRMWxOrderAutoCancleThread implements Runnable {

	private final static Logger logger = Logger.getLogger(TaskRMWxOrderAutoCancleThread.class);

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(10000);
				logger.debug(new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").format(new Date()) + " TaskRMWxOrderAutoCancleThread任务进行中。。。");
				this.getNeed2CancleOrder();
				if (!SQL_DATA.isEmpty()&&SQL_DATA.size()>0) {
					batExecSql();// 批量更新状态取消
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}

	private void batExecSql() {
		Connection conn = null;
		Statement st = null;
		try {
			conn = C3P0DBManager.getConnection();
			st = conn.createStatement();
			for (String sql: SQL_DATA) {
				st.addBatch(sql);
			}
			st.executeBatch();
		}  catch (SQLException e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, st, null);
		}
	}

	private static List<String> SQL_DATA = new ArrayList<String>();
	private void getNeed2CancleOrder() throws RuntimeException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			SQL_DATA.clear();
			conn = C3P0DBManager.getConnection();
			ps = conn.prepareStatement("select ISNULL(wxOrderNO, OrderNO ) orderId,fid from WxOrder where orderStatus=11 and datediff(hour, creatdate,GETDATE()) >=48");
			rs = ps.executeQuery();
			while (rs.next()) {
				StringBuffer sql = new StringBuffer();
				sql.append("update WxOrder set orderStatus=-10  where (wxOrderNO = '").append(rs.getString("orderId")).append("' or OrderNO = '").append(rs.getString("orderId")).append("') and fid = '").append(rs.getString("fid")).append("'");
				SQL_DATA.add(sql.toString());
			}
		} catch (SQLException e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, rs);
		}
	}
	
}
