package com.tvd12.ezyfoxserver.wrapper;

import com.tvd12.ezyfoxserver.constant.EzyUserRemoveReason;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public interface EzyZoneUserManager extends EzyUserManager {
    
	/**
	 * Add user
	 * 
	 * @param session the session
	 * @param user the user
	 */
	void addUser(EzySession session, EzyUser user);
	
	/**
     * Add user
     * 
     * @param session the session
     * @param user the user
     */
    void bind(EzySession session, EzyUser user);
	
	/**
	 * Get user by id
	 * 
	 * @param session the user session
	 * @return the user
	 */
	EzyUser getUser(EzySession session);
	
	/**
	 * Check whether contains user or not
	 * 
	 * @param session the user session
	 * @return true or false
	 */
	boolean containsUser(EzySession session);
	
	/**
	 * Unmap session has mapped to user
	 * 
	 * @param session the session
	 */
	void unmapSessionUser(EzySession session);
	
	/**
	 * Remove user
	 * 
	 * @param user the user
	 * @param reason the reason
	 */
	void removeUser(EzyUser user, EzyUserRemoveReason reason);
	
	/**
	 * 
	 * Find user by the old session and update user to new session
	 * 
	 * @param user the user
	 * @param newSession the new session
	 * @return the updated user
	 */
	EzyUser findAndUpdateUser(EzySession current, EzySession update);
	
}
