package com.rio.core;

public interface BaseTaskListener {
	/**
	 * 定义在 doInBackground()执行的操作 ;
	 *
	 * @return Object depand on type
	 * @throws Exception
	 */
	public Object onBGThread(Object... params) throws Exception;

	/**
	 * 定义在onProgressUpdate()执行的UI更新 ;
	 *
	 * @param action
	 *            the flage
	 * @param data
	 *            result return from sendResult()
	 * @param params
	 *            params execute from connection()
	 */
	public void onUIThread(Object data, Object... params)throws Exception;

	/**
	 * 定义在过程中出现异常的处理;
	 * @param action
	 * @param exception will be null if no data from onBGThread() 
	 * @param params
	 */
	public void onException(Exception exception,Object... params);
}
