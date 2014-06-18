package com.rio.layout;

import java.util.Collection;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.view.View;

import com.rio.core.BaseBroadcastReceiver;
import com.rio.core.U;


public class LayoutRecord {
	
	public LayoutRecord(ILayout child) {
		this.child = child;
	}
	
	View view;
	
	ILayout child;

	String name;// 相对类名

	LinkedHashMap<String, IFragment> fragments;// IFragment副本
	
	boolean plus;//是否为插件
	
	String dex;//插件的路径
	
	void destroy(){		
		
		if(U.notNull(fragments) && !fragments.isEmpty()){
			Collection<IFragment> list = fragments.values();
			for (IFragment f : list) {
				f.onDetach();
			}
			fragments.clear();
		}			
		fragments = null;		
		view = null;		
		name = null; 
		dex = null;
		child.onDetach();
		child = null;
	}
	
}
