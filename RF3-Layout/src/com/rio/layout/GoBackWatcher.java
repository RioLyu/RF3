package com.rio.layout;

import android.app.Activity;

/**
 * 当前activity的onGoBack时触发
 * 
 * @author rio
 *
 */
public interface GoBackWatcher {
	
	/**
	 * 当调用goBack或按返回键的时候，返回false表示不拦截，返回true表示不返回;
	 * 
	 * @return
	 */
	public boolean onGoBack();
	
}
