package com.rio.layout.view;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import com.rio.layout.ILayout;
import com.rio.layout.IWindow;
import com.rio.layout.PopupManager;
import com.rio.layout.TaskManager;

public  class SimpleWindow implements IWindow {


	/**
	 * 隐藏弹出框
	 */
	public void hide(Object...params){
		PopupManager.getInstance().hideWindow(params);
		
	}

	/**
	 * 显示弹出框
	 * @param params
	 */
	public void show(Object...params){
		PopupManager.getInstance().show(this, params);
	}

	@Override
	public boolean onGoBack() {
		hide();
		return true;
	}

	@Override
	public void onDisplay(String callerName, View view, Object... params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View onAttach(LayoutInflater layout, Object... params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Animation getAnimation() {
		// TODO Auto-generated method stub
		return null;
	}


}
