package com.rio.core;




import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BaseBroadcastReceiver extends BroadcastReceiver {

	private ITaskReceiver mChild;

	public BaseBroadcastReceiver(ITaskReceiver child) {
		mChild = child;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (U.notNull(intent)) {
			if (intent.hasExtra(Base.EXTRA_TASK)&& intent.hasExtra(Base.EXTRA_ACTION)) {				
				BaseSerializable task = (BaseSerializable) intent.getExtras()
						.getSerializable(Base.EXTRA_TASK);
				int action = intent.getExtras().getInt(Base.EXTRA_ACTION);	
				// 是否合格
				if (U.notNull(task)) {
					try {
						 mChild.onReceiveTask(action,task.params);
					} catch (Exception e) {
						L.e(e);
					}
				}

			}

		}
	}

}
