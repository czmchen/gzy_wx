package com.weixin.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.wxpay.sdk.GanZhuYouPay;
import com.weixin.comparator.LocationComparator;
import com.weixin.constants.CommonConstants;
import com.weixin.entity.WxSetting;
import com.weixin.pay.util.Sha1Util;
import com.weixin.service.WxSettingService;
import com.weixin.util.CommonUtil;
import com.weixin.util.PropertiesUtils;
import com.weixin.util.SHA1;
import com.weixin.util.SpringUtil;
import com.weixin.util.StringUtil;
import com.weixin.util.WxUtil;
import com.weixin.vo.RMWxUserCust;
import com.weixin.vo.WxJSApiInfo;
import com.yq.entity.Order;
import com.yq.entity.User;
import com.yq.entity.UserMerchants;
import com.yq.service.OrderService;
import com.yq.service.UserMerchantsService;
import com.yq.service.UserService;
import com.yq.util.JsonUtil;
import com.yq.vo.OrderVO;

@Controller
@RequestMapping
public class BindingMphoneController {
	private final static Logger logger = Logger.getLogger(BindingMphoneController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private OrderService orderService;
	@Autowired
	WxSettingService wxSettingService;
	@Autowired
	UserMerchantsService userMerchantsService;

	@ResponseBody
	@RequestMapping(value = "/page/binding.html")
	public String binding(String mphone, String KHMC, String KHLXR, String crm_province, String crm_city, String crm_area, String crm_town, String crmAddrName, String cityPickerCN,String latitude, String longitude,String gaodeId,String merchantsId,HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		String oppenId = WxUtil.oppen_id(request, session).get("oppen_id").toString();
		map.put("oppen_id", oppenId);
		map.put("KHMC", KHMC);
		map.put("KHLXR", KHLXR);
		map.put("crm_province", crm_province);
		map.put("crm_city", crm_city);
		map.put("crm_area", crm_area);
		map.put("crm_town", crm_town);
		map.put("crm_addr_name", crmAddrName);
		map.put("mphone", mphone);
		map.put("cityPickerCN", cityPickerCN);
		map.put("latitude", latitude);
		map.put("longitude", longitude);
		map.put("gaodeId", gaodeId);
		map.put("merchantsId", StringUtil.isRealEmpty(merchantsId)?null:merchantsId);
		int succ = userService.updateRmUserMphone(map);
		if (succ == 1) {
			return "success";
		}
		return "fail";
	}
	
	@RequestMapping(value = "/page/go2Location.html")
	public ModelAndView go2Location(String address,HttpServletRequest request, HttpSession session) throws UnsupportedEncodingException {
		ModelAndView ml = new ModelAndView();
		String oppenId = WxUtil.oppen_id(request, session).get("oppen_id").toString();
		ml.addObject("address", address);
		ml.addObject("oppenId", oppenId);
		ml.setViewName("page/binding_gaodeMap");
		return ml;
	}
	
	@ResponseBody
	@RequestMapping(value = "/page/userMerchants.html", method = { RequestMethod.POST, RequestMethod.GET })
	public String distributionMerchants(UserMerchants objUserMerchants,HttpServletResponse response, HttpServletRequest request, HttpSession session) {
		String result = "error";
		try {
			if(session.getAttribute("oppen_id")==null) {
				return null;
			}
			String openId = (String) session.getAttribute("oppen_id");
			objUserMerchants.setOpenId(openId);
			objUserMerchants.setSetType(1);
			userMerchantsService.save(objUserMerchants);
			result = objUserMerchants.getId()+"";
		} catch (Exception e) {
			logger.error("ScrmSalesManCtrl custDig",e);
		}
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/page/newCustSave.html")
	public ModelAndView newCustSave(String _mphone, String _KHLXR, String _crm_province, String _crm_city, String _crm_area, String _crm_town, String _crmAddrName, String _cityPickerCN,String _latitude, String _longitude,HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView ml = new ModelAndView();
		ml.setViewName("page/freemileresult");

		String operResult = "1";
		Map<String, Object> map = new HashMap<String, Object>();
		Order order = new Order();
		order.setExpressCost(0.0f);
		ml.addObject("order", order);
		
		try {
			String oppenId = WxUtil.oppen_id(request, session).get("oppen_id").toString();

			String keyString = "WX_NEW_USER:" + oppenId;// 删除新用户标识
			RedisTemplate redisTemplate = (RedisTemplate) SpringUtil.getBean("redisTemplate");
			if (redisTemplate.opsForHash().get(keyString, oppenId) == null) {
				ml.addObject("errorMsg", "该活动只针对新客，系统监测到您已为老客户，请自助下单吧！祝您购物愉快！");
				ml.addObject("operResult", "0");
				return ml;
			}
			//redisTemplate.opsForHash().delete(keyString, oppenId);

			map.put("oppen_id", oppenId);
			map.put("KHLXR", _KHLXR);
			map.put("crm_province", _crm_province);
			map.put("crm_city", _crm_city);
			map.put("crm_area", _crm_area);
			map.put("crm_town", _crm_town);
			map.put("crm_addr_name", _crmAddrName);
			map.put("mphone", _mphone);
			map.put("cityPickerCN", _cityPickerCN);
			map.put("latitude",_latitude);
			map.put("longitude", _longitude);
			userService.updateRmUserMphone(map);

			order = orderService.freeMile(oppenId);// 生成一张免费的订单
			
			if (order.getExpressCost() > 0.0f) {// 如果需要支付运费
				OrderVO objOrderVO = new OrderVO();
				objOrderVO.setOrder_id(order.getOrder_id());
				objOrderVO.setGoods_total(order.getGoods_total());
				logger.error("BindingMphoneController newCustSave："+"||order.getOrder_id:"+order.getOrder_id()+"||order.getGoods_total()："+order.getGoods_total());
				new GanZhuYouPay().getPayPackage(objOrderVO, request, response, session);
			}
			ml.addObject("order", order);
		} catch (Exception e) {
			logger.error(e);
			operResult = "0";
		}
		ml.addObject("operResult", operResult);
		return ml;
	}
	
	@RequestMapping(value = "page/go2NearbyStore.html")
	public ModelAndView nearbyStore() {
		ModelAndView ml = new ModelAndView();
		try {
			WxSetting wxSetting = wxSettingService.selectByPrimaryKey(1);
			String ticket = CommonUtil.getTicket();
			String noncestr = Sha1Util.getNonceStr();
			String timestamp = Sha1Util.getTimeStamp();
			StringBuffer sbf = new StringBuffer();
			sbf.append("jsapi_ticket=").append(ticket).append("&");
			sbf.append("noncestr=").append(noncestr).append("&");
			sbf.append("timestamp=").append(timestamp).append("&");
			sbf.append("url=").append(PropertiesUtils.read(CommonConstants.WEB_ROOT_URL)).append("/page/go2NearbyStore.html?a=1");
			
			WxJSApiInfo objWxJSApiInfo = new WxJSApiInfo();
			objWxJSApiInfo.setAppId(wxSetting.getAppid());
			objWxJSApiInfo.setNonceStr(noncestr);
			objWxJSApiInfo.setSignature(new SHA1().getDigestOfString(sbf.toString().getBytes()).toLowerCase());
			objWxJSApiInfo.setTimestamp(timestamp);
			ml.addObject("objWxJSApiInfo", objWxJSApiInfo);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		String url = "page/nearbyStore";
		ml.setViewName(url);
		return ml;
	}
	
	@ResponseBody
	@RequestMapping(value = "page/bindingDetail.html")
	public ModelAndView bindingDetail(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView ml = new ModelAndView();
		try {
//			WxSetting wxSetting = wxSettingService.selectByPrimaryKey(1);
//			String ticket = CommonUtil.getTicket();
//			String noncestr = Sha1Util.getNonceStr();
//			String timestamp = Sha1Util.getTimeStamp();
//			StringBuffer sbf = new StringBuffer();
//			sbf.append("jsapi_ticket=").append(ticket).append("&");
//			sbf.append("noncestr=").append(noncestr).append("&");
//			sbf.append("timestamp=").append(timestamp).append("&");
//			sbf.append("url=").append(PropertiesUtils.read(CommonConstants.WEB_ROOT_URL)).append("/page/bindingDetail.html");
//			
//			WxJSApiInfo objWxJSApiInfo = new WxJSApiInfo();
//			objWxJSApiInfo.setAppId(wxSetting.getAppid());
//			objWxJSApiInfo.setNonceStr(noncestr);
//			objWxJSApiInfo.setSignature(new SHA1().getDigestOfString(sbf.toString().getBytes()).toLowerCase());
//			objWxJSApiInfo.setTimestamp(timestamp);
//			ml.addObject("objWxJSApiInfo", objWxJSApiInfo);
			
			String oppen_id = WxUtil.oppen_id(request, session).get("oppen_id").toString();
			User user = new User();
			user.setOppen_id(oppen_id);
			
			List<User> lstUser = userService.listById(user);
			if(lstUser!=null&&lstUser.size()>0){
				User objUser = lstUser.get(0);
				if (objUser != null && !StringUtil.isRealEmpty(objUser.getCrm_area())) {
					ml.addObject("cityPickerCode", objUser.getCrm_province() +","+ objUser.getCrm_city() +","+ objUser.getCrm_area());
				}else{
					ml.addObject("cityPickerCode", "");
				}
				ml.addObject("data", lstUser.get(0));
			}
			ml.setViewName("page/bindingmobile");
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return ml;
	}
	
	@ResponseBody
	@RequestMapping(value = "/page/getNearByStore.html")
	public void binding(String latitude, String longitude,HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		RMWxUserCust nowLocation = new RMWxUserCust();
		nowLocation.setReLatitude(latitude);
		nowLocation.setReLongitude(longitude);
		
		List<RMWxUserCust> lstCusts = userService.getNearByStore(latitude, longitude);
		Collections.sort(lstCusts, new LocationComparator(nowLocation));
		
		returnJson(response, request, JsonUtil.obj2JSON(lstCusts.get(0)));
	}

	@ResponseBody
	@RequestMapping(value = "page/go2binding.html")
	public ModelAndView go2binding() {
		ModelAndView ml = new ModelAndView();
		try {
			WxSetting wxSetting = wxSettingService.selectByPrimaryKey(1);
			String ticket = CommonUtil.getTicket();
			String noncestr = Sha1Util.getNonceStr();
			String timestamp = Sha1Util.getTimeStamp();
			StringBuffer sbf = new StringBuffer();
			sbf.append("jsapi_ticket=").append(ticket).append("&");
			sbf.append("noncestr=").append(noncestr).append("&");
			sbf.append("timestamp=").append(timestamp).append("&");
			sbf.append("url=").append(PropertiesUtils.read(CommonConstants.WEB_ROOT_URL)).append("/page/go2binding.html");
			
			WxJSApiInfo objWxJSApiInfo = new WxJSApiInfo();
			objWxJSApiInfo.setAppId(wxSetting.getAppid());
			objWxJSApiInfo.setNonceStr(noncestr);
			objWxJSApiInfo.setSignature(new SHA1().getDigestOfString(sbf.toString().getBytes()).toLowerCase());
			objWxJSApiInfo.setTimestamp(timestamp);
			ml.addObject("objWxJSApiInfo", objWxJSApiInfo);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		String url = "page/bindingmobile";
		ml.setViewName(url);
		return ml;
	}
	
	
	public static final String CONTENTTYPE_JSON = "application/json; charset=UTF-8";
	public static final String CONTENTTYPE_HTML = "text/html; charset=UTF-8";
	public static final String ACCEPT = "accept";
	public static final String JSON = "json";

	public void returnJson(HttpServletResponse response, HttpServletRequest request, String json) {
		try {
			String contentType = CONTENTTYPE_JSON;
			if (request != null) {
				String accept = request.getHeader(ACCEPT);
				if (accept != null && !accept.contains(JSON)) {
					contentType = CONTENTTYPE_HTML;
				}
			}
			response.setContentType(contentType);
			response.getWriter().write(json);
			response.getWriter().flush();
		} catch (IOException e) {
			logger.error("returnJson is error!", e);
		}
	}
}
