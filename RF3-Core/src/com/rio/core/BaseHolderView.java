package com.rio.core;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public abstract class BaseHolderView<T> extends FrameLayout {

	public BaseHolderView(Context context) {
		super(context);
		View view = onAttach(context);
		if(view != null)addView(view);
	}
	
	public abstract View onAttach(Context context);

	public abstract void bindItemView(View view,ViewGroup parent,int position,T item,Object...parObjects);
	
}
