package com.tvd12.ezyfoxserver.mongodb;

import com.mongodb.MongoClient;

public interface EzyMongoClientAware {

	void setMongoClient(MongoClient mongoClient);
	
}
