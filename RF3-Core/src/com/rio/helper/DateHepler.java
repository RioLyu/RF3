package com.rio.helper;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * @author rio
 * @version 1.0
 */
public class DateHepler {

	private Calendar mRightNow;
	private int mFirstDayOfWeek;

	public DateHepler(){
		mRightNow = Calendar.getInstance();		
	}
	
	public String now(){
		return String.valueOf(mRightNow.getTimeInMillis());	
	}
	
	public  String yesterday(){
		Calendar now = (Calendar) mRightNow.clone();
		now.add(Calendar.HOUR_OF_DAY,-24);
		now.set(Calendar.HOUR_OF_DAY ,0);
		now.set(Calendar.MINUTE,0);
		now.set(Calendar.SECOND,0);	
		return String.valueOf(now.getTimeInMillis());		
	}
	
	public  String today(){
		Calendar now = (Calendar) mRightNow.clone();
		now.set(Calendar.HOUR_OF_DAY ,0);
		now.set(Calendar.MINUTE,0);
		now.set(Calendar.SECOND,0);	
		return String.valueOf(now.getTimeInMillis());
		
	}
	
	public  String thisMonday(){
		Calendar now = (Calendar) mRightNow.clone();		
		now.setFirstDayOfWeek(Calendar.SUNDAY);
		now.set(Calendar.DAY_OF_WEEK , 2);
		now.set(Calendar.HOUR_OF_DAY ,0);
		now.set(Calendar.MINUTE,0);
		now.set(Calendar.SECOND,0);
		return String.valueOf(now.getTimeInMillis());		
	}
	
	public String tomorrow(){
		Calendar now = (Calendar) mRightNow.clone();
		now.add(Calendar.HOUR_OF_DAY,24);
		now.set(Calendar.HOUR_OF_DAY ,0);
		now.set(Calendar.MINUTE,0);
		now.set(Calendar.SECOND,0);	
		return String.valueOf(now.getTimeInMillis());	
	}

	public  String threeDaysAgo(){
		Calendar now = (Calendar) mRightNow.clone();
		now.add(Calendar.HOUR_OF_DAY,-72);
		now.set(Calendar.HOUR_OF_DAY ,0);
		now.set(Calendar.MINUTE,0);
		now.set(Calendar.SECOND,0);	
		return String.valueOf(now.getTimeInMillis());			
	}

	public String dayBerforeYesterday(){
		Calendar now = (Calendar) mRightNow.clone();
		now.add(Calendar.HOUR_OF_DAY,-48);
		now.set(Calendar.HOUR_OF_DAY ,0);
		now.set(Calendar.MINUTE,0);
		now.set(Calendar.SECOND,0);	
		return String.valueOf(now.getTimeInMillis());			
	}
	
	
	public long getTimeAfter(int day){
		Calendar now = (Calendar) mRightNow.clone();
		now.add(Calendar.HOUR_OF_DAY,(24 * day));
		now.set(Calendar.HOUR_OF_DAY,10);
		now.set(Calendar.MINUTE,0);
		now.set(Calendar.SECOND,0);
		
		return now.getTimeInMillis();
	}
	
	public Integer[] getCountDownTime(long millisUntilFinished){
		long time = millisUntilFinished/1000;
        int d = (int) Math.floor((time / 86400));
        int h = (int) Math.floor(((time - (d * 86400)) / 3600));
        int m = (int) Math.floor(((time - (d * 86400) - (h * 3600)) / 60));
        int s = (int) Math.floor(((time - (d * 86400) - (h * 3600) - (m * 60) )));        
        return  new Integer[]{d ,h,m,s};      
	};
	

	/**
	 * 转格式
	 * @param sdate
	 * @param format
	 * @return
	 */
	public static String dateFormat(String sdate, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		java.sql.Date date = java.sql.Date.valueOf(sdate);
		String dateString = formatter.format(date);

		return dateString;
	}
	
	/**
	 * 计算相隔多少天
	 * @param sd
	 * @param ed
	 * @return
	 */
	public static long getIntervalDays(String sd, String ed) {
		return ((java.sql.Date.valueOf(ed)).getTime() - (java.sql.Date.valueOf(sd)).getTime()) / (3600 * 24 * 1000);
	}
	
	/**
	 * 字符串获取Date对象
	 * @param sDate
	 * @param dateFormat
	 * @return
	 */
	public static Date getDate(String sDate, String dateFormat) {
		SimpleDateFormat fmt = new SimpleDateFormat(dateFormat);
		ParsePosition pos = new ParsePosition(0);

		return fmt.parse(sDate, pos);
	}
}
