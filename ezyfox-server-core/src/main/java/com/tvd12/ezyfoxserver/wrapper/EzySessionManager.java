package com.tvd12.ezyfoxserver.wrapper;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzySessionManager<S extends EzySession> {

	/**
	 * Get session by token
	 * 
	 * @param token the token
	 * @return the session mapped to the token
	 */
	EzySession getSession(String token);
	
	/**
	 * Return the session to pool
	 * 
	 * @param session the session
	 */
	void returnSession(S session);
	
	/**
	 * Return the session to pool
	 * 
	 * @param session the session
	 * @param reason the reason
	 */
	void returnSession(S session, EzyConstant reason);
	
}
