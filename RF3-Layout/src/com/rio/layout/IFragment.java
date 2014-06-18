package com.rio.layout;

import com.rio.core.ITaskReceiver;

import android.app.Activity;
import android.util.AttributeSet;
import android.view.View;

/**
 * 支持组件式开发的接口，同一个Activity内组件之间的通讯
 * @author rio
 *
 */
public interface IFragment extends GoBackWatcher{
	
	/**
	 * 从管理器内删除
	 */
	public void onDetach();
	
	/**
	 * 添加到管理器，请不要在这里注册，否则会死循环
	 */
	public void onAttach();
	
	/**
	 * 标志，如果为空，默认使用类名
	 * @param params
	 * @return
	 */
	public String getFlag();
	
	/**
	 * 当 addView()或 goBack()就会被调用<br>
	 * 最好获取同一容器下的IFragment索引
	 * @param content		当前容器的Activity
	 * @param callerName 	等于是之前栈顶的Activity的相对类名
	 * @param frag			返回者的标志 通过该标志 说明不同的信息数组
	 * @param params		信息数组
	 */
	public void onDisplay(String callerName, int frag,
			Object... params);

	/**
	 * Activity的onPause会调用当时顶部图层的onPause前触发
	 */
	public void onPause();

	/**
	 * Activity的onResume会调用当时顶部图层的onResume后触发
	 */
	public void onResume();

}
