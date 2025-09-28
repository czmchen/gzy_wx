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

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/***
 * 
 * @ClassName: SpringUtil
 * @Description: TODO Spring工具类
 * @author 陈周敏 chenzhoumin@china-tcm.com.cn
 * @date 2016年9月12日 上午10:09:52
 *
 */
public class SpringUtil implements ApplicationContextAware {
	private static ApplicationContext ctx = null;

	public static Object getBean(String beanName) {
		if (ctx != null) {
			try {
				return ctx.getBean(beanName);
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ctx = applicationContext;
	}
}
