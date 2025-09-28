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
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.weixin.util.C3P0DBManager;
import com.weixin.util.SpringUtil;
import com.weixin.util.StringUtil;
import com.weixin.vo.WxOrder;
import com.weixin.vo.WxOrderDetail;
@DisallowConcurrentExecution
public class SyncWxOrderJob implements Job {

	private final static Logger logger = Logger.getLogger(SyncWxOrderJob.class);
	private List<WxOrder> sourceWxOrder = new ArrayList<WxOrder>();
	private Map<String, WxOrder> syncOrderData = new HashMap<String, WxOrder>();
	private Map<String, WxOrder> updateOrderData = new HashMap<String, WxOrder>();
	private Map<String, WxOrderDetail> syncDetailData = new HashMap<String, WxOrderDetail>();
	private int batMaxAmount = 100;//一批次执行最大数量
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			ExecutorService pool = Executors.newWorkStealingPool();
	        Future<String> future = pool.submit(() -> {//long nowDateTime = System.currentTimeMillis();
	        	sqlServerSync2MyLocal();//SQLServer数据库同步到本地数据	long nowDateTime2 = System.currentTimeMillis();//logger.error("SyncWxOrderJob结束执行。。。。。。。。。。。。。。。。。。。。。。。。。"+DateUtils.nowDate(DateUtils.DATETIME_FORMATE)+"||一共耗费："+(nowDateTime2-nowDateTime)+"毫秒");
	            return "aaa";
	        });
	        try {
	            future.get(2, TimeUnit.MINUTES);//执行同步，超过指定时间则代表同步失败，杀死进程
	        } catch (TimeoutException e) {
	        	logger.error("线程超时："+e);
	        	logger.error("执行线程的状态：" + future.isDone());
	        	logger.error("执行线程杀死：" +  future.cancel(true)+" 再看执行线程的状态：" + future.isDone());
	        }
	        future.get();
	        pool.shutdown();
	        pool.shutdownNow();
	        pool = null;
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private void sqlServerSync2MyLocal(){
		syncOrderDetail();
		syncOrder();
	}
	
	private void syncOrder() {
		try {
			getNeed2Sync();// 获取到需要同步的数据    	long startDateTime = System.currentTimeMillis();   	logger.error("SyncWxOrderJob开始执行同步order主表，需要处理："+syncOrderData.size()+"条数据。。。。。。。。。。。。。。。。。。。。。。。。"+DateUtils.nowDate(DateUtils.DATETIME_FORMATE));
			if(sourceWxOrder.size()>0) {
				List<WxOrder> lstAllLocalData = execCheckLocalNeed2Update(checkTargetIsExist());// 获取所有需要更新的数据
				if(syncOrderData.size()>0) {//先执行insert数据
					execLocalNeed2Insert();//执行插入数据操作
				}
				if(lstAllLocalData.size()>0) {
					execBatUpdateLocal(lstAllLocalData);//执行更新,如果执行update的数据，会在updateData删除
				}
			}//long endDateTime = System.currentTimeMillis(); 	logger.error("SyncWxOrderJob结束执行同步order主表，需要处理："+syncOrderData.size()+"条数据,一共耗时："+(endDateTime-startDateTime)+"毫秒。。。。。。。。。。。。。。。。。。。。。。。。"+DateUtils.nowDate(DateUtils.DATETIME_FORMATE));
		}catch (Exception e) {
			logger.error(e);
		} finally {
			if(sourceWxOrder.size()>0) {
				sourceWxOrder.clear();
			}if(syncOrderData.size()>0) {
				syncOrderData.clear();
			}if(updateOrderData.size()>0) {
				updateOrderData.clear();
			}
		}
	}

