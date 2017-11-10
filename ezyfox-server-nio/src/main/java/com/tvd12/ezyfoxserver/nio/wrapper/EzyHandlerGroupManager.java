package com.tvd12.ezyfoxserver.nio.wrapper;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.nio.entity.EzyChannel;
import com.tvd12.ezyfoxserver.nio.handler.EzyHandlerGroup;

public interface EzyHandlerGroupManager {

	<T extends EzyHandlerGroup> T getHandlerGroup(Object connection);
	
	<T extends EzyHandlerGroup> T removeHandlerGroup(Object connection);
	
	<T extends EzyHandlerGroup> T newHandlerGroup(EzyChannel channel, EzyConnectionType type);
	
}
