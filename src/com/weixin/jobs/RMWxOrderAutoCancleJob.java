package com.weixin.jobs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.weixin.service.WxOrderService;
import com.weixin.util.C3P0DBManager;
import com.weixin.util.SpringUtil;

@DisallowConcurrentExecution
public class RMWxOrderAutoCancleJob implements Job {

	private final static Logger logger = Logger.getLogger(RMWxOrderAutoCancleJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			logger.debug(new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").format(new Date())+ " TaskRMWxOrderAutoCancleThread任务进行中。。。");
			this.getNeed2CancleOrder();
			if (!SQL_DATA.isEmpty() && SQL_DATA.size() > 0) {
				((WxOrderService) SpringUtil.getBean("wxOrderService")).execCancleOrder(SQL_DATA);// 批量更新状态取消
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (SQL_DATA != null && SQL_DATA.size() > 0) {
				SQL_DATA.clear();
			}
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
			ps = conn.prepareStatement("select ISNULL(wxOrderNO, OrderNO ) orderId,fid from WxOrder where orderStatus=11 and datediff(hour, creatdate,GETDATE()) >=48 and OrderNO is NULL");
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
