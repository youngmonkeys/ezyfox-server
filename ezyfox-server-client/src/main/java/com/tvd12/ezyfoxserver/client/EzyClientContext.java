package com.tvd12.ezyfoxserver.client;

import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.entity.EzyDestroyable;

public interface EzyClientContext extends EzyContext, EzyDestroyable {

	EzyClient getClient();
	
}
