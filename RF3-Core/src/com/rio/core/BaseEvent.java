package com.rio.core;

public interface BaseEvent<T> {

	public void onFail(Object data,Exception exception);
	
	public void onSuccess(T data);
	
}
