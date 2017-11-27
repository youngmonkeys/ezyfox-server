package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopHandler;

public class EzyNioSocketReadingLoopHandler extends EzySocketEventLoopHandler {
	
	@Override
	protected String getThreadName() {
		return "socket-reader";
	}
	
}
