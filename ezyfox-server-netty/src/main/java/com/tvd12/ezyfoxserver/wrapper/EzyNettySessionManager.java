package com.tvd12.ezyfoxserver.wrapper;

import com.tvd12.ezyfoxserver.entity.EzyNettySession;

import io.netty.channel.Channel;

public interface EzyNettySessionManager extends EzySessionManager<EzyNettySession> {

	/**
	 * Borrow session from pool and map the session to channel 
	 * 
	 * @param channel the channel
	 * @return the session mapped channel
	 */
	EzyNettySession borrowSession(Channel channel);
	
	/**
	 * Get session by channel
	 * 
	 * @param channel the channel
	 * @return the session mapped to the channel
	 */
	EzyNettySession getSession(Channel channel);
	
}
