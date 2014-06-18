package com.rio.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import com.rio.core.L;

public class ClassHelper {

	/**
	 * 打印类的信息
	 * @param clsShow
	 */
	static public void printAllInform(Class clsShow) {
		try {
			// 取得所有方法
			Method[] hideMethod = clsShow.getMethods();
			int i = 0;
			for (; i < hideMethod.length; i++) {
				L.i("method name : "+hideMethod[i].getName());
				 Type[] vars = hideMethod[i].getGenericParameterTypes();
				 if(vars.length > 0){
					 
					 for(Type var : vars){						 
						 L.i("Parameter type : "+var.toString());
					 }
					 
				 }
			}
			L.ii();
			// 取得所有变量
			Field[] allFields = clsShow.getFields();
			for (i = 0; i < allFields.length; i++) {
				L.i("Field name : "+allFields[i].getName());
			}
		} catch (SecurityException e) {
			// throw new RuntimeException(e.getMessage());
			L.e(e);
		} catch (IllegalArgumentException e) {
			// throw new RuntimeException(e.getMessage());
			L.e(e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			L.e(e);
		}
	}

}
