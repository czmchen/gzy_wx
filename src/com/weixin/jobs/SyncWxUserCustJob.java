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
import com.weixin.vo.WxUserCust;
@DisallowConcurrentExecution
public class SyncWxUserCustJob implements Job {

	private final static Logger logger = Logger.getLogger(SyncWxUserCustJob.class);
	private List<WxUserCust> sourceWxUserCust = new ArrayList<WxUserCust>();
	private Map<String, WxUserCust> syncWxUserCustData = new HashMap<String, WxUserCust>();
	private Map<String, WxUserCust> updateWxUserCustData = new HashMap<String, WxUserCust>();
	private int batMaxAmount = 100;//一批次执行最大数量
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			ExecutorService pool = Executors.newWorkStealingPool();
	        Future<String> future = pool.submit(() -> {//long nowDateTime = System.currentTimeMillis();
	        	sqlServerSync2MyLocal();//SQLServer数据库同步到本地数据	long nowDateTime2 = System.currentTimeMillis();//logger.error("SyncwxUserCustJob结束执行。。。。。。。。。。。。。。。。。。。。。。。。。"+DateUtils.nowDate(DateUtils.DATETIME_FORMATE)+"||一共耗费："+(nowDateTime2-nowDateTime)+"毫秒");
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
		syncOrder();
	}
	
	private void syncOrder() {
		try {
			getNeed2Sync();// 获取到需要同步的数据    	long startDateTime = System.currentTimeMillis();   	logger.error("SyncwxUserCustJob开始执行同步order主表，需要处理："+syncOrderData.size()+"条数据。。。。。。。。。。。。。。。。。。。。。。。。"+DateUtils.nowDate(DateUtils.DATETIME_FORMATE));
			if(sourceWxUserCust.size()>0) {
				List<WxUserCust> lstAllLocalData = execCheckLocalNeed2Update(checkTargetIsExist());// 获取所有需要更新的数据
				if(syncWxUserCustData.size()>0) {//先执行insert数据
					execLocalNeed2Insert();//执行插入数据操作
				}
				if(lstAllLocalData.size()>0) {
					execBatUpdateLocal(lstAllLocalData);//执行更新,如果执行update的数据，会在updateData删除
				}
			}//long endDateTime = System.currentTimeMillis(); 	logger.error("SyncwxUserCustJob结束执行同步order主表，需要处理："+syncOrderData.size()+"条数据,一共耗时："+(endDateTime-startDateTime)+"毫秒。。。。。。。。。。。。。。。。。。。。。。。。"+DateUtils.nowDate(DateUtils.DATETIME_FORMATE));
		}catch (Exception e) {
			logger.error(e);
		} finally {
			if(sourceWxUserCust.size()>0) {
				sourceWxUserCust.clear();
			}if(syncWxUserCustData.size()>0) {
				syncWxUserCustData.clear();
			}if(updateWxUserCustData.size()>0) {
				updateWxUserCustData.clear();
			}
		}
	}


	private void getNeed2Sync() throws RuntimeException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = C3P0DBManager.getConnection();
			ps = conn.prepareStatement("select * from WxUserCust t where t.isSync = 1");
			rs = ps.executeQuery();
			while (rs.next()) {
				WxUserCust wxUserCust = new WxUserCust();
				wxUserCust.setOpenid(rs.getString("openid"));
				wxUserCust.setNickname(rs.getString("nickname"));
				wxUserCust.setRemark(rs.getString("remark"));
				wxUserCust.setCity(rs.getString("city"));
				wxUserCust.setProvince(rs.getString("province"));
				wxUserCust.setCountry(rs.getString("country"));
				wxUserCust.setHeadimgurl(rs.getString("headimgurl"));
				wxUserCust.setSubscribe_time(rs.getString("subscribe_time"));
				wxUserCust.setUnionid(rs.getString("unionid"));
				wxUserCust.setGroupid(rs.getString("groupid"));
				wxUserCust.setTagid_list(rs.getString("tagid_list"));
				wxUserCust.setSubscribe_scene(rs.getString("subscribe_scene"));
				wxUserCust.setQr_scene(rs.getString("qr_scene"));
				wxUserCust.setQr_scene_str(rs.getString("qr_scene_str"));
				wxUserCust.setCustid(rs.getString("custid"));
				wxUserCust.setCustKHWXH(rs.getString("custKHWXH"));
				wxUserCust.setCustName(rs.getString("custName"));
				wxUserCust.setCreater(rs.getString("creater"));
				wxUserCust.setCreatedate(rs.getString("createdate"));
				wxUserCust.setModer(rs.getString("moder"));
				wxUserCust.setModdate(rs.getString("moddate"));
				wxUserCust.setMphone(rs.getString("mphone"));
				wxUserCust.setCustRemark(rs.getString("custRemark"));
				wxUserCust.setIsMsgNew(rs.getString("isMsgNew"));
				wxUserCust.setCrm_area(rs.getString("crm_area"));
				wxUserCust.setCrm_addr_name(rs.getString("crm_addr_name"));
				wxUserCust.setCrm_province(rs.getString("crm_province"));
				wxUserCust.setCrm_city(rs.getString("crm_city"));
				wxUserCust.setKHMC(rs.getString("kHMC"));
				wxUserCust.setKHLXR(rs.getString("kHLXR"));
				wxUserCust.setCrm_town(rs.getString("crm_town"));
				wxUserCust.setCityPickerCN(rs.getString("cityPickerCN"));
				wxUserCust.setIsWXFocus(rs.getString("isWXFocus"));
				wxUserCust.setSex(rs.getString("sex"));
				wxUserCust.setLatitude(rs.getString("latitude"));
				wxUserCust.setLongitude(rs.getString("longitude"));
				wxUserCust.setNewstatus(rs.getString("newstatus"));
				wxUserCust.setStoreImg(rs.getString("storeImg"));
				wxUserCust.setReLatitude(rs.getString("reLatitude"));
				wxUserCust.setReLongitude(rs.getString("reLongitude"));
				wxUserCust.setSalesman(rs.getString("salesman"));
				wxUserCust.setCustStatus(rs.getString("custStatus"));
				wxUserCust.setSalesPriceTimes(rs.getString("salesPriceTimes"));
				wxUserCust.setInactive(rs.getString("inactive"));
				wxUserCust.setLastUpdateTime(rs.getString("lastUpdateTime"));
				wxUserCust.setGaodeId(rs.getString("gaodeId"));
				sourceWxUserCust.add(wxUserCust);
			}

			for (WxUserCust wxUserCust : sourceWxUserCust) {
				syncWxUserCustData.put("openid ='"+wxUserCust.getOpenid()+"'", wxUserCust);
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
		int round = ((sourceWxUserCust.size() / roundMax) + ((sourceWxUserCust.size() % roundMax)!=0?1:0));
		for (int i = 0; i < round; i++) {
			StringBuffer checkSQL = new StringBuffer("select * from WxUserCust where ");
			int start = i*roundMax;
			int end = (i+1)*roundMax;
			if(i==(round-1)) {
				end = sourceWxUserCust.size();
			}
			for (int j = start; j < end; j++) {
				try {
					WxUserCust wxUserCust = sourceWxUserCust.get(j);
					checkSQL.append("openid='").append(wxUserCust.getOpenid()).append("' or ");
				} catch (Exception e) {
					break;
				}
			}
			checkSQL.replace(checkSQL.length() - 4, checkSQL.length(), "");
			existsCheckSQL.add(checkSQL.toString());
		}
		return existsCheckSQL;
	}

	private List<WxUserCust> execCheckLocalNeed2Update(List<String> existsCheckSQL) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<WxUserCust> lstData = new ArrayList<WxUserCust>();

		for (String sql : existsCheckSQL) {
			try {
				conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					WxUserCust wxUserCust = new WxUserCust();
					wxUserCust.setOpenid(rs.getString("openid"));
					wxUserCust.setNickname(rs.getString("nickname"));
					wxUserCust.setRemark(rs.getString("remark"));
					wxUserCust.setCity(rs.getString("city"));
					wxUserCust.setProvince(rs.getString("province"));
					wxUserCust.setCountry(rs.getString("country"));
					wxUserCust.setHeadimgurl(rs.getString("headimgurl"));
					wxUserCust.setSubscribe_time(rs.getString("subscribe_time"));
					wxUserCust.setUnionid(rs.getString("unionid"));
					wxUserCust.setGroupid(rs.getString("groupid"));
					wxUserCust.setTagid_list(rs.getString("tagid_list"));
					wxUserCust.setSubscribe_scene(rs.getString("subscribe_scene"));
					wxUserCust.setQr_scene(rs.getString("qr_scene"));
					wxUserCust.setQr_scene_str(rs.getString("qr_scene_str"));
					wxUserCust.setCustid(rs.getString("custid"));
					wxUserCust.setCustKHWXH(rs.getString("custKHWXH"));
					wxUserCust.setCustName(rs.getString("custName"));
					wxUserCust.setCreater(rs.getString("creater"));
					wxUserCust.setCreatedate(rs.getString("createdate"));
					wxUserCust.setModer(rs.getString("moder"));
					wxUserCust.setModdate(rs.getString("moddate"));
					wxUserCust.setMphone(rs.getString("mphone"));
					wxUserCust.setCustRemark(rs.getString("custRemark"));
					wxUserCust.setIsMsgNew(rs.getString("isMsgNew"));
					wxUserCust.setCrm_area(rs.getString("crm_area"));
					wxUserCust.setCrm_addr_name(rs.getString("crm_addr_name"));
					wxUserCust.setCrm_province(rs.getString("crm_province"));
					wxUserCust.setCrm_city(rs.getString("crm_city"));
					wxUserCust.setKHMC(rs.getString("kHMC"));
					wxUserCust.setKHLXR(rs.getString("kHLXR"));
					wxUserCust.setCrm_town(rs.getString("crm_town"));
					wxUserCust.setCityPickerCN(rs.getString("cityPickerCN"));
					wxUserCust.setIsWXFocus(rs.getString("isWXFocus"));
					wxUserCust.setSex(rs.getString("sex"));
					wxUserCust.setLatitude(rs.getString("latitude"));
					wxUserCust.setLongitude(rs.getString("longitude"));
					wxUserCust.setNewstatus(rs.getString("newstatus"));
					wxUserCust.setStoreImg(rs.getString("storeImg"));
					wxUserCust.setReLatitude(rs.getString("reLatitude"));
					wxUserCust.setReLongitude(rs.getString("reLongitude"));
					wxUserCust.setSalesman(rs.getString("salesman"));
					wxUserCust.setCustStatus(rs.getString("custStatus"));
					wxUserCust.setSalesPriceTimes(rs.getString("salesPriceTimes"));
					wxUserCust.setInactive(rs.getString("inactive"));
					wxUserCust.setLastUpdateTime(rs.getString("lastUpdateTime"));
					wxUserCust.setGaodeId(rs.getString("gaodeId"));
					lstData.add(wxUserCust);
					updateWxUserCustData.put("openid ='"+wxUserCust.getOpenid()+"'", syncWxUserCustData.get("openid ='"+wxUserCust.getOpenid()+"'"));
					syncWxUserCustData.remove("openid ='"+wxUserCust.getOpenid()+"'");//排除掉需要更新的数据，剩下就为更新的数据
				}
			} catch (SQLException e) {
				logger.error(e);
			} finally {
				C3P0DBManager.releaseConnection(conn, ps, rs);
			}
		}
		return lstData;
	}
	
	
	private void execBatUpdateLocal(List<WxUserCust> lstData) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> updateRMIds = new ArrayList<String>();
		
		try {
			conn = ((com.zaxxer.hikari.HikariDataSource) SpringUtil.getBean("dataSource")).getConnection();
			for (WxUserCust wxUserCust : lstData) {
				WxUserCust updateWxUserCust = updateWxUserCustData.get("openid ='"+wxUserCust.getOpenid()+"'");
				if (updateWxUserCust == null) {
					continue;
				}
				StringBuffer sbf = new StringBuffer("update WxUserCust set ");
				if(wxUserCust.getNickname()!=null&&!wxUserCust.getNickname().equals(updateWxUserCust.getNickname())) {
					sbf.append("Nickname=?,");
				}if(wxUserCust.getRemark()!=null&&!wxUserCust.getRemark().equals(updateWxUserCust.getRemark())) {
					sbf.append("Remark=?,");
				}if(wxUserCust.getCity()!=null&&!wxUserCust.getCity().equals(updateWxUserCust.getCity())) {
					sbf.append("city=?,");
				}if(wxUserCust.getProvince()!=null&&!wxUserCust.getProvince().equals(updateWxUserCust.getProvince())) {
					sbf.append("Province=?,");
				}if(wxUserCust.getCountry()!=null&&!wxUserCust.getCountry().equals(updateWxUserCust.getCountry())) {
					sbf.append("Country=?,");
				}if(wxUserCust.getHeadimgurl()!=null&&!wxUserCust.getHeadimgurl().equals(updateWxUserCust.getHeadimgurl())) {
					sbf.append("Headimgurl=?,");
				}if(wxUserCust.getSubscribe_time()!=null&&!wxUserCust.getSubscribe_time().equals(updateWxUserCust.getSubscribe_time())) {
					sbf.append("Subscribe_time=?,");
				}if(wxUserCust.getUnionid()!=null&&!wxUserCust.getUnionid().equals(updateWxUserCust.getUnionid())) {
					sbf.append("Unionid=?,");
				}if(wxUserCust.getGroupid()!=null&&!wxUserCust.getGroupid().equals(updateWxUserCust.getGroupid())) {
					sbf.append("Groupid=?,");
				}if(wxUserCust.getTagid_list()!=null&&!wxUserCust.getTagid_list().equals(updateWxUserCust.getTagid_list())) {
					sbf.append("Tagid_list=?,");
				}if(wxUserCust.getSubscribe_scene()!=null&&!wxUserCust.getSubscribe_scene().equals(updateWxUserCust.getSubscribe_scene())) {
					sbf.append("Subscribe_scene=?,");
				}if(wxUserCust.getQr_scene()!=null&&!wxUserCust.getQr_scene().equals(updateWxUserCust.getQr_scene())) {
					sbf.append("Qr_scene=?,");
				}if(wxUserCust.getQr_scene_str()!=null&&!wxUserCust.getQr_scene_str().equals(updateWxUserCust.getQr_scene_str())) {
					sbf.append("Qr_scene_str=?,");
				}if(wxUserCust.getCustid()!=null&&!wxUserCust.getCustid().equals(updateWxUserCust.getCustid())) {
					sbf.append("Custid=?,");
				}if(wxUserCust.getCustKHWXH()!=null&&!wxUserCust.getCustKHWXH().equals(updateWxUserCust.getCustKHWXH())) {
					sbf.append("CustKHWXH=?,");
				}if(wxUserCust.getCustName()!=null&&!wxUserCust.getCustName().equals(updateWxUserCust.getCustName())) {
					sbf.append("CustName=?,");
				}if(wxUserCust.getCreater()!=null&&!wxUserCust.getCreater().equals(updateWxUserCust.getCreater())) {
					sbf.append("Creater=?,");
				}if(wxUserCust.getCreatedate()!=null&&!wxUserCust.getCreatedate().equals(updateWxUserCust.getCreatedate())) {
					sbf.append("Createdate=?,");
				}if(wxUserCust.getModer()!=null&&!wxUserCust.getModer().equals(updateWxUserCust.getModer())) {
					sbf.append("Moder=?,");
				}if(wxUserCust.getModdate()!=null&&!wxUserCust.getModdate().equals(updateWxUserCust.getModdate())) {
					sbf.append("Moddate=?,");
				}if(wxUserCust.getMphone()!=null&&!wxUserCust.getMphone().equals(updateWxUserCust.getMphone())) {
					sbf.append("Mphone=?,");
				}if(wxUserCust.getCustRemark()!=null&&!wxUserCust.getCustRemark().equals(updateWxUserCust.getCustRemark())) {
					sbf.append("CustRemark=?,");
				}if(wxUserCust.getIsMsgNew()!=null&&!wxUserCust.getIsMsgNew().equals(updateWxUserCust.getIsMsgNew())) {
					sbf.append("IsMsgNew=?,");
				}if(wxUserCust.getCrm_area()!=null&&!wxUserCust.getCrm_area().equals(updateWxUserCust.getCrm_area())) {
					sbf.append("Crm_area=?,");
				}if(wxUserCust.getCrm_addr_name()!=null&&!wxUserCust.getCrm_addr_name().equals(updateWxUserCust.getCrm_addr_name())) {
					sbf.append("Crm_addr_name=?,");
				}if(wxUserCust.getCrm_province()!=null&&!wxUserCust.getCrm_province().equals(updateWxUserCust.getCrm_province())) {
					sbf.append("Crm_province=?,");
				}if(wxUserCust.getCrm_city()!=null&&!wxUserCust.getCrm_city().equals(updateWxUserCust.getCrm_city())) {
					sbf.append("Crm_city=?,");
				}if(wxUserCust.getKHMC()!=null&&!wxUserCust.getKHMC().equals(updateWxUserCust.getKHMC())) {
					sbf.append("KHMC=?,");
				}if(wxUserCust.getKHLXR()!=null&&!wxUserCust.getKHLXR().equals(updateWxUserCust.getKHLXR())) {
					sbf.append("KHLXR=?,");
				}if(wxUserCust.getCrm_town()!=null&&!wxUserCust.getCrm_town().equals(updateWxUserCust.getCrm_town())) {
					sbf.append("Crm_town=?,");
				}if(wxUserCust.getCityPickerCN()!=null&&!wxUserCust.getCityPickerCN().equals(updateWxUserCust.getCityPickerCN())) {
					sbf.append("CityPickerCN=?,");
				}if(wxUserCust.getIsWXFocus()!=null&&!wxUserCust.getIsWXFocus().equals(updateWxUserCust.getIsWXFocus())) {
					sbf.append("IsWXFocus=?,");
				}if(wxUserCust.getSex()!=null&&!wxUserCust.getSex().equals(updateWxUserCust.getSex())) {
					sbf.append("Sex=?,");
				}if(wxUserCust.getLatitude()!=null&&!wxUserCust.getLatitude().equals(updateWxUserCust.getLatitude())) {
					sbf.append("Latitude=?,");
				}if(wxUserCust.getLongitude()!=null&&!wxUserCust.getLongitude().equals(updateWxUserCust.getLongitude())) {
					sbf.append("Longitude=?,");
				}if(wxUserCust.getNewstatus()!=null&&!wxUserCust.getNewstatus().equals(updateWxUserCust.getNewstatus())) {
					sbf.append("Newstatus=?,");
				}if(wxUserCust.getStoreImg()!=null&&!wxUserCust.getStoreImg().equals(updateWxUserCust.getStoreImg())) {
					sbf.append("StoreImg=?,");
				}if(wxUserCust.getReLatitude()!=null&&!wxUserCust.getReLatitude().equals(updateWxUserCust.getReLatitude())) {
					sbf.append("ReLatitude=?,");
				}if(wxUserCust.getReLongitude()!=null&&!wxUserCust.getReLongitude().equals(updateWxUserCust.getReLongitude())) {
					sbf.append("ReLongitude=?,");
				}if(wxUserCust.getSalesman()!=null&&!wxUserCust.getSalesman().equals(updateWxUserCust.getSalesman())) {
					sbf.append("Salesman=?,");
				}if(wxUserCust.getCustStatus()!=null&&!wxUserCust.getCustStatus().equals(updateWxUserCust.getCustStatus())) {
					sbf.append("CustStatus=?,");
				}if(wxUserCust.getSalesPriceTimes()!=null&&!wxUserCust.getSalesPriceTimes().equals(updateWxUserCust.getSalesPriceTimes())) {
					sbf.append("SalesPriceTimes=?,");
				}if(wxUserCust.getInactive()!=null&&!wxUserCust.getInactive().equals(updateWxUserCust.getInactive())) {
					sbf.append("Inactive=?,");
				}if(wxUserCust.getGaodeId()!=null&&!wxUserCust.getGaodeId().equals(updateWxUserCust.getGaodeId())) {
					sbf.append("gaodeId=?,");
				}
				sbf.append("lastUpdateTime=now()");
				sbf.append(" where openid = '").append(wxUserCust.getOpenid()).append("'");
				
				ps = conn.prepareStatement(sbf.toString());
				
				int runNo = 1;
				if(wxUserCust.getNickname()!=null&&!wxUserCust.getNickname().equals(updateWxUserCust.getNickname())) {
					ps.setString(runNo,updateWxUserCust.getNickname());
					runNo++;
				}if(wxUserCust.getRemark()!=null&&!wxUserCust.getRemark().equals(updateWxUserCust.getRemark())) {
					ps.setString(runNo,updateWxUserCust.getRemark());
					runNo++;
				}if(wxUserCust.getCity()!=null&&!wxUserCust.getCity().equals(updateWxUserCust.getCity())) {
					ps.setString(runNo,updateWxUserCust.getCity());
					runNo++;
				}if(wxUserCust.getProvince()!=null&&!wxUserCust.getProvince().equals(updateWxUserCust.getProvince())) {
					ps.setString(runNo,updateWxUserCust.getProvince());
					runNo++;
				}if(wxUserCust.getCountry()!=null&&!wxUserCust.getCountry().equals(updateWxUserCust.getCountry())) {
					ps.setString(runNo,updateWxUserCust.getCountry());
					runNo++;
				}if(wxUserCust.getHeadimgurl()!=null&&!wxUserCust.getHeadimgurl().equals(updateWxUserCust.getHeadimgurl())) {
					ps.setString(runNo,updateWxUserCust.getHeadimgurl());
					runNo++;
				}if(wxUserCust.getSubscribe_time()!=null&&!wxUserCust.getSubscribe_time().equals(updateWxUserCust.getSubscribe_time())) {
					ps.setString(runNo,updateWxUserCust.getSubscribe_time());
					runNo++;
				}if(wxUserCust.getUnionid()!=null&&!wxUserCust.getUnionid().equals(updateWxUserCust.getUnionid())) {
					ps.setString(runNo,updateWxUserCust.getUnionid());
					runNo++;
				}if(wxUserCust.getGroupid()!=null&&!wxUserCust.getGroupid().equals(updateWxUserCust.getGroupid())) {
					ps.setString(runNo,updateWxUserCust.getGroupid());
					runNo++;
				}if(wxUserCust.getTagid_list()!=null&&!wxUserCust.getTagid_list().equals(updateWxUserCust.getTagid_list())) {
					ps.setString(runNo,updateWxUserCust.getTagid_list());
					runNo++;
				}if(wxUserCust.getSubscribe_scene()!=null&&!wxUserCust.getSubscribe_scene().equals(updateWxUserCust.getSubscribe_scene())) {
					ps.setString(runNo,updateWxUserCust.getSubscribe_scene());
					runNo++;
				}if(wxUserCust.getQr_scene()!=null&&!wxUserCust.getQr_scene().equals(updateWxUserCust.getQr_scene())) {
					ps.setString(runNo,updateWxUserCust.getQr_scene());
					runNo++;
				}if(wxUserCust.getQr_scene_str()!=null&&!wxUserCust.getQr_scene_str().equals(updateWxUserCust.getQr_scene_str())) {
					ps.setString(runNo,updateWxUserCust.getQr_scene_str());
					runNo++;
				}if(wxUserCust.getCustid()!=null&&!wxUserCust.getCustid().equals(updateWxUserCust.getCustid())) {
					ps.setString(runNo,updateWxUserCust.getCustid());
					runNo++;
				}if(wxUserCust.getCustKHWXH()!=null&&!wxUserCust.getCustKHWXH().equals(updateWxUserCust.getCustKHWXH())) {
					ps.setString(runNo,updateWxUserCust.getCustKHWXH());
					runNo++;
				}if(wxUserCust.getCustName()!=null&&!wxUserCust.getCustName().equals(updateWxUserCust.getCustName())) {
					ps.setString(runNo,updateWxUserCust.getCustName());
					runNo++;
				}if(wxUserCust.getCreater()!=null&&!wxUserCust.getCreater().equals(updateWxUserCust.getCreater())) {
					ps.setString(runNo,updateWxUserCust.getCreater());
					runNo++;
				}if(wxUserCust.getCreatedate()!=null&&!wxUserCust.getCreatedate().equals(updateWxUserCust.getCreatedate())) {
					ps.setString(runNo,updateWxUserCust.getCreatedate());
					runNo++;
				}if(wxUserCust.getModer()!=null&&!wxUserCust.getModer().equals(updateWxUserCust.getModer())) {
					ps.setString(runNo,updateWxUserCust.getModer());
					runNo++;
				}if(wxUserCust.getModdate()!=null&&!wxUserCust.getModdate().equals(updateWxUserCust.getModdate())) {
					ps.setString(runNo,updateWxUserCust.getModdate());
					runNo++;
				}if(wxUserCust.getMphone()!=null&&!wxUserCust.getMphone().equals(updateWxUserCust.getMphone())) {
					ps.setString(runNo,updateWxUserCust.getMphone());
					runNo++;
				}if(wxUserCust.getCustRemark()!=null&&!wxUserCust.getCustRemark().equals(updateWxUserCust.getCustRemark())) {
					ps.setString(runNo,updateWxUserCust.getCustRemark());
					runNo++;
				}if(wxUserCust.getIsMsgNew()!=null&&!wxUserCust.getIsMsgNew().equals(updateWxUserCust.getIsMsgNew())) {
					ps.setString(runNo,updateWxUserCust.getIsMsgNew());
					runNo++;
				}if(wxUserCust.getCrm_area()!=null&&!wxUserCust.getCrm_area().equals(updateWxUserCust.getCrm_area())) {
					ps.setString(runNo,updateWxUserCust.getCrm_area());
					runNo++;
				}if(wxUserCust.getCrm_addr_name()!=null&&!wxUserCust.getCrm_addr_name().equals(updateWxUserCust.getCrm_addr_name())) {
					ps.setString(runNo,updateWxUserCust.getCrm_addr_name());
					runNo++;
				}if(wxUserCust.getCrm_province()!=null&&!wxUserCust.getCrm_province().equals(updateWxUserCust.getCrm_province())) {
					ps.setString(runNo,updateWxUserCust.getCrm_province());
					runNo++;
				}if(wxUserCust.getCrm_city()!=null&&!wxUserCust.getCrm_city().equals(updateWxUserCust.getCrm_city())) {
					ps.setString(runNo,updateWxUserCust.getCrm_city());
					runNo++;
				}if(wxUserCust.getKHMC()!=null&&!wxUserCust.getKHMC().equals(updateWxUserCust.getKHMC())) {
					ps.setString(runNo,updateWxUserCust.getKHMC());
					runNo++;
				}if(wxUserCust.getKHLXR()!=null&&!wxUserCust.getKHLXR().equals(updateWxUserCust.getKHLXR())) {
					ps.setString(runNo,updateWxUserCust.getKHLXR());
					runNo++;
				}if(wxUserCust.getCrm_town()!=null&&!wxUserCust.getCrm_town().equals(updateWxUserCust.getCrm_town())) {
					ps.setString(runNo,updateWxUserCust.getCrm_town());
					runNo++;
				}if(wxUserCust.getCityPickerCN()!=null&&!wxUserCust.getCityPickerCN().equals(updateWxUserCust.getCityPickerCN())) {
					ps.setString(runNo,updateWxUserCust.getCityPickerCN());
					runNo++;
				}if(wxUserCust.getIsWXFocus()!=null&&!wxUserCust.getIsWXFocus().equals(updateWxUserCust.getIsWXFocus())) {
					ps.setString(runNo,updateWxUserCust.getIsWXFocus());
					runNo++;
				}if(wxUserCust.getSex()!=null&&!wxUserCust.getSex().equals(updateWxUserCust.getSex())) {
					ps.setString(runNo,updateWxUserCust.getSex());
					runNo++;
				}if(wxUserCust.getLatitude()!=null&&!wxUserCust.getLatitude().equals(updateWxUserCust.getLatitude())) {
					ps.setString(runNo,updateWxUserCust.getLatitude());
					runNo++;
				}if(wxUserCust.getLongitude()!=null&&!wxUserCust.getLongitude().equals(updateWxUserCust.getLongitude())) {
					ps.setString(runNo,updateWxUserCust.getLongitude());
					runNo++;
				}if(wxUserCust.getNewstatus()!=null&&!wxUserCust.getNewstatus().equals(updateWxUserCust.getNewstatus())) {
					ps.setString(runNo,updateWxUserCust.getNewstatus());
					runNo++;
				}if(wxUserCust.getStoreImg()!=null&&!wxUserCust.getStoreImg().equals(updateWxUserCust.getStoreImg())) {
					ps.setString(runNo,updateWxUserCust.getStoreImg());
					runNo++;
				}if(wxUserCust.getReLatitude()!=null&&!wxUserCust.getReLatitude().equals(updateWxUserCust.getReLatitude())) {
					ps.setString(runNo,updateWxUserCust.getReLatitude());
					runNo++;
				}if(wxUserCust.getReLongitude()!=null&&!wxUserCust.getReLongitude().equals(updateWxUserCust.getReLongitude())) {
					ps.setString(runNo,updateWxUserCust.getReLongitude());
					runNo++;
				}if(wxUserCust.getSalesman()!=null&&!wxUserCust.getSalesman().equals(updateWxUserCust.getSalesman())) {
					ps.setString(runNo,updateWxUserCust.getSalesman());
					runNo++;
				}if(wxUserCust.getCustStatus()!=null&&!wxUserCust.getCustStatus().equals(updateWxUserCust.getCustStatus())) {
					ps.setString(runNo,updateWxUserCust.getCustStatus());
					runNo++;
				}if(wxUserCust.getSalesPriceTimes()!=null&&!wxUserCust.getSalesPriceTimes().equals(updateWxUserCust.getSalesPriceTimes())) {
					ps.setString(runNo,updateWxUserCust.getSalesPriceTimes());
					runNo++;
				}if(wxUserCust.getInactive()!=null&&!wxUserCust.getInactive().equals(updateWxUserCust.getInactive())) {
					ps.setString(runNo,updateWxUserCust.getInactive());
					runNo++;
				}if(wxUserCust.getGaodeId()!=null&&!wxUserCust.getGaodeId().equals(updateWxUserCust.getGaodeId())) {
					ps.setString(runNo,updateWxUserCust.getGaodeId());
					runNo++;
				}
				ps.executeUpdate();
				updateRMIds.add("openid ='"+wxUserCust.getOpenid()+"'");
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
			Set<Map.Entry<String, WxUserCust>> ent = syncWxUserCustData.entrySet();
			StringBuffer sbf = new StringBuffer("insert into WxUserCust(");
			sbf.append("openid,nickname,remark,city,province,country,headimgurl,subscribe_time,unionid,groupid,tagid_list,subscribe_scene,qr_scene,qr_scene_str,Custid,CustKHWXH,CustName,creater,createdate,moder,moddate,mphone,CustRemark,IsSys,isMsgNew,crm_area,crm_addr_name,crm_province,crm_city,KHMC,KHLXR,crm_town,cityPickerCN,isWXFocus,sex,latitude,longitude,newstatus,storeImg,reLatitude,reLongitude,IsSync_,salesman,custStatus,wxFocusLogs,salesPriceTimes,inactive,gaodeId");
			sbf.append(") select ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?");
			sbf.append(" where not exists (select 1 from WxUserCust where openid=?)");
			ps = conn.prepareStatement(sbf.toString());
			
			for (Map.Entry<String, WxUserCust> entry : ent) {
				WxUserCust objWxuserCust = syncWxUserCustData.get(entry.getKey());
				ps.setString(1, objWxuserCust.getOpenid());
				ps.setString(2, objWxuserCust.getNickname());
				ps.setString(3, objWxuserCust.getRemark());
				ps.setString(4, objWxuserCust.getCity());
				ps.setString(5, objWxuserCust.getProvince());
				ps.setString(6, objWxuserCust.getCountry());
				ps.setString(7, objWxuserCust.getHeadimgurl());
				ps.setString(8, objWxuserCust.getSubscribe_time());
				ps.setString(9, objWxuserCust.getUnionid());
				ps.setString(10, objWxuserCust.getGroupid());
				ps.setString(11, objWxuserCust.getTagid_list());
				ps.setString(12, objWxuserCust.getSubscribe_scene());
				ps.setString(13, objWxuserCust.getQr_scene());
				ps.setString(14, objWxuserCust.getQr_scene_str());
				ps.setString(15, objWxuserCust.getCustid());
				ps.setString(16, objWxuserCust.getCustKHWXH());
				ps.setString(17, objWxuserCust.getCustName());
				ps.setString(18, objWxuserCust.getCreater());
				ps.setString(19, objWxuserCust.getCreatedate());
				ps.setString(20, objWxuserCust.getModer());
				ps.setString(21, objWxuserCust.getModdate());
				ps.setString(22, objWxuserCust.getMphone());
				ps.setString(23, objWxuserCust.getCustRemark());
				ps.setString(24, objWxuserCust.getIsSys());
				ps.setString(25, objWxuserCust.getIsMsgNew());
				ps.setString(26, objWxuserCust.getCrm_area());
				ps.setString(27, objWxuserCust.getCrm_addr_name());
				ps.setString(28, objWxuserCust.getCrm_province());
				ps.setString(29, objWxuserCust.getCrm_city());
				ps.setString(30, objWxuserCust.getKHMC());
				ps.setString(31, objWxuserCust.getKHLXR());
				ps.setString(32, objWxuserCust.getCrm_town());
				ps.setString(33, objWxuserCust.getCityPickerCN());
				ps.setString(34, objWxuserCust.getIsWXFocus());
				ps.setString(35, objWxuserCust.getSex());
				ps.setString(36, objWxuserCust.getLatitude());
				ps.setString(37, objWxuserCust.getLongitude());
				ps.setString(38, objWxuserCust.getNewstatus());
				ps.setString(39, objWxuserCust.getStoreImg());
				ps.setString(40, objWxuserCust.getReLatitude());
				ps.setString(41, objWxuserCust.getReLongitude());
				ps.setString(42, objWxuserCust.getIsSync_());
				ps.setString(43, objWxuserCust.getSalesman());
				ps.setString(44, objWxuserCust.getCustStatus());
				ps.setString(45, objWxuserCust.getWxFocusLogs());
				ps.setString(46, objWxuserCust.getSalesPriceTimes());
				ps.setString(47, objWxuserCust.getInactive());
				ps.setString(48, objWxuserCust.getGaodeId());
				
				ps.setString(49,objWxuserCust.getOpenid());
				
				ps.addBatch();
				updateRMIds_.add("openid ='"+objWxuserCust.getOpenid()+"'");//有防止重复插入，可重复多次操作，没关系，一次不成功就多次吧
				
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
	
	
	
	private void batResetSyncSourceData(List<String> updateRMIds) throws Exception {
		Connection conn = null;
		Statement st = null;
		try {
			conn = C3P0DBManager.getConnection();
			st = conn.createStatement();
			StringBuffer updateSQL = new StringBuffer("update WxUserCust set isSync = 0 where ");
			for (int i = 0; i < updateRMIds.size(); i++) {
				updateSQL.append(updateRMIds.get(i)).append(" or ");
				if (i>0&&(i % batMaxAmount)==0) {
					updateSQL.replace(updateSQL.length() - 4, updateSQL.length(), "");
					st.executeUpdate(updateSQL.toString());
					updateSQL = new StringBuffer("update WxUserCust set isSync = 0 where ");
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
