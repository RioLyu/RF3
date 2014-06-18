package com.rio.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class BaseView extends View {

	protected int height;
	protected int width;
	
	public BaseView(Context context) {
		super(context);
	}
	

	public BaseView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		height = View.MeasureSpec.getSize(heightMeasureSpec); 
		width = View.MeasureSpec.getSize(widthMeasureSpec); 
		setMeasuredDimension(width,height); 
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
