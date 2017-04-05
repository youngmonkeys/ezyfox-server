package com.tvd12.ezyfoxserver.entity;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;

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
	 * @param <T> the value type
	 * @param index the index
	 * @param item the item to set
	 */
	<T> T set(int index, Object item);
	
	/**
	 * Remove value at the index
	 * 
	 * @param <T> the value type
	 * @param index the index
	 * @return the removed value
	 */
	<T> T remove(int index);
	
	/**
	 * For each
	 * 
	 * @param action the action
	 */
	void forEach(Consumer<Object> action);
	
	/**
	 * @see java.util.List#iterator()
	 * 
	 * @return the iterator
	 */
	Iterator<Object> iterator();
	
	/**
     * @see com.tvd12.ezyfoxserver.entity.EzyData#duplicate()
     */
    @Override
    EzyArray duplicate();
	
}
