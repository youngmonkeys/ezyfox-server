package com.tvd12.ezyfoxserver.entity.impl;

import java.net.SocketAddress;

import com.tvd12.ezyfoxserver.codec.EzyMD5;
import com.tvd12.ezyfoxserver.entity.EzyEntity;
import com.tvd12.ezyfoxserver.entity.EzySession;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id", callSuper = false)
public class EzySimpleSession extends EzyEntity implements EzySession {

	protected long id;
	protected String token;
	protected String fullToken;
	protected long creationTime;
	protected long lastActivityTime;
	protected long lastReadTime;
	protected long lastWriteTime;
	protected long readBytes;
	protected long writtenBytes;
	protected long maxIdleTime;
	protected SocketAddress clientAddress;
	protected SocketAddress serverAddress;

	public void setToken(String token) {
		this.fullToken = token;
		this.token = EzyMD5.cryptUTF(token);
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
	public String getClientAddress() {
		return clientAddress != null ? clientAddress.toString() : "unknown";
	}
	
	@Override
	public String getServerAddress() {
		return serverAddress != null ? serverAddress.toString() : "unknown";
	}
	
}
