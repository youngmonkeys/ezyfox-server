package com.tvd12.ezyfoxserver.hazelcast.mapstore;

import java.util.Properties;

import org.mongodb.morphia.Datastore;

import com.hazelcast.core.MapStore;
import com.tvd12.ezyfoxserver.morphia.EzyDatastoreAware;

import lombok.Setter;

public class EzyMongoDatastoreMapstoreCreator 
		extends EzyMongoDatabaseMapstoreCreator
		implements EzyDatastoreAware {

	@Setter
	protected Datastore datastore;
	
	@SuppressWarnings("rawtypes")
	@Override
	public MapStore create(String mapName, Properties properties) {
		MapStore mapstore = super.create(mapName, properties);
		if(mapstore instanceof EzyDatastoreAware)
			((EzyDatastoreAware)mapstore).setDatastore(datastore);
		return mapstore;
	}
	
}
