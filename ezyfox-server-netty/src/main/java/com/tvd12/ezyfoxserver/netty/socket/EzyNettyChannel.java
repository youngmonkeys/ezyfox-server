package com.tvd12.ezyfoxserver.netty.socket;

import java.net.SocketAddress;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.socket.EzyChannel;

import io.netty.channel.Channel;
import lombok.Getter;

@Getter
public class EzyNettyChannel implements EzyChannel {

	private final Channel channel;
	private final SocketAddress serverAddress;
	private final SocketAddress clientAddress;
	private final EzyConnectionType connectionType;
	
	public EzyNettyChannel(Channel channel, EzyConnectionType connectionType) {
		this.channel = channel;
		this.connectionType = connectionType;
		this.serverAddress = channel.localAddress();
		this.clientAddress = channel.remoteAddress();
	}
	
	@Override
	public int write(Object data) throws Exception {
		channel.writeAndFlush(data);
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Channel getConnection() {
		return channel;
	}
	
	@Override
	public boolean isConnected() {
		return channel.isActive();
	}
	
	@Override
	public void disconnect() {
		channel.disconnect().syncUninterruptibly();
	}
	
	@Override
	public void close() {
		channel.close().syncUninterruptibly();
	}
	
}
