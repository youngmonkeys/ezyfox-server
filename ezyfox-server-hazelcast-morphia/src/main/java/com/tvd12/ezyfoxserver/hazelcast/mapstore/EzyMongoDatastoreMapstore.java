/**
 * 
 */
package com.tvd12.ezyfoxserver.hazelcast.mapstore;

import org.mongodb.morphia.Datastore;

import com.tvd12.ezyfoxserver.morphia.EzyDatastoreAware;

import lombok.Setter;

/**
 * @author tavandung12
 *
 */
public abstract class EzyMongoDatastoreMapstore<K,V> 
		extends EzyAbstractMapstore<K,V>
		implements EzyDatastoreAware {

	@Setter
    protected Datastore datastore;
    
}
