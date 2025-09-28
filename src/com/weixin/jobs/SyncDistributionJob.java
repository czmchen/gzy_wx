package com.weixin.jobs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.weixin.util.C3P0DBManager;
import com.weixin.util.DateUtils;
import com.weixin.util.SpringUtil;
import com.weixin.util.StringUtil;
import com.yq.entity.Distribution;
@DisallowConcurrentExecution
public class SyncDistributionJob implements Job {

	private final static Logger logger = Logger.getLogger(SyncDistributionJob.class);
	private List<Distribution> sourceDistribution = new ArrayList<Distribution>();
	private Map<String, Distribution> updateData = new HashMap<String, Distribution>();
	private List<String> updateRMIds = new ArrayList<String>();
	private int batMaxAmount = 100;//一批次执行最大数量
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		sqlServerSync2MyLocal();//SQLServer数据库同步到本地数据
		localSync2RMSqlServer();//local mysql数据库更新数据同步到RM sqlserver数据库
	}

	private void sqlServerSync2MyLocal(){
		try {
			getNeed2Sync();// 获取到需要同步的数据
			if(sourceDistribution.size()>0) {
				List<Distribution> lstAllLocalData = execCheckLocalNeed2Update(checkTargetIsExist());// 获取所有需要更新的数据
				if(lstAllLocalData.size()>0) {
					execBatUpdateLocal(lstAllLocalData);//执行更新,如果执行update的数据，会在updateData删除
				}
				if(updateData.size()>0) {//updateData里剩下的就为insert的数据了
					execLocalNeed2Insert();//执行插入数据操作
				}
				if(updateRMIds.size()>0) {
					batResetSyncSourceData();//执行批量更新源数据库，告知成功插入/更新数据已同步
				}
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if(sourceDistribution.size()>0) {
				sourceDistribution.clear();
			}if(updateData.size()>0) {
				updateData.clear();
			}
		}
	}
	
	/***
	 * 本地同步到远程sqlserver数据库
	 */
	private void localSync2RMSqlServer(){
		try {
			getNeed2SyncRM();// 获取到需要同步的数据
			if(sourceDistribution.size()>0) {
				execBatUpdateRMSQLServer();//执行批量更新操作
			}if(updateRMIds.size()>0) {
				batResetSyncLocalSourceData();//执行批量更新源数据库，告知成功更新数据已同步
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if(sourceDistribution.size()>0) {
				sourceDistribution.clear();
			}if(updateData.size()>0) {
				updateData.clear();
			}
		}
	}
	
	private void execBatUpdateRMSQLServer() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = C3P0DBManager.getConnection();
			for (Distribution objDistribution : sourceDistribution) {
				Distribution updateDistribution = updateData.get("(driverOpenId='"+objDistribution.getDriverOpenId()+"' and orderNum = '"+objDistribution.getOrderNum()+"' and recDate = '"+objDistribution.getRecDate()+"')");
				if (updateDistribution == null) {
					continue;
				}
				StringBuffer sbf = new StringBuffer("update tb_distribution set ");
				sbf.append("latitude = ?,");
				sbf.append("longitude = ?,");
				sbf.append("recDate = ?,");
				sbf.append("reciever = ?,");
				sbf.append("recStatus = ?,");
				sbf.append("signStatus = ?,");
				sbf.append("storeImg = ?,");
				sbf.append("actionLogs = CONCAT(actionLogs,'").append(DateUtils.nowDate(DateUtils.DATETIME_FORMATE)).append(": 完成同步更新数据操作,内容：").append(sbf.toString().replaceAll("'", "")).append("\r\n')");
				sbf.append(" where openid = ?").append(" and orderNum = ?");
				
				ps = conn.prepareStatement(sbf.toString());
				ps.setString(1, updateDistribution.getLatitude());
				ps.setString(2, updateDistribution.getLongitude());
				ps.setString(3, updateDistribution.getRecDate());
				ps.setString(4, updateDistribution.getReciever());
				ps.setString(5, updateDistribution.getRecStatus());
				ps.setInt(6, updateDistribution.getSignStatus());
				ps.setString(7, updateDistribution.getStoreImg());
				ps.setString(8, updateDistribution.getOpenid());
				ps.setString(9, updateDistribution.getOrderNum());
				ps.executeUpdate();
				updateRMIds.add("(driverOpenId='"+objDistribution.getDriverOpenId()+"' and orderNum = '"+objDistribution.getOrderNum()+"' and recDate = '"+objDistribution.getRecDate()+"')");
				updateData.remove("(driverOpenId='"+objDistribution.getDriverOpenId()+"' and orderNum = '"+objDistribution.getOrderNum()+"' and recDate = '"+objDistribution.getRecDate()+"')");
			}
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, rs);
		}
	}
	
	private void getNeed2SyncRM() throws RuntimeException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
			ps = conn.prepareStatement("select * from ganzhu_common_db.tb_distribution t where t.isRMSysnc = 1");
			rs = ps.executeQuery();
			while (rs.next()) {
				Distribution distribution = new Distribution();
				distribution.setLatitude(rs.getString("latitude"));
				distribution.setLongitude(rs.getString("longitude"));
				distribution.setOpenid(rs.getString("openid"));
				distribution.setOrderNum(rs.getString("orderNum"));
				distribution.setRecDate(rs.getString("recDate"));
				distribution.setReciever(rs.getString("reciever"));
				distribution.setRecStatus(rs.getString("recStatus"));
				distribution.setSignStatus(rs.getInt("signStatus"));
				distribution.setStoreImg(rs.getString("storeImg"));
				sourceDistribution.add(distribution);
			}

			for (Distribution objDistribution : sourceDistribution) {
				updateData.put("(driverOpenId='"+objDistribution.getDriverOpenId()+"' and orderNum = '"+objDistribution.getOrderNum()+"' and recDate = '"+objDistribution.getRecDate()+"')", objDistribution);
			}
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, rs);
		}
	}


	private void getNeed2Sync() throws RuntimeException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = C3P0DBManager.getConnection();
			ps = conn.prepareStatement("select * from tb_distribution t where t.isSync = 1");
			rs = ps.executeQuery();
			while (rs.next()) {
				Distribution distribution = new Distribution();
				distribution.setId(rs.getString("id"));
				distribution.setAddress(rs.getString("address"));
				distribution.setCreateDateTime(rs.getString("createDateTime"));
				distribution.setCustId(rs.getString("custId"));
				distribution.setDriverOpenId(rs.getString("driverOpenId"));
				distribution.setGaodeNavigation(rs.getString("gaodeNavigation"));
				distribution.setGoodsDetail(rs.getString("goodsDetail"));
				distribution.setLatitude(rs.getString("latitude"));
				distribution.setLongitude(rs.getString("longitude"));
				distribution.setMobileNum(rs.getString("mobileNum"));
				distribution.setOpenid(rs.getString("openid"));
				distribution.setOrderNum(rs.getString("orderNum"));
				distribution.setRecDate(rs.getString("recDate"));
				distribution.setReciever(rs.getString("reciever"));
				distribution.setRecStatus(rs.getString("recStatus"));
				distribution.setSignStatus(rs.getInt("signStatus"));
				distribution.setStoreImg(rs.getString("storeImg"));
				distribution.setAreaCode(rs.getString("areaCode"));
				sourceDistribution.add(distribution);
			}

			for (Distribution objDistribution : sourceDistribution) {
				updateData.put("(driverOpenId='"+objDistribution.getDriverOpenId()+"' and orderNum = '"+objDistribution.getOrderNum()+"' and recDate = '"+objDistribution.getRecDate()+"')", objDistribution);
			}
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, rs);
		}
	}

	private List<String> checkTargetIsExist() {
		List<String> existsCheckSQL = new ArrayList<String>();
		int round = (sourceDistribution.size() / 1000) + (sourceDistribution.size() % 1000)!=0?1:0;
		for (int i = 0; i < round; i++) {
			StringBuffer checkSQL = new StringBuffer("select * from ganzhu_common_db.tb_distribution where (openid,orderNum) in (");
			int start = i*1000;
			int end = (i+1)*1000;
			if(i==(round-1)) {
				end = sourceDistribution.size();
			}
			for (int j = start; j < end; j++) {
				try {
					Distribution objDistribution = sourceDistribution.get(j);
					checkSQL.append("('").append(objDistribution.getOpenid()).append("','").append(objDistribution.getOrderNum()).append("'),");
				} catch (Exception e) {
					break;
				}
			}
			checkSQL.replace(checkSQL.length() - 1, checkSQL.length(), "");
			checkSQL.append(")");
			existsCheckSQL.add(checkSQL.toString());
		}
		return existsCheckSQL;
	}

	private List<Distribution> execCheckLocalNeed2Update(List<String> existsCheckSQL) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Distribution> lstData = new ArrayList<Distribution>();

		for (String sql : existsCheckSQL) {
			try {
				conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					Distribution distribution = new Distribution();
					distribution.setId(rs.getString("id"));
					distribution.setAddress(rs.getString("address"));
					distribution.setCreateDateTime(rs.getString("createDateTime"));
					distribution.setCustId(rs.getString("custId"));
					distribution.setDriverOpenId(rs.getString("driverOpenId"));
					distribution.setGaodeNavigation(rs.getString("gaodeNavigation"));
					distribution.setGoodsDetail(rs.getString("goodsDetail"));
					distribution.setLatitude(rs.getString("latitude"));
					distribution.setLongitude(rs.getString("longitude"));
					distribution.setMobileNum(rs.getString("mobileNum"));
					distribution.setOpenid(rs.getString("openid"));
					distribution.setOrderNum(rs.getString("orderNum"));
					distribution.setRecDate(rs.getString("recDate"));
					distribution.setReciever(rs.getString("reciever"));
					distribution.setRecStatus(rs.getString("recStatus"));
					distribution.setSignStatus(rs.getInt("signStatus"));
					distribution.setStoreImg(rs.getString("storeImg"));
					distribution.setAreaCode(rs.getString("areaCode"));
					lstData.add(distribution);
				}
			} catch (SQLException e) {
				logger.error(e);
			} finally {
				C3P0DBManager.releaseConnection(conn, ps, rs);
			}
		}
		return lstData;
	}
	
	
	private void execBatUpdateLocal(List<Distribution> lstData) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
			for (Distribution objDistribution : lstData) {
				Distribution updateDistribution = updateData.get("(driverOpenId='"+objDistribution.getDriverOpenId()+"' and orderNum = '"+objDistribution.getOrderNum()+"' and recDate = '"+objDistribution.getRecDate()+"')");
				if (updateDistribution == null) {
					continue;
				}
				StringBuffer sbf = new StringBuffer("update ganzhu_common_db.tb_distribution set ");
				
				Map<Integer,String> parData = new HashMap<Integer,String>();
				pagPar(updateDistribution.getAddress(), objDistribution.getAddress(), "address", sbf,parData);
				pagPar(updateDistribution.getCreateDateTime(), objDistribution.getCreateDateTime(), "createDateTime", sbf,parData);
				pagPar(updateDistribution.getCustId(), objDistribution.getCustId(), "custId", sbf,parData);
				pagPar(updateDistribution.getDriverOpenId(), objDistribution.getDriverOpenId(), "driverOpenId", sbf,parData);
				pagPar(updateDistribution.getGaodeNavigation(), objDistribution.getGaodeNavigation(), "gaodeNavigation", sbf,parData);
				pagPar(updateDistribution.getGoodsDetail(), objDistribution.getGoodsDetail(), "goodsDetail", sbf,parData);
				pagPar(updateDistribution.getLatitude(), objDistribution.getLatitude(), "latitude", sbf,parData);
				pagPar(updateDistribution.getLongitude(), objDistribution.getLongitude(), "longitude", sbf,parData);
				pagPar(updateDistribution.getMobileNum(), objDistribution.getMobileNum(), "mobileNum", sbf,parData);
				pagPar(updateDistribution.getRecDate(), objDistribution.getRecDate(), "recDate", sbf,parData);
				pagPar(updateDistribution.getReciever(), objDistribution.getReciever(), "reciever", sbf,parData);
				pagPar(updateDistribution.getRecStatus(), objDistribution.getRecStatus(), "recStatus", sbf,parData);
				pagPar(updateDistribution.getAreaCode(), objDistribution.getAreaCode(), "areaCode", sbf,parData);
				pagPar(updateDistribution.getStoreImg(), objDistribution.getStoreImg(), "storeImg", sbf,parData);
				if (objDistribution.getSignStatus() != updateDistribution.getSignStatus()) {
					sbf.append("signStatus = '").append(updateDistribution.getSignStatus()).append("',");
				}
				sbf.append("actionLogs = CONCAT(ifnull(actionLogs,''),'").append(DateUtils.nowDate(DateUtils.DATETIME_FORMATE)).append(": 完成同步更新数据操作,内容：").append(sbf.toString().replaceAll("'", "")).append("\r\n')");
				sbf.append(" where driverOpenId = '").append(objDistribution.getDriverOpenId()).append("' and orderNum = '").append(objDistribution.getOrderNum()).append("'");
				sbf.append(" and recDate = '").append(objDistribution.getRecDate()).append("'");
				
				ps = conn.prepareStatement(sbf.toString());
				Set<Entry<Integer, String>> parSetData = parData.entrySet();
				for(Entry<Integer, String> entr : parSetData) {
					ps.setString(entr.getKey(), entr.getValue());
				}
				ps.executeUpdate();
				updateRMIds.add("(driverOpenId='"+objDistribution.getDriverOpenId()+"' and orderNum = '"+objDistribution.getOrderNum()+"' and recDate = '"+objDistribution.getRecDate()+"')");
				updateData.remove("(driverOpenId='"+objDistribution.getDriverOpenId()+"' and orderNum = '"+objDistribution.getOrderNum()+"' and recDate = '"+objDistribution.getRecDate()+"')");
			}
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, rs);
		}
	}
	
	private void pagPar(String str1,String str2,String str3,StringBuffer sbf,Map<Integer,String> parData) {
		if(StringUtil.isRealEmpty(str1)) {
			sbf.append(str3).append(" = ?,");
			parData.put(parData.size()+1, str1);
		}else if(!str1.equals(str2)) {
			sbf.append(str3).append(" = ?,");
			parData.put(parData.size()+1, str1);
		}
	}

	private void execLocalNeed2Insert() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			int runIndex = 1;
			conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
			Set<Map.Entry<String, Distribution>> ent = updateData.entrySet();
			StringBuffer sbf = new StringBuffer("insert ganzhu_common_db.tb_distribution(");
			sbf.append("address,createDateTime,custId,driverOpenId,gaodeNavigation,goodsDetail,latitude,longitude,mobileNum,openid,orderNum,recDate,reciever,recStatus,signStatus,actionLogs,areaCode,storeImg");
			sbf.append(") select ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?");
			sbf.append(" where not exists (select 1 from ganzhu_common_db.tb_distribution where driverOpenId = ?").append(" and orderNum = ? and recDate = ?").append(")");
			ps = conn.prepareStatement(sbf.toString());
			List<String> updateRMIds_ = new ArrayList<String>(batMaxAmount);
			for (Map.Entry<String, Distribution> entry : ent) {
				Distribution objDistribution = updateData.get(entry.getKey());
				
				ps.setString(1, objDistribution.getAddress());
				ps.setString(2, objDistribution.getCreateDateTime());
				ps.setString(3, objDistribution.getCustId());
				ps.setString(4, objDistribution.getDriverOpenId());
				ps.setString(5, objDistribution.getGaodeNavigation());
				ps.setString(6, objDistribution.getGoodsDetail());
				ps.setString(7, objDistribution.getLatitude());
				ps.setString(8, objDistribution.getLongitude());
				ps.setString(9, objDistribution.getMobileNum());
				ps.setString(10, objDistribution.getOpenid());
				ps.setString(11, objDistribution.getOrderNum());
				ps.setString(12, objDistribution.getRecDate());
				ps.setString(13, objDistribution.getReciever());
				ps.setString(14, objDistribution.getRecStatus());
				ps.setInt(15, objDistribution.getSignStatus());
				ps.setString(16, DateUtils.nowDate(DateUtils.DATETIME_FORMATE)+": 完成同步插入数据操作\r\n'");
				ps.setString(17, objDistribution.getAreaCode());
				ps.setString(18, objDistribution.getStoreImg());
				ps.setString(19, objDistribution.getDriverOpenId());
				ps.setString(20, objDistribution.getOrderNum());
				ps.setString(21, objDistribution.getRecDate());
				
				ps.addBatch();
				updateRMIds_.add("(driverOpenId='"+objDistribution.getDriverOpenId()+"' and orderNum = '"+objDistribution.getOrderNum()+"' and recDate = '"+objDistribution.getRecDate()+"')");//有防止重复插入，可重复多次操作，没关系，一次不成功就多次吧
				
				if(runIndex%batMaxAmount==0) {
					ps.executeBatch();
					ps.clearBatch();
					updateRMIds.addAll(updateRMIds_);
					updateRMIds_ = new ArrayList<String>(batMaxAmount);
				}
			}
			if(ps!=null) {
				ps.executeBatch();
				ps.clearBatch();
				updateRMIds.addAll(updateRMIds_);
				updateRMIds_ = null;
			}
		}catch (SQLException e) {
			logger.error(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, rs);
		}
	}

	private void batResetSyncSourceData() throws Exception {
		Connection conn = null;
		Statement st = null;
		try {
			conn = C3P0DBManager.getConnection();
			st = conn.createStatement();
			StringBuffer updateSQL = new StringBuffer("update tb_distribution set issync = 0 where ");
			for (int i = 0; i < updateRMIds.size(); i++) {
				updateSQL.append(updateRMIds.get(i)).append(" or ");
				if (i>0&&(i % batMaxAmount)==0) {
					updateSQL.replace(updateSQL.length() - 4, updateSQL.length(), "");
					st.executeUpdate(updateSQL.toString());
					updateSQL = new StringBuffer("update tb_distribution set issync = 0 where ");
				}
			}
			if((updateRMIds.size()) % batMaxAmount != 0) {
				updateSQL.replace(updateSQL.length() - 4, updateSQL.length(), "");
				st.executeUpdate(updateSQL.toString());
				st.clearBatch();
			}
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			updateRMIds.clear();
			C3P0DBManager.releaseConnection(conn, st, null);
		}
	}
	
	private void batResetSyncLocalSourceData() throws Exception {
		Connection conn = null;
		Statement st = null;
		try {
			conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
			st = conn.createStatement();
			StringBuffer updateSQL = new StringBuffer("update ganzhu_common_db.tb_distribution set isRMSysnc = 0 where ");
			for (int i = 0; i < updateRMIds.size(); i++) {
				updateSQL.append(updateRMIds.get(i)).append(" or ");
				if (i>0&&(i % batMaxAmount)==0) {
					updateSQL.replace(updateSQL.length() - 4, updateSQL.length(), "");
					st.executeUpdate(updateSQL.toString());
					updateSQL = new StringBuffer("update tb_distribution set isRMSysnc = 0 where ");
				}
			}
			if((updateRMIds.size()) % batMaxAmount != 0) {
				updateSQL.replace(updateSQL.length() - 4, updateSQL.length(), "");
				st.executeUpdate(updateSQL.toString());
				st.clearBatch();
			}
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			updateRMIds.clear();
			C3P0DBManager.releaseConnection(conn, st, null);
		}
	}

}
