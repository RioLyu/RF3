package com.rio.core;

import java.util.logging.FileHandler;

import com.rio.helper.file.FileHelper;

import android.app.Application;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;

/**
 * 全局参数 记得在application填上android:name=".xxx"
 * 
 * @author rio
 * 
 */
public class BaseApplication extends Application {

	/**
	 * 应用上下文
	 */
	private static Context mContext;

	/**
	 * 屏幕的像素
	 */
	private static int mScreenWidth;

	/**
	 * 屏幕的像素
	 */
	private static int mScreenHeight;

	/**
	 * 屏幕的密度
	 */
	private static float mDensity;

	/**
	 * 资源
	 */
	private static Resources mRes;

	/**
	 * 渲染器
	 */
	private static LayoutInflater mInflater;

	/**
	 * 状态栏高度
	 */
	private static int mStatusHeight;

	/**
	 * SDK版本
	 */
	private static int mVersion;

	@Override
	public void onCreate() {
		if (mContext == null)
			mContext = getApplicationContext();
		if (mRes == null)
			mRes = getResources();
		if (mInflater == null)
			mInflater = LayoutInflater.from(mContext);
		mVersion = android.os.Build.VERSION.SDK_INT;
		measureScreen();
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		mContext = null;
		mRes = null;
		super.onTerminate();
	}

	/**
	 * 计算屏幕长宽
	 */
	public void measureScreen() {
		DisplayMetrics dm = getResources().getDisplayMetrics();
		mDensity = dm.density;
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
		mStatusHeight = U.getStatusHeight(mContext);
	}

	/**
	 * 设置应用标识
	 * 
	 * @return
	 */
	public void setTag(String tag) {
		L.TAG = tag;
		FileHelper.setDirectory(tag);
	};

	/**
	 * 设置应用标识
	 * 
	 * @return
	 */
	public void setDebug(boolean enable) {
		L.DEBUG = enable;
	};

	;

	/**
	 * 获取屏幕宽
	 * 
	 * @return
	 */
	public static int getScreenWidth() {
		return mScreenWidth;
	}

	/**
	 * 获取状态栏高度
	 * 
	 * @return
	 */
	public static int getStatusHeight() {
		return mStatusHeight;
	}

	/**
	 * 获取屏幕长
	 * 
	 * @return
	 */
	public static int getScreenHeight() {
		return mScreenHeight;
	}

	/**
	 * 获取屏幕密度
	 * 
	 * @return
	 */
	public static float getDensity() {
		return mDensity;
	}

	/**
	 * 获取上下文
	 * 
	 * @return
	 */
	public static Context getContext() {
		return mContext;
	}

	/**
	 * 获取资源
	 * 
	 * @return
	 */
	public static Resources getRes() {
		return mRes;
	}

	/**
	 * 获取渲染器
	 * 
	 * @return
	 */
	public static LayoutInflater getInflater() {
		return mInflater;
	}

	/**
	 * 转换dip为px
	 * 
	 * @param dip
	 * @return
	 */
	public static int convertToPx(int dip) {
		return (int) (dip * mDensity + 0.5f * (dip >= 0 ? 1 : -1));
	}

	/**
	 * 转换px为dip
	 * 
	 * @param px
	 * @return
	 */
	public static int convertToDip(int px) {
		return (int) (px / mDensity + 0.5f * (px >= 0 ? 1 : -1));
	}

	/**
	 * 获取颜色
	 * 
	 * @param color
	 * @return
	 */
	public static ColorStateList getColor(int color) {
		return getRes().getColorStateList(color);
	}

	/**
	 * 获取字符串
	 * 
	 * @param color
	 * @return
	 */
	public static String getStr(int res) {
		return getRes().getString(res);
	}

	/**
	 * 获取字符串
	 * 
	 * @param color
	 * @return
	 */
	public static String getStr(int res, Object... params) {
		return getRes().getString(res, params);
	}

	/**
	 * 获取SDK版本号
	 * 
	 * @return
	 */
	public static int getSDKVersion() {
		return mVersion;
	}

	
	public static Object getService(String name){
		return getContext().getSystemService(name);
	};
}
