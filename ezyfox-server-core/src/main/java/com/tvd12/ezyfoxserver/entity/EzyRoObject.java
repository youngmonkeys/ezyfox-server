/**
 * 
 */
package com.tvd12.ezyfoxserver.entity;

import java.util.Map;
import java.util.Set;

/**
 * @author tavandung12
 *
 */
public interface EzyRoObject {

    /**
     * @see java.util.Map#size()
     * 
     * @return number of parameters
     */
    int size();
    
    /**
     * @see java.util.Map#isEmpty()
     * 
     * @return true or false
     */
    boolean isEmpty();
    
    /**
     * @see java.util.Map#containsKey(java.lang.Object)
     * 
     * @param key key
     * @return true or false
     */
    boolean containsKey(final Object key);
    
    /**
     * @see java.util.Map#get(java.lang.Object)
     * 
     * @param <V> type of value
     * @param key key 
     * @return a value 
     */
    <V> V get(final Object key);
    
    /**
     * 
     * returns the value to which the specified key is mapped, 
     * or null if contains no mapping for the key and cast 
     * the value to specific type.
     * 
     * @param <V> the type
     * @param key key
     * @param clazz type of value
     * @return a value
     */
    <V> V get(final Object key, final Class<V> clazz);
    
    /**
     * @see java.util.Map#keySet()
     * 
     * @return set of keys
     */
    Set<Object> keySet();

    /**
     * Convert this object to map
     * 
     * @return a map
     */
    @SuppressWarnings("rawtypes")
	Map toMap();
    
    /**
     * @see java.util.Map#get(java.lang.Object)
     * 
     * @param <V> type of value
     * @param key the key 
     * @param defValue the default value
     * @return the value mapped to key 
     */
    default <V> V get(final Object key, final V defValue) {
    	return containsKey(key) ? get(key) : defValue;
    }
}
