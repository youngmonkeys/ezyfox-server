package com.tvd12.ezyfoxserver.wrapper;

import java.util.List;
import java.util.Set;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzySessionManager<S extends EzySession> {

    /**
     * Recognize that the session has logged in
     * 
     * @param session the session
     */
    void addLoggedInSession(S session);
    
	/**
	 * Check contains session mapped to token or not
	 * 
	 * @param token the reconnect token
	 * @return true or false
	 */
	boolean containsSession(String token); 
	
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
	
	/**
     * Get all sessions
     * 
     * @return all sessions
     */
    Set<S> getAllSessions();
	
	/**
	 * Get all alive sessions
	 * 
	 * @return all alive sessions
	 */
	List<S> getAliveSessions();
	
	/**
     * Get logged in sessions
     * 
     * @return all logged in sessions
     */
	List<S> getLoggedInSessions();
	
	/**
	 * @return all sessions count
	 */
	int getAllSessionCount();
	
	/**
	 * @return alive sessions count
	 */
	int getAliveSessionCount();
	
	/**
	 * @return logged in session count
	 */
	int getLoggedInSessionCount();
	
}
