package com.rio.layout;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import com.rio.core.L;
import com.rio.core.U;
import com.rio.layout.WindowBuilder.OnDismissListener;
import com.rio.layout.view.SimpleTask;


/**
 * 负责管理整个应用的所有弹出框，包括对话框，菜单，吐司
 * 第一层是ViewFlipper
 * 第二层是Window
 * 第三层是Progress
 * @author rio
 *
 */
public class PopupManager implements GoBackWatcher{

	private static final String MSG_NO_CONFIG = "PopupManager has not setup well";

	private static PopupManager mInstance;

	private ToastBuilder mToast;

	private Context mContext;

	private FrameLayout mMainFrame;
	
	private ProgressBuilder mProgress;
	
	private LayoutInflater mLayoutInflater;

	private WindowBuilder mWindow;
	


	private PopupManager() {}

	public static PopupManager getInstance() {
		if (U.isNull(mInstance)) {
			mInstance = new PopupManager();
		}
		return mInstance;
	}

	/**
	 * 设置定位
	 * 
	 * @param frame
	 */
	void setFrame(Context context,FrameLayout frame,LayoutInflater inflater) {
		mMainFrame = frame;
		mContext = context;
		mLayoutInflater = inflater;
		mWindow = new WindowBuilder(mMainFrame, mLayoutInflater, mContext);
	}

	/**
	 * 设置全局的Toast样式
	 * @param gravity
	 * @param layout
	 * @param textview	TextView的id
	 */
	public final void setToastConfig(int layout) {
		if (U.isNull(mContext)) {
			L.e(MSG_NO_CONFIG);
		} else {
			mToast = new ToastBuilder(mContext,mLayoutInflater,layout);
		}
	}


	
	/**
	 * 设置全局的Progress样式
	 * 
	 * @param gravity
	 * @param defaultWidth
	 * @param defaultHeight
	 * @param background
	 * @param animation
	 */
	public final void setProgressConfig(int layout) {
		if (U.isNull(mMainFrame)) {
			L.e(MSG_NO_CONFIG);
		} else {
			mProgress = new ProgressBuilder(mMainFrame, mLayoutInflater, layout);
		}

	}
	
	

	/*** TOAST ***/

	/**
	 * 显示TOAST
	 * 
	 * @param resoure
	 *            message
	 */
	public void show(IToast toast, Object... params) {
		if (U.notNull(toast)) {
			if (U.notNull(mToast)) {
				mToast.show(toast, params);
			} else {
				L.e(MSG_NO_CONFIG);
			}
		}

	}



	/**
	 * 清除所有窗体
	 */
	public void clearAllWindow() {
		hideProgress();
		hideWindow();
	}

	/**
	 * 仅供框架使用
	 */
	void destroy() {
		clearAllWindow();		
		mContext = null;
		mMainFrame = null;
		mToast = null;
		mInstance = null;
		mProgress = null;
		mWindow = null;
	}	
	
	/**
	 * 显示进度条
	 */
	public void showProgress(){
		if(U.notNull(mProgress)){
			mProgress.showProgress(null);
		}
	}
	
	/**
	 * 显示进度条
	 */
	public void show(IProgress watcher){
		if(U.notNull(mProgress)){
			mProgress.showProgress(watcher);
		}
	}
	
	/**
	 * 隐藏进度条
	 */
	public void hideProgress(){
		if(U.notNull(mProgress)){
			mProgress.hideProgress();
		}
	}

	@Override
	public boolean onGoBack() {
		if(U.notNull(mProgress) && mProgress.onGoBack()){
			return true;
		}
		if(U.notNull(mWindow) && mWindow.onGoBack()){
			return true;
		}
		return false;
	}	

	/**
	 * 显示图层
	 */
	public void show(IWindow layout, Object... params){
		if(U.notNull(mWindow)){
			mWindow.showWindow(layout,params);
		}

	}
	
	/**
	 * 隐藏图层
	 */
	public void hideWindow(Object... params){
		if(U.notNull(mWindow)){
			mWindow.hideWindow(params);
		}
	}
	
	/**
	 * Activity的onResume会调用当时顶部图层的onResume
	 */
	public boolean onResume(){
		if(U.notNull(mWindow) && mWindow.isShow()){
			mWindow.onResume();
			return true;
		}
		return false;	
	};
	
	/**
	 * Activity的onPause会调用当时顶部图层的onPause
	 */
	public boolean onPause(){
		if(U.notNull(mWindow) && mWindow.isShow()){
			mWindow.onPause();
			return true;
		}
		return false;			
	};
	
	/**
	 * 侦听onActivityResult事件
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public boolean onActivityResult(int requestCode, int resultCode, Intent data){
		if(U.notNull(mWindow)&& mWindow.isShow()){
			mWindow.onActivityResult(requestCode,resultCode,data);
			return true;
		}
		return false;		
	};

	
	
	
	/**
	 * 设置回调侦听
	 * @param listener
	 */
	public void setOnWindowListener(OnDismissListener listener) {
		if(U.notNull(mWindow)&&U.notNull(listener)){
			mWindow.setOnDismissListener(listener);
		}
	}

}
