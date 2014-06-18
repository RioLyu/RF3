package com.rio.layout;

import java.util.ArrayList;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ViewFlipper;

import com.rio.core.BaseTaskListener;
import com.rio.core.L;
import com.rio.core.S;
import com.rio.core.U;
import com.rio.framework2.R;

public class LayoutManager {

	private static final String MSG_NO_ANIMATION = "no Animation in LayoutManager";

	private static LayoutManager mInstance;

	private Stack<LayoutRecord> mViewStack;

	private LayoutInflater mLayoutInflater;

	private Animation mAnimatAddIn;

	private Animation mAnimatAddOut;

	private Animation mAnimatDelIn;

	private Animation mAnimatDelOut;

	private FrameLayout mMainFrame;

	private ViewFlipper mViewFlipper;

	private IBackground mRootChild;

	private int mCurrentIndex;

	private Activity mActivity;

	/**
	 * 标志
	 */
	private int mFlag = 0;

	/**
	 * 返回的结果或发送的参数
	 */
	private Object[] mParam;

	private FragManager mFragmentManager;

	private boolean isLoading;

	private LayoutManager() {
	};

	/**
	 * 初始化
	 * 
	 * @param activity
	 * @param flipper
	 * @param root
	 * @param mResumeListenerList
	 */
	void init(Activity activity, LayoutInflater inflater, FrameLayout frame,
			ViewFlipper flipper) {

		mActivity = activity;

		mMainFrame = frame;

		mLayoutInflater = inflater;

		mViewStack = new Stack<LayoutRecord>();

		mViewFlipper = flipper;

		mViewFlipper.setVisibility(View.GONE);

		mFragmentManager = FragManager.getInstance();
		
		mAnimatAddIn = AnimationUtils.loadAnimation(mActivity, R.anim.push_left_in);		
		mAnimatAddOut = AnimationUtils.loadAnimation(mActivity, R.anim.push_left_out);
		mAnimatDelIn = AnimationUtils.loadAnimation(mActivity, R.anim.push_right_in);
		mAnimatDelOut = AnimationUtils.loadAnimation(mActivity, R.anim.push_right_out);

	}

	public void setAnimation(Animation addin, Animation addout,
			Animation delin, Animation delout) {

		mAnimatAddIn = addin;

		mAnimatAddOut = addout;

		mAnimatDelIn = delin;

		mAnimatDelOut = delout;
	}

	void setRootChild(IBackground root) {
		mRootChild = root;
	}

	/**
	 * 获取容器Activity
	 * 
	 * @return
	 */
	public Activity getActivity() {
		return mActivity;
	}

	/**
	 * 获取容器的底层的FrameLayout
	 * 
	 * @return
	 */
	public FrameLayout getMainFrame() {
		return mMainFrame;
	}

	/**
	 * 获取当前层的名字
	 * 
	 * @return
	 */
	public String getCurrentLayoutName() {
		LayoutRecord r = getTop();
		if (U.notNull(r)) {
			return r.name;
		}
		return null;
	}

	/**
	 * 获取当前层
	 * 
	 * @return
	 */
	public ILayout getCurrentLayout() {
		LayoutRecord r = getTop();
		if (U.notNull(r)) {
			return r.child;
		}
		return null;
	}

	/**
	 * 获取当前层的层数
	 * 
	 * @return
	 */
	public int getCurrentLayoutIndex() {
		return mCurrentIndex;
	}

	/**
	 * 单例
	 * 
	 * @return
	 */
	public static LayoutManager getInstance() {

		if (U.isNull(mInstance)) {
			mInstance = new LayoutManager();
		}
		return mInstance;
	}

	/**
	 * 通过设置不同的标志 说明不同的信息数组，使用完后自动归零
	 * 
	 * @param flag
	 */
	public void setFlag(int flag) {
		mFlag = flag;
	}

	/**
	 * 设置不同的信息数组
	 * 
	 * @param param
	 */
	public void setParam(Object... param) {
		this.mParam = param;
	}

	private void setRoot() {

		if (U.notNull(mRootChild) && U.notNull(mViewFlipper)
				&& mViewFlipper.getVisibility() == View.GONE
				&& U.notNull(mRootChild)) {
			TaskManager.getInstance().async(new RootTask());
		}
	}

