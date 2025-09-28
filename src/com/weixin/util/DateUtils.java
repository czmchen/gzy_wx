package com.weixin.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/***
 * 
 * @ClassName: DateUtils
 * @Description: TODO时间处理工具
 * @author 陈周敏 zmchen@biznest.cn
 * @date 2014年12月4日 下午3:59:44
 *
 */
public class DateUtils {

	public static final String DATETIME_FORMATE = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMATE = "yyyy-MM-dd";
	public static final String TIME_FORMATE = "HH:mm:ss";

	public static String plusOrMinusDate(int dayNum, String timeZone) {
		Calendar rightNow = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(StringUtil.isRealEmpty(timeZone) ? DATE_FORMATE : timeZone);
		// 得到当前时间，+3天
		// rightNow.add(java.util.Calendar.DAY_OF_MONTH, 3);
		// 如果是后退几天，就写 -天数 例如：
		rightNow.add(java.util.Calendar.DAY_OF_MONTH, dayNum);

		return sdf.format(rightNow.getTime());
	}

	public static String plusOrMinusDateWithoutWeekend(int dayNum, String timeZone) {

		Calendar rightNow = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(StringUtil.isRealEmpty(timeZone) ? DATE_FORMATE : timeZone);
		// 得到当前时间，+3天
		// rightNow.add(java.util.Calendar.DAY_OF_MONTH, 3);
		// 如果是后退几天，就写 -天数 例如：
		rightNow.add(java.util.Calendar.DAY_OF_MONTH, dayNum);

		// 进行时间转换
		if (rightNow.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			rightNow.add(java.util.Calendar.DAY_OF_MONTH, 2);
		} else if (rightNow.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			rightNow.add(java.util.Calendar.DAY_OF_MONTH, 1);
		} else {
//			System.out.println("workDate");
		}

		return sdf.format(rightNow.getTime());
	}
	
	public static String plusOrMinusDateWithoutWeekend(String date,int dayNum, String timeZone) throws Exception {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(new SimpleDateFormat(DATE_FORMATE).parse(date));
		SimpleDateFormat sdf = new SimpleDateFormat(StringUtil.isRealEmpty(timeZone) ? DATE_FORMATE : timeZone);
		// 得到当前时间，+3天
		// rightNow.add(java.util.Calendar.DAY_OF_MONTH, 3);
		// 如果是后退几天，就写 -天数 例如：
		rightNow.add(java.util.Calendar.DAY_OF_MONTH, dayNum);

		// 进行时间转换
		if (rightNow.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			rightNow.add(java.util.Calendar.DAY_OF_MONTH, 2);
		} else if (rightNow.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			rightNow.add(java.util.Calendar.DAY_OF_MONTH, 1);
		} else {
//			System.out.println("workDate");
		}

		return sdf.format(rightNow.getTime());
	}

	public static String nowDate(String timeZone) {
		SimpleDateFormat sdf = new SimpleDateFormat(StringUtil.isRealEmpty(timeZone) ? DATE_FORMATE : timeZone);
		return sdf.format(new Date());
	}

	public static void main(String[] args) throws Exception {
		// 开始前X天的数据采集
		/*for (int i = 0; i > -30; i--) {// 把前X天的数据都采集一次
			String beforeDay = DateUtils.plusOrMinusDateWithoutWeekend(DateUtils.nowDate(null),i, "yyyy-MM-dd");
			System.out.println(beforeDay);
		}*/
		
        System.out.println(DateUtils.nowDate(DateUtils.TIME_FORMATE).indexOf("00:00:")!=-1);
		/*
		
		System.out.println(DateUtils.nowDate("HH:mm").equals("00:01"));
		String str2 = "autoStartCollet:08:20";
		String[] st = str2.split(":");
		System.out.println("");
		Set<String> date = new HashSet<String>();
		for(int i=0;i>-32;i--){
			date.add(plusOrMinusDateWithoutWeekend("2018-07-19",i, "yyyyMMdd"));
		}
		for(String str : date){
			System.out.println(str);
		}*/
	}
}
