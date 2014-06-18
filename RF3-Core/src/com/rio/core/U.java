package com.rio.core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StatFs;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 工具类
 * 
 * @author rio
 * @version 1.1
 */
public class U {

	/**
	 * 返回SD卡是否可用
	 * 
	 * @return
	 */
	public static boolean isSDCardAvaiable() {

		String status = Environment.getExternalStorageState();

		return Environment.MEDIA_MOUNTED.equals(status);

	}

	/**
	 * 显示Toast
	 * 
	 * @param context
	 * @param msg
	 * @param gravity
	 */
	public static void showToast(Context context, int msgid) {
		Toast toast = Toast.makeText(context, msgid, Toast.LENGTH_SHORT);
		// toast.setGravity(Gravity.BOTTOM, 0, 0);
		toast.show();
	}

	/**
	 * 显示Toast
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showToast(Context context, CharSequence msg) {
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/**
	 * 显示Toast
	 * 
	 * @param context
	 * @param stringid
	 * @param values
	 */
	public static void showToast(Context context, int stringid,
			Object... values) {
		String msg = getFormatString(context, stringid, values);
		showToast(context, msg);
	}

	/**
	 * 格式化字符串
	 * 
	 * @param context
	 * @param stringid
	 * @param values
	 * @return
	 */
	public static String getFormatString(Context context, int stringid,
			Object... values) {
		return String
				.format(context.getResources().getString(stringid), values);
	}

	/**
	 * 启动动画
	 * 
	 * @param context
	 * @param view
	 * @param animid
	 */
	public static void showAnimation(Context context, View view, int animid) {
		Animation anim = AnimationUtils.loadAnimation(context, animid);
		view.startAnimation(anim);
	}

	/**
	 * 启动动画
	 * 
	 * @param context
	 * @param view
	 * @param animid
	 * @param listener
	 */
	public static void showAnimation(Context context, View view, int animid,
			AnimationListener listener) {
		Animation anim = AnimationUtils.loadAnimation(context, animid);
		anim.setAnimationListener(listener);
		view.startAnimation(anim);
	}

	/**
	 * 获取图片
	 * 
	 * @param context
	 * @param drawableid
	 * @return
	 */
	public static Drawable getDrawable(Context context, int drawableid) {
		return context.getResources().getDrawable(drawableid);
	}

	/**
	 * 显示与隐藏
	 * 
	 * @param view
	 */
	public static void toggleVisibility(View view) {
		if (view.getVisibility() == View.GONE) {
			view.setVisibility(View.VISIBLE);
		} else {
			view.setVisibility(View.GONE);
		}
	}

	/**
	 * 转字符串
	 * 
	 * @param num
	 * @return
	 */
	public static String toString(int num) {
		return num + "";
	}

	/**
	 * 转字符串
	 * 
	 * @param num
	 * @return
	 */
	public static String toString(short num) {
		return num + "";
	}

	/**
	 * 转字符串
	 * 
	 * @param num
	 * @return
	 */
	public static String toString(long num) {
		return num + "";
	}

	/**
	 * 转字符串
	 * 
	 * @param num
	 * @return
	 */
	public static String toString(float num) {
		return num + "";
	}

	/**
	 * 转字符串
	 * 
	 * @param num
	 * @return
	 */
	public static String toString(double num) {
		return num + "";
	}

	/**
	 * 转字符串,如果为空或“”则转化为默认字符
	 * 
	 * @param num
	 * @return
	 */
	public static String toString(String s, String defaultValue) {
		return U.notNull(s) ? s : defaultValue;
	}

	/**
	 * 字符串转为整形
	 * 
	 * @param num
	 * @return
	 */
	public static int toInt(String num) {
		if (U.notNull(num)) {
			return Integer.valueOf(num);
		} else {
			return 0;
		}
	}

	/**
	 * 字符串转为long
	 * 
	 * @param num
	 * @return
	 */
	public static long toLong(String num) {
		if (U.notNull(num)) {
			return Long.valueOf(num);
		} else {
			return 0;
		}
	}

	/**
	 * 字符串转为float
	 * 
	 * @param num
	 * @return
	 */
	public static float toFloat(String num) {
		if (U.notNull(num)) {
			return Float.valueOf(num);
		} else {
			return 0;
		}
	}

	/**
	 * 字符串转为double
	 * 
	 * @param num
	 * @return
	 */
	public static double toDouble(String num) {
		if (U.notNull(num)) {
			return Double.valueOf(num);
		} else {
			return 0;
		}
	}

