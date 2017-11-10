/**
 * 
 */
package com.tvd12.ezyfoxserver.hazelcast.factory;

import java.util.Properties;

import com.hazelcast.core.MapLoader;
import com.hazelcast.core.MapStoreFactory;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.util.EzyPostInit;

/**
 * @author tavandung12
 *
 */
@SuppressWarnings("rawtypes")
public abstract class EzyAbstractMapstoreFactory
		extends EzyLoggable
		implements MapStoreFactory {
	
    @Override
    public final MapLoader newMapStore(String mapName, Properties properties) {
    	Object map = newMapstore(mapName, properties);
    	if(map instanceof EzyPostInit)
    		((EzyPostInit)map).postInit();
    	return (MapLoader) map;
    	
    }
    
	protected abstract Object newMapstore(String mapName, Properties properties);
    
}
