package com.tvd12.ezyfoxserver.entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.delegate.EzySessionDelegate;
import com.tvd12.ezyfoxserver.sercurity.EzyMD5;
import com.tvd12.ezyfoxserver.util.EzyEquals;
import com.tvd12.ezyfoxserver.util.EzyHashCodes;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {"privateKey", "publicKey", "clientKey", "fullReconnectToken"})
public abstract class EzyAbstractSession 
        extends EzyEntity 
        implements EzySession, EzyHasSessionDelegate {
    private static final long serialVersionUID = -4112736666616219904L;
    
    protected long id;
    protected String name;
    protected String clientId;
	protected long creationTime;
	protected long lastReadTime;
	protected long lastWriteTime;
	protected long readBytes;
	protected long writtenBytes;
	protected long lastActivityTime;
	protected int readRequests;
	protected int writtenResponses;
	
	protected byte[] privateKey;
	protected byte[] publicKey;
	protected byte[] clientKey;
	
	protected long loggedInTime;
	
	protected volatile boolean loggedIn;
    protected volatile boolean activated;
	
	protected String clientType;
	protected String clientVersion;
	protected String reconnectToken;
	protected String fullReconnectToken;
	protected EzyConnectionType connectionType;

	protected long maxWaitingTime  = 5 * 1000;
	protected long maxIdleTime     = 3 * 60 * 1000;
	
	protected transient EzySessionDelegate delegate;
	
	@Setter(AccessLevel.NONE)
	protected Map<String, Lock> locks = new ConcurrentHashMap<>();
	
	protected static final String RECONNECT_TOKEN_SALT = "$1$reconnectToken";
	
	@Override
	public void setReconnectToken(String token) {
		this.fullReconnectToken = token;
		this.reconnectToken = EzyMD5.cryptUtf(token, RECONNECT_TOKEN_SALT);
	}
	
	@Override
	public void addReadBytes(long bytes) {
		this.readBytes += bytes;
	}
	
	@Override
	public void addWrittenBytes(long bytes) {
		this.writtenBytes += bytes;
	}
	
	@Override
	public void addReadRequests(int requests) {
	    this.readRequests += requests;
	}
	
	@Override
	public void addWrittenResponses(int responses) {
	    this.writtenResponses += responses;
	}
	
	@Override
	public void setActivated(boolean value) {
		this.activated = value;
	}
	
	@Override
	public Lock getLock(String name) {
	    return locks.computeIfAbsent(name, k -> new ReentrantLock());
	}
	
	@Override
	public final void send(EzyData data, EzyTransportType type) {
	    if(!activated) return;
	    addWrittenResponses(1);
	    setLastWriteTime(System.currentTimeMillis());
        setLastActivityTime(System.currentTimeMillis());
        sendData(data, type);
	}
	
	protected abstract void sendData(EzyData data, EzyTransportType type);
	
	@Override
	public void destroy() {
	    this.delegate = null;
	    this.activated = false;
	    this.loggedIn = false;
	    this.readBytes = 0L;
	    this.writtenBytes = 0L;
	    this.properties.clear();
	}
	
	@Override
	public boolean equals(Object obj) {
	    return new EzyEquals<EzyAbstractSession>()
	            .function(c -> c.id)
	            .isEquals(this, obj);
	}
	
	@Override
	public int hashCode() {
	    return new EzyHashCodes().append(id).toHashCode();
	}
	
}
