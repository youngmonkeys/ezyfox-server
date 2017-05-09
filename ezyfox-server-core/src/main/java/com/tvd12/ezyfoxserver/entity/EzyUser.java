package com.tvd12.ezyfoxserver.entity;

import java.util.Collection;
import java.util.concurrent.locks.Lock;

import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.util.EzyProperties;

public interface EzyUser extends EzySender, EzyProperties, EzyDestroyable {

	/**
	 * Get user id
	 * 
	 * @return the user id
	 */
	long getId();
	
	/**
	 * Get user name
	 * 
	 * @eturn the user name
	 */
	String getName();
	
	/**
	 * Get max session
	 * 
	 * @return the max session
	 */
	int getMaxSessions();
	
	/**
	 * Get max idle time
	 * 
	 * @return the max idle time
	 */
	long getMaxIdleTime();
	
	/**
	 * Get start idle time
	 * 
	 * @return the start idle time
	 */
	long getStartIdleTime();
	
	/**
	 * Set start idle time
	 * 
	 * @param time the start idle time
	 */
	void setStartIdleTime(long time);
	
	/**
     * Get the session count
     * 
     * @return the session count
     */
    int getSessionCount();
	
	/**
	 * Get current session
	 * 
	 * @return the current session
	 */
	Collection<EzySession> getSessions();
	
	/**
	 * Add new session
	 * 
	 * @param session the session to add
	 */
	void addSession(EzySession session);
	
	/**
     * remove a session
     * 
     * @param session the session to remove
     */
    void removeSession(EzySession session);
    
	/**
	 * @param name the lock name
	 * @return the lock
	 */
	Lock getLock(String name);
	
}
