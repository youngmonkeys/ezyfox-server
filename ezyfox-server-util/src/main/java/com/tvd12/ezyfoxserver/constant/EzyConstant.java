package com.tvd12.ezyfoxserver.constant;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

public interface EzyConstant extends EzyHasIntId, EzyHasName {

	// the counter utility
	AtomicInteger COUNTER = new AtomicInteger(0);
	
	/**
	 * Get constant name
	 * 
	 * @return the constant name
	 */
	default String getName() {
		String name = getClass().getSimpleName().trim();
		name = StringUtils.isEmpty(name) ? "attribute" : name;
		return name + "#" + getId();
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
	 * Create new constant
	 * 
	 * @return a constant
	 */
	static EzyConstant one(String name) {
		return one(COUNTER.incrementAndGet(), name);
	}
	
	/**
	 * Create new constant with id
	 * 
	 * @param id the constant id
	 * @return a constant
	 */
	static EzyConstant one(int id) {
		COUNTER.incrementAndGet();
		return new EzyConstant() {
			@Override
			public int getId() {
				return id;
			}
		};
	};
	
	/**
	 * Create new constant with id
	 * 
	 * @param id the constant id
	 * @param name the constant name
	 * @return a constant
	 */
	static EzyConstant one(int id, String name) {
		COUNTER.incrementAndGet();
		return new EzyConstant() {
			@Override
			public int getId() {
				return id;
			}
			
			@Override
			public String getName() {
				return name;
			}
		};
	};
	
}
