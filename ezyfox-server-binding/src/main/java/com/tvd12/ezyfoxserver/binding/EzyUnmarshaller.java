package com.tvd12.ezyfoxserver.binding;

import java.util.Collection;
import java.util.Map;

public interface EzyUnmarshaller {

	/**
	 * unwrap a value to pojo
	 * 
	 * @param value the value
	 * @param output the output type
	 * @return a pojo
	 */
	void unwrap(Object value, Object output);
	
	/**
	 * unmarshal a value to pojo
	 * 
	 * @param value the value
	 * @param outType the pojo type
	 * @return a pojo
	 */
	<T> T unmarshal(Object value, Class<T> outType);
	
	/**
	 * unmarshal a value to collection
	 * 
	 * @param value the value 
	 * @param collectionType the collection type
	 * @param itemType the item type
	 * @return a collection
	 */
	@SuppressWarnings("rawtypes")
	<T> Collection<T> unmarshalCollection(
			Object value, Class collectionType, Class<T> itemType);
	
	/**
	 * unmarshal value to map
	 * 
	 * @param value the value
	 * @param mapType the map type
	 * @param keyType the key type
	 * @param valueType the value type
	 * @return the map
	 */
	@SuppressWarnings("rawtypes")
	<K,V> Map<K,V> unmarshalMap(
			Object value, Class mapType, Class<K> keyType, Class<V> valueType);
	
	/**
	 * unmarshal value to object
	 * 
	 * @param readerClass the reader class
	 * @param value the value
	 * @return a object
	 */
	@SuppressWarnings("rawtypes")
	<T> T unmarshal(Class<? extends EzyReader> readerClass, Object value);
	
}
