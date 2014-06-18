/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rio.core;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;
import android.widget.BaseAdapter;


/**
 * 管理数据的adapter，注意这里只是操作数据，不等于操作页面缓存
 * @author rio
 *
 * @param <T>
 */
public abstract class BaseArrayAdapter<T> extends BaseAdapter {
    /**
     * Contains the list of objects that represent the data of this ArrayAdapter.
     * The content of this list is referred to as "the array" in the documentation.
     */
    private List<T> mObjects;

    /**
     * Lock used to modify the content of {@link #mObjects}. Any write operation
     * performed on the array should be synchronized on this lock. This lock is also
     * used by the filter (see {@link #getFilter()} to make a synchronized copy of
     * the original array of data.
     */
    private final Object mLock = new Object();


    /**
     * Indicates whether or not {@link #notifyDataSetChanged()} must be called whenever
     * {@link #mObjects} is modified.
     */
    private boolean mNotifyOnChange;
   

    /**
     * Constructor
     */
    public BaseArrayAdapter() {
        init(null);
    }


    /**
     * Constructor
     *
     * @param objects The objects to represent in the ListView.
     */
    public BaseArrayAdapter(T[] objects) {
    	if(objects!=null){
    		init(Arrays.asList(objects));
    	}else{
    		init(null);
    	}
        
    }


    /**
     * Constructor
     *
     * @param objects The objects to represent in the ListView.
     */
    public BaseArrayAdapter(List<T> objects) {
        init(objects);
    }

    private void init(List<T> objects) {
    	if(objects ==null){
    		objects = new ArrayList<T>(1);
    	}
        mObjects = objects;
        mNotifyOnChange = true;
    }
    
    /**
     * 当前数据集
     * @return
     */
    public List<T> getArrayList(){
    	return mObjects;
    }
    
    /**
     * 加到末尾
     *
     * @param object The object to add at the end of the array.
     */
    public void add(T object) {
        if (mObjects != null) {
            synchronized (mLock) {
            	mObjects.add(object);
                if (mNotifyOnChange) notifyDataSetChanged();
            }
        } 
    }

    /**
     * 加到某个位置
     *
     * @param object The object to insert into the array.
     * @param index The index at which the object must be inserted.
     */
    public void insert(T object, int index) {
        if (mObjects != null) {
            synchronized (mLock) {
            	mObjects.add(index, object);
                if (mNotifyOnChange) notifyDataSetChanged();
            }
        } 
    }

    /**
     * 删除某一个数据,注意这里只是操作数据，不等于操作页面缓存
     *
     * @param object The object to remove.
     */
    public void remove(T object) {
        if (mObjects != null) {
            synchronized (mLock) {
            	mObjects.remove(object);
            	if (mNotifyOnChange) notifyDataSetChanged();
            }
        }        
    }
    
    /**
     * 删除某一个数据
     *
     * @param object The object to remove.
     */
    public void remove(int index) {
        if (mObjects != null) {
            synchronized (mLock) {
            	mObjects.remove(index);
            	if (mNotifyOnChange) notifyDataSetChanged();
            }
        }        
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        if (mObjects != null) {
            synchronized (mLock) {
            	mObjects.clear();
            	 if (mNotifyOnChange) notifyDataSetChanged();
            }
        }        
    }
   
    
    /**
     * 全部数据替换
     * @param list
     */
    public void replaceAll(List<T> list){
        if (mObjects != null) {
            synchronized (mLock) {
            	mObjects.clear();
            	if(list != null)
            		mObjects.addAll(list);
            	if(mNotifyOnChange) notifyDataSetChanged();
            }
        }       	    	
    }
    
    /**
     * 添加到末尾
     * @param list
     */
    public void addAll(List<T> list){
        if (mObjects != null) {
            synchronized (mLock) {
            	if(list != null)
            		mObjects.addAll(list);
            	if(mNotifyOnChange) notifyDataSetChanged();
            }
        }     	
   }
    
    /**
     * 替换某个数据
     * @param list
     */
    public void replace(T object,int index){
        if (mObjects != null && index < getCount()) {
            synchronized (mLock) {            	
            	mObjects.set(index, object);
            	if (mNotifyOnChange) notifyDataSetChanged();
            }
        }       	    	
    }    
    

    /**
     * 排序
     *
     * @param comparator The comparator used to sort the objects contained
     *        in this adapter.
     */
    public void sort(Comparator<? super T> comparator) {
        Collections.sort(mObjects, comparator);
        if (mNotifyOnChange) notifyDataSetChanged();        
    }


    
    

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    /**
     * 需不需要数据操作就刷新页面
     *
     * @param notifyOnChange if true, modifications to the list will
     *                       automatically call {@link
     *                       #notifyDataSetChanged}
     */
    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }
    
    /**
     * 需不需要数据操作就刷新页面
     *
     * @param notifyOnChange if true, modifications to the list will
     *                       automatically call {@link
     *                       #notifyDataSetChanged}
     */
    public boolean getNotifyOnChange() {
        return mNotifyOnChange;
    } 
    
    /**
     * {@inheritDoc}
     */
    public int getCount() {
        return mObjects.size();
    }

    /**
     * {@inheritDoc}
     */
    public T getItem(int position) {
        return mObjects.get(position);
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     *
     * @return The position of the specified item.
     */
    public int getPosition(T item) {
        return mObjects.indexOf(item);
    }

    /**
     * {@inheritDoc}
     */
    public long getItemId(int position) {
        return position;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position,convertView,parent);
    }


}
