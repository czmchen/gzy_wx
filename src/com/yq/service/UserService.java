package com.yq.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weixin.util.C3P0DBManager;
import com.weixin.util.StringUtil;
import com.weixin.vo.RMWxUserCust;
import com.yq.dao.UserDao;
import com.yq.entity.Address;
import com.yq.entity.User;

@Service
public class UserService{
	@Autowired
	private UserDao userDao;
	@Autowired
	AddressService addressService;
	private final static Logger logger = Logger.getLogger(UserService.class);
	public int insert(Map<String,Object> map ){
		return userDao.insert(map);
	}
	
	public int update(Map<String,Object> map ){
		return userDao.update(map);
	}
	
	public int updateBatch(List<User> updateUsers) {
		return userDao.updateBatch(updateUsers);
	}
	
	public int insertWithRmUser(Map<String,Object> map ){
		int result = userDao.insertNotRep(map);
		
		Connection conn = null;
		Statement st = null;
	    String subtime = "";
	    Object subscribeTimeObj = map.get("subscribe_time");
		if (subscribeTimeObj != null&&!StringUtil.isRealEmpty(subscribeTimeObj.toString())&&!"null".equals(subscribeTimeObj.toString())) {
			Long time = Long.valueOf(Long.parseLong(map.get("subscribe_time").toString()));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			subtime = sdf.format(new Date(time.longValue() * 1000L));
		}
		StringBuffer sbf = new StringBuffer("IF NOT EXISTS (select 1 from WxUserCust where WxUserCust.openid = '").append(map.get("openid")).append("') ");
		sbf.append("insert into WxUserCust(openid,nickname,remark,city,province,country,headimgurl,subscribe_time,unionid,groupid,tagid_list,subscribe_scene,qr_scene,qr_scene_str,isWXFocus,wxFocusLogs) ");
		sbf.append("select '").append(map.get("openid")).append("' openid,'");
		sbf.append(map.get("nickname")).append("' nickname,'");
		sbf.append(map.get("remark")).append("' remark,'");
		sbf.append(map.get("city")).append("' city,'");
		sbf.append(map.get("province")).append("' province,'");
		sbf.append(map.get("country")).append("' country,'");
		sbf.append(map.get("headimgurl")).append("' headimgurl,'");
		sbf.append(subtime).append("' subscribe_time,'");
		sbf.append(map.get("unionid")).append("' unionid,'");
		sbf.append(map.get("groupid")).append("' groupid,'");
		sbf.append(map.get("tagid_list")).append("' tagid_list,'");
		sbf.append(map.get("subscribe_scene")).append("' subscribe_scene,'");
		sbf.append(map.get("qr_scene")).append("' qr_scene,'");
		sbf.append(map.get("qr_scene_str")).append("' qr_scene_str,'");
		sbf.append(map.get("isWXFocus")==null?1:map.get("isWXFocus")).append("' isWXFocus,");
		sbf.append("(CONVERT(varchar(100), GETDATE(), 20)+':关注公众号'+CHAR(10)) wxFocusLogs");
		logger.info(sbf.toString());
		String insertSQL = sbf.toString();
		try {
			conn = C3P0DBManager.getConnection();
			st = conn.createStatement();
			st.executeUpdate(insertSQL);
		}  catch (Exception e) {
			logger.error("UserService.insertWithRmUser错误SQL:"+ insertSQL + "||" + e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, st, null);
		}
		return result;
	}
	
	public int updateWithRmUser(Map<String,Object> map ){
		Connection conn = null;
		PreparedStatement ps = null;
		String subtime = "";
	    Object subscribeTimeObj = map.get("subscribe_time");
	    int result = 0;
		try {
			result = userDao.update(map);
			conn = C3P0DBManager.getConnection();
//			ps = conn.prepareStatement("update WxUserCust set nickname=?,remark=?,city=?,province=?,country=?,headimgurl=?,unionid=?,groupid=?,tagid_list=?,subscribe_scene=?,qr_scene=?,qr_scene_str=?"+(map.get("subscribe_time")!=null?",subscribe_time=?,isWXFocus=1,wxFocusLogs=(ISNULL(wxFocusLogs,'')+CONVERT(varchar(100), GETDATE(), 20)+':关注公众号'+CHAR(10))":"")+" where openid=?");
			ps = conn.prepareStatement("update WxUserCust set nickname=N'"+map.get("nickname")+"',remark=?,city=?,province=?,country=?,headimgurl=?,unionid=?,groupid=?,tagid_list=?,subscribe_scene=?,qr_scene=?,qr_scene_str=?,subscribe_time=? "+(map.get("subscribe_time")!=null?",isWXFocus=1,wxFocusLogs=(ISNULL(wxFocusLogs,'')+CONVERT(varchar(100), GETDATE(), 20)+':关注公众号'+CHAR(10))":"")+" where openid=?");
			ps.setObject(1,map.get("remark"));
			ps.setObject(2,map.get("city"));
			ps.setObject(3,map.get("province"));
			ps.setObject(4,map.get("country"));
			ps.setObject(5,map.get("headimgurl"));
			ps.setObject(6,map.get("unionid"));
			ps.setObject(7,map.get("groupid"));
			ps.setObject(8,map.get("tagid_list"));
			ps.setObject(9,map.get("subscribe_scene"));
			ps.setObject(10,map.get("qr_scene"));
			ps.setObject(11,map.get("qr_scene_str"));
			if (subscribeTimeObj != null&&!StringUtil.isRealEmpty(subscribeTimeObj.toString())&&!"null".equals(subscribeTimeObj.toString())) {
				Long time = Long.valueOf(Long.parseLong(map.get("subscribe_time").toString()));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				subtime = sdf.format(new Date(time.longValue() * 1000L));
			}
			ps.setObject(12, subtime);
			ps.setObject(13,map.get("openid"));
			ps.executeUpdate();
		} catch (Exception e) {
			 logger.error("UserService.updateWithRmUser错误map：" + "||" + e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, null);
		}
		return result;
	}
	
