package com.tvd12.ezyfoxserver.databridge.proxy;

import java.util.Collection;

import com.tvd12.ezyfox.io.EzyLists;
import com.tvd12.ezyfoxserver.entity.EzyUser;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EzyProxyUser {

	protected final EzyUser real;
	
	public static EzyProxyUser proxyUser(EzyUser real) {
		return new EzyProxyUser(real);
	}
	
	public static Collection<EzyProxyUser> newCollection(Collection<EzyUser> reals) {
		return EzyLists.newArrayList(reals, u -> EzyProxyUser.proxyUser(u));
	}
	
	public long getId() {
		return real.getId();
	}
	
	public String getName() {
		return real.getName();
	}
	
	public Collection<EzyProxySession> getSessions() {
		return EzyProxySession.newCollection(real.getSessions());
	}
	
}
