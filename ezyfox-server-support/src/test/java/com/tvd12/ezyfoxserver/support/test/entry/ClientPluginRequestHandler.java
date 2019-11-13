package com.tvd12.ezyfoxserver.support.test.entry;

import com.tvd12.ezyfox.core.annotation.EzyClientRequestListener;
import com.tvd12.ezyfox.function.EzyHandler;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyPluginContextAware;

import lombok.Setter;

@Setter
@EzyClientRequestListener("plugin")
public class ClientPluginRequestHandler
		extends EzyLoggable
		implements 
			EzyPluginContextAware,
			EzyHandler {

	protected EzyPluginContext pluginContext;
	
	@Override
	public void handle() {
	}

}
