package com.tvd12.ezyfoxserver.nio.entity;

import java.net.SocketAddress;

import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.nio.socket.EzyPacketQueue;
import com.tvd12.ezyfoxserver.nio.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.nio.socket.EzySimplePacket;
import com.tvd12.ezyfoxserver.util.EzyProcessor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class EzySimpleSession extends EzyAbstractSession implements EzyNioSession {
	private static final long serialVersionUID = -8390274886953462147L;
	
	protected EzyChannel channel;
	protected EzyPacketQueue packetQueue;
	protected EzySessionTicketsQueue sessionTicketsQueue;
	
	@Override
	public void setChannel(EzyChannel channel) {
		this.channel = channel;
	}
	
	@Override
	protected void sendData(EzyData data, EzyTransportType type) {
		EzySimplePacket packet = new EzySimplePacket();
		packet.setType(type);
		packet.setData(data);
		packetQueue.add(packet);
		sessionTicketsQueue.add(this);
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
		return channel.getConnection();
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
		super.destroy();
		this.packetQueue.clear();
		this.channel = null;
	}
	
}
