package com.rio.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

/**
 * <p>
 * 用于以单例类方式使用{@link SharedPreferences},并提供一些工具方便访问与写入
 * </p>
 * @author rio
 * @version 1
 */
public class PreferenceHelper {
	
	private static PreferenceHelper mHelper;
	
	private SharedPreferences mPreference;

	private PreferenceHelper(Context context){
		mPreference = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	/**
	 * 取得单例
	 * @param context
	 * @return SharedPreferences 实例
	 */
	public static PreferenceHelper create(Context context){
		if(mHelper == null)mHelper = new PreferenceHelper(context);		
		return mHelper;
	}
	
	/**
	 * 读取字符串配置<br>
	 * 首先检验键是否为空，然后检验取出的配置值是否为空，都为空的情况下返回默认值
	 * @param key 键
	 * @param defValue	默认值
	 * @return 
	 */
	public String getString(String key, String defValue){
		String value = defValue;
		if(!TextUtils.isEmpty(key))
		{
			value = mPreference.getString(key, defValue);
			value = value==null ? "" : value;
		}
		return value;
	}
	
	/**
	 * 读取布尔值配置<br>
	 * 首先检验键是否为空，为空的情况下返回默认值
	 * @param key 键
	 * @param defValue	默认值
	 * @return
	 */
	public boolean getBoolean(String key,boolean defValue)
	{ 
		boolean value = defValue;
		if(!TextUtils.isEmpty(key))
		{
			value = mPreference.getBoolean(key, defValue);
		}
		return value;
	}
	
	/**
	 * 读取整形值配置<br>
	 * 首先检验键是否为空，为空的情况下返回默认值
	 * @param key 键
	 * @param defValue	默认值
	 * @return
	 */
	public int getInt(String key,int defValue)
	{ 
		int value = defValue;
		if(!TextUtils.isEmpty(key))
		{
			value = mPreference.getInt(key, defValue);
		}
		return value;
	}
	/**
	 * 写入字符串配置到SharedPreferences
	 * 首先检验键是否为空，为空的情况下无操作
	 * @param key 键
	 * @param value 输入值
	 */
	public void putString(String key,String value)
	{ 
		if(!TextUtils.isEmpty(key))
		{
			mPreference.edit().putString(key, value).commit();
		}
	}
	/**
	 * 写入布尔值配置到SharedPreferences
	 * @param key 键
	 * @param value 输入值
	 */	
	public void putBoolean(String key,boolean value)
	{ 

		mPreference.edit().putBoolean(key, value).commit();

	}
	/**
	 * 写入整形配置到SharedPreferences
	 * @param key 键
	 * @param value 输入值
	 */		
	public void putInt(String key,int value)
	{ 

		mPreference.edit().putInt(key, value).commit();

	}	
	
	/**
	 * 检验SharedPreferences是否有这个键
	 * @param key 键
	 * @return
	 */
	public boolean contains(String key){
		return mPreference.contains(key);
	}
	/**
	 * 删除这个键
	 * @param key 键
	 * @return 是否成功删除
	 */
	public boolean remove(String key){
		return mPreference.edit().remove(key).commit();
	}
}

