package com.tvd12.ezyfoxserver.entity;

import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyEntity;
import com.tvd12.ezyfox.function.EzyFunctions;
import com.tvd12.ezyfox.util.EzyProcessor;
import com.tvd12.ezyfoxserver.delegate.EzySessionDelegate;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzyDatagramChannelAware;
import com.tvd12.ezyfoxserver.socket.EzyDatagramChannelPool;
import com.tvd12.ezyfoxserver.socket.EzyDatagramChannelPoolAware;
import com.tvd12.ezyfoxserver.socket.EzyPacket;
import com.tvd12.ezyfoxserver.socket.EzyPacketQueue;
import com.tvd12.ezyfoxserver.socket.EzyRequestQueue;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySimpleSocketDisconnection;
import com.tvd12.ezyfoxserver.socket.EzySocketDisconnection;
import com.tvd12.ezyfoxserver.socket.EzySocketDisconnectionQueue;
import com.tvd12.ezyfoxserver.socket.EzyUdpClientAddressAware;
import com.tvd12.ezyfoxserver.statistics.EzyRequestFrame;

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
            EzyDatagramChannelAware,
            EzyUdpClientAddressAware,
            EzyImmediateDeliverAware, 
            EzyDroppedPacketsAware,
            EzyDatagramChannelPoolAware {
    private static final long serialVersionUID = -4112736666616219904L;
    
    protected long id;
    protected String name;
    protected String clientId;
    protected String ownerName;
	protected long creationTime;
	protected long lastReadTime;
	protected long lastWriteTime;
	@Setter(AccessLevel.NONE)
	protected long readBytes;
	@Setter(AccessLevel.NONE)
	protected long writtenBytes;
	protected long lastActivityTime;
	protected long loggedInTime;
	protected int readRequests;
	protected int writtenResponses;
	
	protected byte[] privateKey;
	protected byte[] publicKey;
	protected byte[] clientKey;
	protected byte[] sessionKey;
	
	protected volatile boolean loggedIn;
    protected volatile boolean activated;
    protected volatile boolean destroyed;
    protected volatile boolean streamingEnable;

    protected String token;
	protected String clientType;
	protected String clientVersion;
	protected String beforeToken;
	protected EzyConstant connectionType;
	protected EzyConstant disconnectReason;
	protected SocketAddress udpClientAddress;
	protected DatagramChannel datagramChannel;
	protected EzyDatagramChannelPool datagramChannelPool;

	protected long maxWaitingTime  = 5 * 1000;
	protected long maxIdleTime     = 3 * 60 * 1000;
	
	protected EzyChannel channel;
	protected EzyDroppedPackets droppedPackets;
	protected EzyImmediateDeliver immediateDeliver;
    protected EzySessionTicketsQueue sessionTicketsQueue;
    protected EzySocketDisconnectionQueue disconnectionQueue;

    protected EzyPacketQueue packetQueue;
    protected EzyRequestQueue systemRequestQueue;
    protected EzyRequestQueue extensionRequestQueue;
    protected EzyRequestFrame requestFrameInSecond;
	
	protected transient EzySessionDelegate delegate;
	
	@Setter(AccessLevel.NONE)
	protected volatile boolean disconnectionRegistered;
	@Setter(AccessLevel.NONE)
	protected Object disconnectionLock = new Object();
	@Setter(AccessLevel.NONE)
	protected Map<String, Lock> locks = new ConcurrentHashMap<>();
	
	public void setOwner(EzyUser owner) {
	    this.ownerName = owner.getName();
	    this.delegate.onSessionLoggedIn(owner);
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
	public boolean addReceviedRequests(int requests) {
		if(requestFrameInSecond.isExpired())
            requestFrameInSecond = requestFrameInSecond.nextFrame();
        return requestFrameInSecond.addRequests(requests);
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
	    if(loggedIn) {
	        long offset = System.currentTimeMillis() - lastReadTime;
	        boolean idle = maxIdleTime < offset;
	        return idle;
	    }
	    return false;
	}
	
	@Override
	public Lock getLock(String name) {
	    Lock lock = locks.computeIfAbsent(name, EzyFunctions.NEW_REENTRANT_LOCK_FUNC);
	    return lock;
	}
	
	@Override
	public final void send(EzyPacket packet) {
	    if(activated) {
    	    addWrittenResponses(1);
    	    setLastWriteTime(System.currentTimeMillis());
            setLastActivityTime(System.currentTimeMillis());
            addPacketToSessionQueue(packet);
	    }
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
            	EzySessionTicketsQueue ticketsQueue = this.sessionTicketsQueue;
            	if(ticketsQueue != null)
            		sessionTicketsQueue.add(this);
            }
        }
        if(!success) {
            EzyDroppedPackets droppedPacketsNow = droppedPackets;
            if(droppedPacketsNow != null)
                droppedPackets.addDroppedPacket(packet);
            packet.release();
        }
    }
    
    @Override
    public void disconnect(EzyConstant disconnectReason) {
        synchronized (disconnectionLock) {
            if(!disconnectionRegistered) {
                this.disconnectReason = disconnectReason;
                EzySocketDisconnectionQueue queue = this.disconnectionQueue;
                if(queue != null)
                	queue.add(newDisconnection(disconnectReason));
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
	    this.destroyed = true;
	    this.activated = false;
	    this.channel = null;
	    this.delegate = null;
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
	        }
	    }
	    if(systemRequestQueue != null) {
	    	synchronized (systemRequestQueue) {
	    		systemRequestQueue.clear();	
			}
	    }
	    if(extensionRequestQueue != null) {
	    	synchronized (extensionRequestQueue) {
	    		extensionRequestQueue.clear();	
			}
	    }
	    this.sessionTicketsQueue = null;
	    this.disconnectionQueue = null;
	    this.udpClientAddress = null;
	    this.datagramChannel = null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(obj == this)
			return true;
		if(obj instanceof EzyAbstractSession)
			return id == ((EzyAbstractSession)obj).id;
		return false;
	}
	
	@Override
	public int hashCode() {
	    return Long.hashCode(id);
	}
	
	@Override
	public String toString() {
	    return new StringBuilder()
	            .append("(")
	            .append("id: ").append(id)
	            .append(", type: ").append(clientType)
	            .append(", version: ").append(clientVersion)
	            .append(", address: ").append(getClientAddress())
	            .append(", token: ").append(token)
	            .append(")")
	            .toString();
	}
	
}
