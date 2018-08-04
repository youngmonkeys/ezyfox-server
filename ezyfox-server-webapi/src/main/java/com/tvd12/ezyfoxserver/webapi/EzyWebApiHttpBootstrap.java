package com.tvd12.ezyfoxserver.webapi;

import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.EzyHttpBootstrap;
import com.tvd12.ezyfoxserver.context.EzyServerContext;

import lombok.Setter;

public class EzyWebApiHttpBootstrap 
		extends EzyLoggable 
		implements EzyHttpBootstrap {

	@Setter
	protected EzyServerContext serverContext;
	
	@Override
	public void start() throws Exception {
	}
	
	@Override
	public void destroy() {
	}
	
	protected EzyWebApiApplication newApplication() {
		EzyWebApiApplication app = new EzyWebApiApplication(getClass());
		app.setServerContext(serverContext);
		return app;
	}
	
}
