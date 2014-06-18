package com.rio.layout;

import com.rio.core.BaseToast;
import com.rio.core.U;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ToastBuilder {

	private static Toast mToast;

	private Context mContext;

	private View mRoot;

	private int mLayout;

	private LayoutInflater mInflater;

	/**
	 * make sure resoure xml have id : text_toast_content
	 * 
	 * @param context
	 * @param resoure
	 */
	public ToastBuilder(Context context, LayoutInflater inflater, int source) {
		mContext = context;
		mLayout = source;
		mInflater = inflater;
	}

	public void show(IToast toast, Object... params) {
		if (U.isNull(toast))
			return;
		if (U.isNull(mToast))
			mToast = new Toast(mContext);
		if (U.isNull(mRoot))
			mRoot = mInflater.inflate(mLayout, null);
		mToast.setView(toast.onAttach(mRoot, params));
		if(U.notNull(toast.getGravity()))
			mToast.setGravity(toast.getGravity(), 0, 0);
		mToast.setDuration(toast.getDuration());
		mToast.show();
	}

	public void clear() {
		if (U.notNull(mToast)) {
			mToast.cancel();
		}
	}
}
