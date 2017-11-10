package com.tvd12.ezyfoxserver.database.mapservice;

import java.util.Map;

public interface EzyPutMapService<K,V> {

	void put(Map<K, V> map);
	
}
