package com.tvd12.ezyfoxserver.wrapper;

import com.tvd12.ezyfoxserver.entity.EzySession;

import io.netty.channel.Channel;

public interface EzySessionManager {

	/**
	 * Borrow session from pool and map the session to channel 
	 * 
	 * @param channel the channel
	 * @return the session mapped channel
	 */
	EzySession borrowSession(final Channel channel);
	
	/**
	 * Return the session to pool
	 * 
	 * @param session the session
	 */
	void returnSession(final EzySession session);
	
	/**
	 * Get session by channel
	 * 
	 * @param channel the channel
	 * @return the session mapped to the channel
	 */
	EzySession getSession(final Channel channel);
	
}
