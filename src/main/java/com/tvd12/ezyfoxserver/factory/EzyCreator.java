package com.tvd12.ezyfoxserver.factory;

public interface EzyCreator {

	/**
	 * create a product
	 * 
	 * @param <T> the clazz type
	 * @param productType the product type
	 * @return the created product
	 */
	<T> T create(Class<T> productType);
	
	/**
	 * check whether this creator able to create the product with type
	 * 
	 * @param <T> the clazz type
	 * @param productType the product type
	 * @return true of false
	 */
	<T> boolean creatable(Class<T> productType);
	
}
