package com.tvd12.ezyfoxserver.socket;

public class EzySocketDisconnectionHandlingLoopHandler extends EzySocketEventLoopOneHandler {

	@Override
	protected final String getThreadName() {
		return "disconnection-handler";
	}
	
}
