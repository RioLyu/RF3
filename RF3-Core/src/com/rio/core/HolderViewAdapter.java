package com.rio.core;

import java.lang.reflect.Constructor;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class HolderViewAdapter<T> extends BaseArrayAdapter<T> {

	private Class<? extends BaseHolderView<T>> mHolderViewClass;
	
	private Object[] mParam;

	public HolderViewAdapter(Class<? extends BaseHolderView<T>> cls, List<T> objects) {
		super(objects);
		mHolderViewClass = cls;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null && mHolderViewClass != null) {

			try {
				Class[] parameterTypes = { Context.class };				
				Constructor<? extends BaseHolderView<T>> constructor = mHolderViewClass
						.getConstructor(parameterTypes);
				Object[] parameters = { parent.getContext() };
				convertView = (BaseHolderView<T>) constructor.newInstance(parameters);
				onAttachHolderView(convertView);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (convertView != null && convertView instanceof BaseHolderView) {
			BaseHolderView<T> holderView = (BaseHolderView<T>) convertView;
			holderView.bindItemView(convertView, parent, position,
					getItem(position),mParam);

		}
		return convertView;
	}

	/**
	 * 生成convertView的时候调用
	 * @param convertView
	 */
	protected void onAttachHolderView(View convertView) {
	
	}

	public void setParam(Object... param) {
		mParam = param;
	}

	


}
