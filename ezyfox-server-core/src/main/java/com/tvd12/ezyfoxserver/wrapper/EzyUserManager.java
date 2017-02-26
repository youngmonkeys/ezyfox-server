package com.tvd12.ezyfoxserver.wrapper;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public interface EzyUserManager {
	
	/**
	 * Add user
	 * 
	 * @param user the user
	 */
	void addUser(final EzyUser user);
	
	/**
	 * Get user by id
	 * 
	 * @param userId the user id
	 * @return the user
	 */
	EzyUser getUser(final long userId);
	
	/**
	 * Get user by id
	 * 
	 * @param username the user name
	 * @return the user
	 */
	EzyUser getUser(final String username);
	
	/**
	 * Get user by id
	 * 
	 * @param session the user session
	 * @return the user
	 */
	EzyUser getUser(final EzySession session);
	
	/**
	 * Check whether contains user or not
	 * 
	 * @param userId the user id
	 * @return true or false
	 */
	boolean containsUser(final long userId);
	
	/**
	 * Check whether contains user or not
	 * 
	 * @param username the user name
	 * @return true or false
	 */
	boolean containsUser(final String username);
	
	/**
	 * Check whether contains user or not
	 * 
	 * @param session the user session
	 * @return true or false
	 */
	boolean containsUser(final EzySession session);
	
	/**
	 * Remove user
	 * 
	 * @param user the user
	 */
	void removeUser(final EzyUser user);
	
	/**
	 * Check whether contains user or not
	 * 
	 * @param user the user
	 * @return true or false
	 */
	default boolean containsUser(final EzyUser user) {
		return containsUser(user.getName());
	}
	
	/**
	 * Remove user by id
	 * 
	 * @param userId the user id
	 */
	default void removeUser(final long userId) {
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