	private class RootTask implements BaseTaskListener {

		@Override
		public Object onBGThread(Object... params) throws Exception {
			return mRootChild.onAttach();
		}

		@Override
		public void onUIThread(Object data, Object... params) throws Exception {
			if (U.notNull(data)) {
				ILayout layout = (ILayout) data;
				// 清空Fragments
				mFragmentManager.setFragments(null);

				LayoutRecord record = new LayoutRecord(layout);
				record.view = layout.onAttach(mLayoutInflater);
				if (U.isNull(record.view)) {
					return;
				}
				record.name = layout.getName();
				if (U.isNull(record.name)) {
					record.name = U.getName(layout.getClass());
				}
				mViewStack.add(record);

				mCurrentIndex = 0;
				mViewFlipper.setVisibility(View.VISIBLE);
				mViewFlipper.addView(record.view);
				mViewFlipper.setDisplayedChild(mCurrentIndex);

				mFragmentManager.setLayout(layout);
				mRootChild.onDisplayChild(record.name, mCurrentIndex);
				String name = U.getName(mRootChild.getClass());
				layout.onDisplay(name, record.view, mFlag, mParam);
				mFragmentManager.postDisplay(name, mFlag, mParam);

				reset();
				mRootChild.onResume();
			}

		}

		@Override
		public void onException(Exception exception, Object... params) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * 通过类名叠加图层
	 * 
	 * @param classname
	 */
	public void add(String classname) {
		Object o = null;
		try {
			Class<?> c = Class.forName(classname);
			o = c.newInstance();
		} catch (Exception e) {
			L.e(e);
		}
		if (U.notNull(o) && o instanceof ILayout) {
			add((ILayout) o);
		}
	}

	/**
	 * 叠加一个图层
	 * 
	 * @param child
	 */
	public void add(ILayout child) {

		if (!isLoading && U.notNull(child) && U.notNull(mViewStack)
				&& !mViewStack.isEmpty()) {
			// 开始加载
			isLoading = true;

			// 先保存再清空
			LayoutRecord last = mViewStack.lastElement();
			last.fragments = mFragmentManager.getFragments();
			mFragmentManager.setFragments(null);

			LayoutRecord record = new LayoutRecord(child);
			record.view = child.onAttach(mLayoutInflater);
			if (U.isNull(record.view)) {
				mFragmentManager.setFragments(last.fragments);
				record.view = null;
				record = null;
				reset();
				return;
			}

			record.name = child.getName();
			if (U.isNull(record.name)) {
				record.name = U.getName(child.getClass());
			}
			// 添加图层
			mViewStack.add(record);
			mCurrentIndex++;
			mViewFlipper.addView(record.view);

			last.child.onHide(record.name);
			setAddAnimation(last.name,record,false);
			mViewFlipper.setDisplayedChild(mCurrentIndex);

		}

	}

	/**
	 * 插件叠加一个原有图层,不准外部调用 请统一使用PlusManager.add
	 * 
	 * @param classname
	 * @param intent
	 */
	void addAbovePlus(String classname, Intent intent) {
		Object o = null;
		try {
			Class<?> c = Class.forName(classname);
			o = c.newInstance();
		} catch (Exception e) {
			L.e(e);
		}
		if (U.notNull(o) && o instanceof ILayout) {
			ILayout child = (ILayout) o;
			if (!isLoading && U.notNull(mViewStack) && !mViewStack.isEmpty()) {
				// 开始加载
				isLoading = true;

				// 先保存再清空
				LayoutRecord last = mViewStack.lastElement();
				last.fragments = mFragmentManager.getFragments();
				mFragmentManager.setFragments(null);
				PlusManager pm = PlusManager.getInstance(mActivity);
				pm.setActive(false);
				LayoutRecord record = new LayoutRecord(child);
				record.view = child.onAttach(mLayoutInflater);
				if (U.isNull(record.view)) {
					mFragmentManager.setFragments(last.fragments);
					pm.setActive(last.plus);
					if(pm.isActive()){
						pm.loadResources(last.dex);
					}					
					record.view = null;
					record = null;
					reset();
					return;
				}

				record.name = child.getName();
				if (U.isNull(record.name)) {
					record.name = U.getName(child.getClass());
				}
				// 添加图层
				mViewStack.add(record);
				mCurrentIndex++;
				mViewFlipper.addView(record.view);
				
				last.child.onHide(record.name);
				pm.setIntent(intent);
				pm.setActive(false);				
				setAddAnimation(last.name, record,true);
				mViewFlipper.setDisplayedChild(mCurrentIndex);

			}
		}

	}

