package com.tvd12.ezyfoxserver.hazelcast.mapstore;

import java.util.Properties;
import java.util.Set;

import com.hazelcast.core.MapStore;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

import lombok.Setter;

public class EzySimpleMapstoreCreator
		extends EzyLoggable
		implements EzyMapstoreCreator, EzyMapstoresFetcherAware {

	@Setter
	protected EzyMapstoresFetcher mapstoresFetcher;
	
	@Override
	public Set<String> getMapNames() {
		return mapstoresFetcher.getMapNames();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public MapStore create(String mapName, Properties properties) {
		if(mapstoresFetcher.containsMapstore(mapName))
			return (MapStore) mapstoresFetcher.getMapstore(mapName);
		throw new IllegalArgumentException("has no mapstore with name = " + mapName);
	}
	
}
