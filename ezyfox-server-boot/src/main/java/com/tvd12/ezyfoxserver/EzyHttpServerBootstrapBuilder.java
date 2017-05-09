package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.builder.EzyAbtractServerBootstrapBuilder;

public abstract class EzyHttpServerBootstrapBuilder extends EzyAbtractServerBootstrapBuilder {

	@Override
	protected EzyHttpBootstrap newHttpBottstrap() {
		EzyComplexHttpBootstrap bootstrap = new EzyComplexHttpBootstrap();
		bootstrap.setServerContext(serverContext);
		return bootstrap;
	}
	
}
