package com.tvd12.ezyfoxserver.hazelcast;

import com.mongodb.client.MongoDatabase;
import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzyMapstoreCreator;
import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzyMongoDatabaseMapstoreCreator;
import com.tvd12.ezyfoxserver.mongodb.EzyMongoDatabaseAware;

import lombok.Setter;

@Setter
public class EzyMongoDatabaseHazelcastFactory 
		extends EzyAbstractHazelcastFactory 
		implements EzyMongoDatabaseAware {
	
	protected MongoDatabase database;
	
	@Override
	protected EzyMapstoreCreator newMapstoreCreator() {
		EzyMongoDatabaseMapstoreCreator creator = newDatabaseMapstoreCreator();
		creator.setDatabase(database);
		return creator;
	}

	protected EzyMongoDatabaseMapstoreCreator newDatabaseMapstoreCreator() {
		return new EzyMongoDatabaseMapstoreCreator(); 
	}
	
}