	/**
	 * 叠加一个插件,不准外部调用 请统一使用PlusManager.add
	 * 
	 * @param child
	 */
	void addPlus(String path, String classname, Intent intent) {

		if (!isLoading && U.notNull(path) && U.notNull(classname)
				&& U.notNull(mViewStack) && !mViewStack.isEmpty()) {
			// 开始加载
			isLoading = true;

			// 先保存再清空
			LayoutRecord last = mViewStack.lastElement();
			last.fragments = mFragmentManager.getFragments();
			mFragmentManager.setFragments(null);
			PlusManager pm = PlusManager.getInstance(mActivity);
			PlusLayout child = pm.load(path, classname);
			if(U.isNull(child)){
				mFragmentManager.setFragments(last.fragments);
				pm.setActive(last.plus);
				if(pm.isActive()){
					pm.loadResources(last.dex);
				}
				reset();
				return;
			}
			LayoutRecord record = new LayoutRecord(child);
			record.plus = true;
			record.dex = path;
			pm.setActive(true);
			record.view = record.child.onAttach(mLayoutInflater);
			if (U.isNull(record.view)) {
				mFragmentManager.setFragments(last.fragments);
				pm.setActive(last.plus);
				if(pm.isActive()){
					pm.loadResources(last.dex);
				}
				record.destroy();
				record = null;
				reset();
				return;
			}
			record.name = child.getName();
			// 添加图层
			mViewStack.add(record);
			mCurrentIndex++;
			mViewFlipper.addView(record.view);
			
			last.child.onHide(record.name);
			pm.setIntent(intent);
			setAddAnimation(last.name, record,true);
			mViewFlipper.setDisplayedChild(mCurrentIndex);

		}

	}

	private class DisplayAnimationListener implements AnimationListener {

		private String name;
		private LayoutRecord record;
		private boolean isPlus;

		public DisplayAnimationListener(String name, LayoutRecord record,boolean plus) {
			this.name = name;
			this.record = record;
			this.isPlus = plus; 
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			mFragmentManager.setLayout(record.child);
			mRootChild.onDisplayChild(record.name, mCurrentIndex);
			if(isPlus){
				Intent i = PlusManager.getInstance(mActivity).getIntent();
				record.child.onDisplay(name, record.view, mFlag, i);
				mFragmentManager.postDisplay(name, mFlag, i);
			}else{
				record.child.onDisplay(name, record.view, mFlag, mParam);
				mFragmentManager.postDisplay(name, mFlag, mParam);				
			}
			record = null;
			name = null;
			reset();

		}

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

	}

	private void reset() {
		mFlag = 0;
		mParam = null;
		isLoading = false;
	}

	/**
	 * 设置viewFlipper的动画资源
	 * 
	 * @param viewFlipper
	 * @param context
	 */
	private void setBackAnimation(String name, LayoutRecord record,boolean plus) {
		if (mAnimatDelIn != null && mAnimatDelOut != null) {
			mAnimatDelOut.setAnimationListener(new DisplayAnimationListener(
					name, record,plus));
			Animation in = record.child.getAnimatDelIn();
			Animation out = record.child.getAnimatDelOut();
			if (U.notNull(in) && U.notNull(out)) {
				mViewFlipper.setInAnimation(in);
				mViewFlipper.setOutAnimation(out);
			} else {
				mViewFlipper.setInAnimation(mAnimatDelIn);
				mViewFlipper.setOutAnimation(mAnimatDelOut);
			}
		} else {
			L.e(MSG_NO_ANIMATION);
		}
	}

