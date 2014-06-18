package com.rio.utils;

import java.util.List;

import android.content.Context;

import com.rio.core.L;
import com.rio.core.U;
import com.rio.helper.sql.SimpleSQLHelper;

/**
 * 有时效的内部存储
 * 
 * @author rio
 *
 */
public class TimelinePreference extends SimpleSQLHelper<Timeliness> {
	
	private static final String T_NAME = "base_timeliness_string_db"; 
	private static final int T_VERSION = 1; 
	
	private static TimelinePreference mInstance;

	private TimelinePreference(Context context) {
		super(context, T_NAME, T_VERSION, Timeliness.class);
	}
	
	private static TimelinePreference getInstance(Context context){
		if(U.isNull(mInstance)){
			mInstance = new TimelinePreference(context);
		}
		return mInstance;
	}

	/**
	 * 获取某值
	 * @param context
	 * @param key
	 * @return
	 */
	public static String get(Context context,String key){
		Timeliness s = new Timeliness();
		s.key = key;
		try {
			List<Timeliness> list = getInstance(context).find(s);
			if(U.notNull(list) && !list.isEmpty()){
				s =  list.get(0);
				if(U.notNull(s)){
					if(System.currentTimeMillis() <= Long.valueOf(s.deadline)){
						return s.value;	
					}else{
						getInstance(context).remove(s);
					}			
				}
			}else{
				return null;
			}			
		} catch (Exception e) {
			L.e(e);
			return null;
		} 
		return null;
	}
	
	/**
	 * 保存某值
	 * @param context
	 * @param key
	 * @param value
	 * @param deadline 多久之后失效 n*24*60*60*1000
	 */
	public static void set(Context context,String key,String value,long deadline){
		Timeliness s = new Timeliness();
		s.key = key;
		try {
			List<Timeliness> list = getInstance(context).find(s);
			if(U.notNull(list) && !list.isEmpty()){
				s =  list.get(0);
			}else{
				s = null;
			}
		} catch (Exception e) {
			L.e(e);
			s = null;
		} 
		if(U.notNull(s) && U.notNull(s.value)){			
			s.deadline = String.valueOf(System.currentTimeMillis()+deadline);
			try {
				getInstance(context).update(s);
			} catch (Exception e) {
				L.e(e);
			}
		}else{
			s = new Timeliness();
			s.key = key;
			s.deadline = String.valueOf(deadline);
			s.value = value;
			getInstance(context).insert(s);
		}		
			
	}
	
	
}
