package com.yq.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.weixin.util.C3P0DBManager;
import com.weixin.util.StringUtil;
import com.yq.dao.DistributionDao;
import com.yq.entity.Distribution;
import com.yq.util.JsonUtil;
import com.yq.vo.DistributionVo;
import com.yq.vo.UploadDetailVo;
import com.yq.vo.UploadVo;

@Service
public class DistributionService {
	@Autowired
	DistributionDao distributionDao;
	private final static Logger logger = Logger.getLogger(DistributionService.class);

	public List<DistributionVo> search(Distribution distribution) {
		return distributionDao.search(distribution);
	}

	public int update(Distribution distribution) {
		return distributionDao.update(distribution);
	}

	public int orderSign(Distribution distribution) {
		return distributionDao.orderSign(distribution);
	}
	
	public String getRMWXUserCust(String storeOpenId, String custId) throws RuntimeException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			StringBuffer querySQLString = new StringBuffer("select storeImg from WxUserCust t where ");
			if (!StringUtil.isRealEmpty(custId) && !StringUtil.isRealEmpty(storeOpenId)) {
				querySQLString.append("t.Custid = ?");
			}
			if (!StringUtil.isRealEmpty(custId) && StringUtil.isRealEmpty(storeOpenId)) {
				querySQLString.append("t.Custid = ?");
			}
			if (StringUtil.isRealEmpty(custId) && !StringUtil.isRealEmpty(storeOpenId)) {
				querySQLString.append("t.openid = ?");
			}
			conn = C3P0DBManager.getConnection();
			ps = conn.prepareStatement(querySQLString.toString());
			if (!StringUtil.isRealEmpty(custId) && !StringUtil.isRealEmpty(storeOpenId)) {
				ps.setString(1, custId);
			}
			if (!StringUtil.isRealEmpty(custId) && StringUtil.isRealEmpty(storeOpenId)) {
				ps.setString(1, custId);
			}
			if (StringUtil.isRealEmpty(custId) && !StringUtil.isRealEmpty(storeOpenId)) {
				ps.setString(1, storeOpenId);
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getString("storeImg");
			}
		} catch (SQLException e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, rs);
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public String saveStoreImg2RMWXUserCust(UploadVo objUploadVo) throws RuntimeException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String updateStoreImgJson = "";

		try {
			lock.lock();
			
			String storeOpenId = objUploadVo.getDef1();
			String custId = objUploadVo.getDef2();
			String distributionId = objUploadVo.getDef3();

			String storeImgJson = getRMWXUserCust(storeOpenId, custId);
			List<UploadDetailVo> lstUploadDetailVos = null;
			if (!StringUtil.isRealEmpty(storeImgJson)) {// 执行update
				lstUploadDetailVos = (List<UploadDetailVo>) JsonUtil.json2List(storeImgJson);
			} else {// 执行新建插入
				lstUploadDetailVos = new ArrayList<UploadDetailVo>();
			}
			lstUploadDetailVos.add(objUploadVo.getLstUploadDetailVo().get(0));
			updateStoreImgJson = JsonUtil.list2JSON(lstUploadDetailVos);
			
			updateStoreImg(distributionId,storeOpenId, custId, updateStoreImgJson);
			updateStoreImg2RMWXUserCust(storeOpenId, custId, updateStoreImgJson);
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
			C3P0DBManager.releaseConnection(conn, ps, rs);
		}
		return updateStoreImgJson;
	}
	
	private Lock lock = new ReentrantLock();
	
	public String removeStoreImg2RMWXUserCust(String distributionId,String storeOpenId, String custId,String filedeskName) throws RuntimeException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String updateStoreImgJson = "";

		try {
			lock.lock();
			
			String storeImgJson = getRMWXUserCust(storeOpenId, custId);
			if(StringUtil.isRealEmpty(storeImgJson)) {
				return "";
			}
			List dataJSON = JsonUtil.json2List(storeImgJson);
			for(int i=0;i<dataJSON.size();i++) {
				JSONObject jsonObject = (JSONObject) dataJSON.get(i);
				if(jsonObject.getString("filedeskName").equals(filedeskName)) {
					dataJSON.remove(i);
					break;
				}
			}
			updateStoreImgJson = JsonUtil.list2JSON(dataJSON);
			updateStoreImg(distributionId,storeOpenId, custId, updateStoreImgJson);
			updateStoreImg2RMWXUserCust(storeOpenId, custId, updateStoreImgJson);
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
			C3P0DBManager.releaseConnection(conn, ps, rs);
		}
		return updateStoreImgJson;
	}
	
	private void updateStoreImg(String distributionId,String storeOpenId, String custId, String storeImgJson) {
		Distribution distribution = new Distribution();
		distribution.setId(distributionId);
		distribution.setStoreImg(storeImgJson);
		distributionDao.update(distribution);
	}

	private void updateStoreImg2RMWXUserCust(String storeOpenId, String custId, String storeImgJson) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			StringBuffer updateSQL = new StringBuffer("update WxUserCust set storeImg = ? where ");
			if (!StringUtil.isRealEmpty(custId) && !StringUtil.isRealEmpty(storeOpenId)) {
				updateSQL.append("Custid = ?");
			}
			if (!StringUtil.isRealEmpty(custId) && StringUtil.isRealEmpty(storeOpenId)) {
				updateSQL.append("Custid = ?");
			}
			if (StringUtil.isRealEmpty(custId) && !StringUtil.isRealEmpty(storeOpenId)) {
				updateSQL.append("openid = ?");
			}
			conn = C3P0DBManager.getConnection();
			ps = conn.prepareStatement(updateSQL.toString());
			ps.setString(1, storeImgJson);
			if (!StringUtil.isRealEmpty(custId) && !StringUtil.isRealEmpty(storeOpenId)) {
				ps.setString(2, custId);
			}
			if (!StringUtil.isRealEmpty(custId) && StringUtil.isRealEmpty(storeOpenId)) {
				ps.setString(2, custId);
			}
			if (StringUtil.isRealEmpty(custId) && !StringUtil.isRealEmpty(storeOpenId)) {
				ps.setString(2, storeOpenId);
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, null);
		}
	}

}
