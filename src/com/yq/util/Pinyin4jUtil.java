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
package com.yq.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * @author 李勇
 * @date 2016年9月6日 下午2:51:00
 * @Description 汉语转拼音处理
 */
public class Pinyin4jUtil {
	
	
	private static final char splitChar = 1;
	
	/**
	 * 
	 * @author 李勇
	 * @date 2016年9月7日 下午2:25:45
	 * @description 汉字转换位汉语简拼 + 全拼，英文字符不变，特殊字符丢失
	 *              支持多音字，生成方式如（重当参: zdc,cdc,cds,zds,zhongdangcen,zhongdangcan,chongdangcen,
	 *              chongdangshen,zhongdangshen,chongdangcan）；
	 *              并去除重复项
	 * @param chines
	 * @return
	 * @throws BadHanyuPinyinOutputFormatCombination
	 */
	public static String converterToALLAndFirstSpell(String chines) throws BadHanyuPinyinOutputFormatCombination{
		StringBuilder pinyinNameALL = new StringBuilder(1000); 
		StringBuilder pinyinNameFirst = new StringBuilder(500);
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {			
				// 取得汉字的所有全拼
				String[] strs = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat);
				if (strs != null) {
					for (int j = 0; j < strs.length; j++) {						
						pinyinNameALL.append(strs[j]);//保存当前汉字全拼
						pinyinNameFirst.append(strs[j].charAt(0));// 保存汉字拼音首字母
						if (j != strs.length - 1) {
							pinyinNameALL.append(",");
							pinyinNameFirst.append(",");
						}
					}
				}
			} else {
				pinyinNameALL.append(nameChar[i]);
				pinyinNameFirst.append(nameChar[i]);
			}
		    
			pinyinNameALL.append(String.valueOf(splitChar));
			pinyinNameFirst.append(String.valueOf(splitChar));
		}
		// return pinyinName.toString();
		StringBuilder resultSb = new StringBuilder(1500);
		resultSb.append(parseTheChineseByObject(discountTheChinese(pinyinNameFirst.toString())));
		resultSb.append(",");
		resultSb.append(parseTheChineseByObject(discountTheChinese(pinyinNameALL.toString())));
		return resultSb.toString();
	}
	

	/**
	 * 
	 * @author 李勇
	 * @date 2016年9月6日 下午2:56:02
	 * @description 汉字转换位汉语全拼，英文字符不变，特殊字符丢失
	 *              支持多音字，生成方式如（重当参:zhongdangcen,zhongdangcan,chongdangcen,
	 *              chongdangshen,zhongdangshen,chongdangcan）；
	 *              并去除重复项
	 * @param chines
	 *            汉字
	 * @return 拼音
	 * @throws BadHanyuPinyinOutputFormatCombination
	 */
	public static String converterToALLSpell(String chines) throws BadHanyuPinyinOutputFormatCombination {
		StringBuilder pinyinName = new StringBuilder(1000);
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
			
				// 取得当前汉字的所有全拼
				String[] strs = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat);
				if (strs != null) {
					for (int j = 0; j < strs.length; j++) {
						pinyinName.append(strs[j]);
						if (j != strs.length - 1) {
							pinyinName.append(",");
						}
					}
				}
			} else {
				pinyinName.append(nameChar[i]);
			}
		    
			pinyinName.append(String.valueOf(splitChar));
		}
		// return pinyinName.toString();
		return parseTheChineseByObject(discountTheChinese(pinyinName.toString()));
	}

	/**
	 * 
	 * @author 李勇
	 * @date 2016年9月6日 下午2:53:22
	 * @description 汉字转换位汉语首字母拼音（简拼），英文字符不变，特殊字符丢失
	 *              支持多音字，生成方式如（重当参:zdc,cdc,cds,zds）
	 *              并去除重复项
	 * @param chines
	 * @return
	 */
	public static String converterToFirstSpell(String chines) throws BadHanyuPinyinOutputFormatCombination {
		StringBuilder pinyinName = new StringBuilder(500);
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {

				// 取得当前汉字的所有全拼
				String[] strs = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat);
				if (strs != null) {
					for (int j = 0; j < strs.length; j++) {
						// 取首字母
						pinyinName.append(strs[j].charAt(0));
						if (j != strs.length - 1) {
							pinyinName.append(",");
						}
					}
				}

			} else {
				pinyinName.append(nameChar[i]);
			}
			pinyinName.append(String.valueOf(splitChar));
		}
		return parseTheChineseByObject(discountTheChinese(pinyinName.toString()));
	}

	/**
	 * 
	 * @author 李勇
	 * @date 2016年9月6日 下午3:03:44
	 * @description 去除多音字重复拼音
	 * @param theStr
	 * @return
	 */
	private static List<Map<String, Integer>> discountTheChinese(String theStr) {
		// 去除重复拼音后的拼音列表
		List<Map<String, Integer>> mapList = new ArrayList<Map<String, Integer>>();
		// 用于处理每个字的多音字，去掉重复
		Map<String, Integer> onlyOne = null;
		String[] firsts = theStr.split(String.valueOf(splitChar));
		// 读出每个汉字的拼音
		for (String str : firsts) {
			onlyOne = new Hashtable<String, Integer>();
			String[] chinapy = str.split(",");
			// 多音字处理
			for (String key : chinapy) {
				if(!onlyOne.containsKey(key)){
					onlyOne.put(key, 1);
				}
			}
			mapList.add(onlyOne);
		}
		return mapList;
	}

	
	/**
	 * 
	 * @author 李勇
	 * @date 2016年9月6日 下午4:26:09
	 * @description 解析并组合拼音
	 * @param list
	 * @return
	 */
	private static String parseTheChineseByObject(List<Map<String, Integer>> list) {
		Map<String, Integer> first = new Hashtable<String, Integer>(200); // 用于组合中文字符串对应的拼音字符串
		for (int i = 0; i < list.size(); i++) {
			//tmp用于遍历循环中文字符串中的每个中文对应的拼音，并用于拼接
			Map<String, Integer> temp = new Hashtable<String, Integer>(200);
			if (first.size() == 0) {
				for (String s : list.get(i).keySet()) {
					String str = s;
					temp.put(str, 1);
				}
			} else {
				// 取出上次组合与此次集合的字符，并保存
				for (String key : first.keySet()) {
					for (String s1 : list.get(i).keySet()) {
						String str = key + s1;
						temp.put(str, 1);
					}
				}
				
				// 情况上一次数据
				if (temp != null && temp.size() > 0) {
					first.clear();
				}
			}
			// 保存组合数据以便下次循环使用
			if (temp != null && temp.size() > 0) {
				first = temp;
			}
		}
		StringBuilder returnSb = new StringBuilder(600);
		if (first != null) {
			// 遍历取出组合字符串
			for (String str : first.keySet()) {
				returnSb.append(str);
				returnSb.append(",");
			}
		}
		String returnStr = returnSb.toString();
		if ( returnStr.length() > 0) {
			returnStr = returnStr.substring(0, returnStr.length() - 1);
		}
		return returnStr;
	}

	
	public static void main(String[] args) throws BadHanyuPinyinOutputFormatCombination {
		System.out.println(converterToFirstSpell("~"));
	}
}
