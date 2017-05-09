package com.tvd12.ezyfoxserver.mongodb;

import com.mongodb.client.MongoDatabase;

public interface EzyMongoDatabaseAware {

	void setDatabase(MongoDatabase database);
	
}
