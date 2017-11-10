package com.tvd12.ezyfoxserver.database.mapservice;

public interface EzyCheckContainService<K,V> {

	boolean containsKey(K key);
	
	boolean containsValue(V value);
	
}
