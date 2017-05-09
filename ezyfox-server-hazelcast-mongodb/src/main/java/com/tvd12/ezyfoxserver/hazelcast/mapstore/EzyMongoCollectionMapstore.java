package com.tvd12.ezyfoxserver.hazelcast.mapstore;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

public abstract class EzyMongoCollectionMapstore<K,V>
		extends EzyMongoDatabaseMapstore<K, V> {

	protected MongoCollection<Document> collection;
	
	@Override
	public void postInit() {
		super.postInit();
		this.collection = database.getCollection(getCollectionName());
	}
	
	protected abstract String getCollectionName();
	
}
