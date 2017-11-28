package com.tvd12.ezyfoxserver.socket;

public abstract class EzySocketWritingLoopHandler extends EzySocketEventLoopHandler {

	@Override
	protected String getThreadName() {
		return "socket-writer";
	}
	
}
