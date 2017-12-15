package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopHandler;

public class EzyNioConnectionAcceptorLoopHandler extends EzySocketEventLoopHandler {
	
	@Override
	protected String getThreadName() {
		return "connection-acceptor";
	}
	
}
