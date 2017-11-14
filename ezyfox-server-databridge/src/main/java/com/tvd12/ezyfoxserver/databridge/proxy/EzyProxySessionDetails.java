package com.tvd12.ezyfoxserver.databridge.proxy;

import com.tvd12.ezyfoxserver.entity.EzySession;

public class EzyProxySessionDetails extends EzyProxySession {

	public EzyProxySessionDetails(EzySession real) {
		super(real);
	}

	public static EzyProxySessionDetails proxySessionDetails(EzySession real) {
		return new EzyProxySessionDetails(real);
	}
	
}
