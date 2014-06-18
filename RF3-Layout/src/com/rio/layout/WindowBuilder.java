package com.rio.layout;

import com.rio.core.L;
import com.rio.core.U;
import com.rio.layout.view.SimpleTask;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;

public class WindowBuilder implements GoBackWatcher {

	private FrameLayout mMainFrame;

	private LayoutInflater mLayoutInflater;

	private LayoutParams mLayoutParams;

	private IWindow mLayout;

	private String mCurrentLayout;

	private boolean isShow;// 控制只显示一个
	
	private OnDismissListener mOnDismissListener;
		
	private static final int POSITION = 0;

	public WindowBuilder(FrameLayout frame, LayoutInflater inflater,
			Context context) {
		mMainFrame = new FrameLayout(context);
		mLayoutParams = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		frame.addView(mMainFrame, 1, mLayoutParams);
		mLayoutInflater = inflater;
	}

	public boolean isShow() {
		return isShow;
	}


	public void showWindow(IWindow layout,Object... params) {
		

		if (U.notNull(mMainFrame) && U.notNull(layout) && !isShow) {

			mLayout = layout;
			isShow = true;

			TaskManager.getInstance().async(new SimpleTask() {



				@Override
				public Object onBGThread(Object... params) throws Exception {
					mCurrentLayout = LayoutManager.getInstance()
							.getCurrentLayoutName();
					return mLayout.onAttach(mLayoutInflater,params);
				}

				@Override
				public void onUIThread(Object data, Object... params)
						throws Exception {
					if (U.notNull(data)) {
						View view = (View) data;
						mMainFrame.addView(view,POSITION,mLayoutParams);
						Animation in = mLayout.getAnimation();
						if (U.notNull(in)) {
							in.setAnimationListener(new DisplayAnimationListener(view, params));
							view.setAnimation(in);
						} else {
							mLayout.onDisplay(mCurrentLayout,view,params);
						}
					}

				}

			},params);

		}
	}

	public void hideWindow(Object... params) {
		if (U.notNull(mMainFrame) && U.notNull(mLayout) && isShow) {
			TaskManager.getInstance().async(new SimpleTask() {

				@Override
				public void onUIThread(Object data, Object... params)
						throws Exception {
					if(U.notNull(mOnDismissListener)){
						mOnDismissListener.onDismiss(U.getName(mLayout.getClass()), params);
					}
					destroy();
				}

			},params);

		}
	}

	private void destroy() {		
		mMainFrame.removeViewAt(POSITION);
		mLayout.onDetach();
		mOnDismissListener = null;
		mLayout = null;
		mCurrentLayout = null;
		isShow = false;
	}
	
	@Override
	public boolean onGoBack() {
		if(U.notNull(mLayout)){
			return mLayout.onGoBack();
		}
		return false;
	}

	/**
	 * Activity的onResume会调用当时顶部图层的onResume
	 */
	public void onResume(){
		if(U.notNull(mLayout)){
			mLayout.onResume();
		}		
	};
	
	/**
	 * Activity的onPause会调用当时顶部图层的onPause
	 */
	public void onPause(){
		if(U.notNull(mLayout)){
			mLayout.onPause();
		}			
	};
	
	/**
	 * 侦听onActivityResult事件
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		if(U.notNull(mLayout)){
			mLayout.onActivityResult(requestCode,resultCode,data);
		}		
	};
	
	
	/**
	 * 设置回调侦听
	 * @param listener
	 */
	public void setOnDismissListener(OnDismissListener listener) {
		mOnDismissListener = listener;
	}
	/**
	 * 完成动画再回调
	 * @author rio
	 *
	 */
	private class DisplayAnimationListener implements AnimationListener {

		private View view;
		private Object[] params;


		public DisplayAnimationListener(View view,
				Object... params) {
			this.view = view;
			this.params = params;

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			mLayout.onDisplay(mCurrentLayout, view, 0, params);
		}

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

	}
	
	/**
	 * 侦听，接收返回参数
	 * @author rio
	 *
	 */
	public interface OnDismissListener{
		
		public void onDismiss(String windowname,Object... params);
				
	}

}
