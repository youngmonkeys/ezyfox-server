package com.tvd12.ezyfoxserver.factory;

public interface EzyEntityBuilderCreator {

	/**
	 * create a product
	 * 
	 * @param <T> the clazz type
	 * @param productType the product type
	 * @return the created product
	 */
	<T> T create(Class<T> productType);
	
}
