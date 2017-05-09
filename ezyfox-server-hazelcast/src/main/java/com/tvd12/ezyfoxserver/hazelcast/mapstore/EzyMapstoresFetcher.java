package com.tvd12.ezyfoxserver.hazelcast.mapstore;

import java.util.Map;
import java.util.Set;

public interface EzyMapstoresFetcher {

	Set<String> getMapNames();
	
	Object getMapstore(String mapName);
	
	Map<String, Object> getMapstores();
	
	boolean containsMapstore(String mapName);
	
}
