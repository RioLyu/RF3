package com.rio.layout.utils;

import com.rio.layout.view.SimpleFragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;



/**
 * 遮盖所属区域的触摸事件
 * 
 * @author rio
 *
 */
public abstract class TouchFragment extends SimpleFragment implements OnTouchListener{
	
	private boolean isPrevent;

	public TouchFragment(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnTouchListener(this);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {		
		return isPrevent;
	}
	
	protected void preventTouch(boolean prevent) {
		isPrevent = prevent;
	}
	
	public void show(){
		isPrevent = true;
		setVisibility(View.VISIBLE);
	}
	
	public void hide(){
		isPrevent = false;
		setVisibility(View.GONE);
	}

	
	public void toggle(){
		if(getVisibility()==View.GONE){
			show();
		}else{
			hide();
		}		
	}
}
