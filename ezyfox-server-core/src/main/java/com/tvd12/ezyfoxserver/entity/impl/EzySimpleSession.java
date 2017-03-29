package com.tvd12.ezyfoxserver.entity.impl;

import java.net.SocketAddress;

import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzyEntity;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.sercurity.EzyMD5;

import io.netty.channel.Channel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {"privateKey", "publicKey", "clientKey", "fullReconnectToken"})
@EqualsAndHashCode(of = "id", callSuper = false)
public class EzySimpleSession extends EzyEntity implements EzySession {

	protected long id;
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
	protected String reconnectToken;
	protected String fullReconnectToken;
	
	protected Channel channel;

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
	
}