	/**
	 * 整形转为boolean
	 * 
	 * @param i
	 * @return
	 */
	public static boolean toBoolean(int i) {
		if (U.isNull(i)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 字符串转为boolean
	 * 
	 * @param i
	 * @return
	 */
	public static boolean toBoolean(String s) {
		if (U.isNull(s) || S.ZERO.equals(s) || S.FALSE.equals(s)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断是否有足够的参数
	 * 
	 * @param size
	 * @param params
	 * @return
	 */
	public static boolean size(int size, Object... params) {
		if (U.notNull(params)) {
			return size == params.length;
		}
		return false;
	}

	/**
	 * 判断是否为空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean notNull(CharSequence str) {
		return !isNull(str);
	}

	/**
	 * 判断是否为空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean notNull(CharSequence... str) {
		return !isNull(str);
	}

	/**
	 * obj != null
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean notNull(Object obj) {
		return !isNull(obj);
	}

	/**
	 * num != 0
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean notNull(int num) {
		return !isNull(num);
	}

	/**
	 * num != 0
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean notNull(float num) {
		return !isNull(num);
	}

	/**
	 * num != 0
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean notNull(double num) {
		return !isNull(num);
	}

	/**
	 * Object[] length > 0
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean notNull(Object[] list) {
		return !isNull(list);
	}

	/**
	 * 判断是否为空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNull(CharSequence str) {
		return TextUtils.isEmpty(str);
	}

	/**
	 * 判断是否为空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNull(CharSequence... str) {
		if (str != null && str.length > 0) {
			for (CharSequence c : str) {
				if (U.isNull(c))
					return true;
			}
			return false;
		}
		return true;
	}

	/**
	 * obj == null
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj) {
		return obj == null;
	}

	/**
	 * num == 0
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNull(int num) {
		return num == 0;
	}

	/**
	 * num == 0
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNull(double num) {
		return num == 0;
	}

	/**
	 * num == 0
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNull(float num) {
		return num == 0;
	}

	/**
	 * Object[] length == 0
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object[] list) {
		return list == null || list.length == 0;
	}
	


	/**
	 * 读InputStream流
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static byte[] openStream(InputStream is) throws IOException {

		if (is == null)
			return null;

		ByteArrayOutputStream os = new ByteArrayOutputStream();

		byte[] buff = new byte[1024];

		int rc = 0;

		while ((rc = is.read(buff, 0, buff.length)) > 0) {

			os.write(buff, 0, rc);

		}

		buff = os.toByteArray();

		is.close();
		os.close();

		return buff;

	}

	/**
	 * dip转像素
	 * 
	 * @param density
	 * @param dip
	 * @return
	 */
	public static int convertDipToPx(float density, int dip) {
		return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
	}

	/**
	 * 像素转dip
	 * 
	 * @param density
	 * @param px
	 * @return
	 */
	public static int convertPxToDip(float density, int px) {
		return (int) (px / density + 0.5f * (px >= 0 ? 1 : -1));
	}

	/**
	 * <pre>
	 * 震动
	 * <uses-permission android:name="android.permission.VIBRATE" />
	 * </pre>
	 * 
	 * @param context
	 * @param milliseconds
	 *            震动的时长，单位是毫秒
	 */
	public static void vibrate(Context context, long milliseconds) {
		Vibrator vib = (Vibrator) context
				.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}

	/**
	 * <pre>
	 * 震动
	 * <uses-permission android:name="android.permission.VIBRATE" />
	 * </pre>
	 * 
	 * @param context
	 * @param pattern
	 *            自定义震动模式 。数组中数字的含义依次是静止的时长，震动时长，静止时长，震动时长。。。时长的单位是毫秒
	 * @param isRepeat
	 *            是否反复震动，如果是true，反复震动，如果是false，只震动一次
	 */
	public static void vibrate(Context context, long[] pattern, boolean isRepeat) {
		Vibrator vib = (Vibrator) context
				.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(pattern, isRepeat ? 1 : -1);
	}

	/**
	 * 返回某个整型在数组里的序号,如果失败返回-1
	 * 
	 * @param id
	 * @param arrays
	 * @return
	 */
	public static int getIntArrayIndex(int id, int[] arrays) {
		if (arrays != null) {
			int size = arrays.length;
			if (size != 0) {
				for (int i = 0; i < size; i++) {
					if (id == arrays[i]) {
						return i;
					}
				}
			}
		}
		return -1;
	}

	/**
	 * 返回某个字符串在数组里的序号,如果失败返回-1
	 * 
	 * @param id
	 * @param arrays
	 * @return
	 */
	public static int getStringArrayIndex(String str, String[] arrays) {
		if (arrays != null) {
			int size = arrays.length;
			if (size != 0) {
				for (int i = 0; i < size; i++) {
					if (str.equals(arrays[i])) {
						return i;
					}
				}
			}
		}
		return -1;
	}

	/**
	 * 类名
	 * 
	 * @param cls
	 * @return
	 */
	public static String getName(Class cls) {
		return cls.getName();
	}

	/**
	 * 判断是否来自某个类且参数数目符合约定,且同一个协议标识
	 * 
	 * @param cls
	 *            目标类
	 * @param name
	 *            类名
	 * @param flag
	 *            标识
	 * @param proto
	 *            目标标识
	 * @param size
	 *            参数数目
	 * @param params
	 *            参数
	 * @return
	 */
	public static boolean isName(Class cls, String name, int flag, int proto,
			int size, Object... params) {
		if (flag == proto && U.isName(cls, name, size, params)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否来自某个类且参数数目符合约定
	 * 
	 * @param cls
	 *            目标类
	 * @param name
	 *            类名
	 * @param size
	 *            参数数目
	 * @param params
	 *            参数
	 * @return
	 */
	public static boolean isName(Class cls, String name, int size,
			Object... params) {
		if (U.isName(cls, name) && U.size(size, params)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否来自某个类
	 * 
	 * @param cls
	 * @param size
	 * @return
	 */
	public static boolean isName(Class cls, String name) {
		if (U.notNull(name) && name.equals(U.getName(cls))) {
			return true;
		}
		return false;
	}

	/**
	 * String为空，返回默认值
	 * 
	 * @param text
	 * @return
	 */
	public static String getString(String text, String defautlStr) {
		if (U.isNull(text)) {
			return defautlStr;
		}
		return text.trim();
	}

	/**
	 * 生成快捷方式 必须要com.android.launcher.permission.INSTALL_SHORTCUT权限
	 * 
	 * @param activity
	 *            当前activity
	 * @param title
	 *            标题
	 * @param icon
	 *            图标
	 */
	public static void createShortCut(Activity activity, int title, int icon) {
		Intent shortcutintent = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		// 不允许重复创建
		shortcutintent.putExtra("duplicate", false);
		// 需要现实的名称
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				activity.getString(title));
		// 快捷图片
		Parcelable iconRes = Intent.ShortcutIconResource.fromContext(
				activity.getApplicationContext(), icon);
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
		// 点击快捷图片，运行的程序主入口
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(
				activity.getApplicationContext(), activity.getClass()));
		// 发送广播
		activity.sendBroadcast(shortcutintent);
	}

	public static <T extends View> T findViewById(View view, int id) {
		@SuppressWarnings("unchecked")
		T v = (T) view.findViewById(id);
		return v;
	}

	public static <T extends View> T findViewById(Activity activity, int id) {
		@SuppressWarnings("unchecked")
		T v = (T) activity.findViewById(id);
		return v;
	}

	/**
	 * 获取状态栏高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusHeight(Context context) {
		int y = 0;
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			y = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			L.e(e);
		}
		return y;
	}

	/**
	 * 需要android.permission.GET_TASKS权限
	 * 默认取40个
	 * @param context
	 * @return
	 */
	public static boolean isRunning(Context context, String packageName) {
		if (U.notNull(packageName)) {
			ActivityManager activityManager = (ActivityManager) context
					.getSystemService(context.ACTIVITY_SERVICE);
			List<ActivityManager.RunningTaskInfo> runningTasks = activityManager
					.getRunningTasks(40);//默认

			for (ActivityManager.RunningTaskInfo taskInfo : runningTasks) {
				String info;
				info = "一个任务信息开始：/n";

				info += "当前任务中正处于运行状态的activity数目:" + taskInfo.numRunning;

				info += "当前任务中的activity数目: " + taskInfo.numActivities;

				info += "启动当前任务的activity名称:"
						+ taskInfo.baseActivity.getClassName();
				L.i(info);
				if (packageName.equals(taskInfo.baseActivity.getPackageName())) {
					return true;
				}

			}
		}
		return false;
	}
}
