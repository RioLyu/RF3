package com.rio.core;

import java.io.Serializable;

public class BaseSerializable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1302762941498816199L;
	
	public Object[] params;
	
	public BaseSerializable(Object[] params){
		this.params = params;
	}

}
