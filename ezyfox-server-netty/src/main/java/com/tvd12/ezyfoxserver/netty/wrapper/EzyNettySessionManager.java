package com.tvd12.ezyfoxserver.netty.wrapper;

import com.tvd12.ezyfoxserver.netty.entity.EzyNettySession;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;

public interface EzyNettySessionManager extends EzySessionManager<EzyNettySession> {

	/**
	 * Get session by channel
	 * 
	 * @param channel the channel
	 * @return the session mapped to the channel
	 */
	EzyNettySession getSession(EzyChannel channel);
	
	/**
	 * Provide session from pool and map the session to channel 
	 * 
	 * @param channel the channel
	 * @return the session mapped channel
	 */
	EzyNettySession provideSession(EzyChannel channel);
	
}
