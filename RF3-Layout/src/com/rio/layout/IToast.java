package com.rio.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public interface IToast {

	public int getDuration();
	
	public int getGravity();
		
	/**
	 * 绑定一个view
	 * @param layout
	 * @return
	 */
	public View onAttach(View layout, Object... params);
	
}
