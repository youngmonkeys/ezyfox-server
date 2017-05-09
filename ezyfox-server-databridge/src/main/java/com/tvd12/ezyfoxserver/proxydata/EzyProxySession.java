package com.tvd12.ezyfoxserver.proxydata;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.Date;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.io.EzyDates;
import com.tvd12.ezyfoxserver.io.EzyLists;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EzyProxySession {

	private final EzySession real;
	
	public static EzyProxySession newInstance(EzySession real) {
		return new EzyProxySession(real);
	}
	
	public static Collection<EzyProxySession> newCollection(Collection<EzySession> reals) {
		return EzyLists.newArrayList(reals, EzyProxySession::new);
	}
	
	public long getId() {
		return real.getId();
	}
	
	public String getClientId() {
		return real.getClientId();
	}
	
	public String getReconnectToken() {
		return real.getReconnectToken();
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
