package com.rio.layout;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.rio.core.L;
import com.rio.core.U;
import com.rio.layout.view.SimpleLayout;

import dalvik.system.DexClassLoader;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

public class PlusManager {

	private boolean isActive;

	private static PlusManager mInstance;

	private String mDexPath;

	private AssetManager mAssetManager;
	private Resources mResources;
	private Theme mTheme;
	private DexClassLoader mClassLoader;

	private Resources mSuperRes;

	private Theme mSuperTheme;

	private String mOutputPath;

	private static final String ACTION_LAYOUT_GOBACK = "RF3_ACTION_LAYOUT_GOBACK";

	private static final String ACTION_LAYOUT_ADD = "RF3_ACTION_LAYOUT_ADD";

	private static final String ACTION_LAYOUT_ADD_PLUS = "ACTION_LAYOUT_ADD_PLUS";

	private static final String ACTION_LAYOUT_REPLACE = "RF3_ACTION_LAYOUT_REPLACE";

	private static final String ACTION_LAYOUT_REPLACE_PLUS = "ACTION_LAYOUT_REPLACE_PLUS";

	private static final String EXTRA_LAYOUT_CLASSNAME = "RF3_EXTRA_LAYOUT_CLASSNAME";

	private static final String EXTRA_LAYOUT_DEXPATH = "RF3_EXTRA_LAYOUT_DEXPATH";

	private Context mContext;

	private Intent mIntent;

	private BroadcastReceiver mReceiver;

	private PlusManager(Context context) {
		isActive = false;
		mContext = context;
		mOutputPath = mContext.getDir("dex", Context.MODE_PRIVATE)
				.getAbsolutePath();
	}

