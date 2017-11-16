package com.tvd12.ezyfoxserver.builder;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.EzyServerBootstrap;

public interface EzyServerBootstrapBuilder extends EzyBuilder<EzyServerBootstrap> {

	/**
	 * set server
	 * 
	 * @param server the server
	 * @return this pointer
	 */
	EzyServerBootstrapBuilder server(EzyServer server);
	
}
