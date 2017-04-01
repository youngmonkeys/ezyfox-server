package com.tvd12.ezyfoxserver.entity;

import io.netty.channel.Channel;

public interface EzyNettySession extends EzySession {

	/**
	 * Get the channel mapped to this session
	 * 
	 * @return the channel
	 */
	Channel getChannel();
	
	/**
	 * Map this session to the channel
	 * 
	 * @param channel the channel
	 */
	void setChannel(Channel channel);
	
}