	/**
	 * 设置viewFlipper的动画资源
	 * 
	 * @param viewFlipper
	 * @param context
	 */
	private void setAddAnimation(String name, LayoutRecord record,boolean plus) {
		if (mAnimatAddIn != null && mAnimatAddOut != null) {
			mAnimatAddOut.setAnimationListener(new DisplayAnimationListener(
					name, record,plus));
			Animation in = record.child.getAnimatAddIn();
			Animation out = record.child.getAnimatAddOut();
			if (U.notNull(in) && U.notNull(out)) {
				mViewFlipper.setInAnimation(in);
				mViewFlipper.setOutAnimation(out);
			} else {
				mViewFlipper.setInAnimation(mAnimatAddIn);
				mViewFlipper.setOutAnimation(mAnimatAddOut);
			}

		} else {
			L.e(MSG_NO_ANIMATION);
		}
	}

	/**
	 * 叠加多个图层
	 * 
	 * @param child
	 */
	public void add(ILayout... childs) {

		if (!isLoading && U.notNull(childs) && U.notNull(mViewStack)
				&& !mViewStack.isEmpty()) {

			int length = childs.length - 1;

			if (length >= 0) {
				// 开始加载
				isLoading = true;
				// 先保存再清空
				LayoutRecord last = mViewStack.lastElement();
				last.fragments = mFragmentManager.getFragments();
				mFragmentManager.setFragments(null);
				ILayout child = null;
				LayoutRecord record = null;
				for (int i = 0; i <= length; i++) {
					child = childs[i];
					record = new LayoutRecord(child);
					record.view = child.onAttach(mLayoutInflater);
					record.name = child.getName();
					if (U.isNull(record.name)) {
						record.name = U.getName(child.getClass());
					}
					if (i < length) {
						record.fragments = mFragmentManager.getFragments();
						mFragmentManager.setFragments(null);
					}
					mViewStack.add(record);
					mCurrentIndex++;
					mViewFlipper.addView(record.view);
				}

				last.child.onHide(record.name);
				setAddAnimation(last.name,record,false);
				mViewFlipper.setDisplayedChild(mCurrentIndex);

			}
		}

	}

	/**
	 * 反向减少一个图层 返回到插件
	 */
	void goBackFromPlus(Intent intent) {

		if (!isLoading && U.notNull(mViewStack) && !mViewStack.isEmpty()
				&& !mFragmentManager.postGoBack()
				&& !PopupManager.getInstance().onGoBack()) {

			if (mCurrentIndex >= 1) {

				LayoutRecord last = mViewStack.lastElement();
				// 返回侦听
				if (!last.child.onGoBack()) {
					// 开始加载
					isLoading = true;
					// 清空
					last = mViewStack.pop();

					last.fragments = mFragmentManager.getFragments();
					String name = last.name;
					// 回调
					LayoutRecord current = mViewStack.lastElement();
					last.child.onHide(current.name);
					last.destroy();
					last = null;

					PlusManager pm = PlusManager.getInstance(mActivity);
					pm.setIntent(intent);
					pm.setActive(current.plus);
					if (pm.isActive()) {
						pm.loadResources(current.dex);
					}
					// 显示
					mCurrentIndex--;
					mFragmentManager.setFragments(current.fragments);
					setBackAnimation(name,current,true);

					mViewFlipper.setDisplayedChild(mCurrentIndex);
					mViewFlipper.removeViewAt(mCurrentIndex + 1);

				}
			} else {

				if (!mRootChild.onKeyBackHome()) {
					mActivity.finish();
				}

			}

		}

	}

