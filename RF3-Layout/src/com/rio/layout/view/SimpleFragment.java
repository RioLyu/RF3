package com.rio.layout.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.rio.core.L;
import com.rio.core.U;
import com.rio.layout.FragManager;
import com.rio.layout.IFragment;



@SuppressLint("ValidFragment")
public abstract class SimpleFragment extends FrameLayout implements IFragment {
	
	private FragManager mFragmentManager;

	public SimpleFragment(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();		
		getManager().register(this);
	}
	

	public FragManager getManager(){
		if(U.isNull(mFragmentManager))
			mFragmentManager = FragManager.getInstance();
		return mFragmentManager;
	}
	

	@Override
	public void onDetach() {
		mFragmentManager = null;
	}

	@Override
	public void onAttach() {
		View view = onAttach(LayoutInflater.from(getContext()));
		if(U.notNull(view)){
			addView(view);
		}
	}

	@Override
	public String getFlag() {
		return null;
	}

	/**
	 * 绑定一个视图
	 * @param layout
	 * @return
	 */
	public abstract View onAttach(LayoutInflater layout);
	

	@Override
	public void onDisplay(String callerName, int frag, Object... params) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onGoBack() {
		// TODO Auto-generated method stub
		return false;
	}

}
