package com.tvd12.ezyfoxserver.hazelcast.mapstore;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.hazelcast.core.MapStore;
import com.mongodb.client.MongoDatabase;
import com.tvd12.ezyfoxserver.hazelcast.constant.EzyMapNames;
import com.tvd12.ezyfoxserver.mongodb.EzyMongoDatabaseAware;

import lombok.Setter;

public class EzyMongoDatabaseMapstoreCreator 
		extends EzySimpleMapstoreCreator
		implements EzyMongoDatabaseAware {

	@Setter
	protected MongoDatabase database;
	
	@SuppressWarnings("rawtypes")
	private Map<String, MapStore> defaultMapstores = defaultMapstores();
	
	@SuppressWarnings("rawtypes")
	@Override
	public MapStore create(String mapName, Properties properties) {
		MapStore mapstore = defaultMapstores.get(mapName);
		if(mapstore == null)
			mapstore = (MapStore) mapstoresFetcher.getMapstore(mapName);
		if(mapstore instanceof EzyMongoDatabaseAware)
			((EzyMongoDatabaseAware)mapstore).setDatabase(database);
		return mapstore;
	}
	
	public Set<String> getMapNames() {
		Set<String> mapNames = super.getMapNames();
		mapNames.addAll(defaultMapstores.keySet());
		return mapNames;
	}
	
	@SuppressWarnings("rawtypes")
	protected Map<String, MapStore> defaultMapstores() {
		Map<String, MapStore> suppliers = new HashMap<>();
		addDefaultMapStores(suppliers);
		return suppliers;
	}
	
	@SuppressWarnings("rawtypes")
	protected void addDefaultMapStores(Map<String, MapStore> suppliers) {
	    suppliers.put(EzyMapNames.MAX_ID, new EzyMongoMaxIdMapstore());
	}
}
