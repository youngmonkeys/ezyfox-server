package com.tvd12.ezyfoxserver.netty.wrapper;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.netty.entity.EzyNettySession;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;

import io.netty.channel.Channel;

public interface EzyNettySessionManager extends EzySessionManager<EzyNettySession> {

	/**
	 * Get session by channel
	 * 
	 * @param channel the channel
	 * @return the session mapped to the channel
	 */
	EzyNettySession getSession(Channel channel);
	
	/**
	 * Borrow session from pool and map the session to channel 
	 * 
	 * @param channel the channel
	 * @param type the connection type
	 * @return the session mapped channel
	 */
	EzyNettySession borrowSession(Channel channel, EzyConnectionType type);
	
}
