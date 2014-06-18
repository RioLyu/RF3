package com.rio.core;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * 管理View生命周期，页面缓存的Adapter
 * 
 * @author rio
 * @param <T>
 * @version 1.1
 */
public abstract class BaseAdapter<T> extends BaseArrayAdapter<T> {

	/**
	 * 没有选中项的默认-1
	 */
	public static final int INVALID_POSITION = -1;

	private int mSelectPositison = INVALID_POSITION;

	private LayoutInflater mInflater;
	private int mLayoutId;
	private boolean isCacheView;
	private SparseArray<View> mCacheView;


	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文
	 * @param resource
	 *            每个item的布局
	 * @param objects
	 *            数据
	 * @param useCache
	 *            是否使用缓存,默认开启
	 */
	public BaseAdapter(Context context, int resource, List<T> objects,
			boolean useCache) {
		super(objects);
		init(context, resource, useCache);

	}

	/**
	 * 构造函数
	 * 
	 * @param context
	 *            上下文
	 * @param resource
	 *            每个item的布局
	 * @param objects
	 *            数据
	 * @param useCache
	 *            是否使用缓存,默认开启
	 */
	public BaseAdapter(Context context, int resource, T[] objects,
			boolean useCache) {
		super(objects);
		init(context, resource, useCache);

	}

	/**
	 * 默认使用缓存的构造函数 {@link #BaseAdapter},重写bindItemView方法
	 * 
	 * @param context
	 *            上下文
	 * @param resource
	 *            每个item的布局
	 * @param objects
	 *            数据
	 */
	public BaseAdapter(Context context, int resource, List<T> objects) {
		this(context, resource, objects, true);
	}

	/**
	 * 默认使用缓存的构造函数 {@link #BaseAdapter},重写bindItemView方法
	 * 
	 * @param context
	 *            上下文
	 * @param resource
	 *            每个item的布局
	 * @param objects
	 *            数据
	 */
	public BaseAdapter(Context context, int resource, T[] objects) {
		this(context, resource, objects, true);
	}

	private void init(Context context, int resource, boolean useCache) {

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLayoutId = resource;
		isCacheView = useCache;
		mCacheView = new SparseArray<View>();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		T item = getItem(position);		
		View view = mCacheView.get(position);
		
		if (view == null || !isCacheView) {									
			view = bindItemView(createItemLayout(parent,position,item),parent,position,mSelectPositison,false,item);	
		} else {
			view = bindItemView(view, parent,position,mSelectPositison,true,item);
		}
					
		return view;
	}

	private View createItemLayout(ViewGroup parent,int position,T item) {
		View view = mInflater.inflate(mLayoutId, parent, false);
		if(isCacheView){
			view.setTag(getViewHolder(view,item));	
			mCacheView.put(position, view);
		}		
		return view;
	}

	/**
	 * 绑定数据到item
	 * 
	 * @param view
	 * 				生成的View
	 * @param position
	 *            	当前的位置
	 * @param selected
	 *            	被选中的位置，如果没有等于 INVALID_POSITION
	 * @param hasCache
	 * 				是否已经生成View，即是否已赋过值
	 * @param item
	 * @return
	 */
	protected abstract View bindItemView(View view, ViewGroup parent,int position, int selected,boolean hasCache, T item);

	/**
	 * 绑定到VIEW的tag,默认是item，但可以用作ViewHolder提高效率
	 * @param view
	 * @param item
	 * @return
	 */
	protected Object getViewHolder(View view,T item) {
		return item;
	}
	/**
	 * 设置选择项
	 * 
	 * @param position
	 */
	public void setSelectedItem(int position) {
		mSelectPositison = position;
		notifyDataSetChanged();
	}

	
	/**
	 * 清空缓存
	 */
	public void rebindView(){
		if(isCacheView)mCacheView.clear();
		notifyDataSetChanged();
		
	}
	
	@Override
	public void replaceAll(List<T> list) {
		if(isCacheView)mCacheView.clear();
		super.replaceAll(list);
	}


}
