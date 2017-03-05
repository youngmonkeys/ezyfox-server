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
	<T> void add(final T... items);
	
	/**
	 * Add values to array
	 * 
	 * @param items the items to add
	 */
	void add(final Collection<? extends Object> items);
	
	/**
	 * Set value at the index
	 * 
	 * @param index the index
	 * @param item the item to set
	 */
	<T> T set(final int index, final Object item);
	
	/**
	 * Remove value at the index
	 * 
	 * @param index the index
	 */
	<T> T remove(final int index);
	
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
	
}
