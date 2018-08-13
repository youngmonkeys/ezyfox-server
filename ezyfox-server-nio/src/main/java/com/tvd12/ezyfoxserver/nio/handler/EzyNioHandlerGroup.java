package com.tvd12.ezyfoxserver.nio.handler;

import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;

public interface EzyNioHandlerGroup extends EzyHandlerGroup {

	void fireBytesReceived(byte[] bytes) throws Exception;
	
	EzyNioSession getSession();
	
}