	/**
	 * 反向减少一个图层
	 */
	public void goBack() {

		if (!isLoading && U.notNull(mViewStack) && !mViewStack.isEmpty()
				&& !mFragmentManager.postGoBack()
				&& !PopupManager.getInstance().onGoBack()) {

			if (mCurrentIndex >= 1) {

				LayoutRecord last = mViewStack.lastElement();
				// 返回侦听
				if (!last.child.onGoBack()) {
					// 开始加载
					isLoading = true;
					// 清空
					last = mViewStack.pop();

					last.fragments = mFragmentManager.getFragments();
					String name = last.name;
					// 回调
					LayoutRecord current = mViewStack.lastElement();
					last.child.onHide(current.name);
					last.destroy();
					last = null;
					PlusManager pm = PlusManager.getInstance(mActivity);
					pm.setActive(current.plus);
					if (pm.isActive()) {
						pm.loadResources(current.dex);
					}
					// 显示
					mCurrentIndex--;
					mFragmentManager.setFragments(current.fragments);
					setBackAnimation(name,current,false);

					mViewFlipper.setDisplayedChild(mCurrentIndex);
					mViewFlipper.removeViewAt(mCurrentIndex + 1);

				}
			} else {

				if (!mRootChild.onKeyBackHome()) {
					mActivity.finish();
				}

			}

		}

	}

	/**
	 * 用另一图层替代当前插件
	 * 
	 * @param child
	 */
	void replaceFromPlus(String classname, Intent intent) {
		Object o = null;
		try {
			Class<?> c = Class.forName(classname);
			o = c.newInstance();
		} catch (Exception e) {
			L.e(e);
		}
		if (U.notNull(o) && o instanceof ILayout) {
			ILayout child = (ILayout) o;
			if (!isLoading && U.notNull(mViewStack) && !mViewStack.isEmpty()
					&& mCurrentIndex >= 0 ) {
				isLoading = true;

				LayoutRecord last = mViewStack.lastElement();
				last.fragments = mFragmentManager.getFragments();
				mFragmentManager.setFragments(null);
				PlusManager pm = PlusManager.getInstance(mActivity);
				pm.setActive(false);
				LayoutRecord record = new LayoutRecord(child);
				record.view = child.onAttach(mLayoutInflater);
				if (U.isNull(record.view)) {
					mFragmentManager.setFragments(last.fragments);
					pm.setActive(last.plus);
					if(pm.isActive()){
						pm.loadResources(last.dex);
					}					
					record.view = null;
					record = null;
					reset();
					return;
				}
				record.name = child.getName();
				if (U.isNull(record.name)) {
					record.name = U.getName(child.getClass());
				}

				// 清空
				mViewStack.pop();
				mViewStack.push(record);
				mViewFlipper.addView(record.view);

				String name = last.name;
				last.child.onHide(record.name);
				last.destroy();
				last = null;


				pm.setIntent(intent);
				pm.setActive(false);
				
				setAddAnimation(name,record,true);
				mViewFlipper.setDisplayedChild(mCurrentIndex + 1);
				mViewFlipper.removeViewAt(mCurrentIndex);

			}
		}
	}
	
	/**
	 * 用另一图层替代当前图层
	 * 
	 * @param child
	 */
	public void replace(ILayout child) {

		if (!isLoading && U.notNull(mViewStack) && !mViewStack.isEmpty()
				&& mCurrentIndex >= 0 && U.notNull(child)) {
			isLoading = true;

			LayoutRecord last = mViewStack.lastElement();
			last.fragments = mFragmentManager.getFragments();
			mFragmentManager.setFragments(null);
			LayoutRecord record = new LayoutRecord(child);
			record.view = child.onAttach(mLayoutInflater);
			if (U.isNull(record.view)) {
				mFragmentManager.setFragments(last.fragments);
				record.destroy();
				record = null;
				reset();
				return;
			}
			record.name = child.getName();
			if (U.isNull(record.name)) {
				record.name = U.getName(child.getClass());
			}

			// 清空
			mViewStack.pop();
			mViewStack.push(record);
			mViewFlipper.addView(record.view);

			String name = last.name;
			last.child.onHide(record.name);
			last.destroy();
			last = null;

			setAddAnimation(name, record,false);
			mViewFlipper.setDisplayedChild(mCurrentIndex + 1);
			mViewFlipper.removeViewAt(mCurrentIndex);

		}

	}

