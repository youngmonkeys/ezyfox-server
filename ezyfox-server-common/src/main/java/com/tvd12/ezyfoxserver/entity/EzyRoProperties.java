package com.tvd12.ezyfoxserver.entity;

public interface EzyRoProperties {

	/**
     * get the value to which the specified key is mapped
     * 
     * @param <T> type of value
     * @param key the key
     * @return a value
     */
    <T> T getProperty(Object key);
    
    /**
     * get the value to which the specified key is mapped and cast value to specific type
     * 
     * @param <T> the value type
     * @param key key
     * @param clazz specific type
     * @return a value
     */
    <T> T getProperty(Object key, Class<T> clazz);
    
    /**
     * check whether has value mapped to the or not
     * 
     * @param key the key to check
     * @return true or false;
     */
    boolean containsKey(Object key);
    
    /**
     * get the value to which the specified key is mapped
     * 
     * @param <T> type of value
     * @param key the key
     * @return a value
     */
    default <T> T getProperty(Class<T> key) {
    	return getProperty((Object)key);
    }
	
}
