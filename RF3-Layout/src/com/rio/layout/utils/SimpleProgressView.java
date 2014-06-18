package com.rio.layout.utils;

import com.rio.layout.TaskManager;
import com.rio.layout.view.SimpleTask;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class SimpleProgressView extends RelativeLayout {

	public SimpleProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		ProgressBar bar = new ProgressBar(context);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(CENTER_IN_PARENT, TRUE);
		bar.setLayoutParams(params);
		addView(bar);
		hide();
		setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(getVisibility() == View.GONE){
					return false;
				}else{
					return true;
				}
			}
		});		
	}

	
	public void show(){
		
		TaskManager.getInstance().async(new ShowTask());
	}
	
	public void hide(){
		TaskManager.getInstance().async(new HideTask());
	}	

	
	private class ShowTask extends SimpleTask{
		@Override
		public void onUIThread(Object data, Object... params) throws Exception {
			setVisibility(View.VISIBLE);
		}
	}
	private class HideTask extends SimpleTask{
		@Override
		public void onUIThread(Object data, Object... params) throws Exception {
			setVisibility(View.GONE);
		}
	}
	
}
