package com.tvd12.ezyfoxserver.netty.websocket;

import com.tvd12.ezyfoxserver.socket.EzySocketWritingLoopHandler;

public class EzyWsWritingLoopHandler extends EzySocketWritingLoopHandler {
	
	@Override
	protected String getThreadName() {
		return "web-socket-writer";
	}
	
}
