package com.rio.layout.utils;

import com.rio.layout.view.SimpleTask;


/**
 * 一个延后执行的任务
 * 
 * @author rio
 *
 */
public class DelayTask extends SimpleTask {
	
	private long mDelay;

	public DelayTask(long delay) {
		super();
		this.mDelay = delay;
	}
	
	@Override
	public Object onBGThread(Object... params) throws Exception {
		Thread.sleep(mDelay);
		return super.onBGThread(params);
	}

}
