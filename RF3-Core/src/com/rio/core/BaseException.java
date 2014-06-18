package com.rio.core;

/**
 * 异常，提供一个R文件的string id
 * 
 * @author rio
 *
 */
public class BaseException extends Exception {


	private int res;
	
	private Object[] params;

	public int getId() {
		return res;
	}

	public Object[] getParam() {
		return params;
	}
	
	public BaseException(int string,Object...params) {
		this.res = string;
		this.params = params;
	}
	
	public BaseException(int string) {
		this.res = string;
	}

}