	private void syncOrderDetail() {
		try {//long startDetailDateTime = System.currentTimeMillis();
			getNeed2SyncDetail();//检索所有需要插入的明细数据，明细数据是不用再更新的，只需执行插入即可   	logger.error("SyncWxOrderJob开始执行同步orderDetail表，需要处理："+syncDetailData.size()+"条数据。。。。。。。。。。。。。。。。。。。。。。。。"+DateUtils.nowDate(DateUtils.DATETIME_FORMATE));
			if(syncDetailData.size()>0) {
				execLocalNeed2InsertWxOrderDetail();
			}//	long endDetailDateTime = System.currentTimeMillis();  logger.error("SyncWxOrderJob结束执行同步orderDetail表，需要处理："+syncDetailData.size()+"条数据,一共耗时："+(endDetailDateTime-startDetailDateTime)+"毫秒。。。。。。。。。。。。。。。。。。。。。。。。"+DateUtils.nowDate(DateUtils.DATETIME_FORMATE));
		}catch (Exception e) {
			logger.error(e);
		} finally {
			if(syncDetailData.size()>0) {
				syncDetailData.clear();
			}
		}
	}

	private void getNeed2Sync() throws RuntimeException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = C3P0DBManager.getConnection();
			ps = conn.prepareStatement("select * from WxOrder t where t.isSync = 1");
			rs = ps.executeQuery();
			while (rs.next()) {
				WxOrder wxOrder = new WxOrder();
				wxOrder.setAddrId(rs.getInt("addrId"));
				wxOrder.setAmount(rs.getFloat("amount"));
				wxOrder.setCreatdate(rs.getString("creatdate"));
				wxOrder.setCreater(rs.getString("creater"));
				wxOrder.setCustID(rs.getString("custID"));
				wxOrder.setCustMC(rs.getString("custMC"));
				wxOrder.setCustWXnum(rs.getString("custWXnum"));
				wxOrder.setExpressCost(rs.getFloat("expressCost"));
				wxOrder.setFid(rs.getString("fid"));
				wxOrder.setInvID(rs.getString("invID"));
				wxOrder.setInvName(rs.getString("invName"));
				wxOrder.setIsPD(rs.getInt("isPD"));
				wxOrder.setIsRead(rs.getInt("isRead"));
				wxOrder.setIsSend(rs.getInt("isSend"));
				wxOrder.setIsSys(rs.getInt("isSys"));
				wxOrder.setIsVerified(rs.getInt("isVerified"));
				wxOrder.setIsYLZF(rs.getInt("isYLZF"));
				wxOrder.setIszf(rs.getInt("iszf"));
				wxOrder.setLatitude(rs.getString("latitude"));
				wxOrder.setLongitude(rs.getString("longitude"));
				wxOrder.setNote(rs.getString("note"));
				wxOrder.setnQty(rs.getFloat("nQty"));
				wxOrder.setOrdAddress(rs.getString("ordAddress"));
				wxOrder.setOrderNO(rs.getString("orderNO"));
				wxOrder.setOrderStatus(rs.getInt("orderStatus"));
				wxOrder.setOrderWLDH(rs.getString("orderWLDH"));
				wxOrder.setOrdphone(rs.getString("ordphone"));
				wxOrder.setOrdRecipient(rs.getString("ordRecipient"));
				wxOrder.setReceiveCost(rs.getFloat("receiveCost"));
				wxOrder.setReceiveType(rs.getInt("receiveType"));
				wxOrder.setSendDataText(rs.getString("sendDataText"));
				wxOrder.setSignDatetime(rs.getString("signDatetime"));
				wxOrder.setSignFlag(rs.getInt("signFlag"));
				wxOrder.setSignSendFlag(rs.getInt("signSendFlag"));
				wxOrder.setSignSendMsg(rs.getString("signSendMsg"));
				wxOrder.setSysreaddate(rs.getString("sysreaddate"));
				wxOrder.setVerifyDate(rs.getString("verifyDate"));
				wxOrder.setVerifyer(rs.getString("verifyer"));
				wxOrder.setVerifyNote(rs.getString("verifyNote"));
				wxOrder.setWxOrderNO(rs.getString("wxOrderNO"));
				wxOrder.setWxsenddate(rs.getString("wxsenddate"));
				wxOrder.setYJFHRQ(rs.getString("yJFHRQ"));
				wxOrder.setZfamount(rs.getFloat("zfamount"));
				wxOrder.setZfdate(rs.getString("zfdate"));
				wxOrder.setZfTransactionId(rs.getString("zfTransactionId"));
				wxOrder.setPreference1(rs.getFloat("preference1"));
				wxOrder.setOrgAmount(rs.getFloat("orgAmount"));
				wxOrder.setPrice(rs.getFloat("price"));
				wxOrder.setQty(rs.getFloat("qty"));
				wxOrder.setReceive(rs.getString("receive"));
				sourceWxOrder.add(wxOrder);
			}

