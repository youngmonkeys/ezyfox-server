package com.tvd12.ezyfoxserver.socket;

public abstract class EzySocketRequestHandlingLoopHandler extends EzySocketEventLoopHandler {

	@Override
	protected final String getThreadName() {
		return getRequestType() + "-socket-request-handler";
	}
	
	protected abstract String getRequestType();
	
}
