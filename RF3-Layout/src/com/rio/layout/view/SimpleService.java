package com.rio.layout.view;

import com.rio.core.BaseBroadcastReceiver;
import com.rio.core.ITaskReceiver;


import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;


/**
 * @author rio
 * @version 2.0
 */
public abstract class SimpleService extends Service implements ITaskReceiver{
	
	private boolean isRunning;
	
	private BaseBroadcastReceiver mReceiver;
	
	@Override
	public void onCreate() {		
		super.onCreate();				
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(!isRunning){
			mReceiver = new BaseBroadcastReceiver(this);
			registerReceiver(mReceiver, new IntentFilter(getClass().getName()));
			isRunning = true;
		}				
		return super.onStartCommand(intent, flags, startId);
	}
	

	
	@Override
	public void onDestroy() {
		if(isRunning){
			unregisterReceiver(mReceiver);
			mReceiver = null;
			isRunning = false;
		}
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}





}
