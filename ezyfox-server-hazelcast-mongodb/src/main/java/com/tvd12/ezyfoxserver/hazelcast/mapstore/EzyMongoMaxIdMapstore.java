/**
 * 
 */
package com.tvd12.ezyfoxserver.hazelcast.mapstore;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.tvd12.ezyfoxserver.hazelcast.constant.EzyMapNames;

/**
 * @author tavandung12
 *
 */
public class EzyMongoMaxIdMapstore extends EzyMongoCollectionMapstore<String, Long> {
	
    @Override
    public Long load(String key) {
    		Bson filter = getFilter(key);
    		Document first = collection.find(filter).first();
        Long answer = first != null ? first.getLong("maxId") : 0L;
        getLogger().info("load maxId of: {} max: {}", key, answer);
        return answer;
    }
    
    @Override
    public void store(String key, Long value) {
        collection.updateOne(getFilter(key), getUpdate(value), getUpdateOptions());
    }

    protected Bson getUpdate(Long value) {
        return new Document("$set", new Document("maxId", value));
    }
    
    private Bson getFilter(String key) {
        return Filters.eq("_id", key);
    }
    
    private UpdateOptions getUpdateOptions() {
        return new UpdateOptions().upsert(true);
    }
    
	@Override
	protected String getCollectionName() {
		return EzyMapNames.MAX_ID;
	}
}
