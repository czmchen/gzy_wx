package com.yq.service;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weixin.util.C3P0DBManager;
import com.yq.dao.WXPayResultDao;

@Service
public class WXPayResultService {
	@Autowired
	private WXPayResultDao WXPayResultDao;
	private final static Logger logger = Logger.getLogger(WXPayResultService.class);
	

	public int insert(Map<String, String> map) {
		try{
			insertIN2RM(map);//先执行插入到远程
		}catch(Exception e){
		}
		return WXPayResultDao.insert(map);
	}
	
	private int insertIN2RM(Map<String, String> map) throws RuntimeException {
		int succss = 0;
		Connection conn = null;
		Statement st = null;
		try {
			StringBuffer sql = new StringBuffer("IF NOT EXISTS (select 1 from WxPaydetail where transaction_id= '").append(map.get("transaction_id")).append("') ");
			sql.append("insert into WxPaydetail(transaction_id,bank_type,openid,sign,fee_type,mch_id,cash_fee,out_trade_no,appid,total_fee,trade_type,result_code,time_end,is_subscribe,return_code) ");
			sql.append("select '").append(map.get("transaction_id")).append("' transaction_id,'");
			sql.append(map.get("bank_type")).append("' bank_type,'");
			sql.append(map.get("openid")).append("' openid,'");
			sql.append(map.get("sign")).append("' sign,'");
			sql.append(map.get("fee_type")).append("' fee_type,'");
			sql.append(map.get("mch_id")).append("' mch_id,'");
			sql.append(map.get("cash_fee")).append("' cash_fee,'");
			sql.append(map.get("out_trade_no")).append("' out_trade_no,'");
			sql.append(map.get("appid")).append("' appid,'");
			sql.append(map.get("total_fee")).append("' total_fee,'");
			sql.append(map.get("trade_type")).append("' trade_type,'");
			sql.append(map.get("result_code")).append("' result_code,'");
			sql.append(map.get("time_end")).append("' time_end,'");
			sql.append(map.get("is_subscribe")).append("' is_subscribe,'");
			sql.append(map.get("return_code")).append("' return_code");
			
			conn = C3P0DBManager.getConnection();
			st = conn.createStatement();
			st.executeUpdate(sql.toString());
			succss = 1;
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, st, null);
		}
		return succss;
	}
}
