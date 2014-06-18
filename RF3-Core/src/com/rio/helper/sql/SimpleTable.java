package com.rio.helper.sql;

/**
 * 不接受基本类型
 * int => Ingeter
 * long => Long 
 * boolean => Boolean
 * 
 * @author rio
 *
 */
public class SimpleTable {

	public Long _id ;
	
	public Long getIndex(){
		return _id;
	}
	
	public void setIndex(long id){
		_id = id;
	}
}
