/**
 * 
 */
package com.tvd12.ezyfoxserver.hazelcast.mapstore;

import com.mongodb.client.MongoDatabase;
import com.tvd12.ezyfoxserver.mongodb.EzyMongoDatabaseAware;

import lombok.Setter;

/**
 * @author tavandung12
 *
 */
public abstract class EzyMongoDatabaseMapstore<K,V>
		extends EzyAbstractMapstore<K, V> 
		implements EzyMongoDatabaseAware {

	@Setter
    protected MongoDatabase database;
    
}
