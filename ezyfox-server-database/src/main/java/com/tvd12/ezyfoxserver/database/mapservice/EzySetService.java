package com.tvd12.ezyfoxserver.database.mapservice;

public interface EzySetService<K,V> extends
		EzySetKeyValueService<K, V>, 
		EzySetValueService<V>,
		EzySetValuesService<V>,
		EzySetMapService<K, V> {
	
}
