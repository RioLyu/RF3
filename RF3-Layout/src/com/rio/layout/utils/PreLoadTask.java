package com.rio.layout.utils;

import com.rio.core.BaseEvent;
import com.rio.core.BaseEventListener;
import com.rio.core.U;


public abstract class PreLoadTask<T> extends BaseEventListener<T>{
	

	private PreLoadEvent<T> mFace;

	/**
	 * @param iface
	 * @param tag		键，与PostLoadTask对应
	 */
	public PreLoadTask(PreLoadEvent<T> iface,String tag) {
		super(iface);
		mFace = iface;
		setTag(tag);
	}

	protected void setTag(String tag) {
		mFace.setTag(tag);
	}
	
}
