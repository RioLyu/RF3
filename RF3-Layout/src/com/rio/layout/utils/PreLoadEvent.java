package com.rio.layout.utils;

import com.rio.core.BaseEvent;
import com.rio.core.BaseEventListener;
import com.rio.core.L;
import com.rio.core.U;
import com.rio.helper.Sleeper;
import com.rio.layout.PendingManager;
import com.rio.layout.view.SimpleTask;

public abstract class PreLoadEvent<T> implements BaseEvent<T> {
	
	public static final int PRELOAD_FAIL = 11;
	public static final int PRELOAD_SUCCESS = 12;
	public static final int PRELOAD_LOADING = 13;

	private String mTag;

	private PendingManager mPendingManager;

	private String getTag() {
		if (U.isNull(mTag)) {
			setTag(U.getName(getClass()));
		}
		return mTag;
	}

	public void setTag(String tag) {
		mTag = tag;
		mPendingManager = PendingManager.getInstance();
		mPendingManager.addInt(mTag, PRELOAD_LOADING);
	}


	@Override
	public void onFail(Object data, Exception exception) {
		if (U.notNull(mPendingManager))
			mPendingManager.addInt(getTag(), PRELOAD_FAIL);
		
	}
	@Override
	public void onSuccess(T data) {
		if (U.notNull(mPendingManager)) {
			String tag = getTag();
			// L.ii(tag);
			if (U.notNull(data)) {
				mPendingManager.addInt(tag, PRELOAD_SUCCESS);
				mPendingManager.addObj(tag, data);
			} else {
				mPendingManager.addInt(tag, PRELOAD_FAIL);
			}
		}
	}

	


	
}
