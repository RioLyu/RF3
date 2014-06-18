package com.rio.layout;

import com.rio.core.L;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.Html.ImageGetter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

public abstract class IPlus implements GoBackWatcher{
		
	private Context mContext;
	
	private Context mHostContext;
	
	public void init(Context plus,Context host){
		mContext = plus;
		mHostContext = host;
	}
	
	public PlusManager getPlusManager(){
		return PlusManager.getInstance(mContext);
	}
	
	public Context getContext() {
		return mContext;
	}
	
	public Context getApplicationContext() {
		return mHostContext;
	}
	
	/**
	 * 获取插件的资源
	 * @return
	 */
	public AssetManager getAssets() {
		return getContext().getAssets();
	}

	/**
	 * 获取插件的资源
	 * @return
	 */
	public Resources getResources() {
		return getContext().getResources();
	}
	

	/**
	 * 绑定一个view
	 * @param layout
	 * @return
	 */
	public abstract View onAttach(LayoutInflater layout);
	
	/**
	 * Activity的onResume会调用当时顶部图层的onResume
	 */
	public abstract void onResume();
	
	/**
	 * Activity的onPause会调用当时顶部图层的onPause
	 */
	public abstract void onPause();
	
	/**
	 * 当释放时调用
	 */
	public void onDetach(){
		mContext = null;
		mHostContext = null;
	};
	
	/**  
	 * 当被叠加或被释放前调用
	 */
	public abstract void onHide(String nextName);
	
		
	/**
	 * 当 addView()或 goBack()就会被调用<br>
	 * 注意不能在这里调用LayoutManager的图层操作，如果需要的话请使用TaskManager
	 * 
	 * @param callerName 	等于是之前栈顶的Activity的相对类名
	 * @param view			当前视图
	 * @param frag			返回者的标志 通过该标志 说明不同的信息数组
	 * @param Intent		信息数组
	 */
	public abstract void onDisplay(String callerName, View view,Intent intent);
	


}
