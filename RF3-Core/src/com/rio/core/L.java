package com.rio.core;



import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.text.TextUtils;
import android.util.Log;

/**
 * LOG 封装类
 * @author rio
 * @version 2.0
 */
public final class L {
	
	static String TAG = "DEBUG";
	
	private final static String EXCEPTION = "EXCEPTION";
	
	private final static String ERROR_NULL_VALUE = "MESSAGE IS EMPTY";	
		
	private static int mLogTime;

	static boolean DEBUG = true;
	
	/**
	 * 测试模式
	 * @return
	 */
	public static boolean getDebug(){
		return DEBUG;
	}
	
	/**
	 * 应用标识
	 * @return
	 */
	public static String getTag(){
		return TAG;
	}
	
	/**
	 * current time
	 */
	public static void time(){
		L.ii(System.currentTimeMillis());
	}	

	/**
	 * count time
	 */
	public static void count(){
		mLogTime++;
		L.ii(mLogTime);		
	}

	/**
	 * mark
	 */
	public static void i(){
		L.i("L-I");
	}
	/**
	 * mark
	 */
	public static void ii(){
		ii("L-I-I");
	}
	/**
	 * mark
	 */	
	public static void iii(){
		iii("L-I-I-I");
	}

	/***
	 * Log.i(TAG,msg)
	 * @param msg
	 */
	public static void i(String msg){
		if(TextUtils.isEmpty(msg)){
			i(ERROR_NULL_VALUE);
		}else if(DEBUG){
			String name = getFunctionName();
			Log.i(TAG,(name==null?msg:(name+" - "+msg)));
		}
	}
	/***
	 * Log.i(TAG,msg)
	 * @param msg
	 */
	public static void ii(String msg){
		if(TextUtils.isEmpty(msg)){
			ii(ERROR_NULL_VALUE);
		}else if(DEBUG){
			String name = getFunctionName();
			Log.d(TAG,(name==null?msg:(name+" - "+msg)));
		}
	}
	/***
	 * Log.i(TAG,msg)
	 * @param msg
	 */
	public static void iii(String msg){
		if(TextUtils.isEmpty(msg)){
			iii(ERROR_NULL_VALUE);
		}else if(DEBUG){
			String name = getFunctionName();
			Log.w(TAG,(name==null?msg:(name+" - "+msg)));
		}
	}

	/***
	 * Log.i(TAG,msg.tostring)
	 * @param msg
	 */
	public static void i(int msg){
		i(String.valueOf(msg));
	}
	/***
	 * Log.i(TAG,msg.tostring)
	 * @param msg
	 */
	public static void i(long msg) {
		i(String.valueOf(msg));	
	}
	/***
	 * Log.i(TAG,msg.tostring)
	 * @param msg
	 */
	public static void i(boolean msg) {
		i(String.valueOf(msg));	
	}
	/***
	 * Log.i(TAG,msg.tostring)
	 * @param msg
	 */
	public static void i(float msg) {
		i(String.valueOf(msg));	
	}
	/***
	 * Log.i(TAG,msg.tostring)
	 * @param msg
	 */
	public static void i(double msg) {
		i(String.valueOf(msg));	
	}
	/***
	 * Log.i(TAG,msg.tostring)
	 * @param msg
	 */
	public static void i(char msg) {
		i(String.valueOf(msg));
	}
	/***
	 * Log.i(TAG,msg.tostring)
	 * @param msg
	 */
	public static void i(Object msg) {
		if(U.notNull(msg)){
			i(msg.toString());
		}else{
			i(ERROR_NULL_VALUE);
		}			
	}

	/**
	 * 显示所有收到的消息及其细节
	 * @param intent
	 */
	public static void i(Intent intent){
		Bundle b = intent.getExtras();
		Object[] lstName = b.keySet().toArray();
		for (int i = 0; i < lstName.length; i++) {
			String keyName = lstName[i].toString();
			i(keyName+" : "+String.valueOf(b.get(keyName)));
		}		
	}
	/***
	 * Log.i(TAG,msg.tostring)
	 * @param msg
	 */
	public static void ii(int msg){
		ii(String.valueOf(msg));
	}
	/***
	 * Log.i(TAG,msg.tostring)
	 * @param msg
	 */
	public static void ii(long msg) {
		ii(String.valueOf(msg));	
	}
	/***
	 * Log.i(TAG,msg.tostring)
	 * @param msg
	 */
	public static void ii(boolean msg) {
		ii(String.valueOf(msg));	
	}
	/***
	 * Log.i(TAG,msg.tostring)
	 * @param msg
	 */
	public static void ii(float msg) {
		ii(String.valueOf(msg));	
	}
	/***
	 * Log.i(TAG,msg.tostring)
	 * @param msg
	 */
	public static void ii(double msg) {
		ii(String.valueOf(msg));	
	}
	/***
	 * Log.i(TAG,msg.tostring)
	 * @param msg
	 */
	public static void ii(char msg) {
		ii(String.valueOf(msg));
	}
	/***
	 * Log.i(TAG,msg.tostring)
	 * @param msg
	 */
	public static void ii(Object msg) {
		if(U.notNull(msg)){
			ii(msg.toString());
		}else{
			ii(ERROR_NULL_VALUE);
		}	
	}	
	
		
	/***
	 * Log.e(TAG,"EXCEPTION",exception)
	 * @param tr
	 */
	public static void e(Throwable tr){
		if(DEBUG)Log.e(TAG,EXCEPTION,tr);
	}
	/***
	 * Log.e(TAG,"EXCEPTION",exception)
	 * @param tr
	 */
	public static void e(String msg,Throwable tr){
		if(DEBUG)Log.e(TAG,msg,tr);
	}
	/***
	 * Log.e(TAG,msg)
	 * @param msg
	 */
	public static void e(String msg){
		if(TextUtils.isEmpty(msg)){
			i(ERROR_NULL_VALUE);
		}else if(DEBUG){
			String name = getFunctionName();
			Log.e(TAG,(name==null?msg:(name+" - "+msg)));
		}
	}


	/**
	 * 等到调用位置
	 * @return
	 */
	private static String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        
        if (sts == null) {
            return null;
        }
        
        
        for (StackTraceElement st:sts) {
            if (st.isNativeMethod()) {
                continue;
            }

            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }

            if (st.getClassName().equals(L.class.getName())) {
                continue;
            }
            
            return "[ "+Thread.currentThread().getName()+": "+st.getFileName()+": "+st.getLineNumber()+" ]";
        }
        
        return null;
	}
}
