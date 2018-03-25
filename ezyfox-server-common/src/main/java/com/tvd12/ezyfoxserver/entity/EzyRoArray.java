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
	<T> T get(int index);
	
	/**
	 * Get and cast value by index
	 * 
	 * @param <T> the value
	 * @param index the index
	 * @param type the value type
	 * @return the value
	 */
	<T> T get(int index, Class<T> type);
	
	/**
	 * Get value by index but not cast
	 * 
	 * @param <T> the value
	 * @param index the index
	 * @param type the value type
	 * @return the object value
	 */
	@SuppressWarnings("rawtypes")
	Object getValue(int index, Class type);
	
	/**
	 * Check if value in the index is not null
	 * 
	 * @param index the index
	 * @return true or false
	 */
	boolean isNotNullValue(int index);
	
	/**
	 * Get new array
	 * 
	 * @param fromIndex the from index
	 * @param toIndex the to index
	 * @return the new array
	 */
	EzyRoArray sub(int fromIndex, int toIndex);
	
	/**
	 * @return the size of array
	 */
	int size();
	
	/**
	 * @return is empty or not
	 */
	default boolean isEmpty() {
		return size() == 0;
	}
	
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
	 * @param <A> the return type
	 * @param type type array type
	 * @return the array value
	 */
	<T,A> A toArray(Class<T> type);
	
	/**
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyData#duplicate()
	 */
	@Override
	EzyRoArray duplicate();
	
	/**
	 * Get value by index
	 * 
	 * @param <T> the value type
	 * @param index the index
	 * @param def the default value
	 * @return the value
	 */
	default <T> T getWithDefault(int index, T def) {
		return size() > index ? get(index) : def;
	}
	
	/**
	 * Get and cast value by index
	 * 
	 * @param <T> the value
	 * @param index the index
	 * @param type the value type
	 * @param def the default value
	 * @return the value
	 */
	default <T> T get(int index, Class<T> type, T def) {
		return size() > index ? get(index, type) : def;
	}
	
	/**
	 * Get value by index but not cast
	 * 
	 * @param <T> the value
	 * @param index the index
	 * @param type the value type
	 * @param def the default value
	 * @return the object value
	 */
	@SuppressWarnings("rawtypes")
	default Object getValue(int index, Class type, Object def) {
		return size() > index ? getValue(index, type) : def;
	}
}
