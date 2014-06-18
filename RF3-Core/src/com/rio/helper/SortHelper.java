package com.rio.helper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.rio.core.L;
import com.rio.core.U;

/**
 * 排序
 * 
 * @author rio
 * 
 */
public class SortHelper {


	public enum Sort {
		DESC, ASC
	}

	// 是否是英文
	private static boolean isLetter(char c) {
		return Character.isUpperCase(c) || Character.isLowerCase(c);
	}

	// 是否是数字
	private static boolean isNumeric(char c) {
		if (!Character.isDigit(c)) {
			return false;
		}

		return true;
	}

	/**
	 * 比较两个字符串
	 * 
	 * @param str1
	 * @param str2
	 * @param order
	 * @return 大于0表示str1应该在str2的后面，小于0则在前面
	 */
	private static int compareInLetter(final String str1, final String str2) {
		int result = 0;
		if (str1 != null && str2 != null) {
			Collator collator = Collator.getInstance(Locale.CHINESE);
			if (collator == null) {
				collator = Collator.getInstance(Locale.getDefault());
			}
			result = collator.compare(str1.toUpperCase(), str2.toUpperCase());
		}
		return result;
	}

	/**
	 * 通过int值进行比较
	 * 
	 * @author huyong
	 * @param <T>
	 * @param list
	 * @param method
	 * @param methodArgsClass
	 * @param methodArgs
	 * @param order
	 */
	public static <T> void byInt(List<T> list, final String method,
			final Sort order) {
		Collections.sort(list, new Comparator<T>() {

			private Method compareMethod ;
			
			@Override
			public int compare(T object1, T object2) {
				return doCompare(method, order, object1, object2);
			}
			
			private int doCompare(String method, Sort order, T object1,
					T object2) {
				int result = 0;				
				try {
					if(U.isNull(compareMethod))
						compareMethod = object1.getClass().getMethod(method);
					Integer value1 = (Integer) compareMethod.invoke(object1);
					Integer value2 = (Integer) compareMethod.invoke(object2);			
					if (U.isNull(value1) || U.isNull(value2)
							|| value1 == value2) {
						result = 0;
					}
					switch (order) {
					case ASC:
						result = value1 > value2 ? 1 : -1;
						break;
					case DESC:
						result = value2 > value1 ? 1 : -1;
						break;
					default:
						break;
					}			
				} catch (Exception e) {
					L.e(e);
				} 
				return result;
			}

		});
	}



	/**
	 * 通过long值进行比较
	 * 
	 * @author kingyang
	 * @param <T>
	 * @param list
	 * @param method
	 * @param methodArgsClass
	 * @param methodArgs
	 * @param order
	 */
	public static <T> void byLong(List<T> list, final String method,
			final Sort order) {
		Collections.sort(list, new Comparator<T>() {
			
			private Method compareMethod ;

			@Override
			public int compare(T object1, T object2) {
				return doCompare(method,order,
						object1, object2);
			}
			
			private int doCompare(String method, Sort order, T object1,
					T object2) {
				int result = 0;				
				try {
					if(U.isNull(compareMethod))
						compareMethod = object1.getClass().getMethod(method);
					Long value1 = (Long) compareMethod.invoke(object1);
					Long value2 = (Long) compareMethod.invoke(object2);			
					if (U.isNull(value1) || U.isNull(value2)
							|| value1 == value2) {
						result = 0;
					}
					switch (order) {
					case ASC:
						result = value1 > value2 ? 1 : -1;
						break;
					case DESC:
						result = value2 > value1 ? 1 : -1;
						break;
					default:
						break;
					}			
				} catch (Exception e) {
					L.e(e);
				} 
				return result;
			}
		});
	}

	/**
	 * 字符串比较
	 * @param list
	 * @param method
	 */
	public static <T> void byString(List<T> list, final String method) {
		Collections.sort(list, new Comparator<Object>() {

			private Method compareMethod ;
			@Override
			public int compare(Object object1, Object object2) {
				return doCompare(method,object1, object2);
			}

			private int doCompare(String method, Object object1, Object object2) {
				int result = 0;				
				try {
					if(U.isNull(compareMethod))
						compareMethod = object1.getClass().getMethod(method);
					String value1 = (String) compareMethod.invoke(object1);
					String value2 = (String) compareMethod.invoke(object2);			
					if (U.isNull(value1) || U.isNull(value2)) {
						result = 0;
					}
					char c1 = value1.charAt(0);
					char c2 = value2.charAt(0);

					// 如果第一个是英文，则排在前面
					if (isLetter(c1) && !isLetter(c2)) {
						return -1;
					}

					// 如果第一个是数字
					if (isNumeric(c1) && !isNumeric(c2)) {
						// 第二个是英文，英文排在前面
						if (isLetter(c2)) {
							return 1;
						} else {// 其他情况，数字排在前面
							return -1;
						}
					}
					result = compareInLetter(value1,value2);
					//result = value1.compareTo(value2);			
				} catch (Exception e) {
					L.e(e);
				} 
				return result;
			}
			
			
		});
	}

}
