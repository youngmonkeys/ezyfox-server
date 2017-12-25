package com.tvd12.ezyfoxserver.nio.wrapper;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.nio.handler.EzyHandlerGroup;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzySocketDataHandlerGroupFetcher;
import com.tvd12.ezyfoxserver.socket.EzySocketWriterGroupFetcher;

public interface EzyHandlerGroupManager 
		extends EzySocketDataHandlerGroupFetcher, EzySocketWriterGroupFetcher {

	<T extends EzyHandlerGroup> T getHandlerGroup(Object connection);
	
	<T extends EzyHandlerGroup> T removeHandlerGroup(Object connection);
	
	<T extends EzyHandlerGroup> T newHandlerGroup(EzyChannel channel, EzyConnectionType type);
	
}
