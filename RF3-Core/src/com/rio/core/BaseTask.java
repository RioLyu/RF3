package com.rio.core;

import java.io.Serializable;

/**
 * 任务对象，包括需要执行的事件与参数
 * @author rio
 * @version 2.0
 */
public class BaseTask {
	
	public BaseTaskListener listener;
	
	public Object[] params;

	public BaseTask(BaseTaskListener listener,Object[] params) {
		super();
		this.listener = listener;
		this.params = params;
	}

}
