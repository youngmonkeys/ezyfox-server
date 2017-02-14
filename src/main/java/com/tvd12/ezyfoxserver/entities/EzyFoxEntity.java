package com.tvd12.ezyfoxserver.entities;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

/**
 * Each model in application should have properties, and we think key/value is good idea 
 * 
 * @author tavandung12
 *
 */
public abstract class EzyFoxEntity implements EzyFoxProperties {

    // map of key/value properties of model
    @Getter
    protected Map<Object, Object> properties 
        = new ConcurrentHashMap<>();
    
    /**
     * put key and value to map
     * @see java.util.Map#put(Object, Object)
     * 
     * @param key key 
     * @param value value
     */
    @Override
    public void setProperty(Object key, Object value) {
        properties.put(key, value);
    }
    
    /**
     * get the value to which the specified key is mapped
     * @see java.util.Map#get(Object)
     * 
     * @param key key
     * @return a value
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getProperty(Object key) {
        return (T) properties.get(key);
    }
    
    /**
     * get the value to which the specified key is mapped and cast value to specific type
     * @see java.util.Map#get(Object)
     * 
     * @param key key
     * @param clazz specific type
     * @return a value
     */
    @Override
    public <T> T getProperty(Object key, Class<T> clazz) {
        return clazz.cast(properties.get(key));
    }
    
    /**
     * removes the mapping for a key from the map
     * @see java.util.Map#remove(Object)
     * 
     * @param key the key
     */
    @Override
    public void removeProperty(Object key) {
        properties.remove(key);
    }
    
    /**
     * @see java.util.Map#containsKey(Object)
     * 
     * @param key the key
     */
    @Override
    public boolean containsKey(Object key) {
    	return properties.containsKey(key);
    }
    
}