	/**
	 * 用另一插件替代当前插件
	 * 
	 * @param child
	 */
	void replacePlus(String path, String classname, Intent intent) {

		if (!isLoading && U.notNull(mViewStack) && !mViewStack.isEmpty()
				&& mCurrentIndex >= 0 && U.notNull(path) && U.notNull(classname)) {
			isLoading = true;

			LayoutRecord last = mViewStack.lastElement();
			last.fragments = mFragmentManager.getFragments();
			mFragmentManager.setFragments(null);
			PlusManager pm = PlusManager.getInstance(mActivity);		
			PlusLayout child = pm.load(path, classname);
			if(U.isNull(child)){
				mFragmentManager.setFragments(last.fragments);
				pm.setActive(last.plus);
				if(pm.isActive()){
					pm.loadResources(last.dex);
				}
				reset();
				return;
			}
			LayoutRecord record = new LayoutRecord(child);
			record.plus = true;
			record.dex = path;
			pm.setActive(true);
			record.view = child.onAttach(mLayoutInflater);
			if (U.isNull(record.view)) {
				mFragmentManager.setFragments(last.fragments);
				pm.setActive(last.plus);
				if(pm.isActive()){
					pm.loadResources(last.dex);
				}
				record.destroy();
				record = null;
				reset();
				return;
			}
			record.name = child.getName();
			if (U.isNull(record.name)) {
				record.name = U.getName(child.getClass());
			}

			// 清空
			mViewStack.pop();
			mViewStack.push(record);
			mViewFlipper.addView(record.view);

			String name = last.name;
			last.child.onHide(record.name);
			last.destroy();
			last = null;
			
			pm.setIntent(intent);
			setAddAnimation(name,record,true);
			mViewFlipper.setDisplayedChild(mCurrentIndex + 1);
			mViewFlipper.removeViewAt(mCurrentIndex);

		}

	}

	/**
	 * 用栈顶的若干图层替代当前图层
	 * 
	 * @param child
	 */
	public void replace(int offset, ILayout child) {

		if (!isLoading && U.notNull(mViewStack) && !mViewStack.isEmpty()
				&& mCurrentIndex >= offset && offset >= 1 && U.notNull(child)) {
			isLoading = true;

			LayoutRecord last = mViewStack.lastElement();
			last.fragments = mFragmentManager.getFragments();
			mFragmentManager.setFragments(null);
			LayoutRecord record = new LayoutRecord(child);
			record.view = child.onAttach(mLayoutInflater);
			if (U.isNull(record.view)) {
				mFragmentManager.setFragments(last.fragments);
				return;
			}
			record.name = child.getName();
			if (U.isNull(record.name)) {
				record.name = U.getName(child.getClass());
			}

			// 清空
			String name = last.name;
			last.child.onHide(record.name);
			LayoutRecord pop = null;
			for (int i = 0; i < offset; i++) {
				pop = mViewStack.pop();
				pop.destroy();
				pop = null;

			}
			mViewStack.push(record);
			mViewFlipper.addView(record.view);

			setAddAnimation(name, record,false);
			mViewFlipper.setDisplayedChild(mCurrentIndex + 1);
			int start = mCurrentIndex - offset + 1;
			mCurrentIndex = start;
			mViewFlipper.removeViews(start, offset);

		}

	}

	/**
	 * 用栈顶的若干图层替代当前图层
	 * 
	 * @param child
	 */
	public void replace(int offset, ILayout... childs) {

		if (!isLoading && U.notNull(mViewStack) && !mViewStack.isEmpty()
				&& mCurrentIndex >= offset && offset >= 1 && U.notNull(childs)) {
			int length = childs.length - 1;
			if (length >= 0) {
				isLoading = true;

				LayoutRecord last = mViewStack.lastElement();
				last.fragments = mFragmentManager.getFragments();
				mFragmentManager.setFragments(null);

				ILayout child = null;
				LayoutRecord record = null;
				for (int i = 0; i <= length; i++) {
					child = childs[i];
					record = new LayoutRecord(child);
					record.view = child.onAttach(mLayoutInflater);
					record.name = child.getName();
					if (U.isNull(record.name)) {
						record.name = U.getName(child.getClass());
					}
					if (i < length) {
						record.fragments = mFragmentManager.getFragments();
						mFragmentManager.setFragments(null);
					}
					mViewStack.add(record);
					mViewFlipper.addView(record.view);
				}

				// 清空
				String name = last.name;
				last.child.onHide(record.name);
				LayoutRecord pop = null;
				int start = mCurrentIndex - offset + 1;
				for (int i = mCurrentIndex; i >= start; i--) {
					pop = mViewStack.remove(i);
					pop.destroy();
					pop = null;

				}

				setAddAnimation(name, record,false);
				mViewFlipper.setDisplayedChild(mCurrentIndex + length + 1);
				mCurrentIndex = mCurrentIndex - offset + length + 1;
				mViewFlipper.removeViews(start, offset);
			}
		}

	}

