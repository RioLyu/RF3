package com.rio.layout;

import com.rio.core.U;
import com.rio.layout.view.SimpleTask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

public class ProgressBuilder implements GoBackWatcher {

	private View mProgress;

	private IProgress mGoBackWatcher;

	public ProgressBuilder(FrameLayout frame, LayoutInflater context, int layout) {
		super();
		FrameLayout mMainFrame = frame;
		mProgress = context.inflate(layout, null);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		mMainFrame.addView(mProgress,2,params);
		mProgress.setVisibility(View.GONE);
		mProgress.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (mProgress.getVisibility() == View.GONE) {
					return false;
				} else {
					return true;
				}
			}
		});
	}

	void showProgress(IProgress blockGoback) {
		if (U.notNull(mProgress) && mProgress.getVisibility() == View.GONE) {
			mGoBackWatcher = blockGoback;	
			mProgress.setVisibility(View.VISIBLE);
		}
	}


	void hideProgress() {
		if (U.notNull(mProgress) && mProgress.getVisibility() == View.VISIBLE) {
			mGoBackWatcher = null;
			mProgress.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onGoBack() {
		if (U.notNull(mProgress) && mProgress.getVisibility() == View.VISIBLE) {
			//没有侦听设置，截获返回，隐藏进度条
			if (U.notNull(mGoBackWatcher)) {
				//满足截获条件，不允许隐藏
				if(mGoBackWatcher.onGoBack()){					
					return true;
				}				
			}
			mProgress.setVisibility(View.GONE);
			return true;
		}
		return false;
	}

}
