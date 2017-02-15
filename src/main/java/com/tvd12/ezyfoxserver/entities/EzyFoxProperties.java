/**
 * 
 */
package com.tvd12.ezyfoxserver.entities;

import java.util.Map;

/**
 * Each model in application should have properties, and we think key/value is good idea
 * 
 * @author tavandung12
 *
 */
public interface EzyFoxProperties {

    /**
     * put key and value to map
     * 
     * @param key key 
     * @param value value
     */
    void setProperty(final Object key, final Object value);
    
    /**
     * put all
     * 
     * @param map the map to put
     */
    void setProperties(final Map<? extends Object, ? extends Object> map);
    
    /**
     * get the value to which the specified key is mapped
     * 
     * @param <T> type of value
     * @param key key
     * @return a value
     */
    <T> T getProperty(final Object key);
    
    /**
     * get the value to which the specified key is mapped and cast value to specific type
     * 
     * @param <T> the value type
     * @param key key
     * @param clazz specific type
     * @return a value
     */
    <T> T getProperty(final Object key, final Class<T> clazz);
    
    /**
     * removes the mapping for a key from the map
     * 
     * @param key the key
     */
    void removeProperty(final Object key);

    /**
     * check whether has value mapped to the or not
     * 
     * @param key the key to check
     * @return true or false;
     */
    boolean containsKey(final Object key);
    
}
