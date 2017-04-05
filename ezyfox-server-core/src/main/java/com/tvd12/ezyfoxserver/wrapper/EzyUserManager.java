package com.tvd12.ezyfoxserver.wrapper;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public interface EzyUserManager {
	
	/**
	 * Add user
	 * 
	 * @param user the user
	 */
	void addUser(EzyUser user);
	
	/**
	 * Get user by id
	 * 
	 * @param userId the user id
	 * @return the user
	 */
	EzyUser getUser(long userId);
	
	/**
	 * Get user by id
	 * 
	 * @param username the user name
	 * @return the user
	 */
	EzyUser getUser(String username);
	
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
	 * @param userId the user id
	 * @return true or false
	 */
	boolean containsUser(long userId);
	
	/**
	 * Check whether contains user or not
	 * 
	 * @param username the user name
	 * @return true or false
	 */
	boolean containsUser(String username);
	
	/**
	 * Check whether contains user or not
	 * 
	 * @param session the user session
	 * @return true or false
	 */
	boolean containsUser(EzySession session);
	
	/**
	 * Remove user
	 * 
	 * @param user the user
	 */
	void removeUser(EzyUser user);
	
	/**
	 * Check whether contains user or not
	 * 
	 * @param user the user
	 * @return true or false
	 */
	default boolean containsUser(EzyUser user) {
		return containsUser(user.getName());
	}
	
	/**
	 * Remove user by id
	 * 
	 * @param userId the user id
	 */
	default void removeUser(long userId) {
		removeUser(getUser(userId));
	}
	
	/**
	 * Remove user byte name
	 * 
	 * @param username the user name
	 */
	default void removeUser(String username) {
		removeUser(getUser(username));
	}
	
	/**
	 * Remove user by session
	 * 
	 * @param session the user session
	 */
	default void removeUser(EzySession session) {
		removeUser(getUser(session));
	}
}
