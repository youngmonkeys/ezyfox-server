package com.tvd12.ezyfoxserver.socket;

public class EzySocketUserRemovalHandlingLoopHandler extends EzySocketEventLoopOneHandler {

	@Override
	protected final String getThreadName() {
		return "user-removal-handler";
	}
	
}
