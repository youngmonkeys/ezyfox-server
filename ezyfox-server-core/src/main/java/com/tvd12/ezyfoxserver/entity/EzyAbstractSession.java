package com.tvd12.ezyfoxserver.entity;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.delegate.EzySessionDelegate;
import com.tvd12.ezyfoxserver.sercurity.EzyMD5;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzyPacket;
import com.tvd12.ezyfoxserver.socket.EzyPacketQueue;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySimplePacket;
import com.tvd12.ezyfoxserver.socket.EzySocketDataDecoderGroup;
import com.tvd12.ezyfoxserver.socket.EzySocketDataDecoderGroupAware;
import com.tvd12.ezyfoxserver.util.EzyEquals;
import com.tvd12.ezyfoxserver.util.EzyHashCodes;
import com.tvd12.ezyfoxserver.util.EzyProcessor;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class EzyAbstractSession 
        extends EzyEntity 
        implements EzySession, EzyImmediateDataSenderAware, EzySocketDataDecoderGroupAware, EzyHasSessionDelegate {
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
	protected EzyConstant connectionType;

	protected long maxWaitingTime  = 5 * 1000;
	protected long maxIdleTime     = 3 * 60 * 1000;
	
	protected EzyChannel channel;
	protected EzyPacketQueue packetQueue;
    protected EzySessionTicketsQueue sessionTicketsQueue;
    protected EzyImmediateDataSender immediateDataSender;
    protected EzySocketDataDecoderGroup dataDecoderGroup;
	
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
	public final void send(EzyData data, EzyTransportType type) {
	    if(!activated) return;
	    addWrittenResponses(1);
	    setLastWriteTime(System.currentTimeMillis());
        setLastActivityTime(System.currentTimeMillis());
        sendData(data, type);
	}
	
	@Override
	public void sendNow(EzyData data, EzyTransportType type) {
	    EzyPacket packet = createDataPacket(data, type);
        immediateDataSender.sendPacketNow(packet);
	}
	
    protected void sendData(EzyData data, EzyTransportType type) {
        EzyPacket packet = createDataPacket(data, type);
        synchronized (packetQueue) {
            boolean empty = packetQueue.isEmpty();
            boolean success = packetQueue.add(packet);
            if(empty && success) {
                sessionTicketsQueue.add(this);
            }
        }
    }
    
    protected EzyPacket createDataPacket(EzyData data, EzyTransportType type) {
        try {
            Object bytes = dataDecoderGroup.fireDecodeData(data);
            EzySimplePacket packet = new EzySimplePacket();
            packet.setType(type);
            packet.setData(bytes);
            return packet;
        }
        catch(Exception e) {
            throw new IllegalArgumentException("can't send data: " + data + " to client");
        }
    }
    
    @Override
    public void setChannel(EzyChannel channel) {
        this.channel = channel;
    }
    
    @Override
    public void disconnect() {
        EzyProcessor.processWithLogException(() -> channel.close()); 
    }
    
    @Override
    public void close() {
        EzyProcessor.processWithLogException(() -> channel.close());
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
	public void destroy() {
	    this.channel = null;
	    this.delegate = null;
	    this.activated = false;
	    this.loggedIn = false;
	    this.readBytes = 0L;
	    this.writtenBytes = 0L;
	    this.connectionType = null;
	    if(locks != null)
	        this.locks.clear();
	    if(properties != null)
            this.properties.clear();
	    this.locks = null;
	    this.properties = null;
	    this.dataDecoderGroup = null;
	    this.immediateDataSender = null;
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
