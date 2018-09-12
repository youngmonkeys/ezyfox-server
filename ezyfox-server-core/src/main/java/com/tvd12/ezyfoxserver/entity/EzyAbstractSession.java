package com.tvd12.ezyfoxserver.entity;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyEntity;
import com.tvd12.ezyfox.sercurity.EzyMD5;
import com.tvd12.ezyfox.util.EzyEquals;
import com.tvd12.ezyfox.util.EzyHashCodes;
import com.tvd12.ezyfox.util.EzyProcessor;
import com.tvd12.ezyfoxserver.delegate.EzySessionDelegate;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzyPacket;
import com.tvd12.ezyfoxserver.socket.EzyPacketQueue;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySimpleSocketDisconnection;
import com.tvd12.ezyfoxserver.socket.EzySocketDisconnection;
import com.tvd12.ezyfoxserver.socket.EzySocketDisconnectionQueue;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class EzyAbstractSession 
        extends EzyEntity 
        implements 
            EzySession,
            EzyDisconnectReasonAware,
            EzyImmediateDeliverAware, 
            EzyDroppedPacketsAware {
    private static final long serialVersionUID = -4112736666616219904L;
    
    protected long id;
    protected String name;
    protected String clientId;
    protected String ownerName;
	protected long creationTime;
	protected long lastReadTime;
	protected long lastWriteTime;
	protected long readBytes;
	protected long writtenBytes;
	protected long lastActivityTime;
	protected long loggedInTime;
	protected int readRequests;
	protected int writtenResponses;
	
	protected byte[] privateKey;
	protected byte[] publicKey;
	protected byte[] clientKey;
	
	protected volatile boolean loggedIn;
    protected volatile boolean activated;
    
	protected String clientType;
	protected String clientVersion;
	protected String reconnectToken;
	protected String beforeReconnectToken;
	protected String fullReconnectToken;
	protected EzyConstant connectionType;
	protected EzyConstant disconnectReason;

	protected long maxWaitingTime  = 5 * 1000;
	protected long maxIdleTime     = 3 * 60 * 1000;
	
	protected EzyChannel channel;
	protected EzyPacketQueue packetQueue;
	protected EzyDroppedPackets droppedPackets;
	protected EzyImmediateDeliver immediateDeliver;
    protected EzySessionTicketsQueue sessionTicketsQueue;
    protected EzySocketDisconnectionQueue disconnectionQueue;
	
	protected transient EzySessionDelegate delegate;
	
	@Setter(AccessLevel.NONE)
	protected boolean disconnectionRegistered;
	@Setter(AccessLevel.NONE)
	protected Object disconnectionLock = new Object();
	@Setter(AccessLevel.NONE)
	protected Map<String, Lock> locks = new ConcurrentHashMap<>();
	
	protected static final String RECONNECT_TOKEN_SALT = "$1$reconnectToken";
	
	public void setOwner(EzyUser owner) {
	    this.ownerName = owner.getName();
	    this.delegate.onSessionLoggedIn(owner);
	}
	
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
	public boolean isIdle() {
	    if(!loggedIn)
	        return false;
	    return maxIdleTime < (System.currentTimeMillis() - lastReadTime);
	}
	
	@Override
	public Lock getLock(String name) {
	    return locks.computeIfAbsent(name, k -> new ReentrantLock());
	}
	
	@Override
	public final void send(EzyPacket packet) {
	    if(!activated) return;
	    addWrittenResponses(1);
	    setLastWriteTime(System.currentTimeMillis());
        setLastActivityTime(System.currentTimeMillis());
        addPacketToSessionQueue(packet);
	}
	
	@Override
	public void sendNow(EzyPacket packet) {
        immediateDeliver.sendPacketNow(packet);
	}
	
    private void addPacketToSessionQueue(EzyPacket packet) {
        boolean empty = false;
        boolean success = false;
        synchronized (packetQueue) {
            empty = packetQueue.isEmpty();
            success = packetQueue.add(packet);
            if(success && empty) {
                    sessionTicketsQueue.add(this);
            }
        }
        if(!success) {
            droppedPackets.addDroppedPacket(packet);
            packet.release();
        }
    }
    
    @Override
    public void disconnect(EzyConstant disconnectReason) {
        synchronized (disconnectionLock) {
            if(!disconnectionRegistered) {
                this.disconnectReason = disconnectReason;
                this.disconnectionQueue.add(newDisconnection(disconnectReason));
                this.disconnectionRegistered = true;
            }
        } 
    }
    
    @Override
    public void close() {
        EzyProcessor.processWithLogException(() -> channel.close());
    }
    
    private EzySocketDisconnection newDisconnection(EzyConstant reason) {
        return new EzySimpleSocketDisconnection(this, disconnectReason);
    }
    
    @Override
    public <T> T getConnection() {
        return channel != null ? channel.getConnection() : null;
    }
    
    @Override
    public SocketAddress getServerAddress() {
        return channel != null ? channel.getServerAddress() : null;
    }
    
    @Override
    public SocketAddress getClientAddress() {
        return channel != null ? channel.getClientAddress() : null;
    }
    
    @Override
    public String getName() {
        return new StringBuilder()
                .append(name)
                .append("(")
                    .append("owner: ").append(ownerName)
                    .append(", ")
                    .append("address: ").append(getClientAddress())
                .append(")")
                .toString();
    }
	
	@Override
	public void destroy() {
	    this.channel = null;
	    this.delegate = null;
	    this.activated = false;
	    this.loggedIn = false;
	    this.readBytes = 0L;
	    this.writtenBytes = 0L;
	    this.connectionType = null;
	    this.disconnectionLock = null;
	    if(locks != null)
	        this.locks.clear();
	    if(properties != null)
            this.properties.clear();
	    this.locks = null;
	    this.droppedPackets = null;
	    this.immediateDeliver = null;
	    if(packetQueue != null) {
            synchronized (packetQueue) {
                this.packetQueue.clear();
                if(sessionTicketsQueue != null)
                    this.sessionTicketsQueue.remove(this);
            }
        }
	    this.packetQueue = null;
	    this.sessionTicketsQueue = null;
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
	
	@Override
	public String toString() {
	    return new StringBuilder()
	            .append("(")
	            .append("id: ").append(id)
	            .append(", type: ").append(clientType)
	            .append(", version: ").append(clientVersion)
	            .append(", address: ").append(getClientAddress())
	            .append(", reconnectToken: ").append(reconnectToken)
	            .append(")")
	            .toString();
	}
	
}
