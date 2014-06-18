package com.rio.layout;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

public interface IWindow extends GoBackWatcher{

	/**
	 * 当 addView()或 goBack()就会被调用<br>
	 * 注意不能在这里调用LayoutManager的图层操作，如果需要的话请使用TaskManager
	 * 
	 * @param callerName 	等于是之前栈顶的Activity的相对类名
	 * @param view			当前视图
	 * @param frag			返回者的标志 通过该标志 说明不同的信息数组
	 * @param params		信息数组
	 */
	public void onDisplay(String callerName, View view, Object... params);
			
	/**
	 * 绑定一个view
	 * @param layout
	 * @return
	 */
	public View onAttach(LayoutInflater layout, Object... params);
	
	/**
	 * Activity的onResume会调用当时顶部图层的onResume
	 */
	public void onResume();
	
	/**
	 * Activity的onPause会调用当时顶部图层的onPause
	 */
	public void onPause();
	
	/**
	 * 当释放时调用
	 */
	public void onDetach();
	

	/**
	 * 侦听onActivityResult事件
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data);
	
	/**
	 * 进场动画
	 * @return
	 */
	public Animation getAnimation();
	

	
}
