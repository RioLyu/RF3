package com.rio.layout;

import java.util.concurrent.ExecutorService;

import com.rio.core.BaseExecutor;
import com.rio.core.BaseTask;
import com.rio.core.BaseTaskListener;
import com.rio.core.U;



/**
 * 管理应用中的线程
 * @author rio
 *
 */
public class TaskManager {
	
	private static TaskManager mInstance;

	private BaseExecutor mConnectionTask;

	private TaskManager() {
		mConnectionTask = new BaseExecutor();
	}
	
	public static TaskManager getInstance(){
		
		if (U.isNull(mInstance)) {
			mInstance =  new TaskManager();
		}
		return mInstance;
	}
	
	/**
	 * 执行一个无先后之分的异步线程 
	 * @param listener
	 * @param params
	 */
	public void async(BaseTaskListener listener,Object... params){
		if(U.notNull(mConnectionTask)){			
			BaseTask task = new BaseTask(listener, params);
			mConnectionTask.connectInPool(task)	;
		}

	}
		
	/**
	 * {@link BaseExecutor#connectInSche}
	 * @param task
	 */
	public void delay(Runnable command,long milesecond){
		if(U.notNull(mConnectionTask))
			mConnectionTask.connectInSche(command, milesecond);
	}
	/**
	 * {@link BaseExecutor#connectAtRate}
	 * @param task
	 */
	public void repeat(Runnable command,long startTime,long rate){
		if(U.notNull(mConnectionTask))
			mConnectionTask.connectAtRate(command, startTime, rate);
	}
	
	/**
	 * 执行一个先进先执行的异步线程
	 * @param listener
	 * @param params
	 */
	public void sync(BaseTaskListener listener,Object... params){
		if(U.notNull(mConnectionTask)){			
			BaseTask task = new BaseTask(listener, params);
			mConnectionTask.connectInQueue(task);
		}
	}
	
	/**
	 * 获取当前线程池
	 * {@link BaseExecutor#getThreadPool()}
	 */
	protected ExecutorService getThreadPool() {
		if(U.notNull(mConnectionTask)){
			return mConnectionTask.getThreadPool();
		}
		return null;
	}
	
	/**
	 * 设置当前线程池
	 * {@link BaseExecutor#setThreadPool(ExecutorService)}
	 */
	public void setThreadPool(ExecutorService service) {
		if(U.notNull(mConnectionTask)){
			mConnectionTask.setThreadPool(service);
		}
	}
	
	/**
	 * 仅供框架使用
	 */
	void destroy() {
		mConnectionTask.shutdown();
		mConnectionTask = null;
		mInstance = null;
	}
}
