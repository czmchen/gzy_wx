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

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.weixin.util.C3P0DBManager;
import com.weixin.util.SpringUtil;
import com.weixin.vo.RMWxUserCust;
import com.yq.service.UserService;
@DisallowConcurrentExecution
public class RMWxUserCustJob implements Job{
	private final static Logger logger = Logger.getLogger(RMWxUserCustJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			logger.debug(new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").format(new Date()) + " TaskRMWxUserCustThread任务进行中。。。");
			List<RMWxUserCust> lstRMWxUserCust = getWxUserCust();
			if (lstRMWxUserCust.size() > 0) {
				updateUser(lstRMWxUserCust);// 先更新微信端
				updateRMWxUserCustIsSys(lstRMWxUserCust);// 更新状态
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}


	private void updateUser(List<RMWxUserCust> lstRMWxUserCust) {
		UserService objUserService = (UserService) SpringUtil.getBean("userService");
		for (RMWxUserCust objRMWxUserCust : lstRMWxUserCust) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("custRemark", objRMWxUserCust.getCustRemark());
			map.put("custid", objRMWxUserCust.getCustid());
			map.put("custKHWXH", objRMWxUserCust.getCustKHWXH());
			map.put("custName", objRMWxUserCust.getCustName());
			map.put("oppen_id", objRMWxUserCust.getOpenid());
			map.put("custName", objRMWxUserCust.getCustName());
			map.put("crm_addr_name", objRMWxUserCust.getCrm_addr_name());
			map.put("crm_area", objRMWxUserCust.getCrm_area());
			map.put("crm_city", objRMWxUserCust.getCrm_city());
			map.put("crm_province", objRMWxUserCust.getCrm_province());
			map.put("crm_town", objRMWxUserCust.getCrm_town());
			map.put("KHLXR", objRMWxUserCust.getKHLXR());
			map.put("KHMC", objRMWxUserCust.getKHMC());
			map.put("cityPickerCN", objRMWxUserCust.getCityPickerCN());
			map.put("mphone", objRMWxUserCust.getMphone());
			map.put("custStatus", objRMWxUserCust.getCustStatus());
			map.put("salesman", objRMWxUserCust.getSalesman());
			objRMWxUserCust.setIsSys(String.valueOf(objUserService.upRMCustInfo(map)));
		}
	}

	private void updateRMWxUserCustIsSys(List<RMWxUserCust> lstRMWxUserCust) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			StringBuffer sql = new StringBuffer("update WxUserCust set issys = 0 where openid in(");
			for (int i = 0; i < lstRMWxUserCust.size(); i++) {
				RMWxUserCust objRMWxUserCust = lstRMWxUserCust.get(i);
				sql.append("'").append(objRMWxUserCust.getOpenid()).append("'");
				if (i != (lstRMWxUserCust.size() - 1)) {
					sql.append(",");
				}
			}
			sql.append(")");
			logger.debug(sql.toString());
			conn = C3P0DBManager.getConnection();
			ps = conn.prepareStatement(sql.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, null);
		}
	}

	private List<RMWxUserCust> getWxUserCust() throws RuntimeException {
		List<RMWxUserCust> lstRMWxUserCust = new ArrayList<RMWxUserCust>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = C3P0DBManager.getConnection();
			ps = conn.prepareStatement("select openid,mphone,CustRemark,Custid,CustKHWXH,CustName,crm_area,crm_addr_name,crm_province,crm_city,crm_town,KHMC,KHLXR,cityPickerCN,salesman,custStatus from WxUserCust where issys = 1");
			rs = ps.executeQuery();
			while (rs.next()) {
				RMWxUserCust objRMWxUserCust = new RMWxUserCust();
				objRMWxUserCust.setOpenid(rs.getString("openid"));
				objRMWxUserCust.setCustRemark(rs.getString("CustRemark"));
				objRMWxUserCust.setCustid(rs.getString("Custid"));
				objRMWxUserCust.setCustKHWXH(rs.getString("CustKHWXH"));
				objRMWxUserCust.setCustName(rs.getString("CustName"));
				objRMWxUserCust.setCrm_addr_name(rs.getString("crm_addr_name"));
				objRMWxUserCust.setCrm_area(rs.getString("crm_area"));
				objRMWxUserCust.setCrm_city(rs.getString("crm_city"));
				objRMWxUserCust.setCrm_province(rs.getString("crm_province"));
				objRMWxUserCust.setCrm_town(rs.getString("crm_town"));
				objRMWxUserCust.setCrm_town(rs.getString("crm_town"));
				objRMWxUserCust.setCrm_town(rs.getString("crm_town"));
				objRMWxUserCust.setKHLXR(rs.getString("KHLXR"));
				objRMWxUserCust.setKHMC(rs.getString("KHMC"));
				objRMWxUserCust.setCityPickerCN(rs.getString("cityPickerCN"));
				objRMWxUserCust.setMphone(rs.getString("mphone"));
				objRMWxUserCust.setSalesman(rs.getString("salesman"));
				objRMWxUserCust.setCustStatus(rs.getInt("custStatus"));
				lstRMWxUserCust.add(objRMWxUserCust);
			}
		} catch (SQLException e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, rs);
		}
		return lstRMWxUserCust;
	}


}
