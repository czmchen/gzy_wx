package com.weixin.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.weixin.constants.CommonConstants;
import com.weixin.util.C3P0DBManager;
import com.weixin.util.SpringUtil;

@Service
public class WxOrderService {
	private final static Logger logger = Logger.getLogger(WxOrderService.class);
	
	
	public void execSQL(Map<String,List<String>> execSqlData) throws Exception {
		this.batExecSql(C3P0DBManager.getConnection(), execSqlData.get(CommonConstants.DB_SQL_SERVER));//执行远程的批处理
		this.batExecSql(((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection(), execSqlData.get(CommonConstants.DB_MYSQL));//执行本地的表批处理
	}
	
	public void execCancleOrder(List<String> SQL_DATA) throws Exception {
		this.batExecSql(C3P0DBManager.getConnection(), SQL_DATA);//执行远程的批处理
		this.batExecSql(((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection(), SQL_DATA);//执行本地的表批处理
	}

	private void batExecSql(Connection conn,List<String> SQL_DATA) throws Exception {
		Statement st = null;
		try {
			st = conn.createStatement();
			for (int i = 0; i < SQL_DATA.size(); i++) {
				st.addBatch(SQL_DATA.get(i));
				if ((i % 1000) == 0) {
					st.executeBatch();
					st.clearBatch();
				}
			}
			if ((SQL_DATA.size() % 1000) != 0) {
				st.executeBatch();
				st.clearBatch();
			}
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, st, null);
		}
	}
}
