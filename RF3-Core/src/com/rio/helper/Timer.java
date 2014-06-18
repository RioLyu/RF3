package com.rio.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.rio.core.L;
import com.rio.core.U;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class Timer {

	private String BROADCAST_ACTION;

	private static final String EXTRA_NAME = "extra_name";

	private Map<String, PendingIntent> mPendingIntentContent;

	private Map<String, TimeoutTrigger> mReceiverContent;

	private BroadcastReceiver mReceiver;

	private Context mContext;

	private AlarmManager mAlarmManager;
	
	private Random mRandom;

	public void destroy() {

		if (U.notNull(mPendingIntentContent)
				&& !mPendingIntentContent.isEmpty()) {
			for (PendingIntent p : mPendingIntentContent.values()) {
				mAlarmManager.cancel(p);
			}
		}
		mContext.unregisterReceiver(mReceiver);
		mPendingIntentContent.clear();
		mPendingIntentContent = null;
		mReceiverContent.clear();
		mReceiverContent = null;
		mReceiver = null;
		mAlarmManager = null;
		mContext = null;
	}

	public Timer(Context context) {
		mContext = context;
		mAlarmManager = (AlarmManager) mContext
				.getSystemService(Context.ALARM_SERVICE);
		mPendingIntentContent = new ConcurrentHashMap<String, PendingIntent>();
		mReceiverContent = new ConcurrentHashMap<String, Timer.TimeoutTrigger>();
		mRandom = new Random(1000);
		BROADCAST_ACTION = "BROADCAST_ACTION_" + U.toString(mRandom.nextInt());
		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {

				if (U.notNull(intent) && U.notNull(intent.getAction())
						&& intent.getAction().equals(BROADCAST_ACTION)) {
					String name = intent.getStringExtra(EXTRA_NAME);

					if (U.notNull(name) && mReceiverContent.containsKey(name)) {

						TimeoutTrigger receiver = mReceiverContent.get(name);
						if (U.notNull(receiver)) {

							receiver.onTrigger();

						}
					}
				}

			}

		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(BROADCAST_ACTION);
		mContext.registerReceiver(mReceiver, filter);
	}

	/**
	 * 延后触发某个行为
	 * @param name
	 * @param delaytime
	 * @param receiver
	 */
	public void delay(String name, long delaytime, TimeoutTrigger receiver) {
		if (U.notNull(name) && U.notNull(receiver)) {
			if (!mReceiverContent.containsKey(name)) {
				mReceiverContent.put(name, receiver);
				Intent i = new Intent(BROADCAST_ACTION);
				i.putExtra(EXTRA_NAME, name);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						mContext, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
				mPendingIntentContent.put(name, pendingIntent);
				long now = System.currentTimeMillis();
				mAlarmManager.set(AlarmManager.RTC_WAKEUP, now + delaytime,
						pendingIntent);

			}
		}

	}
	
	/**
	 * 重复触发某个行为
	 * @param name
	 * @param time
	 * @param receiver
	 */
	public void repeat(String name,long time,TimeoutTrigger receiver){
		if (U.notNull(name) && U.notNull(receiver)) {
			if (!mReceiverContent.containsKey(name)) {
				mReceiverContent.put(name, receiver);
				Intent i = new Intent(BROADCAST_ACTION);
				i.putExtra(EXTRA_NAME, name);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						mContext, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
				mPendingIntentContent.put(name, pendingIntent);
				long now = System.currentTimeMillis();
				mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, now+time, time,
						pendingIntent);
			}
		}		
		
	}

	/**
	 * 停止触发某个行为
	 * @param name
	 */
	public void stop(String name) {

		if (U.notNull(name) && mPendingIntentContent.containsKey(name)
				&& mReceiverContent.containsKey(name)) {
			PendingIntent pendingIntent = mPendingIntentContent.get(name);
			mAlarmManager.cancel(pendingIntent);
			mPendingIntentContent.remove(name);
			mReceiverContent.remove(name);
		}

	}

	public interface TimeoutTrigger {

		void onTrigger();

	}
}
