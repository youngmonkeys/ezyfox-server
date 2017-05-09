package com.tvd12.ezyfoxserver.nio.entity;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.nio.socket.EzyPacketQueue;
import com.tvd12.ezyfoxserver.nio.socket.EzySessionTicketsQueue;

public interface EzyNioSession extends EzySession {

	String SELECTION_KEY	= "SessionSelectionKey";
	
	/**
	 * Get the channel mapped to this session
	 * 
	 * @return the channel
	 */
	EzyChannel getChannel();
	
	/**
	 * Map this session to the channel
	 * 
	 * @param channel the channel
	 */
	void setChannel(EzyChannel channel);
	
	/**
	 * Get connection
	 * 
	 * @return the connection
	 */
	<T> T getConnection();
	
	/**
	 * Get packet queue
	 * 
	 * @return the packet queue
	 */
	EzyPacketQueue getPacketQueue();
	
	/**
	 * set session tickets queue
	 * 
	 * @param queue the queue
	 */
	void setSessionTicketsQueue(EzySessionTicketsQueue queue);
	
}
