package com.rio.layout;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.rio.core.L;
import com.rio.core.U;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

public class PlusLayout implements ILayout {

	private String mName;

	private Context mContext;

	private Object mPlus;

	private HashMap<String, Method> mLifecircleMethods;

	public PlusLayout(Object plus, String name, Context context,Context host,
			Class<?> localClass) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		mContext = context;
		mName = name;
		mPlus = plus;
		instantiateLifecircleMethods(localClass);
		Method method = getMethodMap().get("init");
		if (U.notNull(method)) {

			method.invoke(mPlus, new Object[] { mContext, host});

		}
	}

	private HashMap<String, Method> getMethodMap() {
		if (U.isNull(mLifecircleMethods)) {
			mLifecircleMethods = new HashMap<String, Method>();
		}
		return mLifecircleMethods;
	}

	private void instantiateLifecircleMethods(Class<?> localClass) {

		Method method = null;
		try {
			method = localClass.getMethod("onAttach",
					new Class[] { LayoutInflater.class });
			method.setAccessible(true);
			getMethodMap().put("onAttach", method);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		try {
			method = localClass
					.getMethod("init", new Class[] { Context.class,Context.class });
			method.setAccessible(true);
			getMethodMap().put("init", method);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		try {
			method = localClass.getMethod("onResume", new Class[] {});
			method.setAccessible(true);
			getMethodMap().put("onResume", method);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		try {
			method = localClass.getMethod("onPause", new Class[] {});
			method.setAccessible(true);
			getMethodMap().put("onPause", method);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		try {
			method = localClass.getMethod("onDetach", new Class[] {});
			method.setAccessible(true);
			getMethodMap().put("onDetach", method);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		try {
			method = localClass.getMethod("onHide",
					new Class[] { String.class });
			method.setAccessible(true);
			getMethodMap().put("onHide", method);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		try {
			method = localClass.getMethod("onDisplay", new Class[] {
					String.class, View.class, Intent.class });
			method.setAccessible(true);
			getMethodMap().put("onDisplay", method);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		try {
			method = localClass.getMethod("onGoBack", new Class[] {});
			method.setAccessible(true);
			getMethodMap().put("onGoBack", method);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onGoBack() {
		Method on = getMethodMap().get("onGoBack");
		if (U.notNull(mPlus) && U.notNull(on)) {

			try {
				return (Boolean) on.invoke(mPlus, new Object[] {});
			} catch (Exception e) {
				L.e(e);
			}
		}
		return false;
	}

	@Override
	public void onDisplay(String callerName, View view, int frag,
			Object... params) {
		Method on = getMethodMap().get("onDisplay");
		if (U.notNull(mPlus) && U.notNull(on)) {
			Intent i = new Intent();
			if (U.size(1, params) && params[0] instanceof Intent) {
				i = (Intent) params[0];
			}
			try {
				on.invoke(mPlus, new Object[] { callerName, view, i });
			} catch (Exception e) {
				L.e(e);
			}
		}
	}

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public View onAttach(LayoutInflater inflater) {
		Method on = getMethodMap().get("onAttach");
		if (U.notNull(mPlus) && U.notNull(on)) {
			try {
				View view = (View) on.invoke(mPlus, new Object[] { inflater });
				return view;
			} catch (Exception e) {
				L.e(e);
			}
		}
		return null;
	}

	@Override
	public void onResume() {
		Method on = getMethodMap().get("onResume");
		if (U.notNull(mPlus) && U.notNull(on)) {

			try {
				on.invoke(mPlus, new Object[] {});
			} catch (Exception e) {
				L.e(e);
			}
		}

	}

	@Override
	public void onPause() {
		Method on = getMethodMap().get("onPause");
		if (U.notNull(mPlus) && U.notNull(on)) {

			try {
				on.invoke(mPlus, new Object[] {});
			} catch (Exception e) {
				L.e(e);
			}
		}

	}

	@Override
	public void onDetach() {
		Method on = getMethodMap().get("onDetach");
		if (U.notNull(mPlus) && U.notNull(on)) {

			try {
				on.invoke(mPlus, new Object[] {});
			} catch (Exception e) {
				L.e(e);
			}
		}
		if(U.isNull(mLifecircleMethods)){
			mLifecircleMethods.clear();
			mLifecircleMethods =null;
		}
		mPlus = null;
		mContext = null;
	}

	@Override
	public void onHide(String nextName) {
		Method on = getMethodMap().get("onHide");
		if (U.notNull(mPlus) && U.notNull(on)) {
			try {
				on.invoke(mPlus, new Object[] { nextName });
			} catch (Exception e) {
				L.e(e);
			}
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

	}

	@Override
	public Animation getAnimatAddIn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Animation getAnimatAddOut() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Animation getAnimatDelIn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Animation getAnimatDelOut() {
		// TODO Auto-generated method stub
		return null;
	}



}
