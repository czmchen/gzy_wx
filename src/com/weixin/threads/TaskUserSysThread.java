package com.weixin.threads;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weixin.entity.Token;
import com.weixin.util.C3P0DBManager;
import com.weixin.util.CommonUtil;
import com.weixin.util.DateUtils;
import com.weixin.util.HttpClientManager;
import com.weixin.util.SpringUtil;
import com.weixin.util.StringUtil;
import com.yq.entity.User;
import com.yq.service.UserService;
import com.yq.util.JsonUtil;

public class TaskUserSysThread implements Runnable {

	private final static Logger logger = Logger.getLogger(TaskUserSysThread.class);
	private List<String> allUserData = new ArrayList<String>();
	private String nextOpenid;
	private final String ZH_CN = "zh_CN";
	public static List<User> USER_INFO_LIST_DATA = new ArrayList<User>();

	@Override
	public void run() {
		while (true) {
			try {
				while(true) {
					Thread.sleep(35000);//35S后检查一次
					if(DateUtils.nowDate(DateUtils.TIME_FORMATE).indexOf("00:00:")!=-1) {//凌晨开始执行
						logger.debug(new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").format(new Date()) + " TaskUserSysThread任务进行中。。。");
						execSysCustomer();
					}
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	private void execSysCustomer() {
		allUserData.clear();
		allUserData = new ArrayList<String>();
		nextOpenid = "";
		try {
			try {
				this.recursionGetAllCustomer();// 递归获取所有的公众号的用户openid
				this.getUserDetailInfo();// 获取用户明细
			} catch (Exception e) {
				logger.error(e);
			} finally {
			}
			this.update2Db();// 更新数据库中的数据值
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@SuppressWarnings("static-access")
	private void recursionGetAllCustomer() {
		StringUtil st = new StringUtil();
		Token token = new CommonUtil().getToken(st.getSetting().getAppid(), st.getSetting().getAppsecret());

		String allUserUrl = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + token.getAccessToken();
		if (!"".equals(nextOpenid)) {// 若最后一个，则显示相关的结果
			allUserUrl += "&next_openid=" + nextOpenid;
		}
		try {
			String responseData = HttpClientManager.loadUrl(allUserUrl);
			if(responseData.indexOf("\"count\":0,")==-1) {// 受微信限制最多一次拉取10000条
				RMWxCustomerMsg objRMWxCustomerMsg = JsonUtil.json2Entity(responseData,RMWxCustomerMsg.class);
				allUserData.addAll(objRMWxCustomerMsg.getData().getOpenid());
				nextOpenid = objRMWxCustomerMsg.getNext_openid();
				recursionGetAllCustomer();// 递归获取所有的公众号关注的用户openId
			}
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	private void getUserDetailInfo() {
		List<String> userJsonPar = new ArrayList<String>();// bat抽取微信用户详细信息条件对象

		List<User_list> userBatPar = new ArrayList<User_list>();
		CustomerInfo objCustomerInfo = new CustomerInfo();
		objCustomerInfo.setUser_list(userBatPar);

		int userIndex = 1;
		for (String openId : allUserData) {
			if (userIndex % 100 == 0) {// 100的整数，从100个开始new新对象
				userJsonPar.add(JsonUtil.obj2JSON(objCustomerInfo));// 到100的倍数时，直接传化为json的条件

				userBatPar.clear();
				userBatPar = null;
				objCustomerInfo.getUser_list().clear();// 清空
				objCustomerInfo = null;

				userBatPar = new ArrayList<User_list>();// 新建对象
				objCustomerInfo = new CustomerInfo();
				objCustomerInfo.setUser_list(userBatPar);
			}
			User_list objUser_list = new User_list();
			objUser_list.setLang(ZH_CN);
			objUser_list.setOpenid(openId);
			userBatPar.add(objUser_list);
			userIndex++;
		}

		int threadAmount = 8;
		ExecutorService executorService = Executors.newFixedThreadPool(threadAmount);
		int oneThreadRunNum = userJsonPar.size() / threadAmount;
		int runIndex = 1;
		List<String> oneThreadUserPar = new ArrayList<String>();
		List<List<String>> threadPar = new ArrayList<List<String>>();
		for (String param : userJsonPar) {// 迭代所有的用户参数
			if (runIndex % oneThreadRunNum == 0) {// 一条线程可以装多少个参数集
				threadPar.add(oneThreadUserPar);// 装完后就放到线程集里；
				oneThreadUserPar = new ArrayList<String>();
			}
			oneThreadUserPar.add(param);
			runIndex++;
		}

		for (int i = runIndex; i < userJsonPar.size(); i++) {// 若剩下的，全部丢到第一条线程执行采集
			threadPar.get(0).add(userJsonPar.get(i));
		}

		for (List<String> lstPar : threadPar) {// 开启线程采集
			executorService.execute(new CollectUserInfoThread(lstPar));
		}
		executorService.shutdown();
		try {
			executorService.awaitTermination(7200, TimeUnit.SECONDS);// 2小时内必须完成，不完成就报销
		} catch (Exception e) {
			logger.error(e);
		}
		executorService.shutdownNow();
	}

	private int updateBatAmount = 1000;

	private void update2Db() {
		List<List<User>> batData = new ArrayList<List<User>>();
		List<User> batUpdateUser = new ArrayList<User>();
		int updateCounter = 1;
		for (User user : USER_INFO_LIST_DATA) {// 迭代所有的用户参数
			if (updateCounter % updateBatAmount == 0) {// 一条线程可以装多少个参数集
				batData.add(batUpdateUser);
				;// 装完后就放到线程集里；
				batUpdateUser = new ArrayList<User>();
			}
			batUpdateUser.add(user);
			updateCounter++;
		}

		for (int i = updateCounter; i < USER_INFO_LIST_DATA.size(); i++) {// 若剩下的，全部丢到第一条线程执行采集
			batData.get(0).add(USER_INFO_LIST_DATA.get(i));
		}

		ExecutorService executorService = Executors.newFixedThreadPool(2);// 单线程执行更新wx数据库更新
		executorService.execute(new Thread(new Runnable() {
			public void run() {
				for (List<User> usersData : batData) {
					try {
						((UserService) SpringUtil.getBean("userService")).updateBatch(usersData);
						Thread.sleep(200);
					} catch (Exception e) {
						logger.error(e);
					}
				}
			}
		}));
		executorService.execute(new Thread(new Runnable() {// 单线程执行更新RM SQLSERVER数据库更新
			public void run() {
				for (List<User> usersData : batData) {
					try {
						update2RMDb(usersData);
						Thread.sleep(200);
					} catch (Exception e) {
						logger.error(e);
					}
				}
			}
		}));
		executorService.shutdown();
		try {
			executorService.awaitTermination(600, TimeUnit.SECONDS);// 10分钟内必须完成，不完成就报销
		} catch (Exception e) {
			logger.error(e);
		}
		executorService.shutdownNow();
	}

	private void update2RMDb(List<User> batData) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = C3P0DBManager.getConnection();
			conn.setAutoCommit(false);
			String batUpdateSQL = "update WxUserCust set nickname=?,headimgurl=? where openid = ?";
			ps = conn.prepareStatement(batUpdateSQL);
			for (User user : batData) {
				ps.setString(1, user.getRealname());
				ps.setString(2, user.getHead_img());
				ps.setString(3, user.getOppen_id());
				ps.addBatch();
			}
			ps.executeBatch();
		} catch (Exception e) {
			logger.error("UserService.update2RMDb错误" + "||" + e);
		} finally {
			try {
				conn.commit();
			} catch (SQLException e) {
				logger.error("UserService.update2RMDb错误" + "||" + e);
			}
			C3P0DBManager.releaseConnection(conn, ps, null);
		}
	}

}

class CollectUserInfoThread implements Runnable {
	private final static Logger logger = Logger.getLogger(CollectUserInfoThread.class);
	private List<String> userJsonPar = null;
	private final int errorRunTime = 10;
	private int errorRun = 0;

	public CollectUserInfoThread(List<String> userJsonPar) {
		super();
		this.userJsonPar = userJsonPar;
	}

	@SuppressWarnings({ "static-access", "rawtypes" })
	@Override
	public void run() {
		StringUtil st = new StringUtil();
		Token token = new CommonUtil().getToken(st.getSetting().getAppid(), st.getSetting().getAppsecret());

		String userBatUrl = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token="
				+ token.getAccessToken();
		for (String param : userJsonPar) {
			try {
				String userData = HttpClientManager.postJSON(userBatUrl, param);
				
				Map data = JsonUtil.json2Map(userData);
				JSONArray objJSONArray = (JSONArray)data.get("user_info_list");
				for(int i = 0;i<objJSONArray.size();i++) {
					JSONObject jsonObject = (JSONObject)objJSONArray.get(i);
					Object headimgurl = jsonObject.get("headimgurl");
					Object nickname = jsonObject.get("nickname");
					Object openid = jsonObject.get("openid");
					User objUser = new User();
					objUser.setOppen_id(openid.toString());
					objUser.setRealname(nickname.toString());
					objUser.setHead_img(headimgurl.toString());
					TaskUserSysThread.USER_INFO_LIST_DATA.add(objUser);
				}
				
				if (errorRun == errorRunTime) {// 防止黑了，错误十次就跳出
					break;
				}
				Thread.sleep(200);
			} catch (Exception e) {
				errorRun++;
				logger.error(e);
			}
		}
	}
}

class UserInfoList {

	private List<User_info_list> user_info_list;

	public void setUser_info_list(List<User_info_list> user_info_list) {
		this.user_info_list = user_info_list;
	}

	public List<User_info_list> getUser_info_list() {
		return user_info_list;
	}

}

class User_info_list {
	private int subscribe;
	private String openid;
	private String nickname;
	private int sex;
	private String language;
	private String city;
	private String province;
	private String country;
	private String headimgurl;
	private long subscribe_time;
	private String unionid;
	private String remark;
	private int groupid;
	private Date tagid_list;
	private String subscribe_scene;
	private long qr_scene;
	private String qr_scene_str;

	public void setSubscribe(int subscribe) {
		this.subscribe = subscribe;
	}

	public int getSubscribe() {
		return subscribe;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getNickname() {
		return nickname;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getSex() {
		return sex;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLanguage() {
		return language;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getProvince() {
		return province;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountry() {
		return country;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setSubscribe_time(long subscribe_time) {
		this.subscribe_time = subscribe_time;
	}

	public long getSubscribe_time() {
		return subscribe_time;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public int getGroupid() {
		return groupid;
	}

	public void setTagid_list(Date tagid_list) {
		this.tagid_list = tagid_list;
	}

	public Date getTagid_list() {
		return tagid_list;
	}

	public void setSubscribe_scene(String subscribe_scene) {
		this.subscribe_scene = subscribe_scene;
	}

	public String getSubscribe_scene() {
		return subscribe_scene;
	}

	public void setQr_scene(long qr_scene) {
		this.qr_scene = qr_scene;
	}

	public long getQr_scene() {
		return qr_scene;
	}

	public void setQr_scene_str(String qr_scene_str) {
		this.qr_scene_str = qr_scene_str;
	}

	public String getQr_scene_str() {
		return qr_scene_str;
	}

}

class RMWxCustomerMsg {

	private int total;
	private int count;
	private Data data;
	private String next_openid;

	public void setTotal(int total) {
		this.total = total;
	}

	public int getTotal() {
		return total;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public Data getData() {
		return data;
	}

	public void setNext_openid(String next_openid) {
		this.next_openid = next_openid;
	}

	public String getNext_openid() {
		return next_openid;
	}

}

class Data {

	private List<String> openid;

	public void setOpenid(List<String> openid) {
		this.openid = openid;
	}

	public List<String> getOpenid() {
		return openid;
	}

}

class CustomerInfo {

	private List<User_list> user_list;

	public void setUser_list(List<User_list> user_list) {
		this.user_list = user_list;
	}

	public List<User_list> getUser_list() {
		return user_list;
	}

}

class User_list {

	private String openid;
	private String lang;

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getLang() {
		return lang;
	}

}
