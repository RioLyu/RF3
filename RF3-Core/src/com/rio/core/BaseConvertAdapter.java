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
public abstract class BaseConvertAdapter<T> extends BaseArrayAdapter<T> {

	private LayoutInflater mInflater;
	private int mLayoutId;

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
	public BaseConvertAdapter(Context context, int resource, List<T> objects) {
		super(objects);
		init(context, resource);

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
	 */
	public BaseConvertAdapter(Context context, int resource, T[] objects) {
		super(objects);
		init(context, resource);

	}

	private void init(Context context, int resource) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLayoutId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {		
		Object holder = null;
		if(convertView == null){
			convertView = mInflater.inflate(mLayoutId, parent, false);
			holder = getViewHolder(convertView);
			convertView.setTag(holder);
		}else{
			holder = convertView.getTag();
		}					
		return bindItemView(convertView,position,holder,getItem(position));
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
	protected View bindItemView(View view, int position, Object object,T item){
		return view;
	};

	/**
	 * 绑定到VIEW的tag,默认是item，但可以用作ViewHolder提高效率
	 * @param view
	 * @param item
	 * @return
	 */
	protected abstract Object getViewHolder(View view);


}
