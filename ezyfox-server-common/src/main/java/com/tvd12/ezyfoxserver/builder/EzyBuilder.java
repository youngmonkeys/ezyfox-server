package com.tvd12.ezyfoxserver.builder;

public interface EzyBuilder<T> {
	
	/**
	 * build project
	 * 
	 * @return the constructed the product
	 */
	T build();
	
}
