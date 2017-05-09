/**
 * 
 */
package com.tvd12.ezyfoxserver.hazelcast.service;

/**
 * @author tavandung12
 *
 */
public interface EzyMaxIdService {

	void loadAll();
	
    Long incrementAndGet(String key);
    
}
