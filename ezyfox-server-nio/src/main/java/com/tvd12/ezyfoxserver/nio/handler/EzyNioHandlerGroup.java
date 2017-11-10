package com.tvd12.ezyfoxserver.nio.handler;

public interface EzyNioHandlerGroup extends EzyHandlerGroup {

	void fireBytesReceived(byte[] bytes) throws Exception;
	
}
