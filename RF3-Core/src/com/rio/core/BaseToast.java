package com.rio.core;



import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public class BaseToast {

	private Toast mToast;
	private View mRoot;

	public BaseToast(Context context,int resoure,int gravity) {
		mToast = new Toast(context);
		if(U.notNull(gravity))
			mToast.setGravity(gravity,0,0);
		mRoot = LayoutInflater.from(context).inflate(resoure, null);
		mToast.setView(mRoot);
	}

	public void show(){
		mToast.show();
	}
	
	public void cancel(){
		mToast.cancel();
	}
	
	public View getRoot() {
		return mRoot;
	}

	public void setDuration(int length){
		if(U.notNull(mToast))
			mToast.setDuration(length);
	}
	
	
	
	
}
