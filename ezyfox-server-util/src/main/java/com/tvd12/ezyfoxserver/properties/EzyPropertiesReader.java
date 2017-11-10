package com.tvd12.ezyfoxserver.properties;

import java.util.Map;

@SuppressWarnings("rawtypes")
public interface EzyPropertiesReader {

	<T> T get(Map properties, Object key);

	<T> T get(Map properties, Object key, T defValue);
	
	<T> T get(Map properties, Object key, Class<T> outType);
	
	<T> T get(Map properties, Object key, Class<T> outType, T defValue);
	
	boolean containsKey(Map properties, Object key);
	
}
