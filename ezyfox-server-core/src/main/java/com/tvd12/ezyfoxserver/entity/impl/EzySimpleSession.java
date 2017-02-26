package com.tvd12.ezyfoxserver.entity.impl;

import java.net.InetAddress;

import com.tvd12.ezyfoxserver.entity.EzyEntity;
import com.tvd12.ezyfoxserver.entity.EzySession;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "id", callSuper = false)
public class EzySimpleSession extends EzyEntity implements EzySession {

	protected long id;
	protected String token;
	protected long creationTime;
	protected long lastActivityTime;
	protected long lastReadTime;
	protected long lastWriteTime;
	protected long readBytes;
	protected long writtenBytes;
	protected long maxIdleTime;
	protected InetAddress clientAddress;
	protected InetAddress serverAddress;

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
		return clientAddress.toString();
	}
	
	@Override
	public String getServerAddress() {
		return serverAddress.toString();
	}
	
}
