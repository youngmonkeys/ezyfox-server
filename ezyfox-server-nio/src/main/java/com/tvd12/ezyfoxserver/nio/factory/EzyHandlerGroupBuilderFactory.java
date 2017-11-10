package com.tvd12.ezyfoxserver.nio.factory;

import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.nio.handler.EzyAbstractHandlerGroup;

public interface EzyHandlerGroupBuilderFactory {

	EzyAbstractHandlerGroup.Builder newBuilder(EzyConnectionType type);
	
}
