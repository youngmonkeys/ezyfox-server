package com.tvd12.ezyfoxserver.entity;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.tvd12.ezyfoxserver.delegate.EzySessionDelegate;
import com.tvd12.ezyfoxserver.sercurity.EzyMD5;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {"privateKey", "publicKey", "clientKey", "fullReconnectToken"})
@EqualsAndHashCode(of = "id", callSuper = false)
public abstract class EzyAbstractSession extends EzyEntity implements EzySession {

	protected long id;
	protected Lock lock;
	protected long creationTime;
	protected long lastActivityTime;
	protected long lastReadTime;
	protected long lastWriteTime;
	protected long readBytes;
	protected long writtenBytes;
	protected long maxIdleTime;
	protected byte[] privateKey;
	protected byte[] publicKey;
	protected byte[] clientKey;
	protected boolean loggedIn;
	protected boolean activated;
	protected long loggedInTime;
	protected long maxWaitingTime;
	protected String reconnectToken;
	protected String fullReconnectToken;
	protected EzySessionDelegate delegate;
	
	{
		maxIdleTime = 3 * 60 * 1000;
		maxWaitingTime = 6 * 1000;
		lock = new ReentrantLock();
	}

	public void setReconnectToken(String token) {
		this.fullReconnectToken = token;
		this.reconnectToken = EzyMD5.cryptUTF(token);
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
	public void setActivated(boolean value) {
		this.activated = value;
	}
	
}
