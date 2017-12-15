package com.tvd12.ezyfoxserver.netty.entity;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.netty.handler.EzyHandlerGroup;

public interface EzyNettySession extends EzySession {
	
	EzyHandlerGroup getHandlerGroup();
	
	void setHandlerGroup(EzyHandlerGroup handlerGroup);
	
}
