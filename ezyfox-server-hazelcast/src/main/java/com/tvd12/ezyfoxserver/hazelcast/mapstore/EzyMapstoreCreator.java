package com.tvd12.ezyfoxserver.hazelcast.mapstore;

import java.util.Properties;
import java.util.Set;

import com.hazelcast.core.MapStore;

public interface EzyMapstoreCreator {

    Set<String> getMapNames();
    
	@SuppressWarnings("rawtypes")
	MapStore create(String mapName, Properties properties);
	
}
