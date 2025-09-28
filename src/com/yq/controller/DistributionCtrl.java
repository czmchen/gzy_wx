package com.yq.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.weixin.service.WxSettingService;
import com.weixin.util.DateUtils;
import com.yq.entity.Distribution;
import com.yq.entity.DistributionMerchants;
import com.yq.service.DistributionMerchantsService;
import com.yq.service.DistributionService;
import com.yq.util.JsonUtil;
import com.yq.util.NavigationUtils;
import com.yq.vo.DistributionVo;

@Controller
@RequestMapping("/distribution")
public class DistributionCtrl {
	private final static Logger logger = Logger.getLogger(DistributionCtrl.class);
	@Autowired
	DistributionService distributionService;
	@Autowired
	WxSettingService wxSettingService;
	@Autowired
	DistributionMerchantsService distributionMerchantsService;

	@RequestMapping(value = "/index.html")
	public ModelAndView index(HttpServletRequest request, HttpSession session) throws UnsupportedEncodingException {
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
//			sbf.append("url=").append(PropertiesUtils.read(CommonConstants.WEB_ROOT_URL)).append("/distribution/index.html?a=1");
//			
//			WxJSApiInfo objWxJSApiInfo = new WxJSApiInfo();
//			objWxJSApiInfo.setAppId(wxSetting.getAppid());
//			objWxJSApiInfo.setNonceStr(noncestr);
//			objWxJSApiInfo.setSignature(new SHA1().getDigestOfString(sbf.toString().getBytes()).toLowerCase());
//			objWxJSApiInfo.setTimestamp(timestamp);
//			ml.addObject("objWxJSApiInfo", objWxJSApiInfo);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		ml.setViewName("page/distribution/index");
		return ml;
	}
	
	
	@RequestMapping(value = "/go2Location.html")
	public ModelAndView go2Location(String address,String lat,String lng,String custId,String custOpenId,String distributionId,String khmc,HttpServletRequest request, HttpSession session) throws UnsupportedEncodingException {
		ModelAndView ml = new ModelAndView();
		ml.addObject("address", address);
		ml.addObject("lat", lat);
		ml.addObject("lng", lng);
		ml.addObject("custId", custId);
		ml.addObject("custOpenId", custOpenId);
		ml.addObject("khmc", khmc);
		ml.addObject("distributionId", distributionId);
		DistributionMerchants objDistributionMerchants = distributionMerchantsService.getMerchantByDistributionId(distributionId);
		if(objDistributionMerchants==null||objDistributionMerchants.getId()==null) {
			objDistributionMerchants = distributionMerchantsService.getMerchantByWxusercust(custId);
		}
		ml.addObject("distributionMerchants", JsonUtil.obj2JSON(objDistributionMerchants));
		ml.setViewName("page/distribution/gaodeMap");
		return ml;
	}
	
	@ResponseBody
	@RequestMapping(value = "/distributionMerchants.html", method = { RequestMethod.POST, RequestMethod.GET })
	public String distributionMerchants(DistributionMerchants objDistributionMerchants,HttpServletResponse response, HttpServletRequest request, HttpSession session) {
		int result = 0;
		try {
			if(session.getAttribute("oppen_id")==null) {
				return null;
			}
			String openId = (String) session.getAttribute("oppen_id");
			objDistributionMerchants.setSetOpenId(openId);
			objDistributionMerchants.setSetType(1);
			distributionMerchantsService.save(objDistributionMerchants);
			
			Distribution distribution = new Distribution();
			distribution.setId(objDistributionMerchants.getDistributionId().toString());
			distribution.setLatitude(objDistributionMerchants.getLat());
			distribution.setLongitude(objDistributionMerchants.getLng());
			distribution.setMerchantsId(objDistributionMerchants.getId());
			distribution.setActionLogs(DateUtils.nowDate(DateUtils.DATETIME_FORMATE)+":司机点击设置当前的定位为收货地址：latitude:"+objDistributionMerchants.getLat()+"||longitude:"+objDistributionMerchants.getLng());
			result = distributionService.update(distribution);
			
		} catch (Exception e) {
			logger.error("ScrmSalesManCtrl custDig",e);
		}
		return result+"";
	}
	
