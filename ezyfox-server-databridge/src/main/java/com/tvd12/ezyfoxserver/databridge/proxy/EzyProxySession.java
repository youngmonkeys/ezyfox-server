package com.tvd12.ezyfoxserver.databridge.proxy;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.Date;

import com.tvd12.ezyfox.io.EzyDates;
import com.tvd12.ezyfox.io.EzyLists;
import com.tvd12.ezyfoxserver.entity.EzySession;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EzyProxySession {

	protected final EzySession real;
	
	public static EzyProxySession proxySession(EzySession real) {
		return new EzyProxySession(real);
	}
	
	public static Collection<EzyProxySession> newCollection(Collection<EzySession> reals) {
		return EzyLists.newArrayList(reals, EzyProxySession::proxySession);
	}
	
	public long getId() {
		return real.getId();
	}
	
	public String getClientId() {
		return real.getClientId();
	}
	
	public String getClientType() {
		return real.getClientType();
	}
	
	public String getToken() {
		return real.getToken();
	}
	
	public long getMaxIdleTime() {
		return real.getMaxIdleTime();
	}
	
	public long getMaxWaitingTime() {
		return real.getMaxWaitingTime();
	}
	
	public boolean isActivated() {
		return real.isActivated();
	}
	
	public boolean isLoggedIn() {
		return real.isLoggedIn();
	}
	
	public SocketAddress getClientAddress() {
		return real.getClientAddress();
	}
	
	public long getReadBytes() {
		return real.getReadBytes();
	}
	
	public long getWrittenBytes() {
		return real.getWrittenBytes();
	}
	
	public String getCreationTime() {
		return EzyDates.format(new Date(real.getCreationTime()));
	}
	
	public String getLastActivityTime() {
		return EzyDates.format(new Date(real.getLastActivityTime()));
	}
	
	public String getLastReadTime() {
		return EzyDates.format(new Date(real.getLastReadTime()));
	}
	
	public String getLastWriteTime() {
		return EzyDates.format(new Date(real.getLastWriteTime()));
	}
	
}
