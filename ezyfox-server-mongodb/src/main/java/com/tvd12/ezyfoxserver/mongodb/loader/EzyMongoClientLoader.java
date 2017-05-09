/**
 * 
 */
package com.tvd12.ezyfoxserver.mongodb.loader;

import com.mongodb.MongoClient;

/**
 * @author dung.tv@zinza.com.vn
 *
 */
public interface EzyMongoClientLoader {

	String URI			= "database.mongo.uri";
    String HOST         = "database.mongo.host";
    String PORT         = "database.mongo.port";
    String USERNAME     = "database.mongo.username";
    String PASSWORD     = "database.mongo.password";
    String DATABASE     = "database.mongo.database";
    String COLLECTION   = "database.mongo.collection";
    
    MongoClient load();
    
}
