package com.tvd12.ezyfoxserver.wrapper;

public interface EzyManagers {

    /**
     * Stop destroyable managers
     */
    void stopManagers();
    
	/**
	 * Start startable managers
	 */
	void startManagers();
	
	/**
	 * Get instance of manager class
	 * 
	 * @param <T> the manager type
	 * @param managerClass the manager class
	 * @return the instance of manager class
	 */
	<T> T getManager(Class<T> clazz);
	
	/**
	 * @param <T> the manager type
	 * @param clazz the manager interface clazz
	 * @param instance the manager instance
	 */
	@SuppressWarnings("rawtypes")
    void addManager(Class clazz, Object instance);
	
}
