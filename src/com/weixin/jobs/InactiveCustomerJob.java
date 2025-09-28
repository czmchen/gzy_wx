package com.weixin.jobs;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.weixin.util.C3P0DBManager;
import com.weixin.util.SpringUtil;
@DisallowConcurrentExecution
public class InactiveCustomerJob  implements Job{
	
	private final static Logger logger = Logger.getLogger(InactiveCustomerJob.class);
	
	private final int inactiveDays = 150;
	
	/***
	 * 设置不活跃的客户，规则：150天不下单的客户为不活跃，不活跃状态inactive修改为1
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		updateAllCustomer();
		updateInactiveCustomer();
		updateRMAllCustomer();
		updateRMInactiveCustomer();
	}

	private void updateAllCustomer() throws RuntimeException {
		Statement st = null;
		Connection conn = null;
		try {
			String sql = "update tb_user set inactive = 0";
			conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
			st = conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, st, null);
		}
	}
	
	private void updateInactiveCustomer() throws RuntimeException {
		Statement st = null;
		Connection conn = null;
		try {
			String sql = "update tb_user set inactive = 1 where not exists (select 1 from (select CustWXnum from WxOrder ft where ((TO_DAYS(now()) - TO_DAYS( ft.creatdate) <="+inactiveDays+") and orderStatus!=-10) GROUP BY CustWXnum) tg where tg.CustWXnum = tb_user.oppen_id)";
			conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
			st = conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, st, null);
		}
	}
	
	private void updateRMAllCustomer() throws RuntimeException {
		Statement st = null;
		Connection conn = null;
		try {
			String sql = "update WxUserCust set inactive = 0";
			conn = C3P0DBManager.getConnection();
			st = conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, st, null);
		}
	}
	
	private void updateRMInactiveCustomer() throws RuntimeException {
		Statement st = null;
		Connection conn = null;
		try {
			String sql = "update WxUserCust set inactive = 1 where not exists (select 1 from (select f.CustWXnum from WxOrder f where DateDiff(dd,f.creatdate,getdate())<="+inactiveDays+" and orderStatus!=-10) tg where tg.CustWXnum = openid)";
			conn = C3P0DBManager.getConnection();
			st = conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, st, null);
		}
	}

}
