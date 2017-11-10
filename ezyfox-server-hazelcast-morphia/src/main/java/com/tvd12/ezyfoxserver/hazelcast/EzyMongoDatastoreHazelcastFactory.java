package com.tvd12.ezyfoxserver.hazelcast;

import org.mongodb.morphia.Datastore;

import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzyMongoDatabaseMapstoreCreator;
import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzyMongoDatastoreMapstoreCreator;
import com.tvd12.ezyfoxserver.morphia.EzyDatastoreAware;

import lombok.Setter;

@Setter
public class EzyMongoDatastoreHazelcastFactory 
		extends EzyMongoDatabaseHazelcastFactory 
		implements EzyDatastoreAware {
	
	protected Datastore datastore;

	@Override
	protected EzyMongoDatabaseMapstoreCreator newDatabaseMapstoreCreator() {
		EzyMongoDatastoreMapstoreCreator creator = new EzyMongoDatastoreMapstoreCreator();
		creator.setDatastore(datastore);
		return creator;
	}
	
}
