package com.tvd12.ezyfoxserver.constant;

import java.util.concurrent.atomic.AtomicInteger;

public interface EzyConstant {

	// the counter utility
	AtomicInteger COUNTER = new AtomicInteger(0);
	
	/**
	 * Get constant id
	 * 
	 * @return the constant id
	 */
	int getId();
	
	/**
	 * Get constant name
	 * 
	 * @return the constant name
	 */
	default String getName() {
		return getClass().getSimpleName() + "-" + getId();
	}
	
	/**
	 * Create new constant
	 * 
	 * @return a constant
	 */
	static EzyConstant one() {
		return one(COUNTER.incrementAndGet());
	}
	
	/**
	 * Create new constant with id
	 * 
	 * @param id the constant id
	 * @return a constant
	 */
	static EzyConstant one(final int id) {
		return new EzyConstant() {
			@Override
			public int getId() {
				return id;
			}
		};
	};
	
}
