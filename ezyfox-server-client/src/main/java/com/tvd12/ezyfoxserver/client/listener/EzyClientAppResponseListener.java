package com.tvd12.ezyfoxserver.client.listener;

import com.tvd12.ezyfoxserver.client.context.EzyClientAppContext;
import com.tvd12.ezyfoxserver.entity.EzyData;

public interface EzyClientAppResponseListener<P extends EzyData> {

	void execute(EzyClientAppContext ctx, P params);
	
}
