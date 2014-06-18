package com.rio.layout.utils;

import com.rio.core.BaseEvent;
import com.rio.core.BaseEventListener;
import com.rio.core.BaseTaskListener;
import com.rio.core.L;
import com.rio.core.U;
import com.rio.helper.Sleeper;
import com.rio.layout.PendingManager;
import com.rio.layout.view.SimpleTask;

public abstract class PostLoadTask<T> extends BaseEventListener<T> {
	
	private boolean isPop;
	
	private String mTag;

	private PostLoadTask(BaseEvent<T> iface) {
		super(iface);
	}
	
	/**
	 * @param iface
	 * @param tag
	 * @param isPop		是否读完后删除 默认不删除
	 */
	public PostLoadTask(BaseEvent<T> iface,String tag,boolean isPop) {
		super(iface);
		this.isPop = isPop;
		mTag = tag;
	}
	
	/**
	 * @param iface
	 * @param tag		键，与PreLoadTask对应
	 */
	public PostLoadTask(BaseEvent<T> iface,String tag) {
		this(iface,tag,false);
	}

	
	/**
	 * 是否读完后删除 默认不删除
	 * @param isPop
	 */
	public PostLoadTask<T> setPop(boolean isPop) {
		this.isPop = isPop;
		return this;
	}


	@Override
	protected T onEvent(Object... params) throws Exception {
		return readData();
	}

	
	private T readData() throws Exception {
		T data = null;
		int state = PendingManager.NONE;
		PendingManager mPendingManager = PendingManager.getInstance();
		while (true) {
			state = mPendingManager.getInt(mTag);
			if (state == PreLoadEvent.PRELOAD_SUCCESS) {
				//读完后删除
				if(isPop){
					data = (T) mPendingManager.popObj(mTag);
				}else{
					data = (T) mPendingManager.getObj(mTag);
				}
				break;
			}
			if (state == PreLoadEvent.PRELOAD_FAIL) {
				break;
			}
			if (state == PendingManager.NONE || state == PreLoadEvent.PRELOAD_LOADING) {
				Sleeper.sleep();
			}
		}
		return data;
	}

	
}
