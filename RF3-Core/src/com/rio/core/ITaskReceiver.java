package com.rio.core;

import java.io.Serializable;

/**
 * 框架的通讯协议
 * @author rio
 *
 */
public interface ITaskReceiver{

	/**
	 * 接受其他Activity或service发送过来的任务
	 * 
	 * @param action	标志 通过该标志 说明不同的信息数组
	 * @param params	信息数组
	 */
	public void onReceiveTask(int action, Object...params);
	
	
	
	
}
