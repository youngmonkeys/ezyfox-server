/**
 * 
 */
package com.tvd12.ezyfoxserver.util;

import java.util.Map;

/**
 * Each model in application should have properties, and we think key/value is good idea
 * 
 * @author tavandung12
 *
 */
public interface EzyProperties extends EzyRoProperties {

    /**
     * put key and value to map
     * 
     * @param key key 
     * @param value value
     */
    void setProperty(Object key, Object value);
    
    /**
     * put all
     * 
     * @param map the map to put
     */
    void setProperties(Map<? extends Object, ? extends Object> map);
    
    /**
     * removes the mapping for a key from the map
     * 
     * @param key the key
     */
    void removeProperty(Object key);
    
}
