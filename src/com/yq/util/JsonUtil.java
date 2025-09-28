package com.yq.util;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * 
 * @ClassName: JsonUtil
 * @Description: TODO Json
 * @author 陈周敏 zmchen@biznest.cn
 * @date 2014年9月23日 上午11:23:48
 *
 */
public final class JsonUtil {

	/***
	 * 
	 * @Title: list2Json
	 * @Description: TODO 将List转换为json
	 * @param @param list
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String list2JSON(List<?> list) {
		String result = JSON.toJSONString(list);
		return result;
	}

	/**
	 * 
	 * @Title: obj2JSON
	 * @Description: TODO 将Object转换为json
	 * @param @param object
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String obj2JSON(Object object) {
		String result = JSON.toJSONString(object);
		return result;
	}

	/**
	 * 
	 * @Title: json2Entity
	 * @Description: TODO 将json转换为Entity
	 * @param @param text
	 * @param @return 设定文件
	 * @return T 返回类型
	 * @throws
	 */
	public static <T> T json2Entity(String text, Class<T> entityClass) {
		T obj = (T) JSON.parseObject(text, entityClass);
		return obj;
	}

	/***
	 * 
	* @Title: json2List 
	* @Description: TODO json2List
	* @param @param text
	* @param @return  设定文件 
	* @return List<?> 返回类型 
	* @throws
	 */
	public static List<?> json2List(String text) {
		List<?> list = JSON.parseObject(text, new TypeReference<List<?>>() {});
		return list;
	}
	
	/***
	 * 
	* @Title: json2List 
	* @Description: TODO json2List
	* @param @param text
	* @param @return  设定文件 
	* @return List<?> 返回类型 
	* @throws
	 */
	@SuppressWarnings("rawtypes")
	public static Map json2Map(String text) {
		return JSON.parseObject(text, new TypeReference<Map>() {});
	}
	
	public static void main(String[] args) {
	}
	
}


