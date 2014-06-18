package com.rio.layout.utils;

import com.rio.core.U;
import com.rio.layout.view.SimpleTask;
import com.rio.helper.file.Logger;



/**
 * 便捷的日志事务
 * 
 * @author rio
 *
 */
public class LoggerTask extends SimpleTask {

	String mMessage;
	Throwable mThrowable;
	
	
	public LoggerTask(String message) {
		mMessage  = message;
	}
	
	public LoggerTask(Throwable throwable) {
		mThrowable  = throwable;
	}	
	@Override
	public Object onBGThread(Object... params) throws Exception {
		if(U.notNull(mMessage))Logger.append(mMessage);
		if(U.notNull(mThrowable))Logger.append(mThrowable);
		mMessage = null;
		mThrowable = null;
		return null;
	}
}
