package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfoxserver.socket.EzySocketEventLoopOneHandler;

public class EzyNioSocketReadingLoopHandler extends EzySocketEventLoopOneHandler {
	
	@Override
	protected String getThreadName() {
		return "socket-reader";
	}
	
}
