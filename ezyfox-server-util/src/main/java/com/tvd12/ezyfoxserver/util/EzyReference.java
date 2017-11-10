package com.tvd12.ezyfoxserver.util;

public interface EzyReference {

	/**
	 * Decrease reference counting 1
	 */
	void release();
	
	/**
	 * Increase reference counting 1
	 */
	void retain();
	
	/**
	 * Get reference counting
	 * 
	 * @return the reference counting
	 */
	int getReferenceCount();
	
	/**
	 * Check whether release able or not
	 * 
	 * @return true or false
	 */
	default boolean releasable() {
		return getReferenceCount() == 0;
	}
	
}
