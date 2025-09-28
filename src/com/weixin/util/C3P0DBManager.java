package com.weixin.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.weixin.util.encrypt.CryptoUtil;

/**
 * MDBManager.java 功能：使用C3P0连接池获取数据库连接对象
 * 
 * @version 1.0 2013-03-07
 */
public class C3P0DBManager {

	private static final C3P0DBManager instance = new C3P0DBManager();
	private static ComboPooledDataSource cpds = new ComboPooledDataSource(true);
	protected final static Logger LOGGER = Logger.getLogger(C3P0DBManager.class);

	
	/**
	 * 此处可以不配置，采用默认也行
	 * 
	 * @throws Exception
	 */
	public void initPools() throws Exception {
		try {
			cpds.setDataSourceName("sqlserverds");
			cpds.setJdbcUrl(CryptoUtil.decode(PropertiesUtils.read("c3p0.jdbcUrl").trim()));
			cpds.setDriverClass(CryptoUtil.decode(PropertiesUtils.read("c3p0.driverClass").trim()));
			cpds.setUser(CryptoUtil.decode(PropertiesUtils.read("c3p0.user").trim()));
			cpds.setPassword(CryptoUtil.decode(PropertiesUtils.read("c3p0.password").trim()));
			cpds.setMaxPoolSize(Integer.valueOf(PropertiesUtils.read("c3p0.maxPoolSize").trim()));
			cpds.setMinPoolSize(Integer.valueOf(PropertiesUtils.read("c3p0.minPoolSize").trim()));
			cpds.setAcquireIncrement(Integer.valueOf(PropertiesUtils.read("c3p0.acquireIncrement").trim()));
			cpds.setInitialPoolSize(Integer.valueOf(PropertiesUtils.read("c3p0.initialPoolSize").trim()));
			cpds.setMaxIdleTime(Integer.valueOf(PropertiesUtils.read("c3p0.maxIdleTime").trim()));
			cpds.setPreferredTestQuery(PropertiesUtils.read("c3p0.preferredTestQuery").trim());
			cpds.setIdleConnectionTestPeriod(Integer.valueOf(PropertiesUtils.read("c3p0.idleConnectionTestPeriod").trim()));
			cpds.setTestConnectionOnCheckout(Boolean.valueOf(PropertiesUtils.read("c3p0.testConnectionOnCheckout").trim()));
			cpds.setTestConnectionOnCheckin(Boolean.valueOf(PropertiesUtils.read("c3p0.testConnectionOnCheckin").trim()));
		} catch (Exception e1) {
			LOGGER.error(e1);
			throw e1;
		}
	}

	private C3P0DBManager() {
	}

	public static C3P0DBManager getInstance() throws Exception {
		return instance;
	}

	public static Connection getConnection() throws SQLException {
		return cpds.getConnection();
	}

	public static void releaseConnection(Connection conn, Statement stmt, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception localException1) {
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception localException2) {
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
	}

	public static void releaseConnection(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception localException1) {
			}
		}
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (Exception localException2) {
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
			}

		}
	}

	public static void main(String[] args) throws Exception {
		C3P0DBManager.getInstance().initPools();
		System.out.println(C3P0DBManager.getConnection());
		cpds.close();
	}
}