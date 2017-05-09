package com.tvd12.ezyfoxserver.proxydata;

import java.util.Collection;

import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.io.EzyLists;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EzyProxyUser {

	private EzyUser real;
	
	public static EzyProxyUser newInstance(EzyUser real) {
		return new EzyProxyUser(real);
	}
	
	public static Collection<EzyProxyUser> newCollection(Collection<EzyUser> reals) {
		return EzyLists.newArrayList(reals, EzyProxyUser::new);
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
