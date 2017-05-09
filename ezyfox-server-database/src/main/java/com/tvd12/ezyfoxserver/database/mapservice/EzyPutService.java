package com.tvd12.ezyfoxserver.database.mapservice;

public interface EzyPutService<K,V> extends 
		EzyPutKeyValueService<K, V>,
		EzyPutValueService<V>,
		EzyPutValuesService<V>,
		EzyPutMapService<K, V> {
}
