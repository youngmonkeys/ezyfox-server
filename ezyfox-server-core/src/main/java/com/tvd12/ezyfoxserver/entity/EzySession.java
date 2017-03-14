package com.tvd12.ezyfoxserver.entity;

import java.net.SocketAddress;

import io.netty.channel.Channel;

public interface EzySession extends EzySender, EzyProperties {

	/**
	 * Get session id
	 * 
	 * @return the session id
	 */
	long getId();
	
	/**
	 * Get session token
	 * 
	 * @return the session token
	 */
	String getToken();
	
	/**
	 * Set token
	 * 
	 * @param token the token
	 */
	void setToken(final String token);
	
	/**
	 * Get creation time in long
	 * 
	 * @return the creation time
	 */
	long getCreationTime();
	
	/**
	 * Set creation time
	 * 
	 * @param time the creation time
	 */
	void setCreationTime(final long time);
	
	/**
	 * Get last activity time in long
	 * 
	 * @return the last activity time
	 */
	long getLastActivityTime();
	
	/**
	 * Set last activity time
	 * 
	 * @param time the last activity time
	 */
	void setLastActivityTime(final long time);
	
	/**
	 * Get last read time in long
	 * 
	 * @return the last read time
	 */
	long getLastReadTime();
	
	/**
	 * Set last read time
	 * 
	 * @param time the last read time
	 */
	void setLastReadTime(final long time);
	
	/**
	 * Get last write time in long
	 * 
	 * @return the last read time
	 */
	long getLastWriteTime();
	
	/**
	 * Set last write time
	 * 
	 * @param time the last read time
	 */
	void setLastWriteTime(final long time);
	
	/**
	 * Get read bytes
	 * 
	 * @return the read bytes
	 */
	long getReadBytes();

	/**
	 * Add read bytes
	 * 
	 * @param bytes the read bytes
	 */
	void addReadBytes(final long bytes);
	
	/**
	 * Get written bytes
	 * 
	 * @return the written bytes
	 */
	long getWrittenBytes();
	
	/**
	 * Add written bytes
	 * 
	 * @param bytes the written bytes
	 */
	void addWrittenBytes(final long bytes);
	
	/**
	 * Get max idle time
	 * 
	 * @return the max idle time
	 */
	long getMaxIdleTime();
	
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
	void setChannel(final Channel channel);
	
	/**
	 * Get client full ip address
	 * 
	 * @return the client full ip address
	 */
	SocketAddress getClientAddress();
	
	/**
	 * Get server full ip address
	 * 
	 * @return the server full ip address
	 */
	SocketAddress getServerAddress();
	
}
