package com.tvd12.ezyfoxserver.netty.wrapper;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.netty.handler.EzyHandlerGroup;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzySocketDataHandlerGroupFetcher;
import com.tvd12.ezyfoxserver.socket.EzySocketWriterGroupFetcher;

public interface EzyHandlerGroupManager
		extends EzySocketDataHandlerGroupFetcher, EzySocketWriterGroupFetcher{

	EzyHandlerGroup getHandlerGroup(Object connection);
	
	EzyHandlerGroup removeHandlerGroup(Object connection);
	
	EzyHandlerGroup newHandlerGroup(EzyChannel channel, EzyConnectionType type);
	
}
