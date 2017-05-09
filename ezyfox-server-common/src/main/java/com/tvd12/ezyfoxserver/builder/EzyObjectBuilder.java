package com.tvd12.ezyfoxserver.builder;

import java.util.Map;

import com.tvd12.ezyfoxserver.entity.EzyObject;

public interface EzyObjectBuilder extends EzyBuilder<EzyObject> {

	/**
	 * append the value mapped to the key to product
	 * 
	 * @param key the key
	 * @param value the value
	 * @return this pointer
	 */
	EzyObjectBuilder append(Object key, Object value);
	
	/**
	 * append a map of key value entries to project
	 * 
	 * @param map the map to add
	 * @return this pointer
	 */
	@SuppressWarnings("rawtypes")
	EzyObjectBuilder append(Map map);
	
	/**
	 * build a value mapped to the key and add to product
	 * 
	 * @param key the key
	 * @param builder the builder to build value
	 * @return this pointer
	 */
	@SuppressWarnings("rawtypes")
	default EzyObjectBuilder append(Object key, EzyBuilder builder) {
		return this.append(key, builder.build());
	}
	
}
