package com.tvd12.ezyfoxserver.nio.wrapper;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.nio.handler.EzyHandlerGroup;
import com.tvd12.ezyfoxserver.socket.EzyChannel;

public interface EzyHandlerGroupManager {

	<T extends EzyHandlerGroup> T getHandlerGroup(Object connection);
	
	<T extends EzyHandlerGroup> T removeHandlerGroup(Object connection);
	
	<T extends EzyHandlerGroup> T newHandlerGroup(EzyChannel channel, EzyConnectionType type);
	
}