	@RequestMapping(value = "/list.html", method = RequestMethod.POST)
	public void list(Distribution distribution, BindingResult bindingResult, Model model, HttpServletResponse response, HttpServletRequest request, HttpSession session) {
		try {
			if(session.getAttribute("oppen_id")==null) {
				return ;
			}
			String openId = (String) session.getAttribute("oppen_id");
			distribution.setDriverOpenId(openId);
			List<DistributionVo> lstdistribution = distributionService.search(distribution);
			List<DistributionVo> lstNSignData = new ArrayList<DistributionVo>();//未签收数据
			List<DistributionVo> lstSignData = new ArrayList<DistributionVo>();//已签收数据
			
			
			for(DistributionVo objDistribution : lstdistribution) {
				objDistribution.setReciever(objDistribution.getReciever().replaceAll("\n", ""));
				objDistribution.setAddress(objDistribution.getAddress().replaceAll("\n", ""));
				if(objDistribution.getSignStatus()==1) {
					lstSignData.add(objDistribution);
				}else {
					lstNSignData.add(objDistribution);
				}
			}
			
			List<DistributionVo> lstNSignNotNeedSortData = new ArrayList<DistributionVo>();//非需排序的数据
			Map<Integer,List<DistributionVo>> sortData = new HashMap<Integer,List<DistributionVo>>();//排序内的数据	
			for(DistributionVo objDistribution : lstNSignData) {
				if(objDistribution.getSortNum()!=0) {//需排序的数据
					List<DistributionVo> sortDetail = sortData.get(objDistribution.getSortNum());
					if(sortDetail==null) {
						sortDetail = new ArrayList<DistributionVo>();
					}
					sortDetail.add(objDistribution);
					sortData.put(objDistribution.getSortNum(), sortDetail);
				}else {//剩下的是不需要排序的数据
					lstNSignNotNeedSortData.add(objDistribution);
				}
			}
			
			for(Entry<Integer,List<DistributionVo>> sortDataDetail : sortData.entrySet()) {//从排序立面的数据进行排序
				if(sortDataDetail.getValue().size()>1) {
					Collections.sort(sortDataDetail.getValue(), new Comparator<Distribution>() {
						@Override
						public int compare(Distribution distribution1, Distribution distribution2) {
							return distribution2.getSortDateTime().compareTo(distribution1.getSortDateTime());
						}
					});
				}
			}
			
			List<DistributionVo> lstAllShowData = new ArrayList<DistributionVo>();//展示的所有数据
			for(int i = 0;i<lstNSignNotNeedSortData.size();i++) {//执行插入队列数据
				int sortIndex = i+1;
				if(sortData.get(sortIndex)!=null) {
					lstAllShowData.addAll(sortData.get(sortIndex));
					sortData.remove(sortIndex);//移除匹配的数据集
				}
				lstNSignNotNeedSortData.get(i).setSortNum(i+1);//排序，并排的编号
				lstAllShowData.add(lstNSignNotNeedSortData.get(i));
			}
			for(Entry<Integer,List<DistributionVo>> sortDataDetail : sortData.entrySet()) {//超出所有的数据队列的，加到最后
				lstAllShowData.addAll(sortDataDetail.getValue());
			}
//			for(int i = 0;i<lstAllShowData.size();i++) {//编排序号码
//				lstAllShowData.get(i).setSortNum(i+1);
//			}
			
			lstAllShowData.addAll(lstSignData);//加入已签收的数据
			
			
			returnJson(response, request, JsonUtil.list2JSON(lstAllShowData));
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getNavigation.html", method = RequestMethod.POST)
	public void getNavigation(String address, HttpServletResponse response, HttpServletRequest request) {
		try {
			returnJson(response, request, JsonUtil.obj2JSON(NavigationUtils.getGeocoder(address)));
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/driverSignConfirm.html", method = RequestMethod.POST)
	public String driverSignConfirm(String recordId) {
		try {
			//distribution.setDriverOpenId(ACCEPT);
			Distribution distribution = new Distribution();
			distribution.setId(recordId);
			distribution.setRecStatus("1");
			distribution.setActionLogs(DateUtils.nowDate(DateUtils.DATETIME_FORMATE)+":司机点击改变状态为1（已配送）!");
			int result = distributionService.update(distribution);
			return result+"";
		} catch (Exception e) {
			logger.error(e);
		}
		return "0";
	}
	

	
	@ResponseBody
	@RequestMapping(value = "/settingSort.html", method = RequestMethod.POST)
	public String settingSort(String id,int sortNum) {
		try {
			//distribution.setDriverOpenId(ACCEPT);
			Distribution distribution = new Distribution();
			distribution.setId(id);
			distribution.setSortNum(sortNum);
			distribution.setSortDateTime(DateUtils.nowDate(DateUtils.DATETIME_FORMATE));
			distribution.setActionLogs(DateUtils.nowDate(DateUtils.DATETIME_FORMATE)+":司机点击改变排序为："+sortNum);
			int result = distributionService.update(distribution);
			return result+"";
		} catch (Exception e) {
			logger.error(e);
		}
		return "0";
	}
	
	@ResponseBody
	@RequestMapping(value = "/setttingNowLocation.html", method = RequestMethod.POST)
	public String setttingNowLocation(String id,String latitude,String longitude) {
		try {
			//distribution.setDriverOpenId(ACCEPT);
			Distribution distribution = new Distribution();
			distribution.setId(id);
			distribution.setLatitude(latitude);
			distribution.setLongitude(longitude);
			distribution.setActionLogs(DateUtils.nowDate(DateUtils.DATETIME_FORMATE)+":司机点击设置当前的定位为收货地址：latitude:"+latitude+"||longitude:"+longitude);
			int result = distributionService.update(distribution);
			return result+"";
		} catch (Exception e) {
			logger.error(e);
		}
		return "0";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/removeStoreImg.html", method = RequestMethod.POST)
	public String removeStoreImg(String distributionId,String storeOpenId, String custId,String filedeskName) {
		try {
			return distributionService.removeStoreImg2RMWXUserCust(distributionId,storeOpenId, custId, filedeskName);
		} catch (Exception e) {
			logger.error(e);
		}
		return "0";
	}
	
	@ResponseBody
	@RequestMapping(value = "/getStoreImg.html", method = RequestMethod.POST)
	public String getStoreImg(String storeOpenId, String custId, HttpServletResponse response, HttpServletRequest request) {
		try {
			return distributionService.getRMWXUserCust(storeOpenId, custId);
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
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
