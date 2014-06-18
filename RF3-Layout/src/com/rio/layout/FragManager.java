package com.rio.layout;

import java.util.Collection;
import java.util.LinkedHashMap;

import com.rio.core.L;
import com.rio.core.U;
import android.app.Activity;
import android.util.AttributeSet;
import android.view.View;


public class FragManager {

	private static FragManager mInstance;

	private static final String MSG_SAME_TAG = "some fragment in this activity have the same tag";

	private LinkedHashMap<String, IFragment> mFragmentList = null;
	
	private ILayout mLayout;

	private FragManager() {}

	public static FragManager getInstance() {

		if (U.isNull(mInstance)) {
			mInstance = new FragManager();
		}
		return mInstance;

	}

	public ILayout getLayout() {
		return mLayout;
	}

	void setLayout(ILayout layout) {
		this.mLayout = layout;
	}

	/**
	 * 加入一个Fragment
	 */
	public void register(IFragment fragment) {
		if (U.notNull(fragment)) {
			String flag = fragment.getFlag();
			if (U.isNull(flag)) {
				flag = U.getName(fragment.getClass());
			}
			if (U.isNull(mFragmentList)) {
				mFragmentList = new LinkedHashMap<String, IFragment>();
			}
			if (mFragmentList.containsKey(flag)) {
				L.e(MSG_SAME_TAG);
			}
			getFragments().put(flag, fragment);
			fragment.onAttach();
		}
	}

	/**
	 * 删除一个Fragment
	 */
	public void unregister(String flag) {
		IFragment fragment = findFragmentByFlag(flag);
		if (U.notNull(fragment)) {
			fragment.onDetach();
			mFragmentList.remove(flag);
		}
	}

	/**
	 * 由Tag去获取Fragment,如果没有返回空
	 * 
	 * @param id
	 * @return
	 */
	public IFragment findFragmentByFlag(String flag) {
		if (U.notNull(mFragmentList) && mFragmentList.containsKey(flag)) {
			return mFragmentList.get(flag);
		}
		return null;
	}
	
	/**
	 * 由Class去获取Fragment,如果没有返回空
	 * 
	 * @param id
	 * @return
	 */
	public IFragment findFragmentByClass(Class cls) {
		return findFragmentByFlag(U.getName(cls));
	}


	/**
	 * 广播OnDisplay事件
	 * @param content
	 * @param callerName
	 * @param frag
	 * @param caller
	 * @param params
	 */
	void postDisplay(String callerName, int frag,Object... params) {
		if (U.notNull(mFragmentList) && !mFragmentList.isEmpty()) {
			Collection<IFragment> list = mFragmentList.values();
			for (IFragment f : list) {
				f.onDisplay(callerName, frag, params);
			}
		}
	}
	
	/**
	 * 广播OnDisplay事件,返回false表示不拦截，返回true表示不返回;
	 */
	boolean postGoBack() {
		if (U.notNull(mFragmentList) && !mFragmentList.isEmpty()) {
			Collection<IFragment> list = mFragmentList.values();
			for (IFragment f : list) {
				if(f.onGoBack())
					return true;
			}
		}
		return false;
	}
			
	/**
	 * 广播onPause事件
	 */
	void postPause() {
		if (U.notNull(mFragmentList) && !mFragmentList.isEmpty()) {
			Collection<IFragment> list = mFragmentList.values();
			for (IFragment f : list) {
				f.onPause();
			}
		}
	}

	/**
	 * 广播onResume事件
	 */
	void postResume() {
		if (U.notNull(mFragmentList) && !mFragmentList.isEmpty()) {
			Collection<IFragment> list = mFragmentList.values();
			for (IFragment f : list) {
				f.onResume();
			}
		}
	}

	/**
	 * 仅供框架使用
	 * @return
	 */
	LinkedHashMap<String, IFragment> getFragments() {
		return mFragmentList;
	}
	
	/**
	 * 仅供框架使用
	 * @param list
	 */
	void setFragments(LinkedHashMap<String, IFragment> list) {
		mFragmentList = list;
	}
	
	/**
	 * 仅供框架使用
	 */
	void destroyAllFragment() {
		if (U.notNull(mFragmentList) && !mFragmentList.isEmpty()) {
			Collection<IFragment> list = mFragmentList.values();
			for (IFragment f : list) {
				f.onDetach();
			}
			mFragmentList.clear();
		}
	}

	/**
	 * 仅供框架使用
	 */
	void destroy() {
		mInstance = null;
		mFragmentList = null;
		mLayout = null;
	}	
}
