package com.tvd12.ezyfoxserver.entity;

import java.util.List;

public interface EzyRoArray extends EzyData {

	/**
	 * Get value by index
	 * 
	 * @param <T> the value type
	 * @param index the index
	 * @return the value
	 */
	<T> T get(final int index);
	
	/**
	 * Get value by index
	 * 
	 * @param <T> the value
	 * @param index the index
	 * @param type the value type
	 * @return the value
	 */
	<T> T get(final int index, final Class<T> type);
	
	/**
	 * Get new array
	 * 
	 * @param fromIndex the from index
	 * @param toIndex the to index
	 * @return the new array
	 */
	EzyArray sub(int fromIndex, int toIndex);
	
	/**
	 * @return the size of array
	 */
	int size();
	
	/**
	 * @param <T> type of value
	 * @return covert this array to list
	 */
	<T> List<T> toList();
	
	/**
	 * @param <T> type of value
	 * @param type the item type
	 * @return covert this array to list
	 */
	<T> List<T> toList(Class<T> type);
	
	/**
	 * @param <T> the array type
	 * @param type type array type
	 * @return the array value
	 */
	<T> T toArray(Class<T> type);
	
}
