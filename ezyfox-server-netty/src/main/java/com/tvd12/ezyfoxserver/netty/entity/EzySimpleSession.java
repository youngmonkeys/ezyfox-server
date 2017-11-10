package com.tvd12.ezyfoxserver.netty.entity;

import java.net.SocketAddress;

import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzyData;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EzySimpleSession extends EzyAbstractSession implements EzyNettySession {
	private static final long serialVersionUID = -6257434105426561446L;
	
	protected transient Channel channel;
	
	@Override
	public SocketAddress getClientAddress() {
		return channel != null ? channel.remoteAddress() : null;
	}
	
	@Override
	public SocketAddress getServerAddress() {
		return channel != null ? channel.localAddress() : null;
	}
	
	@Override
	protected void sendData(EzyData data, EzyTransportType type) {
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
	
	@Override
	public void destroy() {
		super.destroy();
		this.setChannel(null);
	}
	
}
