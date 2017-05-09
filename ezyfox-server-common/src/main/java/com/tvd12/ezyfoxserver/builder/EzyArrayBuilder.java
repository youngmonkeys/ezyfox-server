package com.tvd12.ezyfoxserver.builder;

import com.tvd12.ezyfoxserver.entity.EzyArray;

public interface EzyArrayBuilder extends EzyBuilder<EzyArray> {
	
	/**
	 * append item to product 
	 * 
	 * @param <T> the value type
	 * @param item the item to add
	 * @return this pointer
	 */
	<T> EzyArrayBuilder append(T item);
	
	/**
	 * append items to product 
	 * 
	 * @param <T> the value type
	 * @param items the items to add
	 * @return this pointer
	 */
	@SuppressWarnings("unchecked")
	<T> EzyArrayBuilder append(T... items);
	
	/**
	 * append items to product
	 * 
	 * @param items the items to add
	 * @return this pointer
	 */
	@SuppressWarnings("rawtypes")
	EzyArrayBuilder append(Iterable items);
	
	/**
	 * build and add constructed item to product
	 * 
	 * @param builder the builder to build the item
	 * @return this pointer
	 */
	@SuppressWarnings("rawtypes")
	default EzyArrayBuilder append(EzyBuilder builder) {
		return this.append(builder.build());
	}
	
}
