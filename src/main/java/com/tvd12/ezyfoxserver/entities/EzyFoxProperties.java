/**
 * 
 */
package com.tvd12.ezyfoxserver.entities;

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
    public void setProperty(Object key, Object value);
    
    /**
     * get the value to which the specified key is mapped
     * 
     * @param <T> type of value
     * @param key key
     * @return a value
     */
    public <T> T getProperty(Object key);
    
    /**
     * get the value to which the specified key is mapped and cast value to specific type
     * 
     * @param <T> the value type
     * @param key key
     * @param clazz specific type
     * @return a value
     */
    public <T> T getProperty(Object key, Class<T> clazz);
    
    /**
     * removes the mapping for a key from the map
     * 
     * @param key the key
     */
    public void removeProperty(Object key);

    /**
     * check whether has value mapped to the or not
     * 
     * @param key the key to check
     * @return true or false;
     */
    public boolean containsKey(Object key);
    
}
