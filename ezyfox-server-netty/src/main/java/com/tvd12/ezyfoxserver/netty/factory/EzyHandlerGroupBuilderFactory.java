package com.tvd12.ezyfoxserver.netty.factory;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.netty.handler.EzyAbstractHandlerGroup;

public interface EzyHandlerGroupBuilderFactory {

	EzyAbstractHandlerGroup.Builder newBuilder(EzyConnectionType type);
	
}