	public static PlusManager getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new PlusManager(context);
		}
		return mInstance;
	}

	/**
	 * 获取用于传值的Intent
	 * 
	 * @return
	 */
	Intent getIntent() {
		return mIntent;
	}

	/**
	 * 设置Intent
	 * 
	 * @param intent
	 */
	void setIntent(Intent intent) {
		mIntent = intent;
	}

	void init(Resources r, Theme t) {
		mSuperRes = r;
		mSuperTheme = t;
		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (U.notNull(intent) && U.notNull(intent.getAction())) {
					String action = intent.getAction();
					if (ACTION_LAYOUT_GOBACK.equals(action)) {
						LayoutManager.getInstance().goBackFromPlus(intent);
					}
					if (ACTION_LAYOUT_ADD.equals(action)) {
						String name = intent
								.getStringExtra(EXTRA_LAYOUT_CLASSNAME);
						if (U.notNull(name)) {
							LayoutManager.getInstance().addAbovePlus(name,
									intent);
						}
					}
					if (ACTION_LAYOUT_ADD_PLUS.equals(action)) {
						String name = intent
								.getStringExtra(EXTRA_LAYOUT_CLASSNAME);
						String path = intent
								.getStringExtra(EXTRA_LAYOUT_DEXPATH);
						if (U.notNull(name) && U.notNull(path)) {
							LayoutManager.getInstance().addPlus(path, name,
									intent);
						}
					}
					if (ACTION_LAYOUT_REPLACE.equals(action)) {
						String name = intent
								.getStringExtra(EXTRA_LAYOUT_CLASSNAME);
						if (U.notNull(name)) {
						LayoutManager.getInstance().replaceFromPlus(name,
									intent);
						}
					}
					if (ACTION_LAYOUT_REPLACE_PLUS.equals(action)) {
						String name = intent
								.getStringExtra(EXTRA_LAYOUT_CLASSNAME);
						String path = intent
								.getStringExtra(EXTRA_LAYOUT_DEXPATH);
						if (U.notNull(name) && U.notNull(path)) {
							LayoutManager.getInstance().replacePlus(path, name,
									intent);
						}
					}
				}

			}
		};
		IntentFilter filter = new IntentFilter(ACTION_LAYOUT_GOBACK);
		filter.addAction(ACTION_LAYOUT_ADD);
		filter.addAction(ACTION_LAYOUT_ADD_PLUS);
		filter.addAction(ACTION_LAYOUT_REPLACE);
		filter.addAction(ACTION_LAYOUT_REPLACE_PLUS);

		mContext.registerReceiver(mReceiver, filter);
	}

	void destroy() {
		if (U.notNull(mContext) && U.notNull(mReceiver)) {
			mContext.unregisterReceiver(mReceiver);
			mReceiver = null;
		}

		mDexPath = null;
		mAssetManager = null;
		mResources = null;
		mTheme = null;
		mClassLoader = null;
		mSuperRes = null;
		mSuperTheme = null;
		mOutputPath = null;
		mContext = null;
		mIntent = null;
		mInstance = null;
	}

	void loadResources(String path) {
		if (U.isNull(mDexPath) || (U.notNull(path) && !mDexPath.equals(path))) {
			mDexPath = path;
			try {
				AssetManager assetManager = AssetManager.class.newInstance();
				Method addAssetPath = assetManager.getClass().getMethod(
						"addAssetPath", String.class);
				addAssetPath.invoke(assetManager, mDexPath);
				mAssetManager = assetManager;
			} catch (Exception e) {
				L.e(e);
				return;
			}
			mResources = new Resources(mAssetManager,
					mSuperRes.getDisplayMetrics(), mSuperRes.getConfiguration());
			mTheme = mResources.newTheme();
			mTheme.setTo(mSuperTheme);
			ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
			mClassLoader = new DexClassLoader(mDexPath, mOutputPath, null,
					localClassLoader);
		}
	}

	PlusLayout load(String path, String classname) {
		loadResources(path);
		PlusLayout plus = null;
		if (U.notNull(mClassLoader) && U.notNull(mContext)) {
			try {
				Class<?> localClass = mClassLoader.loadClass(classname);

				Constructor<?> localConstructor = localClass
						.getConstructor(new Class[] {});
				Object instance = localConstructor.newInstance(new Object[] {});
				plus = new PlusLayout(instance, classname, mContext, mContext.getApplicationContext(),localClass);
			} catch (Exception e) {
				L.e(e);
			}
		}
		return plus;
	}
	/**
	 * 页面操作--返回
	 */
	public void goBack() {
		goBack(null);
	}

	/**
	 * 页面操作--返回
	 */
	public void goBack(Intent intent) {
		if (U.notNull(mContext)) {
			if (U.isNull(intent)) {
				intent = new Intent(ACTION_LAYOUT_GOBACK);
			} else {
				intent.setAction(ACTION_LAYOUT_GOBACK);
			}
			mContext.sendBroadcast(intent);
		}
	}

	/**
	 * 页面操作--添加插件
	 */
	public void add(String path, String classname, Intent intent) {
		if (U.notNull(mContext)) {
			if (U.isNull(intent)) {
				intent = new Intent(ACTION_LAYOUT_ADD_PLUS);
			} else {
				intent.setAction(ACTION_LAYOUT_ADD_PLUS);
			}
			intent.putExtra(EXTRA_LAYOUT_CLASSNAME, classname);
			intent.putExtra(EXTRA_LAYOUT_DEXPATH, path);
			mContext.sendBroadcast(intent);
		}
	}

	/**
	 * 页面操作--添加图层
	 */
	public void add(String classname, Intent intent) {
		if (U.notNull(mContext)) {
			if (U.isNull(intent)) {
				intent = new Intent(ACTION_LAYOUT_ADD);
			} else {
				intent.setAction(ACTION_LAYOUT_ADD);
			}
			intent.putExtra(EXTRA_LAYOUT_CLASSNAME, classname);
			mContext.sendBroadcast(intent);
		}
	}

	/**
	 * 页面操作--替换插件
	 */
	public void replace(String path, String classname, Intent intent) {
		if (U.notNull(mContext)) {
			if (U.isNull(intent)) {
				intent = new Intent(ACTION_LAYOUT_REPLACE_PLUS);
			} else {
				intent.setAction(ACTION_LAYOUT_REPLACE_PLUS);
			}
			intent.putExtra(EXTRA_LAYOUT_CLASSNAME, classname);
			intent.putExtra(EXTRA_LAYOUT_DEXPATH, path);
			mContext.sendBroadcast(intent);
		}
	}

	/**
	 * 页面操作--替换图层
	 */
	public void replace(String classname, Intent intent) {
		if (U.notNull(mContext)) {
			if (U.isNull(intent)) {
				intent = new Intent(ACTION_LAYOUT_REPLACE);
			} else {
				intent.setAction(ACTION_LAYOUT_REPLACE);
			}
			intent.putExtra(EXTRA_LAYOUT_CLASSNAME, classname);
			mContext.sendBroadcast(intent);
		}
	}

	/**
	 * 是否插件状态
	 * 
	 * @return
	 */
	boolean isActive() {
		return isActive;
	}

	/**
	 * 设置插件状态
	 * 
	 * @param isPlus
	 */
	void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * 获取插件的资源
	 * 
	 * @return
	 */
	AssetManager getAssetManager() {
		return mAssetManager;
	}

	/**
	 * 获取插件的资源
	 * 
	 * @return
	 */
	Resources getResources() {
		return mResources;
	}

	Theme getTheme() {
		return mTheme;
	}

}
