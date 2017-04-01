package com.tvd12.ezyfoxserver.entity;

import java.net.SocketAddress;

import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzyData;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EzySimpleSession extends EzyAbstractSession implements EzyNettySession {

	protected Channel channel;
	
	@Override
	public SocketAddress getClientAddress() {
		return channel.remoteAddress();
	}
	
	@Override
	public SocketAddress getServerAddress() {
		return channel.localAddress();
	}
	
	@Override
	public void send(EzyData data) {
		channel.writeAndFlush(data);
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
