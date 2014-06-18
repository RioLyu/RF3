package com.rio.helper;


import com.rio.core.L;
import com.rio.core.U;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Toast类自定义样式
 * 调用前必须在某个全局的地方设置ToastHelper.setup()
 * @author rio
 *
 */
public class ToastHelper {
	
	private static Toast mToast;	
	private static TextView mText;	
	private static int mDuration = Toast.LENGTH_SHORT;
	private final static String MESSAGE_NOT_SETUP = "ToastHelper do not setup";
	
	/**
	 * 显示Toast
	 * @param id		字符串资源
	 */
	public static void show(int id){
		show(id,mDuration);
	};
	
	/**
	 * 显示Toast
	 * @param id		字符串资源
	 * @param duration	时间长短Toast.LENGTH_SHORT、Toast.LENGTH_LONG
	 */
	public static void show(int id,int duration){
		if(U.notNull(mToast)&& U.notNull(mText)){
			mText.setText(id);
			mToast.setDuration(duration);
			mToast.show();
		}else{
			L.e(MESSAGE_NOT_SETUP);
		}
	};
	
	/**
	 * 显示Toast
	 * @param msg		字符串
	 */
	public static void show(String msg){
		show(msg,mDuration);
	};
	
	/**
	 * 显示Toast
	 * @param msg		字符串
	 * @param duration	时间长短Toast.LENGTH_SHORT、Toast.LENGTH_LONG
	 */
	public static void show(String msg,int duration){
		if(U.notNull(mToast)&& U.notNull(mText)){
			mText.setText(msg);
			mToast.setDuration(duration);
			mToast.show();
		}else{
			L.e(MESSAGE_NOT_SETUP);
		}
	};	
	
	/**
	 * 初始化
	 * @param context
	 * @param layout	xml
	 * @param textid	文字信息TextView的id
	 * @param gravity	方位
	 */
	public static void setup(Context context,int layout,int textid,int gravity){
		if(U.isNull(mToast)&& U.isNull(mText)){
			mToast = new Toast(context);
			View view = LayoutInflater.from(context).inflate(layout, null);
			mText = (TextView) view.findViewById(textid);
			mToast.setGravity(gravity, 0, 0);
			mToast.setView(view);
		}
	}
	
	/**
	 * 清除
	 */
	public static void clear(){
		if(U.notNull(mToast)&& U.notNull(mText)){
			mToast = null;
			mText = null;
		}
	}
	
	

}