	/**
	 * 回到最低图层
	 */
	public void goRoot() {
		if (!isLoading && U.notNull(mViewStack) && !mViewStack.isEmpty()
				&& mCurrentIndex >= 1) {
			LayoutRecord last = mViewStack.lastElement();
			// 返回侦听
			if (!last.child.onGoBack()) {
				isLoading = true;
				String name = last.name;
				last.fragments = mFragmentManager.getFragments();
				LayoutRecord top = mViewStack.firstElement();
				last.child.onHide(top.name);
				LayoutRecord record = null;
				int size = mCurrentIndex;
				for (int i = 0; i < size; i++) {

					record = mViewStack.pop();
					record.destroy();
					record = null;

				}

				// 显示
				mFragmentManager.setFragments(top.fragments);
				setBackAnimation(name,top,false);
				mCurrentIndex = 0;
				mViewFlipper.setDisplayedChild(mCurrentIndex);
				mViewFlipper.removeViews(1, size);
			}
		}

	}

	/**
	 * 反向减少图层
	 * 
	 * @param offset
	 */
	public void goBack(int offset) {

		if (!isLoading && U.notNull(mViewStack) && !mViewStack.isEmpty()
				&& mCurrentIndex >= offset && offset >= 1) {
			LayoutRecord last = mViewStack.lastElement();
			// 返回侦听
			if (!last.child.onGoBack()) {
				// 开始加载
				isLoading = true;

				String name = last.name;
				last.fragments = mFragmentManager.getFragments();
				mCurrentIndex = mCurrentIndex - offset;
				LayoutRecord top = mViewStack.get(mCurrentIndex);
				last.child.onHide(top.name);
				LayoutRecord record = null;
				for (int i = 0; i < offset; i++) {
					record = mViewStack.pop();
					record.destroy();
					record = null;
				}

				PlusManager pm = PlusManager.getInstance(mActivity);
				pm.setActive(top.plus);
				if (pm.isActive()) {
					pm.loadResources(top.dex);
				}
				// 显示
				mFragmentManager.setFragments(top.fragments);
				setBackAnimation(name, top,false);
				mViewFlipper.setDisplayedChild(mCurrentIndex);
				mViewFlipper.removeViews(mCurrentIndex + 1, offset);
			}
		}

	}

	/**
	 * 计算当前层的层级差
	 * 
	 * @param index
	 * @return
	 */
	public int getOffset(int index) {
		if (index >= 0) {
			return mCurrentIndex - index;
		}
		return -1;

	}

	/**
	 * 计算当前层的层级差
	 * 
	 * @param name
	 * @return
	 */
	public int getOffset(String name) {
		return getOffset(findIndexByName(name));
	}

	/**
	 * 计算当前层的层级差
	 * 
	 * @param cls
	 * @return
	 */
	public int getOffset(Class cls) {
		return getOffset(findIndexByClass(cls));
	}

	/**
	 * 返回到某个图层
	 * 
	 * @param index
	 * 
	 */
	public void goBackTo(int index) {
		goBack(getOffset(index));
	}

	/**
	 * 返回到某个图层
	 * 
	 * @param index
	 * 
	 */
	public void goBackTo(String name) {
		goBackTo(findIndexByName(name));
	}

	/**
	 * 返回到某个图层
	 * 
	 * @param index
	 * 
	 */
	public void goBackTo(Class cls) {
		goBackTo(findIndexByClass(cls));
	}