			for (WxOrder wxOrder : sourceWxOrder) {
				syncOrderData.put("fid ='"+wxOrder.getFid()+"'", wxOrder);
			}
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, rs);
		}
	}
	
	private void getNeed2SyncDetail() throws RuntimeException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = C3P0DBManager.getConnection();
			ps = conn.prepareStatement("select * from WxOrderDetail t where t.isSync = 1");
			rs = ps.executeQuery();
			while (rs.next()) {
				WxOrderDetail wxOrderDetail = new WxOrderDetail();
				wxOrderDetail.setFid(rs.getString("fid"));
				wxOrderDetail.setGoodsQty(rs.getInt("goodsQty"));
				wxOrderDetail.setInvID(rs.getString("invID"));
				wxOrderDetail.setInvName(rs.getString("invName"));
				wxOrderDetail.setNote(rs.getString("note"));
				wxOrderDetail.setNQty(rs.getFloat("nQty"));
				wxOrderDetail.setOrgAmount(rs.getFloat("orgAmount"));
				wxOrderDetail.setOrgPrice(rs.getFloat("orgPrice"));
				wxOrderDetail.setPreference1(rs.getFloat("preference1"));
				wxOrderDetail.setPrice(rs.getFloat("price"));
				wxOrderDetail.setQty(rs.getFloat("qty"));
				wxOrderDetail.setAmount(rs.getFloat("amount"));
				syncDetailData.put("(fid ='"+wxOrderDetail.getFid()+"' and invID ='"+wxOrderDetail.getInvID()+"')", wxOrderDetail);
			}
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, rs);
		}
	}

	private int roundMax = 100;
	private List<String> checkTargetIsExist() {
		List<String> existsCheckSQL = new ArrayList<String>();
		int round = ((sourceWxOrder.size() / roundMax) + ((sourceWxOrder.size() % roundMax)!=0?1:0));
		for (int i = 0; i < round; i++) {
			StringBuffer checkSQL = new StringBuffer("select * from WxOrder where ");
			int start = i*roundMax;
			int end = (i+1)*roundMax;
			if(i==(round-1)) {
				end = sourceWxOrder.size();
			}
			for (int j = start; j < end; j++) {
				try {
					WxOrder wxOrder = sourceWxOrder.get(j);
					checkSQL.append("fid='").append(wxOrder.getFid()).append("' or ");
				} catch (Exception e) {
					break;
				}
			}
			checkSQL.replace(checkSQL.length() - 4, checkSQL.length(), "");
			existsCheckSQL.add(checkSQL.toString());
		}
		return existsCheckSQL;
	}

	private List<WxOrder> execCheckLocalNeed2Update(List<String> existsCheckSQL) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<WxOrder> lstData = new ArrayList<WxOrder>();

		for (String sql : existsCheckSQL) {
			try {
				conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					WxOrder wxOrder = new WxOrder();
					wxOrder.setOrderNO(rs.getString("orderNO"));
					wxOrder.setWxOrderNO(rs.getString("wxOrderNO"));
					wxOrder.setCustWXnum(rs.getString("custWXnum"));
					wxOrder.setOrderStatus(rs.getInt("orderStatus"));
					wxOrder.setIsSend(rs.getInt("IsSend"));
					wxOrder.setSignDatetime(rs.getString("signDatetime"));
					wxOrder.setSignFlag(rs.getInt("signFlag"));
					wxOrder.setFid(rs.getString("fid"));
					wxOrder.setSignSendMsg(rs.getString("signSendMsg"));
					lstData.add(wxOrder);
					updateOrderData.put("fid ='"+wxOrder.getFid()+"'", syncOrderData.get("fid ='"+wxOrder.getFid()+"'"));
					syncOrderData.remove("fid ='"+wxOrder.getFid()+"'");//排除掉需要更新的数据，剩下就为更新的数据
				}
			} catch (SQLException e) {
				logger.error(e);
			} finally {
				C3P0DBManager.releaseConnection(conn, ps, rs);
			}
		}
		return lstData;
	}
	
	
	private void execBatUpdateLocal(List<WxOrder> lstData) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> updateRMIds = new ArrayList<String>();
		
		try {
			conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
			for (WxOrder wxOrder : lstData) {
				WxOrder updateWxOrder = updateOrderData.get("fid ='"+wxOrder.getFid()+"'");
				if (updateWxOrder == null) {
					continue;
				}
				StringBuffer sbf = new StringBuffer("update WxOrder set ");
				sbf.append("orderStatus=?,OrderNO=?,IsSend=?");
				if(wxOrder.getSignFlag()!=updateWxOrder.getSignFlag()) {
					sbf.append(",signFlag=?,signSendMsg=?");
					if(!StringUtil.isRealEmpty(updateWxOrder.getSignDatetime())) {
						sbf.append(",signDatetime=?");
					}
				}
				sbf.append(" where fid = '").append(wxOrder.getFid()).append("'");
				ps = conn.prepareStatement(sbf.toString());
				ps.setInt(1, updateWxOrder.getOrderStatus());
				ps.setString(2, updateWxOrder.getOrderNO());
				ps.setInt(3, updateWxOrder.getIsSend());
				if(wxOrder.getSignFlag()!=updateWxOrder.getSignFlag()) {
					ps.setInt(4, updateWxOrder.getSignFlag());
					ps.setString(5, updateWxOrder.getSignSendMsg()==null?"":updateWxOrder.getSignSendMsg());
					if(!StringUtil.isRealEmpty(updateWxOrder.getSignDatetime())) {
						ps.setString(6, updateWxOrder.getSignDatetime());
					}
				}
				ps.executeUpdate();
				updateRMIds.add("fid ='"+wxOrder.getFid()+"'");
			}
			batResetSyncSourceData(updateRMIds);//执行批量更新源数据库，告知成功插入/更新数据已同步
		} catch (Exception e) {
			logger.error(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, rs);
			if(updateRMIds.size()>0) {
				updateRMIds.clear();
				updateRMIds = null;
			}
		}
	}

	private void execLocalNeed2Insert() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> updateRMIds = new ArrayList<String>();
		List<String> updateRMIds_ = new ArrayList<String>(batMaxAmount);
		
		try {
			int runIndex = 1;
			conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
			Set<Map.Entry<String, WxOrder>> ent = syncOrderData.entrySet();
			StringBuffer sbf = new StringBuffer("insert into WxOrder(");
			sbf.append("wxOrderNO,OrderNO,CustID,CustMC,CustWXnum,creater,creatdate,sysreaddate,wxsenddate,IsRead,IsSend,invID,invName,nQty,Qty,Amount,IsVerified,ordAddress,VerifyDate,Verifyer,Price,Note,receive,receiveType,fid,Iszf,zfdate,zfamount,YJFHRQ,VerifyNote,isSys,orderStatus,OrderWLDH,receiveCost,zfTransactionId,sendDataText,orgAmount,preference1,IsPD,IsYLZF,ordphone,ordRecipient,addrId,expressCost,signFlag,signDatetime,signSendMsg,signSendFlag,latitude,longitude");
			sbf.append(") select ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?");
			sbf.append(" where not exists (select 1 from WxOrder where fid=?)");
			ps = conn.prepareStatement(sbf.toString());
			
			for (Map.Entry<String, WxOrder> entry : ent) {
				WxOrder objWxOrder = syncOrderData.get(entry.getKey());
				ps.setString(1, objWxOrder.getWxOrderNO());
				ps.setString(2, objWxOrder.getOrderNO());
				ps.setString(3, objWxOrder.getCustID());
				ps.setString(4, objWxOrder.getCustMC());
				ps.setString(5, objWxOrder.getCustWXnum());
				ps.setString(6, objWxOrder.getCreater());
				ps.setString(7, objWxOrder.getCreatdate());
				ps.setString(8, objWxOrder.getSysreaddate());
				ps.setString(9, objWxOrder.getWxsenddate());
				ps.setInt(10, objWxOrder.getIsRead());
				ps.setInt(11, objWxOrder.getIsSend());
				ps.setString(12, objWxOrder.getInvID());
				ps.setString(13, objWxOrder.getInvName());
				ps.setFloat(14, objWxOrder.getnQty());
				ps.setFloat(15, objWxOrder.getQty());
				ps.setFloat(16, objWxOrder.getAmount());
				ps.setInt(17, objWxOrder.getIsVerified());
				ps.setString(18, objWxOrder.getOrdAddress());
				ps.setString(19, objWxOrder.getVerifyDate());
				ps.setString(20, objWxOrder.getVerifyer());
				ps.setFloat(21, objWxOrder.getPrice());
				ps.setString(22, objWxOrder.getNote());
				ps.setString(23, objWxOrder.getReceive());
				ps.setInt(24, objWxOrder.getReceiveType());
				ps.setString(25, objWxOrder.getFid());
				ps.setInt(26, objWxOrder.getIszf());
				ps.setString(27, objWxOrder.getZfdate());
				ps.setFloat(28, objWxOrder.getZfamount());
				ps.setString(29, objWxOrder.getYJFHRQ());
				ps.setString(30, objWxOrder.getVerifyNote());
				ps.setInt(31, objWxOrder.getIsSys());
				ps.setInt(32, objWxOrder.getOrderStatus());
				ps.setString(33, objWxOrder.getOrderWLDH());
				ps.setFloat(34, objWxOrder.getReceiveCost());
				ps.setString(35, objWxOrder.getZfTransactionId());
				ps.setString(36, objWxOrder.getSendDataText());
				ps.setFloat(37, objWxOrder.getOrgAmount());
				ps.setFloat(38, objWxOrder.getPreference1());
				ps.setInt(39, objWxOrder.getIsPD());
				ps.setInt(40, objWxOrder.getIsYLZF());
				ps.setString(41, objWxOrder.getOrdphone());
				ps.setString(42, objWxOrder.getOrdRecipient());
				ps.setInt(43, objWxOrder.getAddrId());
				ps.setFloat(44, objWxOrder.getExpressCost());
				ps.setInt(45, objWxOrder.getSignFlag());
				ps.setString(46, objWxOrder.getSignDatetime());
				ps.setString(47, objWxOrder.getSignSendMsg());
				ps.setInt(48, objWxOrder.getSignSendFlag());
				ps.setString(49, objWxOrder.getLatitude());
				ps.setString(50, objWxOrder.getLongitude());
				
				ps.setString(51,objWxOrder.getFid());
				
				ps.addBatch();
				updateRMIds_.add("fid ='"+objWxOrder.getFid()+"'");//有防止重复插入，可重复多次操作，没关系，一次不成功就多次吧
				
				if(runIndex%batMaxAmount==0) {
					updateRMIds.addAll(updateRMIds_);
					updateRMIds_ = new ArrayList<String>(batMaxAmount);
					
					ps.executeBatch();//执行批量更新主表
					ps.clearBatch();
				}
				runIndex++;
			}
			if(ps!=null) {
				updateRMIds.addAll(updateRMIds_);
				ps.executeBatch();//执行批量更新主表
				ps.clearBatch();
			}
			batResetSyncSourceData(updateRMIds);//执行批量更新源数据库，告知成功插入/更新数据已同步
		}catch (Exception e) {
			logger.error(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, rs);
			if(updateRMIds_.size()>0) {
				updateRMIds_.clear();
				updateRMIds_ = null;
			}
		}
	}
	
	
	private void execLocalNeed2InsertWxOrderDetail() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			int runIndex = 1;
			conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
			Set<Map.Entry<String, WxOrderDetail>> ent = syncDetailData.entrySet();
			StringBuffer sbf = new StringBuffer("insert into WxOrderDetail (fid,InvID,InvName,Qty,NQty,Price,Amount,Note,goodsQty,orgPrice,orgAmount,preference1) ");
			sbf.append("  select ?,?,?,?,?,?,?,?,?,?,?,?");
			sbf.append(" where not exists (select 1 from WxOrderDetail where fid=? and InvID=?)");
			ps = conn.prepareStatement(sbf.toString());
			List<String> updateRMDetailIds = new ArrayList<String>(1000);
			List<String> updateRMDetailIds_ = new ArrayList<String>(batMaxAmount);
			
			for (Map.Entry<String, WxOrderDetail> entry : ent) {
				WxOrderDetail objWxOrderDetail = syncDetailData.get(entry.getKey());
				ps.setString(1, objWxOrderDetail.getFid());
				ps.setString(2, objWxOrderDetail.getInvID());
				ps.setString(3, objWxOrderDetail.getInvName());
				ps.setFloat(4, objWxOrderDetail.getQty());
				ps.setFloat(5, objWxOrderDetail.getNQty());
				ps.setFloat(6, objWxOrderDetail.getPrice());
				ps.setFloat(7, objWxOrderDetail.getAmount());
				ps.setString(8, objWxOrderDetail.getNote());
				ps.setInt(9, objWxOrderDetail.getGoodsQty());
				ps.setFloat(10, objWxOrderDetail.getOrgPrice());
				ps.setFloat(11, objWxOrderDetail.getOrgAmount());
				ps.setFloat(12, objWxOrderDetail.getPreference1());
				ps.setString(13, objWxOrderDetail.getFid());
				ps.setString(14, objWxOrderDetail.getInvID());
				
				ps.addBatch();
				updateRMDetailIds_.add(entry.getKey());//有防止重复插入，可重复多次操作，没关系，一次不成功就多次吧
				
				if(runIndex%batMaxAmount==0) {
					ps.executeBatch();//执行批量更新明细表
					ps.clearBatch();
					
					updateRMDetailIds.addAll(updateRMDetailIds_);//防止更新出错，执行批处理后再执行
					updateRMDetailIds_ = new ArrayList<String>(batMaxAmount);
				}
				runIndex++;
			}
			if(ps!=null) {
				ps.executeBatch();//执行批量更新明细表
				ps.clearBatch();
				updateRMDetailIds.addAll(updateRMDetailIds_);
			}
			batResetSyncDetailSourceData(updateRMDetailIds);
		}catch (Exception e) {
			logger.error(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, rs);
		}
	}
	
	
	private void batResetSyncSourceData(List<String> updateRMIds) throws Exception {
		Connection conn = null;
		Statement st = null;
		try {
			conn = C3P0DBManager.getConnection();
			st = conn.createStatement();
			StringBuffer updateSQL = new StringBuffer("update WxOrder set issync = 0 where ");
			for (int i = 0; i < updateRMIds.size(); i++) {
				updateSQL.append(updateRMIds.get(i)).append(" or ");
				if (i>0&&(i % batMaxAmount)==0) {
					updateSQL.replace(updateSQL.length() - 4, updateSQL.length(), "");
					st.executeUpdate(updateSQL.toString());
					updateSQL = new StringBuffer("update WxOrder set issync = 0 where ");
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
	
	
	private void batResetSyncDetailSourceData(List<String> updateRMDetailIds) throws Exception {
		Connection conn = null;
		Statement st = null;
		try {
			conn = C3P0DBManager.getConnection();
			st = conn.createStatement();
			StringBuffer updateSQL = new StringBuffer("update WxOrderDetail set isSync = 0 where ");
			for (int i = 0; i < updateRMDetailIds.size(); i++) {
				updateSQL.append(updateRMDetailIds.get(i)).append(" or ");
				if (i>0&&(i % batMaxAmount)==0) {
					updateSQL.replace(updateSQL.length() - 4, updateSQL.length(), "");
					st.executeUpdate(updateSQL.toString());
					updateSQL = new StringBuffer("update WxOrderDetail set isSync = 0 where ");
				}
			}
			if((updateRMDetailIds.size()) % batMaxAmount != 0) {
				updateSQL.replace(updateSQL.length() - 4, updateSQL.length(), "");
				st.executeUpdate(updateSQL.toString());
				st.clearBatch();
			}
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			updateRMDetailIds.clear();
			C3P0DBManager.releaseConnection(conn, st, null);
		}
	}

}
