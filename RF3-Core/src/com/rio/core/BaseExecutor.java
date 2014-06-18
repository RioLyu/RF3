package com.rio.core;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

/**
 * 
 * @author rio
 * @version 2.0
 */
public class BaseExecutor {


	private static final int MESSAGE_POST_ERROR = 2;

	private static final int MESSAGE_POST_RESULT = 1;
	
	private static final int MESSAGE_POST_POOL_TASK = 3;
	
	private static final int MESSAGE_POST_QUEUE_TASK = 4;

	private boolean mSyncConnecting;

	private LinkedTask mLindedTask;

	private LinkedList<BaseTask> mTaskStack;

	private ExecutorService mTaskPool;

	private TaskHandler mHandler;

	private static ScheduledExecutorService mTaskSche;

	public BaseExecutor(){
		mHandler = new TaskHandler();
		mSyncConnecting = false;
	}
	
	/**
	 * 每个BaseExecutor都有一个ExecutorService。异步线程都在这个线程池里。<br>
	 * 默认是一个CachedThreadPool
	 * @return
	 */
	public ExecutorService getThreadPool() {
		if (mTaskPool == null)
			mTaskPool = Executors.newCachedThreadPool();		
		return mTaskPool;
	}
	
	/**
	 * 设置线程池
	 * @param service
	 */
	public void setThreadPool(ExecutorService service){
		if (service != null)
			mTaskPool = service;
	}
	
	/**
	 * 把任务生成线程并执行,允许在子线程里再生成线程
	 * @see OnTaskHandlerListener
	 * @param task 任务对象
	 */
	public void connectInQueue(BaseTask task) {
		if(U.notNull(mHandler)){
			Message message = mHandler.obtainMessage();
			message.obj = task;
			message.what = MESSAGE_POST_QUEUE_TASK;
			message.sendToTarget();
		}

	}
	/**
	 * 保证任务非空并任务的事件非空的前提下，生成线程
	 * @param task
	 */	
	private void submitInQueue(BaseTask task)
	{
		if (U.notNull(task) && U.notNull(task.listener)) {
			if (U.isNull(mTaskStack)){
				mTaskStack = new LinkedList<BaseTask>();
			}
			mTaskStack.add(task);				
			if (!mSyncConnecting) {
				mLindedTask = new LinkedTask();
				mSyncConnecting = true;
				mLindedTask.execute();
			}
		}
	}
	/**
	 * 把任务生成线程并执行,允许在子线程里再生成线程
	 * @see OnTaskHandlerListener
	 * @param task 任务对象
	 */
	public void connectInPool(BaseTask task) {
		if(U.notNull(mHandler)){
			Message message = mHandler.obtainMessage();
			message.obj = task;
			message.what = MESSAGE_POST_POOL_TASK;
			message.sendToTarget();
		}		
	}
	
	/**
	 * 保证任务非空并任务的事件非空的前提下，生成线程
	 * @param task
	 */
	private void submitInPool(BaseTask task){
		if (U.notNull(task) && U.notNull(task.listener)) {
			AsyncCallable call = new AsyncCallable(task);
			AsyncCallableFuture future = new AsyncCallableFuture(task,call);
			getThreadPool().submit(future);
		}		
	}
			
	/**
	 * 隔一段时间后执行
	 * @param task Runnable
	 * @param delay 延后多少毫秒
	 */
	public void connectInSche(Runnable command, long delay) {
		if (command != null) {
			if (mTaskSche == null)
				mTaskSche = Executors.newScheduledThreadPool(1);
			mTaskSche.schedule(command, delay, TimeUnit.MILLISECONDS);
		}
	}

	/**
	 * 按某个频率执行
	 * @param task Runnable
	 * @param startTime 开始时间 毫秒
	 * @param rate 频率 毫秒
	 */
	public void connectAtRate(Runnable command, long initialDelay, long rate) {
		if (command != null) {
			if (mTaskSche == null)
				mTaskSche = Executors.newScheduledThreadPool(1);
			mTaskSche.scheduleWithFixedDelay(command, initialDelay, rate,
					TimeUnit.MILLISECONDS);
		}
	}


	/**
	 * 判断是否处理异常，然后把结果发送个Handler
	 * @param result
	 */
	private void sendResultMessage(TaskResult result)
	{
		if(U.notNull(mHandler) && U.notNull(result)){

			Message message = mHandler.obtainMessage();
			message.obj = result;

			if (U.isNull(result.exception))
			{
				message.what = MESSAGE_POST_RESULT;
			} else {
				message.what = MESSAGE_POST_ERROR;
			}
			message.sendToTarget();

		}
	}	
	
	
	
