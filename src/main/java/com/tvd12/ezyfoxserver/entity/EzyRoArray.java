package com.tvd12.ezyfoxserver.entity;

import java.util.List;

public interface EzyRoArray {

	/**
	 * Get value by index
	 * 
	 * @param <T> the value type
	 * @param index the index
	 * @return the value
	 */
	<T> T get(int index);
	
	/**
	 * Get value by index
	 * 
	 * @param <T> the value
	 * @param index the index
	 * @param type the value type
	 * @return the value
	 */
	<T> T get(int index, Class<T> type);
	
	/**
	 * @return the size of array
	 */
	int size();
	
	/**
	 * @param <T> type of value
	 * @return covert this array to list
	 */
	<T> List<T> toList();
	
}
