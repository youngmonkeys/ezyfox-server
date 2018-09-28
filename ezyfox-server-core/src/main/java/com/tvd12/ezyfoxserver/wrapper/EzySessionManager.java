package com.tvd12.ezyfoxserver.wrapper;

import java.util.List;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.socket.EzyChannel;

public interface EzySessionManager<S extends EzySession> {

    /**
     * Recognize that the session has logged in
     * 
     * @param session the session
     */
    void addLoggedInSession(S session);
    
    /**
     * Check contains session mapped to id or not
     * 
     * @param id the session id
     * @return true or false
     */
    boolean containsSession(long id); 
    
	/**
     * Get session by id
     * 
     * @param id the id
     * @return the session mapped to the id
     */
    EzySession getSession(long id);
	
	/**
	 * Add session to disconnect queue
	 * 
	 * @param session the session
	 * @param reason the reason
	 */
	void removeSession(S session, EzyConstant reason);
	
	/**
	 * clear session
	 * 
	 * @param session the session
	 */
	void clearSession(S session);
	
	/**
     * Get all sessions
     * 
     * @return all sessions
     */
    List<S> getAllSessions();
	
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
	
	/**
     * Provide session from pool and map the session to channel 
     * 
     * @param channel the channel
     * @return the session mapped channel
     */
    S provideSession(EzyChannel channel);
    
    /**
     * Get session by connection
     * 
     * @param connection the connection
     * @return the session mapped to the connection
     */
    S getSession(Object connection);
    
    /**
     * Add session to disconnect queue
     * 
     * @param session the session
     */
    default void removeSession(S session) {
        removeSession(session, EzyDisconnectReason.UNKNOWN);
    }
	
}
