package com.tvd12.ezyfoxserver.hazelcast;

import com.hazelcast.config.MapConfig;

public interface EzyMapConfigsFetcher {

	Iterable<MapConfig> get();
	
}
