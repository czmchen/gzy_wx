package com.yq.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.weixin.constants.CommonConstants;
import com.weixin.util.PropertiesUtils;
import com.weixin.util.StringUtil;
import com.yq.entity.User;
import com.yq.service.UserService;
import com.yq.util.JsonUtil;

@Controller
@RequestMapping("/wxCodeLoginCtrl")
public class WxCodeLoginCtrl {
	private Logger logger = Logger.getLogger(this.getClass());

	private static String BUILTINSYS_REDIS_SCRM_SALESMAN_SERVER_UUID_DATA_CONSTANTS = "BUILTINSYS_REDIS_SCRM_SALESMAN_SERVER_UUID_DATA_CONSTANTS";
	private static String BUILTINSYS_REDIS_SCRM_SERVICE_SERVER_UUID_DATA_CONSTANTS = "BUILTINSYS_REDIS_SCRM_SERVICE_SERVER_UUID_DATA_CONSTANTS";
	@Autowired
	private UserService userService;
	@Autowired
	private RedisTemplate<String, ?> redisTemplate;
	
	@RequestMapping(value = "/salesmanLogin.html")
	public void salesmanLogin(String uuid, HttpServletResponse response, HttpSession session) {
		try {
			if (StringUtil.isRealEmpty(uuid)) {
				response.sendRedirect(PropertiesUtils.read(CommonConstants.WEB_ROOT_URL)+"/submitStatusCtrl/go2SubResult.html?operResult=0&returnURL=/scrm/salesman/index.html&operType=close&errorMsg=您不是业务员，登陆失败!&cu=");
				return ;
			}
			String openId = (String) session.getAttribute("oppen_id");//openId = "olSMfty-Izl7sqsQM493M4YjmEsI";//测试，生产记得删除
			User userData = userService.getUserByOpenId(openId);
			if (userData != null && userData.getIsSalesMan() == 1) {
				redisTemplate.opsForHash().put(BUILTINSYS_REDIS_SCRM_SALESMAN_SERVER_UUID_DATA_CONSTANTS, uuid, JsonUtil.obj2JSON(userData));
				response.sendRedirect(PropertiesUtils.read(CommonConstants.WEB_ROOT_URL)+"/submitStatusCtrl/go2SubResult.html?operResult=1&returnURL=/scrm/salesman/index.html&operType=close&cu=");
			} else {
				response.sendRedirect(PropertiesUtils.read(CommonConstants.WEB_ROOT_URL)+"/submitStatusCtrl/go2SubResult.html?operResult=0&returnURL=/scrm/salesman/index.html&operType=close&errorMsg=您不是业务员，登陆失败!&cu=");
			}
		} catch (Exception e) {
			logger.error("WxCodeLoginCtrl customerLogin",e);
		}
	}
	
	
	@RequestMapping(value = "/scrmServiceLogin.html")
	public void customerLogin(String uuid, HttpServletResponse response, HttpSession session) {
		try {
			if (StringUtil.isRealEmpty(uuid)) {
				response.sendRedirect(PropertiesUtils.read(CommonConstants.WEB_ROOT_URL)+"/submitStatusCtrl/go2SubResult.html?operResult=0&returnURL=/scrm/index.html&operType=close&errorMsg=您不是客服，登陆失败!&cu=");
				return ;
			}
			String openId = (String) session.getAttribute("oppen_id");//openId = "olSMfty-Izl7sqsQM493M4YjmEsI";//测试，生产记得删除
			User userData = userService.getUserByOpenId(openId);
			if (userData != null && userData.getIsCustomer() == 1) {
				redisTemplate.opsForHash().put(BUILTINSYS_REDIS_SCRM_SERVICE_SERVER_UUID_DATA_CONSTANTS, uuid, JsonUtil.obj2JSON(userData));
				response.sendRedirect(PropertiesUtils.read(CommonConstants.WEB_ROOT_URL)+"/submitStatusCtrl/go2SubResult.html?operResult=1&returnURL=/scrm/index.html&operType=close&cu=");
			} else {
				response.sendRedirect(PropertiesUtils.read(CommonConstants.WEB_ROOT_URL)+"/submitStatusCtrl/go2SubResult.html?operResult=0&returnURL=/scrm/index.html&operType=close&errorMsg=您不是客服，登陆失败!&cu=");
			}
		} catch (Exception e) {
			logger.error("WxCodeLoginCtrl customerLogin",e);
		}
	}
	
}