	public int updateRMIsWXFocus(Map<String,Object> map){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = C3P0DBManager.getConnection();
			ps = conn.prepareStatement("update WxUserCust set isWXFocus=?,wxFocusLogs=(ISNULL(wxFocusLogs,'')+CONVERT(varchar(100), GETDATE(), 20)+':取消关注公众号'+CHAR(10)) where openid=?");
			ps.setObject(1,map.get("isWXFocus"));
			ps.setObject(2,map.get("oppen_id"));
			ps.executeUpdate();
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, ps, null);
		}
		return userDao.updateIsWXFocus(map);
	}
	
	
	public int updateRmUserMphone(Map<String,Object> map){
		Connection conn = null;
		PreparedStatement ps = null;
		int succ = 0;
		try {
			conn = C3P0DBManager.getConnection();
			ps = conn.prepareStatement("update WxUserCust set KHMC=?,KHLXR=?,mphone=?,crm_province=?,crm_city=?,crm_area=?,crm_town=?,crm_addr_name=?,cityPickerCN=?,latitude=?,longitude=?,gaodeId=?,newstatus=1 where openid=?");
			ps.setObject(1,map.get("KHMC"));
			ps.setObject(2,map.get("KHLXR"));
			ps.setObject(3,map.get("mphone"));
			ps.setObject(4,map.get("crm_province"));
			ps.setObject(5,map.get("crm_city"));
			ps.setObject(6,map.get("crm_area"));
			ps.setObject(7,map.get("crm_town"));
			ps.setObject(8,map.get("crm_addr_name"));
			ps.setObject(9,map.get("cityPickerCN"));
			ps.setObject(10,map.get("latitude"));
			ps.setObject(11,map.get("longitude"));
			ps.setObject(12,map.get("gaodeId"));
			ps.setObject(13,map.get("oppen_id"));
			ps.executeUpdate();
			userDao.upmphone(map);
			
			Address queryAddress = new Address();
			queryAddress.setOppen_id((String)map.get("oppen_id"));
			queryAddress.setIsCustomerRegist(1);
			Address objAddress = addressService.getAddressById(queryAddress);
			Map<String,Object> addressPar = new HashMap<String,Object>();
			addressPar.put("province", map.get("crm_province"));
			addressPar.put("city", map.get("crm_city"));
			addressPar.put("area", map.get("crm_area"));
			addressPar.put("town", map.get("crm_town"));
			addressPar.put("addr_name", map.get("crm_addr_name"));
			addressPar.put("addr_tel", map.get("mphone"));
			addressPar.put("addr_user", map.get("KHLXR"));
			addressPar.put("oppen_id", map.get("oppen_id"));
			addressPar.put("isCustomerRegist", 1);
			addressPar.put("cityPickerCN", map.get("cityPickerCN"));
			if(objAddress!=null&&objAddress.getAddr_id()!=null){
				addressPar.put("addr_id", objAddress.getAddr_id());
				addressService.update(addressPar);
			}else{
				addressService.insert(addressPar);
			}
			succ = 1;
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}finally{
			C3P0DBManager.releaseConnection(conn, ps, null);
		}
		return succ;
	}
	
	public int uparea(Map<String,Object> map ){
		return userDao.uparea(map);
	}
	
	public int upstatus(Map<String,Object> map){
		return userDao.upstatus(map);
	}
	
	public int upmbertime(Map<String,Object> map){
		return userDao.upmbertime(map);
	}
	
	public int upRMCustInfo(Map<String,Object> map){
		return userDao.upRMCustInfo(map);
	}
	
	public int upmphone(Map<String,Object> map){
		return userDao.upmphone(map);
	}

	public List<User> list(User user){
		return userDao.list(user);
	}
	public int count(User user){
		return userDao.count(user);
	}
	
	public List<User> listById(User user){
		return userDao.listById(user);
	}
	
	public int isMember(User user){
		return userDao.isMember(user);
	}
	
	public int updateSalesPrice1(String oppen_id){
		return userDao.updateSalesPrice1(oppen_id);
	}
	
	public String getCustidByOpenId(String oppen_id){
		return userDao.getCustidByOpenId(oppen_id);
	}
	
	public User getUserByOpenId(String oppen_id){
		return userDao.getUserByOpenId(oppen_id);
	}
	
	public List<User> allUser(String wxName) {
		return userDao.allUser(wxName);
	}
	
	public List<RMWxUserCust> getNearByStore(String latitude, String longitude){
		Connection conn = null;
		Statement st = null;
		List<RMWxUserCust> lstCusts = new ArrayList<RMWxUserCust>();
		try {
			StringBuffer querySQLBuffer = new StringBuffer();
			querySQLBuffer.append("SELECT ");
			querySQLBuffer.append("	t.* ");
			querySQLBuffer.append("FROM ");
			querySQLBuffer.append("	WxUserCust t ");
			querySQLBuffer.append("WHERE ");
			querySQLBuffer.append("	reLongitude IS NOT NULL ");
			querySQLBuffer.append("	AND reLongitude != '' ");
			querySQLBuffer.append("	AND Custid IS NOT NULL ");
			querySQLBuffer.append("	AND Custid != '' ");
			querySQLBuffer.append("	AND inactive = 0 ");
			querySQLBuffer.append("	AND sqrt( ");
			querySQLBuffer.append("		( ");
			querySQLBuffer.append("			( ");
			querySQLBuffer.append("				( ").append(longitude).append("-CAST ( CAST ( [reLongitude] AS MONEY ) AS DECIMAL ( 10, 2 ) ) ) * PI( ) * 12656 * cos( ");
			querySQLBuffer.append("					( ( ").append(latitude).append("+CAST ( CAST ( [reLatitude] AS MONEY ) AS DECIMAL ( 10, 2 ) ) ) / 2 ) * PI( ) / 180 ");
			querySQLBuffer.append("				) / 180 ");
			querySQLBuffer.append("				) * ( ");
			querySQLBuffer.append("				( ").append(longitude).append("-CAST ( CAST ( [reLongitude] AS MONEY ) AS DECIMAL ( 10, 2 ) ) ) * PI( ) * 12656 * cos(  ");
			querySQLBuffer.append("					( ( ").append(latitude).append("+CAST ( CAST ( [reLatitude] AS MONEY ) AS DECIMAL ( 10, 2 ) ) ) / 2 ) * PI( ) / 180 ");
			querySQLBuffer.append("				) / 180  ");
			querySQLBuffer.append("			) ");
			querySQLBuffer.append("			) + ( ");
			querySQLBuffer.append("			( ( ").append(latitude).append("-CAST ( CAST ( [reLatitude] AS MONEY ) AS DECIMAL ( 10, 2 ) ) ) * PI( ) * 12656 / 180 ) * ( ( ").append(latitude).append("-CAST ( CAST ( [reLatitude] AS MONEY ) AS DECIMAL ( 10, 2 ) ) ) * PI( ) * 12656 / 180 ) ");
			querySQLBuffer.append("		) ");
			querySQLBuffer.append("	) < 10 ");
			conn = C3P0DBManager.getConnection();
			st = conn.createStatement();
			ResultSet rsResultset = st.executeQuery(querySQLBuffer.toString());
			while(rsResultset.next()) {
				RMWxUserCust rmWxUserCust = new RMWxUserCust();
				rmWxUserCust.setKHMC(rsResultset.getString("KHMC"));
				rmWxUserCust.setCustName(rsResultset.getString("CustName"));
				rmWxUserCust.setCityPickerCN(rsResultset.getString("cityPickerCN"));
				rmWxUserCust.setCrm_addr_name(rsResultset.getString("crm_addr_name"));
				rmWxUserCust.setLongitude(rsResultset.getString("longitude"));
				rmWxUserCust.setReLongitude(rsResultset.getString("reLongitude"));
				rmWxUserCust.setReLatitude(rsResultset.getString("reLatitude"));
				rmWxUserCust.setLatitude(rsResultset.getString("latitude"));
				lstCusts.add(rmWxUserCust);
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		} finally {
			C3P0DBManager.releaseConnection(conn, st, null);
		}
		return lstCusts;
	}
}
