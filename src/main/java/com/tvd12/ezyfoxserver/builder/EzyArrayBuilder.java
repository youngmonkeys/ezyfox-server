package com.tvd12.ezyfoxserver.builder;

import java.util.Collection;

import com.tvd12.ezyfoxserver.entity.EzyArray;

public interface EzyArrayBuilder extends EzyBuilder<EzyArray> {
	
	/**
	 * append items to product 
	 * 
	 * @param <T> the value type
	 * @param items the items to add
	 * @return this pointer
	 */
	@SuppressWarnings("unchecked")
	<T> EzyArrayBuilder append(final T... items);
	
	/**
	 * append items to product
	 * 
	 * @param items the items to add
	 * @return this pointer
	 */
	EzyArrayBuilder append(final Collection<? extends Object> items);
	
	/**
	 * build and add constructed item to product
	 * 
	 * @param builder the builder to build the item
	 * @return this pointer
	 */
	@SuppressWarnings("rawtypes")
	default EzyArrayBuilder append(final EzyBuilder builder) {
		return this.append(builder.build());
	}
	
}