	void onResume() {

		LayoutRecord top = getTop();
		if (U.notNull(top)) {
			if (U.notNull(mRootChild) && !mRootChild.onResume()) {
				top.child.onResume();
				mFragmentManager.postResume();
			}
		} else {
			setRoot();
		}

	}

	void onPause() {
		LayoutRecord top = getTop();
		if (U.notNull(top) && U.notNull(mRootChild) && !mRootChild.onPause()) {
			mFragmentManager.postPause();
			top.child.onPause();

		}
	}

	LayoutRecord getTop() {
		if (U.notNull(mViewStack) && !mViewStack.isEmpty()) {
			return mViewStack.lastElement();
		}
		return null;
	}

	void onActivityResult(int requestCode, int resultCode, Intent data) {
		LayoutRecord top = getTop();
		if (U.notNull(top)) {
			top.child.onActivityResult(requestCode, resultCode, data);

		}

	}

	boolean onKeyDown(int keyCode, KeyEvent event) {
		LayoutRecord top = getTop();
		if (U.notNull(top)) {
			if (!top.child.onKeyDown(keyCode, event)
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					goBack();
					return true;
				case KeyEvent.KEYCODE_MENU:
					mRootChild.onKeyMenu();
					return true;
				default:
					break;
				}
			}
			;
		}
		return false;
	}

	/**
	 * 查询某图层
	 * 
	 * @param cls
	 */
	public ILayout findLayoutByClass(Class cls) {
		return findLayoutByName(U.getName(cls));
	}

	/**
	 * 查询某图层
	 * 
	 * @param name
	 * @return
	 */
	public ILayout findLayoutByName(String name) {

		if (U.notNull(name) && U.notNull(mViewStack) && !mViewStack.isEmpty()) {

			for (LayoutRecord r : mViewStack) {

				if (name.equals(r.name)) {

					return r.child;

				}

			}

		}
		return null;
	}

	/**
	 * 查询某图层
	 * 
	 * @param cls
	 */
	public int findIndexByClass(Class cls) {
		return findIndexByName(U.getName(cls));
	}

	/**
	 * 通过类名查询某图层索引
	 * 
	 * @param name
	 * @return
	 */
	public int findIndexByName(String name) {
		if (U.notNull(name) && U.notNull(mViewStack) && !mViewStack.isEmpty()) {

			LayoutRecord record = null;

			for (int i = mCurrentIndex; i >= 0; i--) {

				record = mViewStack.get(i);
				if (name.equals(record.name)) {

					return i;

				}

			}

		}
		return -1;
	}

	/**
	 * 通过索引查询某图层
	 * 
	 * @param index
	 * @return
	 */
	public ILayout findLayoutByIndex(int index) {

		if (U.notNull(mViewStack) && !mViewStack.isEmpty()
				&& index <= mCurrentIndex && index >= 0) {

			return mViewStack.get(index).child;
		}
		return null;
	}

	/**
	 * 离开应用
	 */
	public void finish() {
		if (U.notNull(mActivity))
			mActivity.finish();
	}

	/**
	 * 仅供框架使用
	 */
	void destroy() {
		LayoutRecord record = getTop();
		if (U.notNull(record))
			record.fragments = mFragmentManager.getFragments();
		if (U.notNull(mViewStack)) {
			for (int i = 0; i <= mCurrentIndex; i++) {
				record = mViewStack.pop();
				if (U.notNull(record))
					record.destroy();
				record = null;
			}
		}

		mRootChild.onDetach();

		mActivity = null;

		mLayoutInflater = null;

		mViewStack = null;

		mViewFlipper = null;

		mMainFrame = null;

		mRootChild = null;

		mFragmentManager = null;

		mAnimatAddIn = null;

		mAnimatAddOut = null;

		mAnimatDelIn = null;

		mAnimatDelOut = null;

		mInstance = null;
	}

	/**
	 * 打印当前堆栈
	 * 
	 * @return
	 */
	public String printStack() {

		if (U.notNull(mViewStack) && !mViewStack.isEmpty()) {

			StringBuffer sb = new StringBuffer();

			for (LayoutRecord record : mViewStack) {
				sb.append(record.name).append(S.LINE);
			}

			return sb.toString();
		}
		return null;

	}

}
