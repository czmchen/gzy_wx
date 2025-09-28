/***************************************************************************/
/*                                                                         */
/* Copyright (c) 2016 China Traditional Chinese Medicine Co. Limited       */
/* 中国中药有限公司版权所有                                               								   */
/*                                                                         */
/* PROPRIETARY RIGHTS of China Traditional Chinese Medicine Co. Limited    */
/* are involved in the subject matter of this material. All manufacturing, */
/* reproduction, use,and sales rights pertaining to this subject matter    */
/* are governed by the license agreement. The recipient of this software   */
/* implicitly accepts the terms of the license.                            */
/* 本软件文档资料是中国中药有限公司版权所有的资产，任何人士阅读和              		   */
/* 使用本资料必须获得相应的书面授权，承担保密责任和接受相应的法律约束。   		   */
/*                                                                         */
/***************************************************************************/
package com.weixin.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * 
 * @ClassName: PropertiesUtils
 * @Description: TODO Properties工具类
 * @author 陈周敏 chenzhoumin@china-tcm.com.cn
 * @date 2016年9月12日 上午10:09:52
 *
 */
public class PropertiesUtils {

	protected final static Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);
	private static final String INIT_PROPERTIES_FILES = "init.properties";
	private static Properties p = null;

	/***
	 * 
	 * @Title: read @Description: TODO 通过KEY取VAL @param @param
	 * key @param @return 设定文件 @return String 返回类型 @throws
	 */
	public static String read(String key) {
		loadProperties();
		if (p == null) {
			return null;
		}
		return p.getProperty(key);
	}

	/**
	 * @throws IOException @throws IOException *
	 * 
	 * @Title: loadProperties @Description: TODO 读取配置文件 @param 设定文件 @return void
	 * 返回类型 @throws
	 */
	private static void loadProperties() {
		InputStream in = null;
		try {
			if (p == null) {
				in = PropertiesUtils.class.getClassLoader().getResourceAsStream(INIT_PROPERTIES_FILES);
				p = new Properties();
				p.load(in);
			}
		} catch (IOException e) {
			LOGGER.error("PropertiesUtils loadProperties", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					LOGGER.error("PropertiesUtils loadProperties", e);
				}
			}
		}
	}

	public static void main(String[] args) {
		System.out.println(PropertiesUtils.read("customer.file.location"));
	}
}
