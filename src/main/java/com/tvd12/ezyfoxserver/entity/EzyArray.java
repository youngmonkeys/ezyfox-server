package com.tvd12.ezyfoxserver.entity;

import java.util.Collection;

public interface EzyArray extends EzyRoArray {
	
	/**
	 * Add values to array
	 * 
	 * @param <T> the value type
	 * @param items the items to add
	 */
	@SuppressWarnings("unchecked")
	<T> void add(T... items);
	
	/**
	 * Add values to array
	 * 
	 * @param items the items to add
	 */
	void add(Collection<? extends Object> items);
	
	/**
	 * Set value at the index
	 * 
	 * @param index the index
	 * @param item the item to set
	 */
	Object set(int index, Object item);
	
	/**
	 * Remove value at the index
	 * 
	 * @param index the index
	 */
	Object remove(int index);
	
	
}
