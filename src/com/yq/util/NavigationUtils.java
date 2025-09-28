package com.yq.util;

import org.apache.log4j.Logger;

import com.weixin.util.HttpClientManager;
import com.weixin.util.PropertiesUtils;
import com.yq.constants.CommonConstants;
import com.yq.vo.navigation.NavigationVO;

public class NavigationUtils {
	private final static Logger logger = Logger.getLogger(NavigationUtils.class);

	public static NavigationVO getGeocoder(final String address) {
		NavigationVO navigationVO = null;
		try {
			String url = "https://apis.map.qq.com/ws/geocoder/v1/?address=" + address + "&key="
					+ PropertiesUtils.read(CommonConstants.NAVIGATION_KEY).trim();
			String response = HttpClientManager.loadUrl(url);
			navigationVO = com.alibaba.fastjson.JSONObject.parseObject(response, NavigationVO.class);
			if (navigationVO.getStatus() == 0) {
				return navigationVO;
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			//client.releaseConnection();
		}
		return navigationVO;
	}
}
