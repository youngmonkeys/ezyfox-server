package com.tvd12.ezyfoxserver.entity;

import java.io.Serializable;
import java.net.SocketAddress;
import java.util.concurrent.locks.Lock;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.constant.EzyHasName;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyProperties;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzyPacketQueue;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketDisconnectionQueue;

public interface EzySession extends EzyDeliver, EzyHasName, EzyProperties, EzyDestroyable, Serializable {

	/**
	 * Get session id
	 * 
	 * @return the session id
	 */
	long getId();
	
	/**
	 * Get client id
	 * 
	 * @return  the client id
	 */
	String getClientId();
	
	/**
	 * Set client id
	 * 
	 * @param id the client id
	 */
	void setClientId(String id);
	
	/**
	 * Get client key to encrypt data send to client
	 * 
	 * @return the client key
	 */
	byte[] getClientKey();
	
	/**
	 * 
	 * Set client key
	 * 
	 * @param key the client key
	 */
	void setClientKey(byte[] key);
	

	/**
	 * Get client type
	 * 
	 * @return the client type
	 */
	String getClientType();
	
	/**
	 * Set client type
	 * 
	 * @param type the client type
	 */
	void setClientType(String type);
	
	/**
	 * Get client version
	 * 
	 * @return the client version
	 */
	String getClientVersion();
	
	/**
	 * Set client version
	 * 
	 * @param version the client version
	 */
	void setClientVersion(String version);
	
	/**
	 * Get session connection type
	 * 
	 * @return the connection type
	 */
	EzyConstant getConnectionType();
	
	/**
	 * Get session reconnect token
	 * 
	 * @return the session reconnect token
	 */
	String getToken();

	/**
	 * Get before reconnect token
	 * 
	 * @return before reconnect token
	 */
	String getBeforeToken();
	
	/**
	 * Set reconnect token
	 * 
	 * @param token the reconnect token
	 */
	void setToken(String token);
	
	/**
	 * The private key that decrypt data
	 * 
	 * @param key the key
	 */
	void setPrivateKey(byte[] key);
	
	/**
	 * Get private key
	 * 
	 * @return the private key
	 */
	byte[] getPrivateKey();
	
	/**
	 * Set public key that encrypt data
	 * 
	 * @param key the public key
	 */
	void setPublicKey(byte[] key);
	
	/**
	 * Get public key
	 * 
	 * @return the public key
	 */
	byte[] getPublicKey();
	
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
	void setCreationTime(long time);
	
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
	void setLastActivityTime(long time);
	
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
	void setLastReadTime(long time);
	
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
	void setLastWriteTime(long time);
	
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
	void addReadBytes(long bytes);
	
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
	void addWrittenBytes(long bytes);
	
	/**
	 * Get read requests
	 * 
	 * @return the number of read request
	 */
	int getReadRequests();
	
	/**
	 * add read requests
	 * 
	 * @param requests number of read requests
	 */
	void addReadRequests(int requests);

	/**
	 * Get written responses
	 * 
	 * @return number of written responses
	 */
	int getWrittenResponses();
	
	/**
	 * add written responses
	 * 
	 * @param responses number of responses
	 */
	void addWrittenResponses(int responses);
	
	/**
	 * Get max idle time
	 * 
	 * @return the max idle time
	 */
	long getMaxIdleTime();
	
	/**
	 * 
	 * @param time the max idle time
	 */
	void setMaxIdleTime(long time);
	
	/**
	 * Set logged in or not
	 * 
	 * @param value logged in or not
	 */
	void setLoggedIn(boolean value);
	
	/**
	 * @return true of user has logged in
	 */
	boolean isLoggedIn();

	/**
	 * @param time the logged time
	 */
	void setLoggedInTime(long time);
	
	/**
	 * @return the logged in time
	 */
	long getLoggedInTime();
	
	/**
	 * @param time the max waiting for user login time
	 */
	void setMaxWaitingTime(long time);
	
	/**
	 * 
	 * @return time the max waiting for user login time
	 */
	long getMaxWaitingTime();
	
	/**
	 * @param activated session is active or not
	 */
	void setActivated(boolean activated);
	
	/**
	 * @return session is active or not
	 */
	boolean isActivated();
	
	/**
	 * @return session is idle or not
	 */
	boolean isIdle();
	
	/**
     * @return destroyed or not
     */
    boolean isDestroyed();
	
	/**
	 * @return streaming enable or not
	 */
	boolean isStreamingEnable();
	
	/**
	 * @return the owner name
	 */
	String getOwnerName();
	
	/**
	 * @return the disconnect reason
	 */
	EzyConstant getDisconnectReason();
	
	/**
	 * 
	 * @param name the lock name
	 * @return the lock
	 */
	Lock getLock(String name);
	
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
	
	/**
	 * close this session
	 */
	void close();
	
	/**
	 * disconnect this session
	 * 
	 * @param reason the reason
	 */
	void disconnect(EzyConstant reason);
	
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
     * @param <T> the connection type
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
    
    /**
     * set disconnection queue
     * 
     * @param queue the disconnection queue
     */
    void setDisconnectionQueue(EzySocketDisconnectionQueue queue);
    
    /**
     * disconnect
     */
    default void disconnect() {
        disconnect(EzyDisconnectReason.UNKNOWN);
    }
	
}
