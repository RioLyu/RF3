package com.rio.layout;

import android.app.Activity;

/**
 * 栈底
 * 
 * @author rio
 *
 */
public interface IBackground {
	
	
	/**
	 * 当加载时调用	
	 */
	public ILayout onAttach();	
	
	/**
	 * 当释放时调用
	 */
	public void onDetach();
	
	/**
	 * 通知该栈被提到前台	
	 * @param current 当前栈顶Activity
	 * @param index
	 */
	public void onDisplayChild(String childName,int index);
	
	/**
	 * 处理在栈底的Activity按返回事件,默认是FALSE,调用Activity.finish()
	 */
	public boolean onKeyBackHome();
	
	/**
	 * 处理菜单事件
	 */
	public void onKeyMenu();

	/**
	 * 截获onResume事件
	 * @return
	 */
	public boolean onResume();

	/**
	 * 截获onPause事件
	 * @return
	 */
	public boolean onPause();
}
