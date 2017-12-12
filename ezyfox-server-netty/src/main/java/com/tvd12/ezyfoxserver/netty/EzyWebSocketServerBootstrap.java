package com.tvd12.ezyfoxserver.netty;

public class EzyWebSocketServerBootstrap extends EzySocketServerBootstrap {

	@Override
	protected String getSocketType() {
		return "web socket";
	}
	
}