	/**
	 * {@link connectInPool}方法的后台处理部分，返回一个对象作为结果
	 * @author rio
	 * @version 2.0
	 */
	private class AsyncCallable implements Callable<TaskResult> {

		private TaskResult mResult;

		public AsyncCallable(BaseTask task) {
			mResult = new TaskResult();
			mResult.task = task;
		}

		@Override
		public TaskResult call() {

				try {
					Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
					mResult.data = mResult.task.listener.onBGThread(mResult.task.params);
				} catch (Exception e) {
					mResult.exception = e;
				}

			return mResult;
		}
	}

	/**
	 * {@link connectInPool}方法的前台处理部分，向handler发送一个UI更新请求
	 * @author rio
	 * @version 2.0
	 */
	private class AsyncCallableFuture extends FutureTask<TaskResult> {

		private BaseTask mTask;

		public AsyncCallableFuture(BaseTask task, AsyncCallable callable) {
			super(callable);
			mTask = task;
		}

		@Override
		protected void done() {

			TaskResult result = null;
			try {
				result = get();
			} catch (Exception e) {
				result = new TaskResult();
				result.task = mTask;
				result.exception = e;
			}
			sendResultMessage(result);

		}
	}

	/**
	 * 结果的结构体
	 * @author rio
	 * @version 2.0
	 */
	private class TaskResult {

		/**
		 * 异常
		 */
		Exception exception;

		/**
		 * 任务请求对象
		 */
		BaseTask task;

		/**
		 * 交付的结果
		 */
		Object data;

	}

	/**
	 * 对应四种WHAT执行更新<br>
	 * {@link MESSAGE_POST_RESULT}执行UI更新<br>
	 * {@link MESSAGE_POST_ERROR}执行异常处理<br>
	 * {@link MESSAGE_POST_POOL_TASK}执行{@link submitInPool}<br>
	 * {@link MESSAGE_POST_QUEUE_TASK}执行UI更新<br>
	 * @author rio
	 * @version 2.0
	 */
	private class TaskHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case MESSAGE_POST_RESULT:
				TaskResult result = (TaskResult) msg.obj;
				try {
					result.task.listener.onUIThread(result.data,result.task.params);
				} catch (Exception e) {
					result.task.listener.onException(e,result.task.params);
				}
				break;

			case MESSAGE_POST_ERROR:
				TaskResult error = (TaskResult) msg.obj;
				error.task.listener.onException(error.exception,error.task.params);
				break;

			case MESSAGE_POST_POOL_TASK:
				BaseTask pool = (BaseTask) msg.obj;
				submitInPool(pool);
				break;

			case MESSAGE_POST_QUEUE_TASK:
				BaseTask queue = (BaseTask) msg.obj;
				submitInQueue(queue);
				break;

			default:

				break;

			}
		}
	}


	
	
	
	
	/**
	 * 把放进堆栈里面的任务按先进先出的顺序执行
	 * @author rio
	 * @version 2.0
	 */
	class LinkedTask extends AsyncTask<Object, TaskResult, BaseTask> {

		@Override
		protected BaseTask doInBackground(Object... params) {
			BaseTask task = null;
			while (!mTaskStack.isEmpty() && mSyncConnecting) {
				task = mTaskStack.removeFirst();
				if (U.notNull(task) && U.notNull(task.listener)) {
					TaskResult result = new TaskResult();
					result.task = task;
					try {
						result.data = result.task.listener.onBGThread(task.params);
					} catch (Exception e) {
						result.exception = e;
					}
					publishProgress(result);
				}
			}
			return task;
		}

		@Override
		protected void onProgressUpdate(TaskResult... values) {
			TaskResult result = values[0];
			sendResultMessage(result);
		}

		@Override
		protected void onPostExecute(BaseTask result) {
			super.onPostExecute(result);
			mSyncConnecting = false;
			if (mTaskStack != null) {
				mTaskStack.clear();
				mTaskStack = null;
			}
		}
	}
	
	
	/**
	 * 关闭线程池，停止所有线程
	 */
	public void shutdown(){
		getThreadPool().shutdownNow();
		if(U.notNull(mTaskStack))
			mTaskStack.clear();
		mTaskStack = null;	
		mTaskPool = null;
		mHandler = null;
	}
	



}
