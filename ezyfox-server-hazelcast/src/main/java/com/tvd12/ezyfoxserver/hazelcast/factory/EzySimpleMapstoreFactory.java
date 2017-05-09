package com.tvd12.ezyfoxserver.hazelcast.factory;

import java.util.Properties;

import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzyMapstoreCreator;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EzySimpleMapstoreFactory extends EzyAbstractMapstoreFactory {

	protected EzyMapstoreCreator mapstoreCreator;
	
	@Override
	protected Object newMapstore(String mapName, Properties properties) {
		return mapstoreCreator.create(mapName, properties);
	}
	
}
