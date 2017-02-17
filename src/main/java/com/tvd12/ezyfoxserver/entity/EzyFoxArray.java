package com.tvd12.ezyfoxserver.entity;

import java.util.Collection;

public interface EzyFoxArray extends EzyFoxRoArray {
	
	/**
	 * Add values to array
	 * 
	 * @param values the values
	 */
	void add(Object... values);
	
	/**
	 * Add values to array
	 * 
	 * @param values the values
	 */
	void add(Collection<Object> values);
	
	/**
	 * Set value at the index
	 * 
	 * @param index the index
	 * @param value the value
	 */
	Object set(int index, Object value);
	
	/**
	 * Remove value at the index
	 * 
	 * @param index the index
	 */
	Object remove(int index);
	
	
}
