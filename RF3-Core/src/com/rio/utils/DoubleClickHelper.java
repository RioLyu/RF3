package com.rio.utils;

import android.content.Context;

import com.rio.core.U;

/**
 * 双击事件
 * 
 * @author rio
 *
 */
public class DoubleClickHelper {

	
	private static DoubleClickHelper mInstance;

	private long exitTime;

	private DoubleClickHelper() {

	}

	/**
	 * 开始运行,返回true为第一次点击，返回false为第二次点击
	 * @param mListener
	 * @param deylay 		两次点击相隔时间
	 * @return
	 */
	public static boolean start(OnDoubleClickListener  mListener,long deylay) {
		if(U.notNull(mListener)){
			DoubleClickHelper helper = getInstance();
			if ((System.currentTimeMillis() - helper.exitTime) > deylay) {
				mListener.onFirstClick();
				helper.exitTime = System.currentTimeMillis();
				return true;
			}else{
				mListener.onSecondClick();
			}		
		}
		return false;
	}
	
	private static DoubleClickHelper getInstance(){
		if(U.isNull(mInstance)){
			mInstance = new DoubleClickHelper();
		}
		return mInstance;
		
	}


}
