package com.rio.core;

import android.test.IsolatedContext;

public  abstract class BaseEventListener<T> implements BaseTaskListener {

	private BaseEvent<T> mFace;
	
	/**
	 * 事件触发的时候调用
	 */
	protected abstract void onEventStart() ;
	
	/**
	 * 后台数据已处理的时候调用
	 */
	protected abstract void onEventFinish() ;
	
	/**
	 * 后台已处理数据
	 */
	protected abstract T onEvent(Object... params) throws Exception ;	
	
	public BaseEventListener(BaseEvent<T> iface) {
		mFace = iface;	
		onEventStart();
	}
	
	@Override
	public Object onBGThread(Object... params) throws Exception {	
		return onEvent(params);
	}
	
	@Override
	public void onUIThread(Object data, Object... params) throws Exception {
		onEventFinish();
		if (U.notNull(mFace)) {
			if(U.notNull(data)){
				try {
					T obj = (T)data;
					mFace.onSuccess(obj);						
				} catch (Exception e) {
					mFace.onFail(data,e);
				}	
			}else{
				mFace.onFail(data,null);
			}
		}
	}

	@Override
	public void onException(Exception exception, Object... params) {
		onEventFinish();
		if (U.notNull(mFace))
			mFace.onFail(null,exception);
	}



}
