package com.tvd12.ezyfoxserver.client.entity;

import com.tvd12.ezyfoxserver.entity.EzySender;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.util.EzyProperties;

public interface EzyClientUser extends EzySender, EzyProperties, EzyDestroyable {

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
	EzyClientSession getSession();
	
	/**
	 * Add new session
	 * 
	 * @param session the session to add
	 */
	void setSession(EzyClientSession session);
	
}
