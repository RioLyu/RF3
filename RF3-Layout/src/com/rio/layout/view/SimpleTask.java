package com.rio.layout.view;

import com.rio.core.BaseTaskListener;
import com.rio.core.L;



public class SimpleTask implements BaseTaskListener {

	@Override
	public Object onBGThread(Object... params) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onUIThread(Object data, Object... params)throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onException(Exception exception, Object... params) {
		L.e(exception);
		
	}



}
