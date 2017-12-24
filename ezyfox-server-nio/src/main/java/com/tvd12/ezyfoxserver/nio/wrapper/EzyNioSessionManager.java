package com.tvd12.ezyfoxserver.nio.wrapper;

import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;

public interface EzyNioSessionManager extends EzySessionManager<EzyNioSession> {

	/**
	 * Provide session from pool and map the session to channel 
	 * 
	 * @param channel the channel
	 * @return the session mapped channel
	 */
	EzyNioSession provideSession(EzyChannel channel);
	
	/**
	 * Get session by connection
	 * 
	 * @param connection the connection
	 * @return the session mapped to the connection
	 */
	EzyNioSession getSession(Object connection);
	
}
