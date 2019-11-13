package com.tvd12.ezyfoxserver.wrapper;

import java.util.List;
import java.util.concurrent.locks.Lock;

import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public interface EzyUserManager extends EzyDestroyable {
    
    EzyUser addUser(EzyUser user);
    
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
	 * Get users as list
	 * 
	 * @return The users as list
	 */
	List<EzyUser> getUserList();
	
	/**
	 * Get count of users
	 * 
	 * @return count of users
	 */
	int getUserCount();
	
	/**
	 * Get max users count
	 * 
	 * @return the max users count
	 */
	int getMaxUsers();
	
	/**
	 * available to add user or not
	 * 
	 * @return available to add user or not
	 */
	boolean available();
	
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
	 * Remove user
	 * 
	 * @param user the user
	 * @return the current user
	 */
	EzyUser removeUser(EzyUser user);
	
	/**
	 * Get lock mapped to username
	 * 
	 * @param username the username
	 * @return the lock
	 */
	Lock getLock(String username);
	
	/**
	 * Remove lock mapped to username
	 * 
	 * @param username the username
	 */
	void removeLock(String username);
	
	/**
	 * clear all user
	 */
	void clear();
	
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
	 * @return the current user
	 */
	default EzyUser removeUser(long userId) {
		return removeUser(getUser(userId));
	}
	
	/**
	 * Remove user byte name
	 * 
	 * @param username the user name
	 * @return the current user
	 */
	default EzyUser removeUser(String username) {
		return removeUser(getUser(username));
	}
	
}
