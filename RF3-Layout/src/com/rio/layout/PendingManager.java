package com.rio.layout;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import com.rio.core.L;
import com.rio.core.U;

/**
 * 悬而不决的事务
 * 
 * @author rio
 * 
 */
public class PendingManager {

	private static PendingManager mInstance;

	private HashMap<String, Object> mPendingMap;

	private HashMap<String, Integer> mPendingStateMap;

	private static final int SIZE = 5;

	public static final int NONE = Integer.MIN_VALUE;

	private PendingManager() {
		mPendingMap = new HashMap<String, Object>(SIZE);
		mPendingStateMap = new HashMap<String, Integer>(SIZE);
	};

	/**
	 * 单例
	 * 
	 * @return
	 */
	public static PendingManager getInstance() {

		if (U.isNull(mInstance)) {
			mInstance = new PendingManager();
		}
		return mInstance;
	}

	/**
	 * 获取状态码
	 * 
	 * @param tag
	 * @param c
	 * @return
	 */
	public int getInt(String tag) {
		if (U.notNull(tag) && U.notNull(mPendingStateMap)
				&& mPendingStateMap.containsKey(tag)) {
			return mPendingStateMap.get(tag);
		}
		return NONE;
	}

	/**
	 * 获取状态码 然后删掉
	 * 
	 * @param tag
	 * @param c
	 * @return
	 */
	public int popInt(String tag) {

		if (U.notNull(tag) && U.notNull(mPendingStateMap)
				&& mPendingStateMap.containsKey(tag)) {

			int result = mPendingStateMap.get(tag);
			mPendingStateMap.remove(tag);
			return result;
		}
		return NONE;
	}

	/**
	 * 加入状态码
	 * 
	 * @param tag
	 * @param obj
	 */
	public boolean addInt(String tag, int value) {

		if (U.notNull(tag) && U.notNull(mPendingStateMap)) {
			mPendingStateMap.put(tag, value);
			return true;
		}
		return false;
	}

	/**
	 * 删除状态码
	 * 
	 * @param tag
	 * @return
	 */
	public boolean removeInt(String tag) {

		if (U.notNull(tag) && U.notNull(mPendingStateMap)
				&& mPendingStateMap.containsKey(tag)) {
			mPendingStateMap.remove(tag);
			return true;
		}
		return false;
	}

	/**
	 * 获取对象
	 * 
	 * @param tag
	 * @param c
	 * @return
	 */
	public Object getObj(String tag) {
		if (U.notNull(tag) && U.notNull(mPendingMap)
				&& mPendingMap.containsKey(tag)) {
			return mPendingMap.get(tag);
		}
		return null;
	}

	/**
	 * 获取对象 然后删掉
	 * 
	 * @param tag
	 * @param c
	 * @return
	 */
	public Object popObj(String tag) {

		if (U.notNull(tag) && U.notNull(mPendingMap)
				&& mPendingMap.containsKey(tag)) {

			Object result = mPendingMap.get(tag);
			mPendingMap.remove(tag);
			return result;
		}
		return null;
	}

	/**
	 * 加入事务队列
	 * 
	 * @param tag
	 * @param obj
	 */
	public boolean addObj(String tag, Object obj) {

		if (U.notNull(tag) && U.notNull(mPendingMap)) {
			mPendingMap.put(tag, obj);
			return true;
		}
		return false;
	}

	/**
	 * 删除事务
	 * 
	 * @param tag
	 * @return
	 */
	public boolean removeObj(String tag) {

		if (U.notNull(tag) && U.notNull(mPendingMap)
				&& mPendingMap.containsKey(tag)) {
			mPendingMap.remove(tag);
			return true;
		}
		return false;
	}

	/**
	 * 清空
	 */
	public void clear() {
		if (U.notNull(mPendingMap)) {
			mPendingMap.clear();
			mPendingMap = new HashMap<String, Object>(SIZE);
		}
		if (U.notNull(mPendingStateMap)) {
			mPendingStateMap.clear();
			mPendingStateMap = new HashMap<String, Integer>(SIZE);
		}
	}

	public void printState() {
		L.ii();
		if (U.notNull(mPendingStateMap) && !mPendingStateMap.isEmpty()) {
			Iterator it = mPendingStateMap.entrySet().iterator();
			Entry entry = null;
			while (it.hasNext()) {
				entry = (Entry) it.next();
				L.i(entry.getKey() + ":" + entry.getValue());
			}
		}else{
			L.i("STATE CONTENT NONE");
		}
		L.ii();
		if (U.notNull(mPendingMap) && !mPendingMap.isEmpty()) {
			Iterator it = mPendingMap.entrySet().iterator();
			Entry entry = null;
			while (it.hasNext()) {
				L.i(entry.getKey());
			}
		}else{
			L.i("OBJECT CONTENT NONE");
		}
	}
}
