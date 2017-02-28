package com.tvd12.ezyfoxserver.wrapper;

public interface EzyManagers {

	/**
	 * Get instance of manager class
	 * 
	 * @param managerClass the manager class
	 * @return the instance of manager class
	 */
	<T> T getManager(final Class<T> managerClass);
	
}
