package com.rio.layout.utils;

import com.rio.core.U;
import com.rio.layout.TaskManager;
import com.rio.layout.view.SimpleTask;


/**
 * 有进度功能的线程
 * 
 * @author rio
 *
 */
public class SimpleProgressTask extends SimpleTask {

	private TaskManager mManager;
	
	private boolean isPause;


	public boolean isRunning() {
		return !isPause;
	}

	public void stop() {
		this.isPause = true;
	}

	/**
	 * 一个进度在UI界面的刷新
	 * @param params
	 */
	protected void onUIProgress(Object data,Object... params) {

	}
	
	/**
	 * 一个进度在副线程的计算
	 * @param params
	 * @return
	 */
	protected Object onBGProgress(Object... params) {
		return null;
	}

	/**
	 * 发送一个进度
	 * @param params
	 */
	protected final void postProgress(Object... params) {
		if(!isPause)getManager().async(new PTask(), params);
	}

	protected final TaskManager getManager() {
		if (U.isNull(mManager))
			mManager = TaskManager.getInstance();
		return mManager;
	}
	
	private class PTask extends SimpleTask {

		@Override
		public Object onBGThread(Object... params) throws Exception {
			return onBGProgress(params);
		}
		
		@Override
		public void onUIThread(Object data, Object... params) throws Exception {
			onUIProgress(data,params);
		}

	}
}
