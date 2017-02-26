package com.tvd12.ezyfoxserver.entity;

public interface EzyUser extends EzyProperties {

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
	 * Get current session
	 * 
	 * @return the current session
	 */
	EzySession getSession();
	
	/**
	 * Set current session
	 * 
	 * @param session the session to set
	 */
	void setSession(EzySession session);
	
}
