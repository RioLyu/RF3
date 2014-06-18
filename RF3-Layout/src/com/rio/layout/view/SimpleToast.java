package com.rio.layout.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.rio.core.U;
import com.rio.layout.IToast;
import com.rio.layout.ToastBuilder;


public class SimpleToast implements IToast {
	


	@Override
	public int getDuration() {
		return Toast.LENGTH_SHORT;
	}

	@Override
	public int getGravity() {
		return Gravity.CENTER;
	}

	@Override
	public View onAttach(View layout, Object... params) {
		return layout;
	}

}
